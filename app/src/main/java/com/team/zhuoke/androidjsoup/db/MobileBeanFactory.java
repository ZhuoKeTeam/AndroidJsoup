package com.team.zhuoke.androidjsoup.db;

import com.team.zhuoke.androidjsoup.db.exception.BizException;
import com.team.zhuoke.androidjsoup.db.impl.BaseServiceImpl;
import com.team.zhuoke.androidjsoup.db.impl.IMyDataServiceImpl;
import com.team.zhuoke.androidjsoup.db.interfaces.IBaseService;
import com.team.zhuoke.androidjsoup.db.interfaces.IBeanFactory;
import com.team.zhuoke.androidjsoup.db.interfaces.IDatabaseProvider;
import com.team.zhuoke.androidjsoup.db.interfaces.IMyDataService;
import com.team.zhuoke.androidjsoup.db.interfaces.ITransaction;
import com.team.zhuoke.androidjsoup.db.proxy.IMyDataProxy;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * 实例工厂
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class MobileBeanFactory implements IBeanFactory
{
    private TableClassRegist tableClassRegist;
    
    private IBaseService baseService;
    
    private Map<Class, Object> beanRegist;
    
    private ListenerRegister listenerRegister;
    
    private StorageServiceFactory _storageServiceFactory;
    
    private StorageService storageService = null;
    
    private AppLock appLock = null;
    
    public MobileBeanFactory(ListenerRegister listenerRegister, StorageServiceFactory storageServiceFactory,
        AppLock appLock)
    {
        this.listenerRegister = listenerRegister;
        this.tableClassRegist = new TableClassRegist();
        this._storageServiceFactory = storageServiceFactory;
        this.appLock = appLock;
        this.beanRegist = new HashMap<Class, Object>();
    }
    
    public void initSimple()
    {
        initSimpleBeans();
    }
    
    private void initSimpleBeans()
    {
        DatabaseStorageService databaseStorageService = _storageServiceFactory.getDatabaseStorageService();
        registBean(IDatabaseProvider.class, databaseStorageService);
        
        storageService = _storageServiceFactory.getStorageService();
        registBean(StorageService.class, storageService);
        
        CacheConfig config = new CacheConfig(storageService);
        config.config();
        
        baseService = new BaseServiceImpl(tableClassRegist, storageService, databaseStorageService);
        registBean(IBaseService.class, baseService);
        
        registBean(ITransaction.class, _storageServiceFactory.getTransaction());

        IMyDataService systemTypeService = new IMyDataServiceImpl(baseService);
        IMyDataProxy systemTypeProxy = new IMyDataProxy(systemTypeService, appLock);
        registBean(IMyDataService.class, systemTypeProxy);
    }
    
    /**
     * 注册实例.
     * 
     * @param <T>
     *            型别.
     * @param clazz
     *            注册类型.
     * @param bean
     *            注册的对象.
     */
    protected <T> void registBean(Class<T> clazz, T bean)
    {
        beanRegist.put(clazz, bean);
    }
    
    @Override
    public <T> T getBean(Class<T> clazz)
    {
        if (beanRegist.containsKey(clazz))
        {
            return (T)beanRegist.get(clazz);
        }
        throw new BizException("class not regist. class = " + clazz.getName());
    }
    
    @Override
    public TableClassRegist getTableClassRegist()
    {
        return tableClassRegist;
    }
}
