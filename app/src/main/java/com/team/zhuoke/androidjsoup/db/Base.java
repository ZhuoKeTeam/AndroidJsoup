package com.team.zhuoke.androidjsoup.db;

/**
 * 
 * 数据持久化基类
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public abstract class Base extends AbstractStorage
{
    /**
     * [serialVersionUID]
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 是
     */
    public static final Short TRUE = (short)1;
    
    /**
     * 否
     */
    public static final Short FALSE = (short)0;
}
