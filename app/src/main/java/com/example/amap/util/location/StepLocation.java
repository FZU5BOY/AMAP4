package com.example.amap.util.location;

import android.content.Context;
import android.util.Log;

import com.esri.core.geometry.Point;
import com.example.amap.CustomApplcation;
import com.example.amap.bean.AMapPoint;
import com.example.amap.util.CommonUtils;
import com.example.amap.util.rount.MyPoint;
import com.example.amap.util.rount.ShangeUtil;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2015/7/31.
 */
public class StepLocation {
    public  AMapPoint lastAMapPoint=new AMapPoint(0.5,0.5,1,30,0);
    private static class LazyHolder {
        private static final StepLocation INSTANCE = new StepLocation();
    }
    public static final StepLocation getInstance() {
        return LazyHolder.INSTANCE;
    }
    private StepLocation(){
    }
}
