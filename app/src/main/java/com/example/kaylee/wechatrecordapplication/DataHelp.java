package com.example.kaylee.wechatrecordapplication;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;


/**
 * Created by zxf on 2016/10/25.
 */

public class DataHelp {

    /**
     * 操作数据库读取微信消息
     * @param context
     * @param path
     */
    public static void readWeChatDatabase(Context context,String path) {
        SQLiteDatabase.loadLibs(context);

        String password= (MD5Util.md5(XmlUtil.getIMEI(context)+XmlUtil.getUin(Constants.uinPath))).substring(0,7).toLowerCase();
        Log.e("password", "password:"+password );

        SQLiteDatabaseHook hook = new SQLiteDatabaseHook(){
            public void preKey(SQLiteDatabase database){
            }
            public void postKey(SQLiteDatabase database){
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };
        try {
            long time=System.currentTimeMillis();
            SQLiteDatabase db =  SQLiteDatabase.openDatabase(path, password, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS, hook);
            long time2=System.currentTimeMillis();
            long time_1=time2-time;
            Toast.makeText(context,"time_3:"+time_1, Toast.LENGTH_SHORT).show();
            Log.e("readWeChatDatabase", "time_3:"+time_1 );
            int count=0;
            Cursor c = db.rawQuery("select * from message" , null);
            while (c.moveToNext()) {
                int _id = c.getInt(c.getColumnIndex("msgId"));
                String content= c.getString(c.getColumnIndex("content"));
                count++;
                Log.e("readWeChatDatabase", "content:"+content );
            }
            c.close();
            db.close();
            long time_2=System.currentTimeMillis()-time2;
            Toast.makeText(context,"time_4:"+time_2+",count:"+count , Toast.LENGTH_SHORT).show();
            Log.e("readWeChatDatabase", "time_4:"+time_2+",count:"+count );
        } catch (Exception e) {
            Log.e("e", "readWeChatDatabase: "+e.toString() );
        }
    }
    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(Context context,String oldPath, String newPath) {
        long time=System.currentTimeMillis();
        deleteFolderFile(newPath,true);
        long time2=System.currentTimeMillis();
        long time_1=time2-time;
        Log.e("copyFile", "time_1:"+time_1 );
        InputStream inStream=null;
        FileOutputStream fs=null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                GetVideoFileName(oldPath);
                inStream = new FileInputStream(oldPath);
                //读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[2048];
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                long time_2=System.currentTimeMillis()-time2;
                Log.e("copyFile", "time_2:"+time_2);
                readWeChatDatabase(context,newPath);//对copy出来的数据进行操作
            }
        }catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }finally {
            try {
                if (inStream!=null) {
                    inStream.close();
                }
                if (fs!=null){
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 获取当前目录下所有的mp4文件
    public static Vector<String> GetVideoFileName(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为MP4结尾
                if (filename.trim().toLowerCase().endsWith(".mp4")) {
                    vecFile.add(filename);
                }
            }
        }
        return vecFile;
    }
}