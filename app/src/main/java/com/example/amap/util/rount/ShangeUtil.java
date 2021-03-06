package com.example.amap.util.rount;

import android.content.Context;
import android.util.Log;

import com.example.amap.CustomApplcation;
import com.example.amap.R;
import com.example.amap.config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/19.
 * 静态内部类 懒汉单例
 */
public class ShangeUtil {
    private static class LazyHolder {
        private static final ShangeUtil INSTANCE = new ShangeUtil();
    }
    private ShangeUtil(){
        Log.i("zjx","start to init");
        for(int i=0;i<Config.Main_allfloor;i++)
       shapeList.add(getMap(i));
    }
    public static final ShangeUtil getInstance() {
        return LazyHolder.INSTANCE;
    }
    public int[] HIT = {1}; //设置哪些值代表为障碍物
    public final int SHANGESIZE = Config.GRID_COUNT;
    private   List shapeList=new ArrayList();
    private static Context context = CustomApplcation.getInstance();

    public List getShapeList() {
        return shapeList;
    }

    //静态工厂方法
    public  int[][] getMap(int floor){
        InputStream inputStream=this.context.getResources().openRawResource(Config.ROUNT_floorShange[floor]);
        String all="";
        all=getString(inputStream);
        String as[]=all.split("\\s");
//        Log.i("zjx", "as:" + as.length);
        int asd[][] = new int[SHANGESIZE][SHANGESIZE];
        int in=-1;
        try {
            for(int i=0;i<as.length;i++){
                if(i%Config.GRID_COUNT==0)in++;
                asd[in][i%SHANGESIZE]=nuechange(Integer.parseInt(as[i]));
//                System.out.print(asd[in][i%SHANGESIZE]);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(asd[1][1]);
        }

//	 System.out.println(as.length);
        return asd;
    }
    public  int nuechange(int num){
//        if(num==-9999)return 0;//可走
//        else return 1;//不可走
        if(num==0)return 0;
        else return 1;
    }
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
