package com.team.zhuoke.androidjsoup.db;

import android.os.Message;

import com.team.zhuoke.androidjsoup.db.interfaces.IMessageListener;
import com.team.zhuoke.androidjsoup.util.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 监听注册器
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class ListenerRegister
{
    private Lock lock = new ReentrantLock();
    
    private List<IMessageListener> listeners;
    
    public ListenerRegister()
    {
        listeners = new ArrayList<IMessageListener>();
    }
    
    public void registListener(IMessageListener messageListener)
    {
        lock.lock();
        try
        {
            listeners.add(messageListener);
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void clearAll()
    {
        lock.lock();
        try
        {
            listeners.clear();
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void clearListeners()
    {
        lock.lock();
        try
        {
            listeners.clear();
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void unregistListener(IMessageListener messageListener)
    {
        lock.lock();
        try
        {
            listeners.remove(messageListener);
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void dispatchMessage(Message message)
    {
        lock.lock();
        try
        {
            for (IMessageListener messageListener : listeners)
            {
                ThreadUtils.run(new MessageRunnable(message, messageListener));
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    private class MessageRunnable implements Runnable
    {
        private Message message;
        
        private IMessageListener messageListener;
        
        public MessageRunnable(Message message, IMessageListener messageListener)
        {
            super();
            this.message = message;
            this.messageListener = messageListener;
        }
        
        @Override
        public void run()
        {
            messageListener.onMessageReceive(message);
        }
        
    }
}
