package com.team.zhuoke.androidjsoup.db.query;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

/**
 * 
 * SQL查询构建器
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class SqlQueryBuilder
{
    private StringBuilder _sql = new StringBuilder();
    
    private List<String> _params = new ArrayList<String>();
    
    public SqlQueryBuilder()
    {
    
    }
    
    public SqlQueryBuilder(String sql)
    {
        _sql.append(sql);
    }
    
    /**
     * @param sql
     * @return
     */
    public SqlQueryBuilder addSql(String sql)
    {
        _sql.append(' ').append(sql);
        return this;
    }
    
    public SqlQueryBuilder addSql(String sql, String[] params)
    {
        _sql.append(' ').append(sql);
        if (params != null && params.length > 0)
        {
            for (String param : params)
            {
                _params.add(param);
            }
        }
        return this;
    }
    
    public SqlQueryBuilder addIfNotNull(Object param, String sql)
    {
        return addIfNotNull(param, sql, null);
    }
    
    public SqlQueryBuilder addIfNotNull(Object param, String sql, String[] params)
    {
        if (param == null)
        {
            return this;
        }
        if (param instanceof String)
        {
            String p = (String)param;
            if (TextUtils.isEmpty(p))
            {
                return this;
            }
        }
        return addSql(sql, params);
    }
    
    public SqlQueryBuilder addIfNull(Object param, String sql)
    {
        return addIfNull(param, sql, null);
    }
    
    public SqlQueryBuilder addIfNull(Object param, String sql, String[] params)
    {
        if (param != null)
        {
            return this;
        }
        if (param instanceof String)
        {
            String p = (String)param;
            if (!TextUtils.isEmpty(p))
            {
                return this;
            }
        }
        return addSql(sql, params);
    }
    
    public String getSql()
    {
        return this._sql.toString();
    }
    
    public String[] getParams()
    {
        return _params.toArray(new String[_params.size()]);
    }
}
