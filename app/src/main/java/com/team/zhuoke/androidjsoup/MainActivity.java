package com.team.zhuoke.androidjsoup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.team.zhuoke.androidjsoup.db.DBUtils;
import com.team.zhuoke.androidjsoup.db.interfaces.IMyDataService;
import com.team.zhuoke.androidjsoup.db.table.MyData;
import com.team.zhuoke.entitys.Note;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    private IMyDataService myDataService;

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
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDataService = DBUtils.getInstance().getMobileBeanFactory().getBean(IMyDataService.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        parse.setOnClickListener(this);
        //String url = "https://gupiao.caimao.com/weixin/note/reader/view/53103";
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
    }

    private void setCount() {
        saveCount.setText("保存成功" + successCount + "个,失败" + filedCount + "个");
    }

    @Override
    public void onClick(View view) {
        //页数
        page = Integer.parseInt(inputLayout.getEditText().getText().toString());

        successCount = 0;
        filedCount = 0;

        getUrl(page);
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
        if (price != null) {
            priceString = price.text();
            //MyData data = new MyData(noteIdStr, exString, currString, lossString, priceString);
//            Log.e("4444", url + "\n" + data.toString());
//            boolean isSaved = saveData(data);
//            if (isSaved) {
//                handler.sendEmptyMessage(0x02);
//                urlList.add(url);
//            } else {
//                handler.sendEmptyMessage(0x03);
//            }
        }
    }

    /**
     * 保存数据
     */
    private boolean saveData(MyData data) {


        return true;
    }
}
