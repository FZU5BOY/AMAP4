package com.example.amap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.amap.bean.AMapPoint;
import com.example.amap.util.location.AnalogLocation;
import com.example.amap.util.rount.MyPoint;

public class LocationService extends Service {
    private AnalogLocation analogLocation=null;
    private boolean threadDisable=false;
    private final static String TAG=LocationService.class.getSimpleName();
//    private MyPoint lastMyPoint=null;
    private AMapPoint lastAMapPoint=new AMapPoint();
    @Override
    public void onCreate() {
        super.onCreate();

        analogLocation=new AnalogLocation();

        new Thread(new Runnable(){
            @Override
            public void run() {
                while (!threadDisable) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(analogLocation!=null){ //当结束服务时analogLocation为空
                        //获取经纬度
                        AMapPoint location=analogLocation.location();
//                        MyPoint newMyPoint=analogLocation.getMyPoint();
                        if(lastAMapPoint.equal(location)){
                            //什么都不做
                        }
                        else {
                            lastAMapPoint=location;
                            Log.i("zjx", location.toString());
                            //发送广播
                            Intent intent = new Intent();
                            intent.putExtra("ax", location.getX());
                            intent.putExtra("ay", location.getY());
                            intent.putExtra("az", location.getZ());
                            intent.putExtra("astate", location.getState());
                            intent.putExtra("amapid", location.getAmapId());
                            intent.putExtra("adetail", location.getDetailAddress());
                            intent.setAction("com.example.amap.service.LocationService");
                            sendOrderedBroadcast(intent, null);
                        }
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