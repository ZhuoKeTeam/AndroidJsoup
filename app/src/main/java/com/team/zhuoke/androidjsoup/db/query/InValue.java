package com.team.zhuoke.androidjsoup.db.query;

import java.util.List;

/**
 * 
 * in条件对象
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class InValue
{
    /**
     * 条件语句
     */
    private String condition;
    
    /**
     * 比较值
     */
    private List<String> values;
    
    /**
     * 构造In条件对象.
     * @param condition 条件语句.
     * @param values 比较值.
     */
    InValue(String condition, List<String> values)
    {
        this.condition = condition;
        this.values = values;
    }
    
    /**
     * 得到条件语句.
     * @return 条件语句.
     */
    public String getCondition()
    {
        return condition;
    }
    
    /**
     * 设置条件语句.
     * @param condition 条件语句.
     */
    public void setCondition(String condition)
    {
        this.condition = condition;
    }
    
    /**
     * 得到比较值.
     * @return 比较值.
     */
    public List getValues()
    {
        return values;
    }
    
    /**
     * 设置比较值.
     * @param values 比较值.
     */
    public void setValues(List values)
    {
        this.values = values;
    }
}
