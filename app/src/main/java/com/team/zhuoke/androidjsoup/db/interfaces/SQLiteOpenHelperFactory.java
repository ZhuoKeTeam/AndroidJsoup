package com.team.zhuoke.androidjsoup.db.interfaces;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * SQLite数据库工厂类
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface SQLiteOpenHelperFactory
{
    /**
     * 得到一个新的SqliteOpenHelper对象.
     * @param context
     * @param name
     * @param factory
     * @param version
     * @return
     */
    public SQLiteOpenHelper newSQliteSqLiteOpenHelper(Context context, String name,
                                                      SQLiteDatabase.CursorFactory factory, int version);
}
