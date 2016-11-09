package com.team.zhuoke.androidjsoup.db.impl;

import com.team.zhuoke.androidjsoup.db.interfaces.IBaseService;
import com.team.zhuoke.androidjsoup.db.interfaces.IMyDataService;
import com.team.zhuoke.androidjsoup.db.table.MyData;

import java.util.List;

/**
 * Created by zhongruan on 2016/11/9.
 */

public class IMyDataServiceImpl implements IMyDataService{

    private IBaseService baseService;

    public IMyDataServiceImpl(IBaseService baseService)
    {
        this.baseService = baseService;
    }

    @Override
    public MyData addMyData(MyData myData) {
        return baseService.save(myData);
    }

    @Override
    public void addMyData(List<MyData> myDatas) {
        for(MyData data : myDatas){
            baseService.save(data);
        }
    }

    @Override
    public MyData updateMyData(String myDataId, MyData myData) {
        MyData myOldData = baseService.get(MyData.class, myDataId);
        if(myOldData != null){
            myOldData.setCurrent(myData.getCurrent());
            myOldData.setExpected(myData.getExpected());
            myOldData.setLoss(myData.getLoss());
            myOldData.setNoteId(myData.getNoteId());
            myOldData.setPrice(myData.getPrice());
            return baseService.save(myOldData);
        }
        return null;
    }

    @Override
    public void deleteMyData(String myDataId) {
        MyData myOldData = baseService.get(MyData.class, myDataId);
        if(myOldData != null){
            baseService.remove(myOldData);
        }
    }
}
