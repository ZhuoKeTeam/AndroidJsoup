package com.team.zhuoke.androidjsoup.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.team.zhuoke.androidjsoup.db.exception.StorageException;
import com.team.zhuoke.androidjsoup.db.interfaces.ICacheClear;
import com.team.zhuoke.androidjsoup.db.interfaces.IDatabaseProvider;
import com.team.zhuoke.androidjsoup.db.interfaces.IMessageDispatcher;
import com.team.zhuoke.androidjsoup.db.interfaces.SQLiteOpenHelperFactory;
import com.team.zhuoke.androidjsoup.db.query.BatchSql;
import com.team.zhuoke.androidjsoup.db.query.ExecSqlItem;
import com.team.zhuoke.androidjsoup.db.query.Op;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 数据库存储数据服务. 外部不直接调用具体的存取逻辑
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class DatabaseStorageService implements IDatabaseProvider
{
    private static final String LOAD_SQL = "select * from %s where id=?";
    
    private BlockingQueue<StoragePack> waitStorageQueue;
    
    private BlockingQueue<String> messageQueue;
    
    private Map<String, Storage> waitStorageMap;
    
    private SQLiteOpenHelper mainDBHelper;
    
    private ReentrantLock mainLock;
    
    private Map<SQLiteOpenHelper, ReentrantLock> dbLockMap = null;
    
    private Set<Integer> inProcessKeys = null;
    
    private ReentrantLock keysLock = new ReentrantLock();
    
    private ICacheClear cacheClear = null;
    
    private Thread storeThread = null;
    
    private Thread dispatcherThread = null;
    
    private int numToStore = 0;
    
    private IMessageDispatcher _messageDispatcher = null;
    
    private int numStored = 0;
    
    private int index = 1;
    
    private Context context;
    
    private int version;
    
    private String mainDBFile;
    
    /**
     * 是否需要重启
     */
    private Boolean isNeedRestart = false;
    
    private SQLiteOpenHelperFactory sqLiteOpenHelperFactory = null;
    
    public DatabaseStorageService(Context context, int version, String mainDBFile)
    {
        this(context, version, mainDBFile, new SQLiteOpenHelperFactory()
        {
            @Override
            public SQLiteOpenHelper newSQliteSqLiteOpenHelper(Context context, String name,
                SQLiteDatabase.CursorFactory factory, int version)
            {
                return new DBHelper(context, name, factory, version);
            }
        });
    }
    
    public DatabaseStorageService(Context context, int version, String mainDBFile,
        SQLiteOpenHelperFactory sqLiteOpenHelperFactory)
    {
        this.context = context;
        this.version = version;
        this.mainDBFile = mainDBFile;
        
        this.sqLiteOpenHelperFactory = sqLiteOpenHelperFactory;
        
        this.mainDBHelper = createSqLiteOpenHelper(mainDBFile);
        waitStorageQueue = new LinkedBlockingQueue<StoragePack>();
        messageQueue = new LinkedBlockingQueue<String>();
        waitStorageMap = new ConcurrentHashMap<String, Storage>();
        inProcessKeys = new HashSet<Integer>();
        dbLockMap = new HashMap<SQLiteOpenHelper, ReentrantLock>();
        mainLock = new ReentrantLock();
        dbLockMap.put(mainDBHelper, mainLock);
        
        storeThread = new Thread(new StoreRunnable());
        storeThread.setDaemon(true);
        storeThread.start();
        
        dispatcherThread = new Thread(new MessageDispatchRunnable());
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
    }
    
    /**
     * 创建数据库帮助类.
     * 
     * @param filename
     *            文件名.
     * @return 创建好的数据库帮助类.
     */
    private SQLiteOpenHelper createSqLiteOpenHelper(String filename)
    {
        SQLiteOpenHelper dfireDBHelper =
            sqLiteOpenHelperFactory.newSQliteSqLiteOpenHelper(context, filename, null, version);
        dfireDBHelper.getWritableDatabase();
        return dfireDBHelper;
    }
    
    /**
     * 设置消息广播接口.
     * 
     * @param messageDispatcher
     *            消息广播接口.
     */
    public void setMessageDispatcher(IMessageDispatcher messageDispatcher)
    {
        this._messageDispatcher = messageDispatcher;
    }
    
    /**
     * 等待刷新到数据库中. 一直等到刚才加到队列中的数据更新到数据库中，才会退出本方法.
     * 
     * @param integer
     */
    public void waitFlush(Integer integer)
    {
        while (true)
        {
            keysLock.lock();
            try
            {
                if (inProcessKeys.isEmpty())
                {
                    return;
                }
            }
            finally
            {
                keysLock.unlock();
            }
            int min = integer + 1;
            keysLock.lock();
            try
            {
                for (int key : inProcessKeys)
                {
                    min = Math.min(min, key);
                }
            }
            finally
            {
                keysLock.unlock();
            }
            if (min <= integer)
            {
                try
                {
                    Thread.sleep(5L);
                }
                catch (InterruptedException e)
                {
                }
                continue;
            }
            else
            {
                return;
            }
        }
    }
    
    public void waitFlush()
    {
        int cnumToStore = numToStore;
        while (true)
        {
            if (numStored < cnumToStore)
            {
                try
                {
                    Thread.sleep(1L);
                }
                catch (InterruptedException e)
                {
                }
                continue;
            }
            else
            {
                return;
            }
        }
    }
    
    void setCacheClear(ICacheClear cacheClear)
    {
        this.cacheClear = cacheClear;
    }
    
    @Override
    public void batchExecuteWithCommit(BatchSql... batchSqls)
    {
        if (batchSqls == null || batchSqls.length == 0)
        {
            return;
        }
        // 多数据库数据分组
        Map<SQLiteOpenHelper, List<ExecSqlItem>> dbSqls = new HashMap<SQLiteOpenHelper, List<ExecSqlItem>>();
        Set<Class> clearCaches = new HashSet<Class>();
        for (BatchSql batchSql : batchSqls)
        {
            SQLiteOpenHelper openHelper = null;
            List<ExecSqlItem> exeSqls = batchSql.getSqlList();
            if (exeSqls == null || exeSqls.isEmpty())
            {
                continue;
            }
            if (openHelper == null)
            {
                openHelper = mainDBHelper;
            }
            clearCaches.add(batchSql.getClass());
            if (!dbSqls.containsKey(openHelper))
            {
                List<ExecSqlItem> sqls = new ArrayList<ExecSqlItem>();
                sqls.addAll(exeSqls);
                dbSqls.put(openHelper, sqls);
                continue;
            }
            List<ExecSqlItem> sqls = dbSqls.get(openHelper);
            sqls.addAll(exeSqls);
        }
        // 先行清除缓存
        if (cacheClear != null)
        {
            for (Class class1 : clearCaches)
            {
                cacheClear.clear(class1);
            }
        }
        // 分数据库开启事务,执行相关操作
        for (Map.Entry<SQLiteOpenHelper, List<ExecSqlItem>> entry : dbSqls.entrySet())
        {
            SQLiteOpenHelper openHelper = entry.getKey();
            List<ExecSqlItem> dataList = entry.getValue();
            if (dataList == null || dataList.isEmpty())
            {
                continue;
            }
            if (dbLockMap != null && !dbLockMap.isEmpty())
            {
                ReentrantLock lock = dbLockMap.get(openHelper);
                lock.lock();
                SQLiteDatabase db = openHelper.getWritableDatabase();
                try
                {
                    db.beginTransaction();
                    for (ExecSqlItem sqlItems : dataList)
                    {
                        String[] params = sqlItems.getParams();
                        if (params == null || params.length == 0)
                        {
                            db.execSQL(sqlItems.getSql());
                        }
                        else
                        {
                            db.execSQL(sqlItems.getSql(), params);
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally
                {
                    db.endTransaction();
                    lock.unlock();
                    
                }
            }
        }
        // 再次清除缓存，防止事务处理过程中又加载到缓存中去了.
        if (cacheClear != null)
        {
            for (Class class1 : clearCaches)
            {
                cacheClear.clear(class1);
            }
        }
    }
    
    /**
     * 添加一个存储包.
     * 
     * @param storagePack
     */
    void addStoragePack(StoragePack storagePack)
    {
        if (storagePack == null)
        {
            return;
        }
        List<Storage> slist = storagePack.getStorages();
        if (slist == null || slist.isEmpty())
        {
            return;
        }
        keysLock.lock();
        try
        {
            inProcessKeys.add(storagePack.getId());
        }
        finally
        {
            keysLock.unlock();
        }
        numToStore++;
        waitStorageQueue.add(storagePack);
        for (Storage storage : slist)
        {
            AbstractStorage st = storage.object;
            String key = String.format(String.format("%s_%s", st.getClass().getName(), st.getId()));
            waitStorageMap.put(key, storage);
        }
    }
    
    /**
     * 得到可读数据源.
     * 
     * @param tClass
     *            类.
     * @return 可读数据源.
     */
    public SQLiteDatabase getReadableDatabase(Class tClass)
    {
        return mainDBHelper.getReadableDatabase();
    }
    
    public <T extends AbstractStorage> T get(Class<T> tClass, Serializable id)
    {
        String key = String.format("%s_%s", tClass.getName(), id);
        if (waitStorageMap.containsKey(key))
        {
            Storage storage = waitStorageMap.get(key);
            if (storage.op == Op.DELETE)
            {
                return null;
            }
            return (T)storage.object;
        }
        try
        {
            T t = tClass.newInstance();
            String tableName = t.getTableName();
            String sql = getLoadSql(tableName);
            Cursor cursor = mainDBHelper.getReadableDatabase().rawQuery(sql, new String[] {(String)id});
            try
            {
                if (cursor.getCount() == 0)
                {
                    return null;
                }
                cursor.moveToNext();
                t.init(cursor);
                return t;
            }
            finally
            {
                cursor.close();
            }
        }
        catch (InstantiationException e)
        {
            throw new StorageException("实例化对象出错！", e);
        }
        catch (IllegalAccessException e)
        {
            throw new StorageException("错误的参数！", e);
        }
    }
    
    private String getLoadSql(String tableName)
    {
        return String.format(LOAD_SQL, tableName);
    }
    
    void close()
    {
        waitFlush();
        storeThread.interrupt();
        dispatcherThread.interrupt();
        
        waitStorageQueue.clear();
        messageQueue.clear();
        waitStorageMap.clear();
    }
    
    private void removeStorageCache(List<Storage> datas)
    {
        // 从队列里面清除掉
        for (Storage storage : datas)
        {
            AbstractStorage base = storage.object;
            String key = String.format("%s_%s", base.getClass().getName(), base.getId());
            if (waitStorageMap.containsKey(key))
            {
                Storage wStorage = waitStorageMap.get(key);
                if (wStorage == storage)
                {
                    waitStorageMap.remove(key);
                }
            }
        }
    }
    
    private void storeSignleDB(StoragePack storagePack)
    {
        List<Storage> datas = storagePack.getStorages();
        List<Storage> newDatas = new ArrayList<Storage>();
        newDatas.addAll(datas);
        mainLock.lock();
        try
        {
            flushToDB(mainDBHelper, datas);
        }
        finally
        {
            mainLock.unlock();
        }
        removeStorageCache(newDatas);
    }
    
    private void flushToDB(SQLiteOpenHelper openHelper, List<Storage> dataList)
    {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ReentrantLock lock = dbLockMap.get(openHelper);
        lock.lock();
        try
        {
            storageDatas(dataList, db);
        }
        finally
        {
            lock.unlock();
        }
    }
    
    private void storageDatas(List<Storage> dataList, SQLiteDatabase db)
    {
        if (dataList.isEmpty())
        {
            return;
        }
        db.beginTransaction();
        try
        {
            for (Storage storage : dataList)
            {
                AbstractStorage base = storage.object;
                switch (storage.op)
                {
                    case INSERT:
                        base.save(db);
                        continue;
                    case UPDATE:
                        base.update(db);
                        continue;
                    case DELETE:
                        base.delete(db);
                }
            }
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }
    
    @Override
    public void vacuumDatabase()
    {
        if (dbLockMap != null && !dbLockMap.isEmpty())
        {
            Set<Map.Entry<SQLiteOpenHelper, ReentrantLock>> databases = dbLockMap.entrySet();
            for (Map.Entry<SQLiteOpenHelper, ReentrantLock> entry : databases)
            {
                vacuumDatabase(entry.getValue(), entry.getKey());
            }
            if (dbLockMap.containsKey(mainDBHelper))
            {
                return;
            }
        }
        vacuumDatabase(mainLock, mainDBHelper);
    }
    
    private void vacuumDatabase(ReentrantLock lock, SQLiteOpenHelper dbHelper)
    {
        lock.lock();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try
        {
            db.execSQL("VACUUM");
        }
        finally
        {
            lock.unlock();
        }
    }
    
    private class StoreRunnable implements Runnable
    {
        
        @Override
        public void run()
        {
            while (true)
            {
                int id = 0;
                try
                {
                    StoragePack storagePack = waitStorageQueue.take();
                    storeSignleDB(storagePack);
                    id = storagePack.getId();
                    numStored++;
                    String messages = storagePack.getMessages();
                    if (messages != null && messages.trim().length() > 0 && _messageDispatcher != null)
                    {
                        messageQueue.add(messages);
                    }
                }
                catch (InterruptedException e)
                {
                    break;
                }
                finally
                {
                    keysLock.lock();
                    try
                    {
                        inProcessKeys.remove(id);
                    }
                    finally
                    {
                        keysLock.unlock();
                    }
                }
            }
        }
        
    }
    
    /**
     * 发送广播消息. 采用线程方式，避免阻塞数据存储线程.
     */
    private class MessageDispatchRunnable implements Runnable
    {
        
        @Override
        public void run()
        {
            while (true)
            {
                try
                {
                    String message = messageQueue.take();
                    if (_messageDispatcher != null)
                    {
                        _messageDispatcher.dispatchMessage(message);
                    }
                }
                catch (InterruptedException e)
                {
                    break;
                }
            }
        }
    }
    
    @Override
    public void resetDB(SharedPreferences sp)
    {
        mainDBHelper = resetDB(mainDBFile, mainDBHelper, mainLock);
        isNeedRestart = true;
    }
    
    /**
     * 得到是否需要重启.
     * 
     * @return 是否需要重启.
     */
    public Boolean getIsNeedRestart()
    {
        return isNeedRestart;
    }
    
    /**
     * 重置数据库.
     * 
     * @param filename
     *            数据库文件名.
     * @param sqliteOpenHelper
     * @param lock
     * @return 重置后的数据库.
     */
    private SQLiteOpenHelper resetDB(String filename, SQLiteOpenHelper sqliteOpenHelper, ReentrantLock lock)
    {
        lock.lock();
        try
        {
            sqliteOpenHelper.close();
            
            File dbFile = new File(filename);
            if (dbFile.exists())
            {
                dbFile.delete();
            }
            SQLiteOpenHelper newOpenHelper = createSqLiteOpenHelper(filename);
            // 更换相关资源
            if (dbLockMap.containsKey(sqliteOpenHelper))
            {
                dbLockMap.remove(sqliteOpenHelper);
                dbLockMap.put(newOpenHelper, lock);
            }
            return newOpenHelper;
        }
        finally
        {
            lock.unlock();
        }
    }
}
