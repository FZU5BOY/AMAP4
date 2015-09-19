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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2015/7/31.
 */
public class AnalogLocation {
    private AMapPoint aMapPoint = new AMapPoint();
    private static Context context = CustomApplcation.getInstance();
    ShangeUtil su= ShangeUtil.getInstance();//栅格工具类
    public String ipstring="";
    private int LOCATION_OK = 1;
    private int LOCATION_NO_IN_MAP = 2;
    private int LOCATION_NET_ERROR = 3;
    private int LOCATION_LOCATION_IP_NOSET = 4;
    private int LOCATION_LOCATION_IP_ERROR = 5;
    private static class LazyHolder {
        private static final AnalogLocation INSTANCE = new AnalogLocation();
    }
    public static final AnalogLocation getInstance() {
        return LazyHolder.INSTANCE;
    }
    private AnalogLocation(){
    }
//    /**
//     * 初始化
//     *
//     * @param ctx
//     */
//    public AnalogLocation(Context ctx) {
//        context=ctx;
////        locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
////        location = locationManager.getLastKnownLocation(getProvider());
////        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
//    }

//得到定位ip
public String getLocationIP(){
    final String FILE_NAME = "ip.txt";
    FileInputStream fis = null;
    try {
        fis = this.context.openFileInput(FILE_NAME);
        if (fis.available() == 0) {
            return "";
        }
        byte[] readBytes = new byte[fis.available()];
        while (fis.read(readBytes) != -1) {
        }
        String text = new String(readBytes);
        return text;
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return "";
}
    //连接定位接口并得到json
    public String GetJSONString(URL url){
        StringBuilder sb = new StringBuilder();
        try
        {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(2000);//设置超时时间
            conn.setReadTimeout(2000);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()
                            , "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            return sb.toString();
        }
        catch (Exception e)
        {
//            e.printStackTrace();
        }
        return null;
    }
    //模拟定位
   public AMapPoint location()
    {
//        String locateIp = readFileByBytes("ip.txt");
        String locateIp="";
        AMapPoint bMapPoint=new AMapPoint();
        if(ipstring==null||"".equals(ipstring)){
         locateIp = getLocationIP();
            Log.i("zjx","ip is null,re get");
            ipstring=locateIp;
        }
        else locateIp=ipstring;
        if(locateIp==null||"".equals(locateIp)){
            bMapPoint.setState(LOCATION_LOCATION_IP_NOSET);
            return bMapPoint;
        }
        boolean isNetConnected = CommonUtils.isNetworkAvailable(this.context);
        if(!isNetConnected){
            bMapPoint.setState(LOCATION_NET_ERROR);
            return bMapPoint;
        }
        String jsonresult="";
        try {
            URL ipurl = new URL(locateIp);
             jsonresult = GetJSONString(ipurl);
            if(jsonresult==null||"".equals(jsonresult)){
                throw new Exception("LOCATION_LOCATION_IP_ERROR");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            bMapPoint.setState(LOCATION_LOCATION_IP_ERROR);
            return bMapPoint;
        }

        try{
            JSONArray jsonArray=new JSONArray(jsonresult);
            double tempx=(double) jsonArray.get(0);
            double tempy=(double) jsonArray.get(1);
            int tempz=(int)((double)jsonArray.get(2)+0.5);
            Point mapPoint = new Point(LocationToMapX(tempx),LocationToMapY(tempy));
            MyPoint locateMyPoint = new MyPoint(MapToMyPointX(mapPoint.getX()), MapToMyPointY(mapPoint.getY()),tempz);
            if(locateMyPoint.x>=su.SHANGESIZE-1||locateMyPoint.x<0||locateMyPoint.y>=su.SHANGESIZE-1||locateMyPoint.x<0||locateMyPoint.z>2||locateMyPoint.z<0){
                bMapPoint.setState(LOCATION_NO_IN_MAP);
                return bMapPoint;
            }
            bMapPoint.setState(LOCATION_OK);
            bMapPoint.setX(tempx);
            bMapPoint.setY(tempy);
            bMapPoint.setZ(tempz);
        }
        catch (Exception e){

            Log.e("zjx", "e:" + e);
            bMapPoint.setState(LOCATION_NO_IN_MAP);
            return bMapPoint;
        }
        return bMapPoint;
    }
    public MyPoint getMyPoint(){
        Point mapPoint = new Point(LocationToMapX(aMapPoint.getX()),LocationToMapY(aMapPoint.getY()));
        MyPoint newMyPoint = new MyPoint(MapToMyPointX(mapPoint.getX()), MapToMyPointY(mapPoint.getY()),aMapPoint.getZ());
        return newMyPoint;
    }
    //Point，MyPoint,Location转换
    /*
    Location:json得到的定位值
    Point:arcgis for android 地图上点的坐标值
    MyPoint:栅格坐标点
     */
    public int MapToMyPointX(Object x){
        return (int)((double)x / 20.0);
    }
    public int MapToMyPointY(Object y){
        return (int) (- (double)y / 20.0);
    }
    public double LocationToMapX(double x){
        return x*1000.0;
    }
    public double LocationToMapY(double y){
        return (y-1.0)*1000.0;
    }
    public double MapToLocationX(double x){
        return x/1000.0;
    }
    public double MapToLocationY(double y){
        return y/1000.0+1;
    }
    public AMapPoint getLocation(){
        return aMapPoint;
    }

    public void closeLocation(){
//        if(locationManager!=null){
//            if(locationListener!=null){
//                locationManager.removeUpdates(locationListener);
//                locationListener=null;
//            }
//            locationManager=null;
//        }
    }


}
