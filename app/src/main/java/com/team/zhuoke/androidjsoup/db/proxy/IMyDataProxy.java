package com.team.zhuoke.androidjsoup.db.proxy;


import android.text.TextUtils;

import com.team.zhuoke.androidjsoup.db.AppLock;
import com.team.zhuoke.androidjsoup.db.interfaces.IMyDataService;
import com.team.zhuoke.androidjsoup.db.table.MyData;

import java.util.List;

/**
 * 
 * 任务代理类
 *
 * <p>detailed comment
 * @author ztw 2016年5月12日
 * @see
 * @since 1.0
 */
public class IMyDataProxy implements IMyDataService
{
    private IMyDataService dataService;
    
    private AppLock appLock;
    
    public IMyDataProxy(IMyDataService dataService, AppLock appLock)
    {
        this.dataService = dataService;
        this.appLock = appLock;
    }

    @Override
    public MyData addMyData(MyData myData) {
        boolean isSuccess = true;
        try
        {
            appLock.lockWithMonitor();
            if (myData != null)
            {
                dataService.addMyData(myData);
            }
        }
        catch (Exception e)
        {
            isSuccess = false;
        }
        finally
        {
            appLock.unlockWithBroadcast(isSuccess);
        }
        return null;
    }

    @Override
    public void addMyData(List<MyData> myDatas) {
        boolean isSuccess = true;
        try
        {
            appLock.lockWithMonitor();
            if (myDatas != null && !myDatas.isEmpty())
            {
                dataService.addMyData(myDatas);
            }
        }
        catch (Exception e)
        {
            isSuccess = false;
        }
        finally
        {
            appLock.unlockWithBroadcast(isSuccess);
        }
    }

    @Override
    public MyData updateMyData(String myDataId, MyData myData) {
        boolean isSuccess = true;
        try
        {
            appLock.lockWithMonitor();
            if (!TextUtils.isEmpty(myDataId) && myData != null)
            {
                dataService.updateMyData(myDataId, myData);
            }
        }
        catch (Exception e)
        {
            isSuccess = false;
        }
        finally
        {
            appLock.unlockWithBroadcast(isSuccess);
        }
        return null;
    }

    @Override
    public void deleteMyData(String myDataId) {
        boolean isSuccess = true;
        try
        {
            appLock.lockWithMonitor();
            if (!TextUtils.isEmpty(myDataId))
            {
                dataService.deleteMyData(myDataId);
            }
        }
        catch (Exception e)
        {
            isSuccess = false;
        }
        finally
        {
            appLock.unlockWithBroadcast(isSuccess);
        }
    }

}
