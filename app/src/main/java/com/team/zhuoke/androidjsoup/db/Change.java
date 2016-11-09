package com.team.zhuoke.androidjsoup.db;

import com.team.zhuoke.androidjsoup.db.interfaces.IChangeObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 
 * 更新对象
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class Change<T extends IChangeObject>
{
    /**
     * <code>键值</code>.
     */
    private Short key;
    
    /**
     * <code>最后更新时间</code>.
     */
    private Long lastRefreshTime;
    
    /**
     * 数据更新时间.
     */
    private Long dataRefreshTime;
    
    /**
     * 增量更新数据.
     */
    private List<ChangedObject<T>> datas;
    
    /**
     * <code>数据映射</code>.
     */
    private HashMap<String, ChangedObject<T>> dataMap;
    
    /**
     * <code>读写锁</code>.
     */
    private Lock lock = new ReentrantLock();
    
    /**
     * 构建更新对象.
     * 
     * @param key
     *            键值.
     */
    public Change(Short key)
    {
        this.key = key;
        refreshLastTime();
        this.datas = new LinkedList<ChangedObject<T>>();
        this.dataMap = new HashMap<String, ChangedObject<T>>();
    }
    
    /**
     * 得到得到键值.
     * 
     * @return 得到键值.
     */
    public Short getKey()
    {
        return key;
    }
    
    /**
     * 得到得到最后更新时间.
     * 
     * @return 得到最后更新时间.
     */
    public Long getLastRefreshTime()
    {
        return lastRefreshTime;
    }
    
    /**
     * 更新最后刷新时间.
     */
    public void refreshLastTime()
    {
        this.lastRefreshTime = System.currentTimeMillis();
    }
    
    /**
     * 得到数据更新时间.
     * @return 数据更新时间.
     */
    public Long getDataRefreshTime()
    {
        return dataRefreshTime;
    }
    
    /**
     * 添加变更的对象.
     * 
     * @param objects
     *            变更的对象.
     */
    public void addDatas(Long dataRefreshTime, List<ChangedObject<T>> objects)
    {
        try
        {
            this.dataRefreshTime = dataRefreshTime;
            lock.lock();
            for (ChangedObject<T> changedObject : objects)
            {
                if (dataMap.containsKey(changedObject.getKey()))
                {
                    ChangedObject<T> oldObject = dataMap.remove(changedObject.getKey());
                    datas.remove(oldObject);
                }
                datas.add(changedObject);
                dataMap.put(changedObject.getKey(), changedObject);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    /**
     * 得到一段时间内变更的对象.
     * @param entityId TODO
     * @param clientRefreshTime
     *            客户端最后更新时间.
     * 
     * @return 变更的对象.
     */
    public List<T> getChangedObjects(String entityId, Long clientRefreshTime)
    {
        if (datas.isEmpty() || clientRefreshTime >= dataRefreshTime)
        {
            return null;
        }
        List<T> lt = new ArrayList<T>();
        try
        {
            lock.lock();
            for (ChangedObject<T> obj : datas)
            {
                Long updateTime = obj.getUpdateTime();
                if (updateTime >= clientRefreshTime)
                {
                    lt.add(obj.getObject());
                }
            }
            return lt;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    /**
     * @param timeToClear
     */
    public void deleteDataBeforeTime(long timeToClear)
    {
        if (datas.isEmpty())
        {
            return;
        }
        List<ChangedObject<T>> lt = new ArrayList<ChangedObject<T>>();
        try
        {
            lock.lock();
            for (ChangedObject<T> obj : datas)
            {
                Long updateTime = obj.getUpdateTime();
                if (updateTime < timeToClear)
                {
                    lt.add(obj);
                }
            }
            if (lt.isEmpty())
            {
                return;
            }
            for (ChangedObject<T> t : lt)
            {
                dataMap.remove(t.getObject().getId());
                datas.remove(t);
            }
        }
        finally
        {
            lock.unlock();
        }
        
    }
    
    /**
     * @param lastTime
     */
    public void updateLastTime(long lastTime)
    {
        if (lastTime > this.lastRefreshTime)
        {
            this.lastRefreshTime = lastTime;
        }
    }
}
