package com.team.zhuoke.androidjsoup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.zhuoke.androidjsoup.db.DBUtils;
import com.team.zhuoke.androidjsoup.db.interfaces.IBaseService;
import com.team.zhuoke.androidjsoup.db.interfaces.IMyDataService;
import com.team.zhuoke.androidjsoup.db.table.MyData;
import com.team.zhuoke.androidjsoup.util.FileUtil;
import com.team.zhuoke.entitys.Note;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout inputLayout;
    private Button parse;
    private TextView text;
    private TextView saveCount;
    private int successCount = 0;
    private int filedCount = 0;
    //装载内容页地址
    private ArrayList<String> urlList;
    private int page;//页数
    private Button copy2SDCardBtn;

    private IMyDataService myDataService;

    private IBaseService baseService;

    private boolean isCopying = false;

    private String oldText = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    StringBuilder builder = new StringBuilder();
                    for (String data : urlList) {
                        builder.append(data + "\n");
                    }
                    text.setText(builder);
                    break;
                case 0x02:
                    successCount++;
                    setCount();
                    break;
                case 0x03:
                    filedCount++;
                    setCount();
                    break;
                case 0x04:
                    copy2SDCardBtn.setText(oldText);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDataService = DBUtils.getInstance().getMobileBeanFactory().getBean(IMyDataService.class);
        baseService = DBUtils.getInstance().getMobileBeanFactory().getBean(IBaseService.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        parse.setOnClickListener(this);
        copy2SDCardBtn.setOnClickListener(this);
        //String url = "https://gupiao.caimao.com/weixin/note/reader/view/53103";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            initData();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            initData();
        }

    }

    /**
     * 解决权限问题
     */
    private void checkPermission() {
        Context context = this;

        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            initData();
        }
    }

    /**
     * 获取某一页的内容地址
     *
     * @param page
     */
    private void getUrl(final int page) {
        final ExecutorService cachedThreadPool = Executors.newFixedThreadPool(10);
        urlList = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();
                StringBuilder noteUrl = new StringBuilder("https://gupiao.caimao.com/weixin/note/square/success/" + page);
                for (int i = 1; i < 50; i++) {
                    String conUrl = noteUrl.toString() + "?p=" + i;
                    Request request = new Request.Builder().url(conUrl).get().build();
                    OkHttpClient client = new OkHttpClient();
                    String json = null;
                    try {
                        Response response = client.newCall(request).execute();
                        json = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Note note = gson.fromJson(json, Note.class);
                    if (note.getList() == null || note.getList().isEmpty() || note.getList().size() == 0) {
                        break;
                    } else {
                        for (Note.ListBean listBean : note.getList()) {
                            final String contentUrl = "https://gupiao.caimao.com/weixin/note/reader/view/" + listBean.getNoteId();
                            //    Log.e("---", contentUrl);
                            cachedThreadPool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    parse(contentUrl);
                                }
                            });

                        }
                    }
                }

                handler.sendEmptyMessage(0x01);
            }
        }.start();
    }

    private void initView() {
        inputLayout = (TextInputLayout) findViewById(R.id.url_edit);
        parse = (Button) findViewById(R.id.parse);
        text = (TextView) findViewById(R.id.show_text);
        saveCount = (TextView) findViewById(R.id.save_text);
        copy2SDCardBtn = (Button) findViewById(R.id.button2);
        oldText = copy2SDCardBtn.getText().toString();
    }

    private void setCount() {
        saveCount.setText("保存成功" + successCount + "个,失败" + filedCount + "个");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.parse:
                //页数
                page = Integer.parseInt(inputLayout.getEditText().getText().toString());

                successCount = 0;
                filedCount = 0;

                getUrl(page);
                break;
            case R.id.button2:
                if (!isCopying) {
                    isCopying = true;
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        int REQUEST_EXTERNAL_STORAGE = 1;
                        String[] PERMISSIONS_STORAGE = {
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        };
                        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            // We don't have permission so prompt the user
                            ActivityCompat.requestPermissions(
                                    this,
                                    PERMISSIONS_STORAGE,
                                    REQUEST_EXTERNAL_STORAGE
                            );
                        } else {
                            File appRootFile = new File(Environment.getExternalStorageDirectory(), "jsoup");
                            FileUtil.makeDirIfNotExist(appRootFile);
                            File dbRootFile = new File(appRootFile, "db");
                            FileUtil.makeDirIfNotExist(dbRootFile);
                            File dbFile = new File(dbRootFile, "jsoup.db");
                            if (dbFile.exists()) {
                                dbFile.delete();
                            } else {
                                try {
                                    dbFile.createNewFile();
                                } catch (IOException e) {

                                }
                            }
                            File dbOldFile = new File(getDatabasePath("jsoup") + ".db");
                            try {
                                String tips = String.format("%s%s", oldText, "--拷贝中--");
                                copy2SDCardBtn.setText(tips);
                                FileUtil.copyFile(dbOldFile, dbFile);
                                String tips2 = String.format("%s%s", oldText, "--拷贝完成--");
                                copy2SDCardBtn.setText(tips2);
                                handler.sendEmptyMessageDelayed(0x04, 1000);
                                isCopying = false;
                            } catch (IOException e) {
                                Log.e("TTT", e.getMessage(), e);
                                isCopying = false;
                            }
                        }
                    } else {
                        Toast.makeText(this, "拷贝失败，SDCard不可用", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "正在拷贝中", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 解析数据
     *
     * @param url
     */
    private void parse(String url) {
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
            List<MyData> oldMyDatas = baseService.getAll(MyData.class);
            if (oldMyDatas != null && !oldMyDatas.isEmpty()) {
                if (!oldMyDatas.contains(data)) {
                    Log.e("4444", url + "\n" + data.toString());
                    boolean isSaved = saveData(data);
                    if (isSaved) {
                        handler.sendEmptyMessage(0x02);
                        urlList.add(url);
                    } else {
                        handler.sendEmptyMessage(0x03);
                    }
                } else {
                    Log.e("4444", url + "\n" + data.getNoteId() + "已经添加过了");
                }
            } else {
                boolean isSaved = saveData(data);
                if (isSaved) {
                    handler.sendEmptyMessage(0x02);
                    urlList.add(url);
                } else {
                    handler.sendEmptyMessage(0x03);
                }
            }
        }
    }

    /**
     * 保存数据
     */
    private boolean saveData(MyData data) {
        Log.e("TTT", "--data.toString=" + data.toString() + "--");
        MyData storeData = myDataService.addMyData(data);
        if (storeData != null) {
            Log.e("TTT", "--storeData.toString=" + storeData.toString() + "--");
            return true;
        } else {
            Log.e("TTT", "--storeData is null--");
            return false;
        }
    }
}
