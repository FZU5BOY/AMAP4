package com.example.amap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.amap.CustomApplcation;
import com.example.amap.bean.AMapPoint;
import com.example.amap.util.location.AnalogLocation;
import com.example.amap.util.rount.MyPoint;

public class LocationService extends Service {
    CustomApplcation customApplcation;
    private AnalogLocation analogLocation=null;
    private boolean threadDisable=false;
    private final static String TAG=LocationService.class.getSimpleName();
//    private MyPoint lastMyPoint=null;
    private AMapPoint lastAMapPoint=new AMapPoint();
    @Override
    public void onCreate() {
        super.onCreate();
        customApplcation=CustomApplcation.getInstance();
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
//                            customApplcation.setAmapx(lastAMapPoint.getX().toString());
                            lastAMapPoint=location;
                           if(location.getState()==1) customApplcation.GeoPoint(lastAMapPoint);
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
//    如果一个 Service 已经被启动，
//    其他代码再试图调用 startService() 方法，
//    是不会执行 onCreate() 的，
//    但会重新执行一次 onStart() 。

    //不用bindService()启动的原因，多个activity会启动service 不能以为某个调用Activity退出就也退出service了，手动stop
    @Override
    public void onStart(Intent intent, int startId){
        Log.i("zjx","LocationService onStart");
        super.onStart(intent, startId);
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