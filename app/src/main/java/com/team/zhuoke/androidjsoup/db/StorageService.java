package com.team.zhuoke.androidjsoup.db;

import android.support.v4.util.LruCache;

import com.team.zhuoke.androidjsoup.db.exception.StorageException;
import com.team.zhuoke.androidjsoup.db.interfaces.ICacheClear;
import com.team.zhuoke.androidjsoup.db.query.Op;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据存储服务，利用缓存进行事务提交，变更的数据会通过持久化服务，持久化到数据库中去. 获取数据时，如果缓存中没有命中，则自动从持久化中获取
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class StorageService implements ICacheClear
{
    /**
     * 默认最大缓存数量.
     */
    private static final int DEFAULT_CACHE_MAX_SIZE = 50;
    
    /**
     * 默认最小缓存量.
     */
    private static final int DEFAULT_CACHE_MIN_SIZE = 40;
    
    private int seq = 1;
    
    private DatabaseStorageService databaseStorageService;
    
    private Map<Class, LruCache> classCacheMap;
    
    /**
     * 当前线程要存储的内容.
     */
    private ThreadLocal<StoragePack> storages = new ThreadLocal<StoragePack>();
    
    /**
     * 创建缓存服务.
     * 
     * @param databaseStorageService 持久化存储服务.
     */
    public StorageService(final DatabaseStorageService databaseStorageService)
    {
        if (databaseStorageService == null)
        {
            throw new IllegalArgumentException("存储服务不能为空！");
        }
        this.databaseStorageService = databaseStorageService;
        classCacheMap = Collections.synchronizedMap(new HashMap<Class, LruCache>());
    }
    
    /**
     * 初始化缓存信息. 一些特定的类型才需要，如果不指定，将用默认的缓存策略来处理.
     * 
     * @param tClass
     *            类型.
     * @param maxSize
     *            最大数量.
     * @param <T>
     *            型别.
     * @return 缓存服务.
     */
    public synchronized <T extends AbstractStorage> StorageService initCache(Class<T> tClass, int maxSize)
    {
        if (classCacheMap.containsKey(tClass))
        {
            LruCache<Serializable, T> cache = classCacheMap.get(tClass);
            classCacheMap.remove(tClass);
        }
        LruCache<Serializable, T> cache = new LruCache<Serializable, T>(maxSize);
        classCacheMap.put(tClass, cache);
        return this;
    }
    
    /**
     * 保存对象.
     * 
     * @param t
     *            要保存的对象.
     * @param <T>
     *            型别.
     * @return 保存后的对象.
     */
    public <T extends AbstractStorage> T save(T t)
    {
        return addStorage((T)t, Op.INSERT, "您要保存的对象不支持克隆！");
    }
    
    /**
     * 更新对象.
     * 
     * @param t
     *            要更新的对象.
     * @param <T>
     *            型别.
     * @return 更新后的对象.
     */
    public <T extends AbstractStorage> T update(T t)
    {
        return addStorage(t, Op.UPDATE, "您要保存的对象不支持克隆！");
    }
    
    /**
     * 删除对象.
     * 
     * @param t
     *            要删除对象.
     * @param <T>
     *            对象型别.
     * @return 删除的对象.
     */
    public <T extends AbstractStorage> T remove(T t)
    {
        return addStorage(t, Op.DELETE, "您要删除的对象不支持克隆！");
    }
    
    /**
     * 根据类型，删除指定ID的对象. 如果找不到，则返回null.
     * 
     * @param clazz
     *            类型.
     * @param id
     *            数据主键.
     * @param <T>
     *            型别.
     * @return 删除的对象.
     */
    public <T extends AbstractStorage> T remove(Class<T> clazz, Serializable id)
    {
        T t = get(clazz, id);
        return remove(t);
    }
    
    /**
     * 获取指定类型，指定主键值的对象. 如果找不到，则返回null对象.
     * 
     * @param clazz
     *            类型.
     * @param id
     *            主键.
     * @param <T>
     *            型别.
     * @return 获取到的对象.
     */
    public <T extends AbstractStorage> T get(Class<T> clazz, Serializable id)
    {
        if (id == null)
        {
            return null;
        }
        StoragePack storagePack = getCurrentStoragePack();
        Storage storage = storagePack.get(clazz, id);
        if (storage != null)
        {
            if (storage.op == Op.DELETE)
            {
                return null;
            }
            try
            {
                return (T)storage.object.clone();
            }
            catch (CloneNotSupportedException e)
            {
                throw new StorageException("您要获取的对象不支持克隆！", e);
            }
        }
        LruCache<Serializable, T> cache = getFastLRUCache(clazz);
        T result = cache.get(id);
        if (result == null)
        {
            result = databaseStorageService.get(clazz, id);
            if (result == null)
            {
                return null;
            }
            cache.put(id, result);
        }
        try
        {
            return (T)result.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new StorageException("您要保存的对象不支持克隆！", e);
        }
    }
    
    /**
     * 清空并关闭服务. 清空缓存里缓存的数据，但不会删除持久化的数据.
     */
    public void close()
    {
        Collection<LruCache> values = classCacheMap.values();
        for (LruCache value : values)
        {
            value.evictAll();
        }
        classCacheMap.clear();
        databaseStorageService.close();
    }
    
    /**
     * 回滚事件.
     */
    public void rollback()
    {
        storages.set(null);
    }
    
    /**
     * 把缓存的数据同步到持久化里面去. 启用线程来同步，不会阻塞操作线程.
     * 
     * @param message
     *            成功发送的消息.
     */
    public int flush(String message)
    {
        return flushTemp(message);
    }
    
    /**
     * 立即把缓存的数据同步到持久化里面去. 直到同步到持久化里，才会返回，会阻塞线程. 能不用这个方法，尽量不要用这个方法.推荐使用flush().
     * 
     * @param opUserId 操作人ID.
     * @param clientTag 客户端标志.
     * @param message 成功发送的消息.
     */
    public int flushNow(String opUserId, String clientTag, String message)
    {
        int r = flushTemp(message);
        if (r == 0)
        {
            return 0;
        }
        databaseStorageService.waitFlush(seq);
        return r;
    }
    
    /**
     * 等待刷新到数据库.
     */
    public void waitFlush()
    {
        databaseStorageService.waitFlush(seq);
    }
    
    /**
     * 清空缓存.
     */
    public void clear()
    {
        Collection<LruCache> caches = classCacheMap.values();
        if (caches != null && !caches.isEmpty())
        {
            for (LruCache lruCache : caches)
            {
                lruCache.evictAll();
            }
        }
    }
    
    @Override
    public void clear(Class tClass)
    {
        if (classCacheMap.containsKey(tClass))
        {
            LruCache cache = classCacheMap.get(tClass);
            cache.evictAll();
        }
    }
    
    private int flushTemp(String message)
    {
        StoragePack storagePack = getCurrentStoragePack();
        if (storagePack == null)
        {
            return 0;
        }
        List<Storage> slist = storagePack.getStorages();
        if (slist == null || slist.isEmpty())
        {
            return 0;
        }
        storagePack.setMessages(message);
        // 更新到缓存
        for (Storage storage : slist)
        {
            switch (storage.op)
            {
                case INSERT:
                case UPDATE:
                    doSaveOrUpdate(storage.object);
                    continue;
                case DELETE:
                    doRemove(storage.object);
            }
        }
        // 加入到数据存储任务中去.
        databaseStorageService.addStoragePack(storagePack);
        int key = storagePack.getId();
        storages.remove();
        return key;
    }
    
    private <T extends AbstractStorage> T addStorage(T t, Op op, String message)
    {
        try
        {
            T result = (T)t.clone();
            Storage storage = new Storage(op, result);
            getCurrentStoragePack().addStorage(storage);
            return t;
        }
        catch (CloneNotSupportedException e)
        {
            throw new StorageException(message, e);
        }
    }
    
    public <T extends AbstractStorage> void addCache(T t)
    {
        doSaveOrUpdate(t);
    }
    
    private <T extends AbstractStorage> void doSaveOrUpdate(T t)
    {
        Class tClass = t.getClass();
        LruCache<Serializable, T> cache = getFastLRUCache(tClass);
        cache.put(t.getId(), t);
    }
    
    private <T extends AbstractStorage> void doRemove(T t)
    {
        Class tClass = t.getClass();
        if (classCacheMap.containsKey(tClass))
        {
            LruCache<Serializable, T> cache = classCacheMap.get(tClass);
            cache.remove(t.getId());
        }
    }
    
    private <T extends AbstractStorage> LruCache<Serializable, T> getFastLRUCache(Class<T> clazz)
    {
        if (!classCacheMap.containsKey(clazz))
        {
            return initDefaultCache(clazz);
        }
        else
        {
            return classCacheMap.get(clazz);
        }
    }
    
    private synchronized <T extends AbstractStorage> LruCache<Serializable, T> initDefaultCache(Class<T> tClass)
    {
        if (classCacheMap.containsKey(tClass))
        {
            return classCacheMap.get(tClass);
        }
        LruCache<Serializable, T> cache = new LruCache<Serializable, T>(DEFAULT_CACHE_MAX_SIZE);
        classCacheMap.put(tClass, cache);
        return cache;
    }
    
    private StoragePack getCurrentStoragePack()
    {
        StoragePack sp = storages.get();
        if (sp == null)
        {
            synchronized (this)
            {
                seq++;
                sp = new StoragePack(seq);
            }
            storages.set(sp);
        }
        return sp;
    }
}
