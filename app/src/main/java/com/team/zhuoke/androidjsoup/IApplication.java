package com.team.zhuoke.androidjsoup;

import android.app.Application;

import com.team.zhuoke.androidjsoup.db.DBUtils;

/**
 * Created by ztw on 2016/11/9.
 */

public class IApplication extends Application{

    private static IApplication INSTANCE = null;

    public static IApplication getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        DBUtils.getInstance().initDB(getApplicationContext());
    }
}
