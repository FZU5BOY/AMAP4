package com.example.amap.util.rount;

import android.content.Context;

import com.example.amap.CustomApplcation;
import com.example.amap.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/5/19.
 * 静态内部类 懒汉单例
 */
public class HeuryCache {
    public LinkedHashMap<Integer,Integer> hashMap=new LinkedHashMap<>(100);
    private static class LazyHolder {
        private static final HeuryCache INSTANCE = new HeuryCache();
    }
    private HeuryCache(){
    }
    public static final HeuryCache getInstance() {
        return LazyHolder.INSTANCE;
    }
    private static Context context = CustomApplcation.getInstance();

}
