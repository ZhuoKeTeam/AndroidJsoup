package com.team.zhuoke.androidjsoup.db.query;

/**
 * 
 * between条件值对象
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class BetweenValue
{
    /**
     * 条件语句
     */
    private String condition;
    
    /**
     * 开始值
     */
    private String begin;
    
    /**
     * 结束值
     */
    private String end;
    
    /**
     * 构造Between条件值.
     * @param condition 条件语句.
     * @param begin 开始值.
     * @param end 结束值.
     */
    public BetweenValue(String condition, String begin, String end)
    {
        this.condition = condition;
        this.begin = begin;
        this.end = end;
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
     * 得到开始值.
     * @return 开始值.
     */
    public String getBegin()
    {
        return begin;
    }
    
    /**
     * 设置开始值.
     * @param begin 开始值.
     */
    public void setBegin(String begin)
    {
        this.begin = begin;
    }
    
    /**
     * 得到结束值.
     * @return 结束值.
     */
    public String getEnd()
    {
        return end;
    }
    
    /**
     * 设置结束值.
     * @param end 结束值.
     */
    public void setEnd(String end)
    {
        this.end = end;
    }
}
