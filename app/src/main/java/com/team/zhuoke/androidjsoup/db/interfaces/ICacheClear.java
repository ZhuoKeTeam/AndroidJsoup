package com.team.zhuoke.androidjsoup.db.interfaces;

/**
 * 
 * 清理缓存接口
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface ICacheClear
{
    /**
     * 清除某类型的缓存.
     * @param tClass 要清除缓存的类型.
     */
    public void clear(Class tClass);
}
