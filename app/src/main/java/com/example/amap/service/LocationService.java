package com.example.amap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.amap.bean.AMapPoint;
import com.example.amap.util.location.AnalogLocation;

public class LocationService extends Service {
    private AnalogLocation analogLocation=null;
    private boolean threadDisable=false;
    private final static String TAG=LocationService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        analogLocation=new AnalogLocation(LocationService.this);

        new Thread(new Runnable(){
            @Override
            public void run() {
                while (!threadDisable) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(analogLocation!=null){ //当结束服务时analogLocation为空
                        //获取经纬度
                        AMapPoint location=analogLocation.location();
                        Log.i("zjx",location.toString());
                        //发送广播
                        Intent intent=new Intent();
                        intent.putExtra("ax", location==null?"":location.getX()+"");
                        intent.putExtra("ay", location==null?"":location.getY()+"");
                        intent.putExtra("az", location==null?"":location.getZ()+"");
                        intent.setAction("com.example.amap.service.LocationService");
                        sendBroadcast(intent);
                    }

                }
            }
        }).start();

    }

    @Override
    public void onDestroy() {
        threadDisable=true;
        if(analogLocation!=null){
            analogLocation.closeLocation();
            analogLocation=null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}