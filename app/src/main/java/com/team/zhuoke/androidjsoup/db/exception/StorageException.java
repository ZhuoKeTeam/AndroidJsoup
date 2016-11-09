package com.team.zhuoke.androidjsoup.db.exception;

/**
 * 
 * 存储出错异常
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class StorageException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public StorageException()
    {
    }
    
    public StorageException(String detailMessage)
    {
        super(detailMessage);
    }
    
    public StorageException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }
    
    public StorageException(Throwable throwable)
    {
        super(throwable);
    }
}
