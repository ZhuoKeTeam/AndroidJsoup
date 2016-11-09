package com.team.zhuoke.androidjsoup.db.impl;


import com.team.zhuoke.androidjsoup.db.StorageService;
import com.team.zhuoke.androidjsoup.db.interfaces.ITransaction;

/**
 * 
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class TransactionImpl implements ITransaction
{
    private StorageService storageService;
    
    public TransactionImpl(StorageService storageService)
    {
        super();
        this.storageService = storageService;
    }
    
    @Override
    public void rollback()
    {
        storageService.rollback();
    }
    
    @Override
    public int commit(String message)
    {
        return storageService.flush(message);
    }
    
    @Override
    public void waitFlush()
    {
        storageService.waitFlush();
    }
}
