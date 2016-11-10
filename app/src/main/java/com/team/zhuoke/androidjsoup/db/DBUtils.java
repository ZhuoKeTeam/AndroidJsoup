package com.team.zhuoke.androidjsoup.db;

import android.content.Context;

import com.team.zhuoke.androidjsoup.db.interfaces.IBaseService;


/**
 * 
 * 数据库初始化
 *
 * <p>detailed comment
 * @author ztw 2016年7月8日
 * @see
 * @since 1.0
 */
public class DBUtils
{
    private Context applicationContext = null;
    
    private static DBUtils INSTANCE = null;
    
    private ListenerRegister listenerRegister = null;
    
    private AppLock appLock = null;
    
    private MobileBeanFactory mobileBeanFactory = null;
    
    private ThreadLocal<SimpleObjectChangedRegist> simpleObjectChangedRegist =
        new ThreadLocal<SimpleObjectChangedRegist>();
    
    /**
     * ID生成器
     */
    private UUIDGenerator idGenerator = null;

    public void initDB(Context applicationContext)
    {
        this.applicationContext = applicationContext;
        this.listenerRegister = new ListenerRegister();
        Broadcast broadcast = new Broadcast(listenerRegister);

        idGenerator = new UUIDGenerator();
        StorageServiceFactory storageServiceFactory =
                new StorageServiceFactory(applicationContext, "jsoup.db");
        DatabaseStorageService ds = storageServiceFactory.getDatabaseStorageService();
        ds.setMessageDispatcher(broadcast);
        appLock = new AppLock(storageServiceFactory.getTransaction());
        appLock.setMessageDispatcher(broadcast);
        mobileBeanFactory = new MobileBeanFactory(listenerRegister, storageServiceFactory, appLock);
        mobileBeanFactory.initSimple();
        IBaseService baseService = mobileBeanFactory.getBean(IBaseService.class);
        baseService.clearCache();
    }
    
    private DBUtils()
    {
        
    }
    
    public static DBUtils getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new DBUtils();
        }
        return INSTANCE;
    }
    
    public ListenerRegister getListenerRegister()
    {
        checkContextInitialized();
        return listenerRegister;
    }
    
    public AppLock getAppLock()
    {
        checkContextInitialized();
        return appLock;
    }
    
    public MobileBeanFactory getMobileBeanFactory()
    {
        checkContextInitialized();
        return mobileBeanFactory;
    }
    
    /**
     * 得到当前的SimpleObjectChangedRegist.
     * 
     * @return 当前的SimpleObjectChangedRegist.
     */
    public SimpleObjectChangedRegist getSimpleObjectChangedRegist()
    {
        checkContextInitialized();
        return simpleObjectChangedRegist.get();
    }
    
    /**
     * 新建SimpleObjectChangedRegist.
     * 
     * @param isMonitor
     *            是否需要监控变化.
     */
    public void newSimpleObjectChangedRegist(boolean isMonitor)
    {
        checkContextInitialized();
        SimpleObjectChangedRegist socr2 = new SimpleObjectChangedRegist();
        if (isMonitor)
        {
            socr2.startMonitorChange();
        }
        simpleObjectChangedRegist.set(socr2);
    }
    
    public UUIDGenerator getIdGenerator()
    {
        checkContextInitialized();
        return idGenerator;
    }
    
    private void checkContextInitialized()
    {
        if (applicationContext == null)
        {
            throw new IllegalArgumentException("请先初始化applicationContext");
        }
    }
}
