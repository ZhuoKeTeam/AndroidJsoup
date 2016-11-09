package com.team.zhuoke.androidjsoup.db.interfaces;

import android.database.Cursor;

/**
 * 
 * 查询回调
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public interface IExecuteCallback
{
    public Object execute(Cursor cursor);
}
