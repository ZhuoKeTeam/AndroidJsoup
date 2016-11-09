package com.team.zhuoke.androidjsoup.db.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 批量执行的SQL
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class BatchSql
{
    private Class tClass;
    
    private ExecSqlItem[] sqls;
    
    public BatchSql(Class tClass, ExecSqlItem... sqls)
    {
        super();
        this.tClass = tClass;
        this.sqls = sqls;
    }
    
    /**
     * 得到用于分库的类.
     * @return 分库类.
     */
    public Class gettClass()
    {
        return tClass;
    }
    
    /**
     * 得到执行的SQL语句.
     * @return 执行的SQL语句.
     */
    public ExecSqlItem[] getSqls()
    {
        return sqls;
    }
    
    /**
     * 得到执行的SQL语句.
     * @return 执行的SQL语句.
     */
    public List<ExecSqlItem> getSqlList()
    {
        if (sqls == null || sqls.length == 0)
        {
            return Collections.EMPTY_LIST;
        }
        List<ExecSqlItem> result = new ArrayList<ExecSqlItem>();
        for (ExecSqlItem string : sqls)
        {
            result.add(string);
        }
        return result;
    }
}
