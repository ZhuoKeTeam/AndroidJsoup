package com.team.zhuoke.androidjsoup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.team.zhuoke.androidjsoup.db.DBUtils;
import com.team.zhuoke.androidjsoup.db.interfaces.IBaseService;
import com.team.zhuoke.androidjsoup.db.interfaces.IMyDataService;
import com.team.zhuoke.androidjsoup.db.table.MyData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;

/**
 * Created by WangQing on 2016/11/14.
 */
@RunWith(AndroidJUnit4.class)
public class AndroidTest {

    private static final String TAG = "AndroidTest";

    private IMyDataService myDataService = null;

    private IBaseService baseService = null;

    private Context context = null;

    @Before
    public void init() {
        context = InstrumentationRegistry.getTargetContext();
        DBUtils.getInstance().initDB(context);
        myDataService = DBUtils.getInstance().getMobileBeanFactory().getBean(IMyDataService.class);
        baseService = DBUtils.getInstance().getMobileBeanFactory().getBean(IBaseService.class);
    }

    @Test
    public void testInsert(){
        clearTable();
        MyData generateData = generateOneData();
        MyData insertMyData = insertOneData(generateData);
        MyData dbMyData = baseService.get(MyData.class, insertMyData.getId());
        assertNotNull(dbMyData);
        assertEquals("9098", dbMyData.getNoteId());
    }

    @Test
    public void testDelete(){
        clearTable();
        MyData generateData = generateOneData();
        MyData insertMyData = insertOneData(generateData);
        assertNotNull(insertMyData);
        MyData dbMyData = baseService.get(MyData.class, insertMyData.getId());
        assertNotNull(dbMyData);
        myDataService.deleteMyData(dbMyData.getId());
        List<MyData> myDatas = baseService.getAll(MyData.class);
        assertNull(myDatas);
    }

    @Test
    public void testUpdate(){
        clearTable();
        MyData generateData = generateOneData();
        MyData insertMyData = insertOneData(generateData);
        assertNotNull(insertMyData);
        insertMyData.setNoteId("2220");
        MyData updateData = myDataService.updateMyData(insertMyData.getId(), insertMyData);
        assertNotNull(updateData);
        assertEquals("2220", updateData.getNoteId());
        assertNotSame(insertMyData.getOpTime(), updateData.getOpTime());
    }

    @Test
    public void testQuery(){
        clearTable();
        MyData generateData = generateOneData();
        MyData insertMyData = insertOneData(generateData);
        assertNotNull(insertMyData);
        MyData updateData = baseService.get(MyData.class, insertMyData.getId());
        assertNotNull(updateData);
    }

    @After
    public void testClear(){
        clearTable();
    }

    private void clearTable(){
        baseService.clearCache();
        List<MyData> myDatas = baseService.getAll(MyData.class);
        if(myDatas != null && !myDatas.isEmpty()){
            for(MyData cMyData : myDatas){
                myDataService.deleteMyData(cMyData.getId());
            }
        }
    }

    private MyData insertOneData(MyData testMyData){
        return myDataService.addMyData(testMyData);
    }

    private MyData generateOneData(){
        MyData testMyData = new MyData();
        testMyData.setNoteId("9098");
        testMyData.setExpected("1.2");
        testMyData.setCurrent("2.3");
        testMyData.setLoss("-0.4");
        testMyData.setPrice("4.59");
        testMyData.setEndTime("2016-11-18 15:08");
        testMyData.setPageAddress("http://www.baidu.com");
        return testMyData;
    }

}
