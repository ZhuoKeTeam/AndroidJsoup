package com.team.zhuoke.androidjsoup.db.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.text.TextUtils;

/**
 * 
 * 查询条件组对象
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class Criteria
{
    /**
     * 等于
     */
    private static final String EQ = "=";
    
    /**
     * 不等于
     */
    private static final String NE = "<>";
    
    /**
     * 大于
     */
    private static final String GT = ">";
    
    /**
     * 小于
     */
    private static final String LT = "<";
    
    /**
     * 大于等于
     */
    private static final String GE = ">=";
    
    /**
     * 小于等于
     */
    private static final String LE = "<=";
    
    /**
     * 匹配
     */
    private static final String LIKE = " like ";
    
    /**
     * 不匹配
     */
    private static final String NOTLIKE = " not like ";
    
    /**
     * 之间
     */
    private static final String BETWEEN = " between ";
    
    /**
     * 非之间
     */
    private static final String NOTBETWEEN = " not between ";
    
    /**
     * 为空
     */
    private static final String ISNULL = " is null";
    
    /**
     * 不为空
     */
    private static final String ISNOTNULL = " is not null";
    
    /**
     * 在...中
     */
    private static final String IN = " in ";
    
    /**
     * 不在...中
     */
    private static final String NOTIN = " not in ";
    
    /**
     * 无参数部分
     */
    protected List<String> criteriaWithoutValue;
    
    /**
     * 带一个参数部分
     */
    protected List<ConditionValue> criteriaWithSingleValue;
    
    /**
     * 带一组参数部分(in)
     */
    protected List<InValue> criteriaWithListValue;
    
    /**
     * (不)在...之中部分
     */
    protected List<BetweenValue> criteriaWithBetweenValue;
    
    /**
     * 构造查询条件对象.
     */
    Criteria()
    {
        criteriaWithoutValue = new ArrayList<String>();
        criteriaWithSingleValue = new ArrayList<ConditionValue>();
        criteriaWithListValue = new ArrayList<InValue>();
        criteriaWithBetweenValue = new ArrayList<BetweenValue>();
    }
    
    /**
     * 是否有查询条件.
     * @return true,有查询条件.
     */
    public boolean isValid()
    {
        return criteriaWithoutValue.size() > 0 || criteriaWithSingleValue.size() > 0 || criteriaWithListValue.size() > 0
            || criteriaWithBetweenValue.size() > 0;
    }
    
    /**
     * 添加SQL语句.
     * @param sql SQL语句.
     * @return 条件组.
     */
    public Criteria addSql(String sql)
    {
        criteriaWithoutValue.add(sql);
        return this;
    }
    
    /**
     * 添加等于条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria eq(String column, Object value)
    {
        return addSingleCondition(column, EQ, value);
    }
    
    /**
     * 添加不等于条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria ne(String column, Object value)
    {
        return addSingleCondition(column, NE, value);
    }
    
    /**
     * 字符串相加.
     * @param source 源字符串.
     * @param add 要添加的字符串.
     * @return 添加的结果.
     */
    private String add(String source, String add)
    {
        return new StringBuilder().append(source).append(add).toString();
    }
    
    /**
     * 添加like条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria like(String column, String value)
    {
        if (TextUtils.isEmpty(value))
        {
            return this;
        }
        return addSingleCondition(column, LIKE, new StringBuilder("%").append(value).append("%").toString());
    }
    
    /**
     * 添加左like条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria llike(String column, String value)
    {
        if (TextUtils.isEmpty(value))
        {
            return this;
        }
        return addSingleCondition(column, LIKE, new StringBuilder().append(value).append("%").toString());
    }
    
    /**
     * 添加右like条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria rlike(String column, String value)
    {
        if (TextUtils.isEmpty(value))
        {
            return this;
        }
        return addSingleCondition(column, LIKE, new StringBuilder("%").append(value).toString());
    }
    
    /**
     * 添加not like条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria notLike(String column, String value)
    {
        if (TextUtils.isEmpty(value))
        {
            return this;
        }
        return addSingleCondition(column, NOTLIKE, new StringBuilder("%").append(value).append("%").toString());
    }
    
    /**
     * 添加单参数条件.
     * @param column 数据列. 
     * @param add 条件语句.
     * @param value 值
     * @return 条件组.
     */
    private Criteria addSingleCondition(String column, String add, Object value)
    {
        if (value == null)
        {
            return this;
        }
        if (value instanceof String)
        {
            String str = (String)value;
            if (TextUtils.isEmpty(str))
            {
                return this;
            }
        }
        criteriaWithSingleValue.add(new ConditionValue(add(column, add), convertToString(value)));
        return this;
    }
    
    /**
     * 添加大于条件.
     * @param column 数据列.
     * @param d 比较值.
     * @return 条件组.
     */
    public Criteria gt(String column, Object d)
    {
        return addSingleCondition(column, GT, d);
    }
    
    /**
     * 添加小于条件.
     * @param column 数据列.
     * @param d 比较值.
     * @return 条件组.
     */
    public Criteria lt(String column, Object d)
    {
        return addSingleCondition(column, LT, d);
    }
    
    /**
     * 添加大于等于条件.
     * @param column 数据列.
     * @param d 比较值.
     * @return 条件组.
     */
    public Criteria ge(String column, Object d)
    {
        return addSingleCondition(column, GE, d);
    }
    
    /**
     * 添加小于等于条件.
     * @param column 数据列.
     * @param d 比较值.
     * @return 条件组.
     */
    public Criteria le(String column, Object d)
    {
        return addSingleCondition(column, LE, d);
    }
    
    /**
     * 添加在...之中条件.
     * @param column 数据列.
     * @param begin 开始值.
     * @param end 结束值.
     * @return 条件组.
     */
    public Criteria between(String column, Object begin, Object end)
    {
        if (begin == null || end == null)
        {
            return this;
        }
        if (begin == null)
        {
            return lt(column, end);
        }
        if (end == null)
        {
            return gt(column, begin);
        }
        criteriaWithBetweenValue
            .add(new BetweenValue(add(column, BETWEEN), convertToString(begin), convertToString(end)));
        return this;
    }
    
    /**
     * 添加不在...之中条件.
     * @param column 数据列.
     * @param begin 开始值.
     * @param end 结束值.
     * @return 条件组.
     */
    public Criteria notBetween(String column, Object begin, Object end)
    {
        criteriaWithBetweenValue
            .add(new BetweenValue(add(column, NOTBETWEEN), convertToString(begin), convertToString(end)));
        return this;
    }
    
    /**
     * 添加为空条件.
     * @param column 数据列.
     * @return 条件组.
     */
    public Criteria isNull(String column)
    {
        criteriaWithoutValue.add(add(column, ISNULL));
        return this;
    }
    
    /**
     * 添加不为空条件.
     * @param column 数据列.
     * @return 条件组.
     */
    public Criteria isNotNull(String column)
    {
        criteriaWithoutValue.add(add(column, ISNOTNULL));
        return this;
    }
    
    private List<String> convertList(List<Object> values)
    {
        List<String> result = new ArrayList<String>();
        for (Object object : values)
        {
            result.add(convertToString(object));
        }
        return result;
    }
    
    /**
     * 添加in条件.
     * @param column 数据列.
     * @param values 条件值.
     * @return 条件组.
     */
    public Criteria in(String column, List<Object> values)
    {
        if (values == null || values.isEmpty())
        {
            return this;
        }
        criteriaWithListValue.add(new InValue(add(column, IN), convertList(values)));
        return this;
    }
    
    /**
     * 添加not in条件.
     * @param column 数据列.
     * @param values 条件值.
     * @return 条件组.
     */
    public Criteria notIn(String condition, List<Object> values)
    {
        if (values == null || values.size() == 0)
        {
            return this;
        }
        criteriaWithListValue.add(new InValue(add(condition, NOTIN), convertList(values)));
        return this;
    }
    
    private String convertToString(Object value)
    {
        if (value instanceof Date)
        {
            Date date = (Date)value;
            return Long.toString(date.getTime());
        }
        else
        {
            return value.toString();
        }
    }
    
    /**
     * 得到无参数条件.
     * @return 无参数条件.
     */
    public List<String> getCriteriaWithoutValue()
    {
        return criteriaWithoutValue;
    }
    
    /**
     * 得到单参数条件.
     * @return 单参数条件.
     */
    public List<ConditionValue> getCriteriaWithSingleValue()
    {
        return criteriaWithSingleValue;
    }
    
    /**
     * 得到in条件.
     * @return in条件.
     */
    public List<InValue> getCriteriaWithListValue()
    {
        return criteriaWithListValue;
    }
    
    /**
     * 得到between条件.
     * @return between条件.
     */
    public List<BetweenValue> getCriteriaWithBetweenValue()
    {
        return criteriaWithBetweenValue;
    }
}
