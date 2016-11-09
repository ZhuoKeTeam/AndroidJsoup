package com.team.zhuoke.androidjsoup.db.interfaces;


import com.team.zhuoke.androidjsoup.db.TableClassRegist;

/**
 * 
 * 对象工厂
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface IBeanFactory
{
    /**
     * 得到Bean对象.
     * @param <T> 型别.
     * @param clazz 对象类型.
     * @return 获取的Bean对象,如果没有注册,则抛出NotRegistException.
     */
    public abstract <T> T getBean(Class<T> clazz);
    
    TableClassRegist getTableClassRegist();
}
