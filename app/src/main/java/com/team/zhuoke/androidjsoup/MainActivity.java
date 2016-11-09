package com.team.zhuoke.androidjsoup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout inputLayout;
    private Button parse;
    private TextView text;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MyData data = (MyData) msg.obj;
            text.setText(data.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        parse.setOnClickListener(this);
        //String url = "https://gupiao.caimao.com/weixin/note/reader/view/53103";
    }

    private void initView() {
        inputLayout = (TextInputLayout) findViewById(R.id.url_edit);
        parse = (Button) findViewById(R.id.parse);
        text = (TextView) findViewById(R.id.show_text);
    }

    @Override
    public void onClick(View view) {
        final String url = inputLayout.getEditText().getText().toString();
        Log.e("---", url);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                parse(url);
            }
        };

        new Thread(runnable).start();
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
        String priceString = price.text();
        MyData data = new MyData(noteIdStr, exString, currString, lossString, priceString);

        Message msg = new Message();
        msg.obj = data;
        handler.sendMessage(msg);

        //保存吧
        saveData(data);
    }

    /**
     * 保存数据
     */
    private void saveData(MyData data) {

    }
}
