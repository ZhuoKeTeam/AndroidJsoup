package com.team.zhuoke.androidjsoup.db.interfaces;

/**
 * 
 * 消息广播器
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface IMessageDispatcher
{
    /**
     * 广播消息.
     * @param message 消息内容.
     */
    public void dispatchMessage(String message);
}
