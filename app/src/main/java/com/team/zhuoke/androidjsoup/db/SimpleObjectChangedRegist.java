package com.team.zhuoke.androidjsoup.db;

import android.text.TextUtils;

import com.team.zhuoke.androidjsoup.db.interfaces.IMessage;
import com.team.zhuoke.androidjsoup.db.interfaces.IMessageKey;
import com.team.zhuoke.androidjsoup.db.table.MyData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * 简易对象变更注册器
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class SimpleObjectChangedRegist
{
    private static final String ALL = "0";
    
    private static Map<Integer, Class> keyClass = new HashMap<Integer, Class>();
    
    private static Map<Class, Integer> classKey = new HashMap<Class, Integer>();
    
    private Map<Integer, Set<String>> keyIdsMap = new HashMap<Integer, Set<String>>();
    
    private boolean isMonitor = false;
    
    private int count = 0;
    
    private String messageToClient;
    
    static
    {
        regist(MyData.class, IMessageKey.KEY_CLOUDTASK);
    }
    
    public SimpleObjectChangedRegist()
    {
        count = 1;
    }
    
    public String getMessageToClient()
    {
        return messageToClient;
    }
    
    public void setMessageToClient(String messageToClient)
    {
        this.messageToClient = messageToClient;
    }
    
    /**
     * 增加调用次数.
     */
    public void increaseCall()
    {
        count++;
    }
    
    /**
     * 减少调用次数.
     */
    public void reduceCall()
    {
        count--;
    }
    
    /**
     * 是否完结.
     * @return 是否完结.
     */
    public boolean isEnd()
    {
        return count <= 0;
    }
    
    public void startMonitorChange()
    {
        isMonitor = true;
        clear();
    }
    
    public void stopMonitorChange()
    {
        isMonitor = false;
    }
    
    public boolean isMonitorChange()
    {
        return isMonitor;
    }
    
    /**
     * 注册类.
     * 
     * @param clazz
     *            类.
     * @param key
     *            键值．
     */
    private static void regist(Class clazz, int key)
    {
        keyClass.put(key, clazz);
        classKey.put(clazz, key);
    }
    
    public void clear()
    {
        keyIdsMap.clear();
    }
    
    /**
     * 对象有变化时的处理.
     * @param object
     */
    public void doChange(Object object)
    {
        if (object instanceof MyData)
        {
            MyData task = (MyData)object;
            String taskId = task.getId();
            addKeyId(IMessageKey.KEY_CLOUDTASK, taskId);
            return;
        }
    }
    
    public void filterMessage(int... message)
    {
        if (keyIdsMap.isEmpty())
        {
            return;
        }
    }
    
    public String getMessages()
    {
        if (keyIdsMap.isEmpty())
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Entry<Integer, Set<String>> e : keyIdsMap.entrySet())
        {
            sb.append(e.getKey()).append(IMessage.MSG_KIND_ID_SPLIT);
            Set<String> ids = e.getValue();
            boolean isFirst = true;
            for (String id : ids)
            {
                if (!isFirst)
                {
                    sb.append(IMessage.MSG_ID_SPLIT);
                }
                else
                {
                    isFirst = false;
                }
                sb.append(id);
            }
            sb.append(IMessage.MSG_KIND_SPLIT);
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    private void addKeyId(Integer key, String id)
    {
        Set<String> ids = getKeyIds(key);
        if (TextUtils.isEmpty(id))
        {
            id = ALL;
        }
        if (!ids.contains(id))
        {
            ids.add(id);
        }
    }
    
    private Set<String> getKeyIds(Integer key)
    {
        if (!keyIdsMap.containsKey(key))
        {
            Set<String> ids = new HashSet<String>();
            keyIdsMap.put(key, ids);
        }
        return keyIdsMap.get(key);
    }
}
