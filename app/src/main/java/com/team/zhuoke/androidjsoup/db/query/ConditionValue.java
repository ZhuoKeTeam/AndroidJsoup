package com.team.zhuoke.androidjsoup.db.query;

/**
 * 
 * 单参数条件对象
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class ConditionValue
{
    /**
     * 条件语句
     */
    private String condition;
    
    /**
     * 参数值
     */
    private String value;
    
    /**
     * 构造单参数条件对象.
     * @param condition 条件语句.
     * @param value 参数值.
     */
    public ConditionValue(String condition, String value)
    {
        this.condition = condition;
        this.value = value;
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
     * 得到参数值.
     * @return 参数值.
     */
    public String getValue()
    {
        return value;
    }
    
    /**
     * 设置参数值.
     * @param value 参数值.
     */
    public void setValue(String value)
    {
        this.value = value;
    }
}
