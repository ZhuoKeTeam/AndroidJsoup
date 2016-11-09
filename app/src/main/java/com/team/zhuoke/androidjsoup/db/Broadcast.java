package com.team.zhuoke.androidjsoup.db;

import android.os.Message;
import android.text.TextUtils;

import com.team.zhuoke.androidjsoup.db.interfaces.IMessageDispatcher;

import java.util.List;
import java.util.Set;

/**
 * 
 * 广播消息实现
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class Broadcast implements IMessageDispatcher
{
    private ListenerRegister listenerRegister;
    
    public Broadcast(ListenerRegister listenerRegister)
    {
        super();
        this.listenerRegister = listenerRegister;
    }
    
    /**
     * 广播消息.
     * @param message 消息内容.
     */
    public void dispatchMessage(String message)
    {
        if (TextUtils.isEmpty(message))
        {
            return;
        }
        List<Message> messages = MessageUtils.parseMessages(message);
        if (messages == null || messages.isEmpty())
        {
            return;
        }
        for (Message m : messages)
        {
            listenerRegister.dispatchMessage(m);
        }
    }
    
    /**
     * 广播消息.
     * 仅内部系统广播,不扩散.
     * @param message 消息内容.
     * @param ids 关联ID.
     */
    public void dispatchInternalMessage(int message, Set<String> ids)
    {
        Message message2 = new Message();
        message2.what = message;
        message2.obj = ids;
        listenerRegister.dispatchMessage(message2);
    }
    
    /**
     * 广播消息.
     * 仅内部系统广播,不扩散.
     * @param message 消息内容.
     * @param object 内部消息对象.
     */
    public void dispatchInternalMessage(int message, Object object)
    {
        Message message2 = new Message();
        message2.what = message;
        message2.obj = object;
        listenerRegister.dispatchMessage(message2);
    }
}
