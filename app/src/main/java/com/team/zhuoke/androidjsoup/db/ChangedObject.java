package com.team.zhuoke.androidjsoup.db;


import com.team.zhuoke.androidjsoup.db.interfaces.IChangeObject;

/**
 * 
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class ChangedObject<T extends IChangeObject>
{
    /**
     * 更新时间
     */
    private Long updateTime;
    
    /**
     * 更新的对象
     */
    private T object;
    
    /**
     * @param updateTime
     * @param object
     */
    public ChangedObject(Long updateTime, T object)
    {
        super();
        this.updateTime = updateTime;
        this.object = object;
    }
    
    /**
     * 得到得到更新时间.
     * @return 得到更新时间.
     */
    public Long getUpdateTime()
    {
        return updateTime;
    }
    
    /**
     * 得到更新对象.
     * @return 更新对象.
     */
    public T getObject()
    {
        return object;
    }
    
    /**
     * 得到主键.
     * @return 主键.
     */
    public String getKey()
    {
        return object.getId();
    }
}
