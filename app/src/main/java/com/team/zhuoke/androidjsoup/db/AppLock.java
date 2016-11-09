package com.team.zhuoke.androidjsoup.db;

import com.team.zhuoke.androidjsoup.db.interfaces.IMessageDispatcher;
import com.team.zhuoke.androidjsoup.db.interfaces.ITransaction;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 
 * 应用数据库同步调用锁
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class AppLock
{
    private Lock fileLock = new ReentrantLock();
    
    private ITransaction _transaction;
    
    private IMessageDispatcher _messageDispatcher = null;
    
    private DBUtils dbUtils = null;
    
    public AppLock(ITransaction transaction)
    {
        super();
        this._transaction = transaction;
        this.dbUtils = DBUtils.getInstance();
    }
    
    public void setMessageDispatcher(IMessageDispatcher messageDispatcher)
    {
        this._messageDispatcher = messageDispatcher;
    }
    
    public void lock()
    {
        boolean isMonitor = false;
        beginLock(isMonitor);
    }
    
    public void beginLock(boolean isMonitor)
    {
        SimpleObjectChangedRegist socr = dbUtils.getSimpleObjectChangedRegist();
        if (socr == null || socr.isEnd())
        {
            dbUtils.newSimpleObjectChangedRegist(isMonitor);
        }
        else
        {
            socr.increaseCall();
        }
    }
    
    public void lockWithMonitor()
    {
        beginLock(true);
    }
    
    public void unlock(boolean isSuccess)
    {
        SimpleObjectChangedRegist socr = dbUtils.getSimpleObjectChangedRegist();
        if (socr != null)
        {
            socr.reduceCall();
            if (!socr.isEnd())
            {
                return;
            }
        }
        if (isSuccess)
        {
            _transaction.commit(null);
        }
        else
        {
            _transaction.rollback();
        }
    }
    
    public void unlockWithBroadcast(boolean isSuccess)
    {
        unlockWithBroadcast(isSuccess, false);
    }
    
    public void unlockWithBroadcastNow(boolean isSuccess)
    {
        unlockWithBroadcast(isSuccess, true);
    }
    
    public void unlockWithBroadcast(boolean isSuccess, boolean now)
    {
        SimpleObjectChangedRegist socr = dbUtils.getSimpleObjectChangedRegist();
        socr.reduceCall();
        if (!socr.isEnd())
        {
            return;
        }
        if (isSuccess)
        {
            SimpleObjectChangedRegist sor = dbUtils.getSimpleObjectChangedRegist();
            sor.stopMonitorChange();
            String message = null;
            if (sor != null)
            {
                message = sor.getMessages();
            }
            if (now && _messageDispatcher != null)
            {
                _transaction.commit(null);
                _messageDispatcher.dispatchMessage(message);
            }
            else
            {
                _transaction.commit(message);
            }
        }
        else
        {
            _transaction.rollback();
        }
    }
    
    public void fileLock()
    {
        fileLock.lock();
    }
    
    public void fileUnLock()
    {
        fileLock.unlock();
    }
}
