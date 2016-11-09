package com.team.zhuoke.androidjsoup.db.interfaces;

/**
 * 
 * 变更对象接口．
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface IChangeObject
{
    /**
     * 得到键值.
     * @return 键值.
     */
    public String getId();
    
    /**
     * 是否修改过.
     * @param newObject 新对象.
     * @return 是否修改过.
     */
    public boolean isChanged(Object newObject);
}
