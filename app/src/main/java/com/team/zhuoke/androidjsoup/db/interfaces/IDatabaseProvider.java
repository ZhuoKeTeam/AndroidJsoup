package com.team.zhuoke.androidjsoup.db.interfaces;


import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.team.zhuoke.androidjsoup.db.query.BatchSql;

/**
 * 
 * 数据源提供器
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface IDatabaseProvider
{
    /**
     * 得到数据库.
     * @param tClass 类型.
     * @return 得到数据库
     */
    SQLiteDatabase getReadableDatabase(Class tClass);
    
    /**
     * 排量处理SQL脚本，并提交事务.
     * @param batchSqls 处理脚本
     */
    void batchExecuteWithCommit(BatchSql... batchSqls);
    
    /**
     * 优化数据库.
     */
    void vacuumDatabase();
    
    /**
     * 得到是否需要重启.
     * @return 是否需要重启.
     */
    public Boolean getIsNeedRestart();
    
    /**
     * 重置数据库.
     * @param sp
     */
    void resetDB(SharedPreferences sp);
}
