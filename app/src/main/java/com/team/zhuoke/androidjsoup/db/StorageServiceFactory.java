package com.team.zhuoke.androidjsoup.db;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.team.zhuoke.androidjsoup.db.exception.StorageException;
import com.team.zhuoke.androidjsoup.db.impl.TransactionImpl;
import com.team.zhuoke.androidjsoup.db.interfaces.ITransaction;

/**
 * 
 * 构造Storage相关服务
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class StorageServiceFactory
{
    private DatabaseStorageService databaseStorageService;
    
    private StorageService storageService;
    
    private ITransaction transaction;
    
    public StorageServiceFactory(Context context, String mainDBFilename)
    {
        PackageInfo packageInfo;
        try
        {
            packageInfo =
                context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            int versionCode = packageInfo.versionCode;
            databaseStorageService = new DatabaseStorageService(context, versionCode, mainDBFilename);
            storageService = new StorageService(databaseStorageService);
            transaction = new TransactionImpl(storageService);
        }
        catch (NameNotFoundException e)
        {
            throw new StorageException("无法初始化数据库！", e);
        }
    }
    
    public DatabaseStorageService getDatabaseStorageService()
    {
        return databaseStorageService;
    }
    
    public StorageService getStorageService()
    {
        return storageService;
    }
    
    public ITransaction getTransaction()
    {
        return transaction;
    }
}
