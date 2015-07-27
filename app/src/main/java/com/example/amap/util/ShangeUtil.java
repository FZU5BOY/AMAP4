package com.example.amap.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2015/5/19.
 */
public class ShangeUtil {
    public final int SHANGESIZE =50;
    public  int[][] getMap(InputStream inputStream){
        String all="";
        all=getString(inputStream);
        String as[]=all.split("\\s");
//        Log.i("zjx", "as:" + as.length);
        int asd[][] = new int[SHANGESIZE][SHANGESIZE];
        int in=-1;
        try {
            for(int i=0;i<as.length;i++){
                if(i%50==0)in++;
                asd[in][i%SHANGESIZE]=nuechange(Integer.parseInt(as[i]));
                System.out.print(asd[in][i%SHANGESIZE]);
            }
//		 System.out.println(asd[30][15]);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(asd[1][1]);
        }

//	 System.out.println(as.length);
        return asd;
    }
    public  int nuechange(int num){
        if(num==-9999)return 0;
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
//                sb.append("\n");
            }
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
