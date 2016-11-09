package com.team.zhuoke.androidjsoup.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 一个存储操作包.
 * 对应的是一个事务中，需要存储的所有变动
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class StoragePack
{
    private static final String KEY = "%s_%s";
    
    private int id;
    
    /**
     * 要保存的内容.
     */
    private List<Storage> storages;
    
    /**
     * 事务提交完成后，发送的消息.
     */
    private String messages;
    
    /**
     * 临时缓存用.
     */
    private Map<String, Storage> tempCacheMap = null;
    
    public StoragePack(int id)
    {
        this.id = id;
        this.storages = new ArrayList<Storage>();
        this.tempCacheMap = new HashMap<String, Storage>();
    }
    
    /**
     *
     */
    public StoragePack(int id, String messages)
    {
        this.id = id;
        this.storages = new ArrayList<Storage>();
        this.tempCacheMap = new HashMap<String, Storage>();
        this.messages = messages;
    }
    
    public int getId()
    {
        return id;
    }
    
    private String getCacheKey(Class clazz, Serializable id)
    {
        return String.format(KEY, clazz.getName(), id);
    }
    
    public void setMessages(String messages)
    {
        this.messages = messages;
    }
    
    /**
     * 添加一个存储操作.
     * @param storage 存储操作.
     */
    public void addStorage(Storage storage)
    {
        storages.add(storage);
        Class clazz = storage.object.getClass();
        String id = storage.object.getId();
        
        tempCacheMap.put(getCacheKey(clazz, id), storage);
    }
    
    /**
     * 得到所有需要变动的操作.
     * @return 所有需要变更的操作.
     */
    public List<Storage> getStorages()
    {
        return this.storages;
    }
    
    /**
     * 得到临时缓存的数据.
     * @param tClass 类型.
     * @param id 主键.
     * @return 缓存结果，如果为null,则表示未缓存.
     */
    public Storage get(Class tClass, Serializable id)
    {
        String key = getCacheKey(tClass, id);
        if (tempCacheMap.containsKey(key))
        {
            return tempCacheMap.get(key);
        }
        return null;
    }
    
    /**
     * 得到需要发送的消息.
     * @return 需要发送的消息.
     */
    public String getMessages()
    {
        return messages;
    }
}
