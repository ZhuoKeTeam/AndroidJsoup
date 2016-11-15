package com.team.zhuoke.androidjsoup.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 数据库帮助类
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String SQLFILE = "init.sql";
    
    private static final String CHARSET = "UTF8";

    private final String sql = "CREATE TABLE SHARES(\n" +
            "ID                   VARCHAR(32)           NOT NULL,\n" +
            "NOTEID               VARCHAR(255)          NULL,\n" +
            "EXPECTED             VARCHAR(255)          NULL,\n" +
            "CURRENT              VARCHAR(255)          NULL,\n" +
            "LOSS                 VARCHAR(32)           NULL,\n" +
            "PRICE                VARCHAR(32)           NULL,\n" +
            "ISVALID              INT2                  NOT NULL,\n" +
            "CREATETIME           VARCHAR(32)           NOT NULL,\n" +
            "OPTIME               VARCHAR(32)           NOT NULL,\n" +
            "LASTVER              INT8                  NOT NULL,\n" +
            "ENDTIME              VARCHAR(32)           NULL,\n" +
            "CONSTRAINT PK_SYSTEMTYPE PRIMARY KEY (ID)\n" +
            ");";
    
    public DBHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        execSql(db, "PRAGMA  default_synchronous  =  OFF");
        execSql(db, "PRAGMA  auto_vacuum  =  1");
        execSql(db, "PRAGMA  cache_size=8000");
        execSql(db, "PRAGMA  temp_store  =  MEMORY");
        try
        {
            db.beginTransaction();
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        catch (Throwable t)
        {
        }
        finally
        {
            db.endTransaction();
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    
    }
    
    private void execSql(SQLiteDatabase db, String sql)
    {
        try
        {
            db.execSQL(sql);
        }
        catch (Throwable expected)
        {
        }
    }
    
}
