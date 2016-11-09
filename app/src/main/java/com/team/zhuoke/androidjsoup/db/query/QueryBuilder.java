package com.team.zhuoke.androidjsoup.db.query;

import android.text.TextUtils;

import com.team.zhuoke.androidjsoup.db.Base;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 查询建造者
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class QueryBuilder
{
    /**
     * 排序条件
     */
    private List<String> orderBy;
    
    /**
     * 查询条件
     */
    private List<Criteria> criterias;
    
    /**
     * 当前条件组对象
     */
    private Criteria currentCriteria;
    
    /**
     * 记录开始位置
     */
    private Integer first;
    
    /**
     * 记录结束位置
     */
    private Integer end;
    
    /**
     * 构造查询建造者.
     */
    public QueryBuilder()
    {
        this.orderBy = new ArrayList<String>();
        this.criterias = new ArrayList<Criteria>();
        newCriteria();
    }
    
    public static QueryBuilder newInstance()
    {
        QueryBuilder qb = new QueryBuilder();
        qb.eq(Base.ISVALID, Base.TRUE);
        return qb;
    }
    
    /**
     * 创建一个新的查询条件组.
     * @return 新的查询条件组.
     */
    public Criteria newCriteria()
    {
        Criteria criteria = new Criteria();
        criterias.add(criteria);
        currentCriteria = criteria;
        return criteria;
    }
    
    /**
     * 得到记录开始位置.
     * @return 记录开始位置.
     */
    public Integer getFirst()
    {
        return first;
    }
    
    /**
     * 设置记录开始位置.
     * @param first 记录开始位置.
     */
    public void setFirst(Integer first)
    {
        this.first = first;
    }
    
    /**
     * 得到记录结束位置.
     * @return 记录结束位置.
     */
    public Integer getEnd()
    {
        return end;
    }
    
    /**
     * 设置记录结束位置.
     * @param end 记录结束位置.
     */
    public void setEnd(Integer end)
    {
        this.end = end;
    }
    
    /**
     * 添加SQL语句.
     * @param sql SQL语句.
     * @return 条件组.
     */
    public Criteria addSql(String sql)
    {
        return currentCriteria.addSql(sql);
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
        return currentCriteria.between(column, begin, end);
    }
    
    /**
     * 添加等于条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria eq(String column, Object value)
    {
        return currentCriteria.eq(column, value);
    }
    
    /**
     * 添加大于等于条件.
     * @param column 数据列.
     * @param d 比较值.
     * @return 条件组.
     */
    public Criteria ge(String column, Object d)
    {
        return currentCriteria.ge(column, d);
    }
    
    /**
     * 添加大于条件.
     * @param column 数据列.
     * @param d 比较值.
     * @return 条件组.
     */
    public Criteria gt(String column, Object d)
    {
        return currentCriteria.gt(column, d);
    }
    
    /**
     * 添加in条件.
     * @param column 数据列.
     * @param values 条件值.
     * @return 条件组.
     */
    public Criteria in(String condition, List values)
    {
        return currentCriteria.in(condition, values);
    }
    
    /**
     * 添加不为空条件.
     * @param column 数据列.
     * @return 条件组.
     */
    public Criteria isNotNull(String column)
    {
        return currentCriteria.isNotNull(column);
    }
    
    /**
     * 添加为空条件.
     * @param column 数据列.
     * @return 条件组.
     */
    public Criteria isNull(String column)
    {
        return currentCriteria.isNull(column);
    }
    
    /**
     * 添加小于等于条件.
     * @param column 数据列.
     * @param d 比较值.
     * @return 条件组.
     */
    public Criteria le(String column, Object d)
    {
        return currentCriteria.le(column, d);
    }
    
    /**
     * 添加like条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria like(String column, String value)
    {
        
        return currentCriteria.like(column, value);
    }
    
    /**
     * 添加小于条件.
     * @param column 数据列.
     * @param d 比较值.
     * @return 条件组.
     */
    public Criteria lt(String column, Object d)
    {
        return currentCriteria.lt(column, d);
    }
    
    /**
     * 添加不等于条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria ne(String column, Object value)
    {
        return currentCriteria.ne(column, value);
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
        return currentCriteria.notBetween(column, begin, end);
    }
    
    /**
     * 添加not in条件.
     * @param column 数据列.
     * @param values 条件值.
     * @return 条件组.
     */
    public Criteria notIn(String condition, List values)
    {
        return currentCriteria.notIn(condition, values);
    }
    
    /**
     * 添加not like条件.
     * @param column 数据列.
     * @param value 值.
     * @return 条件组.
     */
    public Criteria notlike(String column, String value)
    {
        return currentCriteria.notLike(column, value);
    }
    
    /**
     * 添加正序排序条件.
     * @param column 列名.
     * @return 当前查询建造者.
     */
    public QueryBuilder orderByAsc(String column)
    {
        if (TextUtils.isEmpty(column))
        {
            return this;
        }
        orderBy.add(column);
        return this;
    }
    
    /**
     * 添加反序排序条件.
     * @param column 列名.
     * @return 当前查询建造者.
     */
    public QueryBuilder orderByDesc(String column)
    {
        if (TextUtils.isEmpty(column))
        {
            return this;
        }
        orderBy.add(new StringBuilder(column).append(" desc").toString());
        return this;
    }
    
    /**
     * 得到排序条件.
     * @return 排序条件.
     */
    public List<String> getOrderBy()
    {
        return orderBy;
    }
    
    /**
     * 得到查询条件.
     * @return 查询条件.
     */
    public List<Criteria> getCriterias()
    {
        return criterias;
    }
}
