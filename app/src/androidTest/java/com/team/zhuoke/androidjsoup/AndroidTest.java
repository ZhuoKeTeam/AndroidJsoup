package com.team.zhuoke.androidjsoup;

import android.content.Context;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.team.zhuoke.androidjsoup.db.DBUtils;
import com.team.zhuoke.androidjsoup.db.interfaces.IBaseService;
import com.team.zhuoke.androidjsoup.db.interfaces.IMyDataService;
import com.team.zhuoke.androidjsoup.db.table.MyData;
import com.team.zhuoke.entitys.Note;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;

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
        Log.d(TAG, "init() called");
        context = InstrumentationRegistry.getTargetContext();
        DBUtils.getInstance().initDB(context);
        myDataService = DBUtils.getInstance().getMobileBeanFactory().getBean(IMyDataService.class);
        baseService = DBUtils.getInstance().getMobileBeanFactory().getBean(IBaseService.class);
    }

    @Test
    public void getContext() {
        Log.i(TAG, "getContext: ");

        final Context context = InstrumentationRegistry.getTargetContext();
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "显示时间很短： hello", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void testInsert() {
        clearTable();
        MyData generateData = generateOneData();
        MyData insertMyData = insertOneData(generateData);
        MyData dbMyData = baseService.get(MyData.class, insertMyData.getId());
        assertNotNull(dbMyData);
        assertEquals("9098", dbMyData.getNoteId());
    }


    @Test
    public void testDelete() {
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
    public void testUpdate() {
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
    public void testQuery() {
        clearTable();
        MyData generateData = generateOneData();
        MyData insertMyData = insertOneData(generateData);
        assertNotNull(insertMyData);
        MyData updateData = baseService.get(MyData.class, insertMyData.getId());
        assertNotNull(updateData);
    }

    @After
    public void testClear() {
        Log.d(TAG, "end() called");
        clearTable();
    }

    private void clearTable() {
        baseService.clearCache();
        List<MyData> myDatas = baseService.getAll(MyData.class);
        if (myDatas != null && !myDatas.isEmpty()) {
            for (MyData cMyData : myDatas) {
                myDataService.deleteMyData(cMyData.getId());
            }
        }
    }

    private MyData insertOneData(MyData testMyData) {
        return myDataService.addMyData(testMyData);
    }

    private MyData generateOneData() {
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

    @Test
    public void testGuPiao() {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        String url = "https://gupiao.caimao.com/weixin/note/square/success/1?p=1";
//        String url = "https://www.baidu.com";
        RequestCall requestCall = OkHttpUtils.get().url(url).build();
        Log.i(TAG, "testNetWork: " + url);
        requestCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "onError() called with: call = [" + call + "], e = [" + e + "], id = [" + id + "]");
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "onResponse() called with: response = [" + response + "], id = [" + id + "]");
                Gson gson = new Gson();
                Note note = gson.fromJson(response, Note.class);

                if (note.getList() == null || note.getList().isEmpty() || note.getList().size() == 0) {
                    Assert.assertNull(note);
                }

                for (Note.ListBean listBean : note.getList()) {

                }
                final String contentUrl = "https://gupiao.caimao.com/weixin/note/reader/view/" + note.getList().get(0).getNoteId();
                Log.i(TAG, "contentUrl: " + contentUrl);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        parse(contentUrl, countDownLatch);
                    }
                }).start();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void parseData() {
        String url = "https://gupiao.caimao.com/weixin/note/reader/view/58004";

        parseElement(url, "doc_section");
    }

    private void parseElement(String url, String element) {
        Document html = null;
        try {
            html = Jsoup.parse(new URL(url), 10 * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element body = html.body();

        // NoteId
        Element noteId = body.getElementById(element);
        String noteIdStr = noteId.attr("data-note-id");


        Log.i(TAG, "parseElement: noteIdStr->" + noteIdStr);
    }

    /**
     * 解析数据
     *
     * @param url
     * @param countDownLatch
     */
    private void parse(String url, CountDownLatch countDownLatch) {
        Document html = null;
        try {
            html = Jsoup.parse(new URL(url), 10 * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element body = html.body();
        // NoteId
        Element noteId = body.getElementById("doc_section");
        String noteIdStr = noteId.attr("data-note-id");
        // 预期收益
        Element expected = body.getElementsByClass("note_detail").first()
                .getElementsByClass("grid").get(1).getElementsByTag("p").first();
        String exString = expected.text();
        // 当前收益
        Element current = body.getElementsByClass("note_detail").first()
                .getElementsByClass("grid").last().getElementsByTag("p").first();
        String currString = current.text();
        // 止损
        Element loss = body.getElementsByClass("note_info").first()
                .getElementsByClass("group").last().getElementsByClass("content").first();
        String lossString = loss.text();
        // 价格
        Element price = body.select("a.btn_red.full.fill.huge.m_t_10.no_radius").first();
        String priceString;
        // 结束时间
        Element endTime = body.select("div.content").first();
        String endTimeStr = endTime.text();
        if (price != null) {
            priceString = price.text();
            MyData data = new MyData();
            data.setNoteId(noteIdStr);
            data.setExpected(exString);
            data.setCurrent(currString);
            data.setLoss(lossString);
            data.setPrice(priceString);
            data.setEndTime(endTimeStr);
            data.setPageAddress(url);

            Log.i(TAG, "parse: " + data.toString());

        } else {
            Assert.assertTrue(false);
        }

        if (countDownLatch != null)
            countDownLatch.countDown();
        Assert.assertTrue(true);
    }

    @Test
    public void testNetWork() {
        String url = "https://gupiao.caimao.com/weixin/note/square/success/1?p=1";
//        String url = "https://www.baidu.com";
        RequestCall requestCall = OkHttpUtils.get().url(url).build();
        Log.i(TAG, "testNetWork: " + url);

        requestCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "onError() called with: call = [" + call + "], e = [" + e + "], id = [" + id + "]");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "onResponse() called with: response = [" + response + "], id = [" + id + "]");
            }
        });
    }

}
