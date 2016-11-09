package com.team.zhuoke.androidjsoup.db.interfaces;

/**
 * 
 * 事务处理接口
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface ITransaction
{
    /**
     * 回滚接口.
     */
    public void rollback();
    
    /**
     * 事务提交接口. 事务仅提交到内存缓存中去，由独立线程同步到DB中去.
     * @param opUserId 操作人ID.
     * @param message
     *            提交后要广播的消息.
     */
    public int commit(String message);
    
    /**
     * 等待更新到数据库中.
     */
    public void waitFlush();
}
