package com.example.kaylee.wechatrecordapplication;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by kaylee on 2018/3/15.
 */

public class XmlUtil {
    public static String getIMEI(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (manager.getDeviceId() != null && manager.getDeviceId().length() > 0) {
            return manager.getDeviceId();
        } else {
            return "";
        }
    }
    public static String getUin(String path) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(path));
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();// 产生第一个事件
            while (eventType != XmlPullParser.END_DOCUMENT) { //处理事件，不碰到文档结束就一直处理
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        // 不做任何操作或初开始化数据
                        break;
                    case XmlPullParser.START_TAG:
                        // 解析XML节点数据
                        // 获取当前标签名字
                        String tagName = parser.getName();
                        if ("int".equals(parser.getName())) {
                            String name = parser.getAttributeValue(0);
                            String value = parser.getAttributeValue(1);
                            Log.e("int", "name:" + name + ",value:" + value);
                            return value;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        // 单节点完成，可往集合里边添加新的数据
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                // 别忘了进入下一个元素并触发相应事件 ，不然就会死循环
                eventType = parser.next();
            }
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException:", e.toString());
        } catch (XmlPullParserException e) {
            Log.e("XmlPullParserException:", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
