package com.team.zhuoke.androidjsoup.db.query;

/**
 * 
 * 一句可执行的SQL 用于删除、修改,不返回结果
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class ExecSqlItem
{
    /**
     * 执行的SQL.
     */
    private String sql;
    
    /**
     * 参数.
     */
    private String[] params;
    
    /**
     * @param sql
     * @param params
     */
    public ExecSqlItem(String sql, String[] params)
    {
        super();
        this.sql = sql;
        this.params = params;
    }
    
    /**
     * 得到执行的SQL.
     * @return 执行的SQL.
     */
    public String getSql()
    {
        return sql;
    }
    
    /**
     * 得到参数.
     * @return 参数.
     */
    public String[] getParams()
    {
        return params;
    }
}
