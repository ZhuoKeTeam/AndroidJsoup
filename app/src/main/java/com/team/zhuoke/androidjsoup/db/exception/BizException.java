package com.team.zhuoke.androidjsoup.db.exception;

public class BizException extends RuntimeException
{
    /**
     * <code>[serialVersionUID]</code>.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 默认异常构造器.
     */
    public BizException()
    {
        super();
    }
    
    /**
     * 根据异常信息和原生异常构造对象.
     * 
     * @param message
     *            异常信息.
     * @param cause
     *            原生异常.
     */
    public BizException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
    
    /**
     * 根据异常信息构造对象.
     * 
     * @param message
     *            异常信息.
     */
    public BizException(final String message)
    {
        super(message);
    }
    
    /**
     * 根据原生异常构造对象.
     * 
     * @param cause
     *            原生异常.
     */
    public BizException(final Throwable cause)
    {
        super(cause);
    }
}
