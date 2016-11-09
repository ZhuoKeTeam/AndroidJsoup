package com.team.zhuoke.androidjsoup.db.interfaces;

import android.os.Message;

/**
 * 
 * 广播消息监听器
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface IMessageListener
{
    /**
     * 接收到广播消息的处理.
     * @param message 广播消息.
     */
    public void onMessageReceive(Message message);
}
