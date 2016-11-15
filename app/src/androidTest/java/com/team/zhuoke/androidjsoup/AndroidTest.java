package com.team.zhuoke.androidjsoup;

import android.content.Context;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.Toast;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by WangQing on 2016/11/14.
 */
@RunWith(AndroidJUnit4.class)
public class AndroidTest {
    private static final String TAG = "AndroidTest";

    @Test
    public void getContext() {
        final Context context = InstrumentationRegistry.getTargetContext();
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                    Toast.makeText(context, "显示时间很短： hello", Toast.LENGTH_LONG).show();
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "请在 logcat 里面查看： getContext() called");

    }

}
