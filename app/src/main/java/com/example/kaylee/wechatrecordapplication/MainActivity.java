package com.example.kaylee.wechatrecordapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Timer timer;
    TimerTask mTimerTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        timer = new Timer();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String old_path="/data/data/com.tencent.mm/MicroMsg/.2333c1061ae85dc0d907327fda5391b0";
        final String new_path="/data/data/copy/com.example.kaylee.wechatrecordapplication/EnMicroMsg.db";

        DataHelp.copyFile(this,old_path,new_path);
        mTimerTask =new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DataHelp.copyFile(MainActivity.this,old_path,new_path);
                    }
                });
            }
        };
        timer.schedule(mTimerTask, 0, 10000);//10秒一获取
        //Toast.makeText(this, pwd, Toast.LENGTH_SHORT).show();
    }
}
