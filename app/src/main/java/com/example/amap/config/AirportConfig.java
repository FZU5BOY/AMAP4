package com.example.amap.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.FeatureGroupInfo;

import com.example.amap.CustomApplcation;
import com.example.amap.R;

/**
 * Created by Administrator on 2016/5/18.
 */
public class AirportConfig implements BaseMapConfig {
    private static class LazyHolder{
        private static final AirportConfig mInstance=new AirportConfig();
    }
    public static final AirportConfig getInstance(){
        return LazyHolder.mInstance;
    }
    private AirportConfig(){}
    private  String applicationId = "7d9c8d5335cab36401a5c9873283acf0";//这是Bmob的ApplicationId,用于初始化操作
    private  String ClientID="uK0DxqYT0om1UXa9";
    private  int MainToMipca = 6;
    private  int Main_allfloor = 3;
    private  double Main_SCAMAX=4000.0;//放大的最大比例
    private  String Main_tpkPath[]={"/arcgis/b1/b1.tpk", "/arcgis/f1/f1.tpk", "/arcgis/f2/f2.tpk"};
    private  String Main_floorName[]= {"B1", "F1", "F2"};
    private  String GEO_FILENAME[] = {"/arcgis/b1/data/b1.geodatabase", "/arcgis/f1/data/f1.geodatabase", "/arcgis/f2/data/f2.geodatabase"};
    private  double Real_Map_Size = 400;//总室内长约400m
    private  double SHOP_LENGHT =400.0;//制图时1:20000 而地图显示8cm(大概占一个屏幕) 总室内长约400m 即1:50 20000/50=400 50格400米 1格8米
    private  double GRID_SIZE=20.0;//网格大小
    private  double MAP_PIX_SIZE=1000.0;//地图的像素长度
    private  int CurrentFloorDefault=1;//当前楼层 飞机场为1 农大体育馆为0
    private  int GRID_COUNT = 50;//栅格的行或列的数量= MAP_PIX_SIZE/GRID_SIZE
    private  int ROUNT_floorShange[] = {R.raw.b1shange, R.raw.f1shange, R.raw.f2shange};
    private  double Location_houseNorth = 30.0;//在地图上 从入口进去的时候指南针的角度 30为预设
    private  String[] MapPackageZip={"b1.zip","f1.zip","f2.zip"};
    private  int featureTableIDs[]={1,0,1};//原机场地图制作的bug
    private  int StartScale=50;

    @Override
    public String getApplicationId() {
        return applicationId;
    }

    @Override
    public String getClientID() {
        return ClientID;
    }

    @Override
    public int getMainToMipca() {
        return MainToMipca;
    }

    @Override
    public int getMain_allfloor() {
        return Main_allfloor;
    }

    @Override
    public double getMain_SCAMAX() {
        return Main_SCAMAX;
    }

    @Override
    public String[] getMain_tpkPath() {
        return Main_tpkPath;
    }

    @Override
    public String[] getMain_floorName() {
        return Main_floorName;
    }

    @Override
    public double getReal_Map_Size() {
        return Real_Map_Size;
    }

    @Override
    public String[] getGeoFilename() {
        return GEO_FILENAME;
    }

    @Override
    public double getShopLenght() {
        return SHOP_LENGHT;
    }

    @Override
    public double getGridSize() {
        return GRID_SIZE;
    }

    @Override
    public double getMapPixSize() {
        return MAP_PIX_SIZE;
    }

    @Override
    public int getGridCount() {
        return GRID_COUNT;
    }

    @Override
    public int getCurrentFloorDefault() {
        return CurrentFloorDefault;
    }

    @Override
    public int[] getROUNT_floorShange() {
        return ROUNT_floorShange;
    }

    @Override
    public double getLocation_houseNorth() {
        return Location_houseNorth;
    }

    @Override
    public String[] getMapPackageZip() {
        return MapPackageZip;
    }

    @Override
    public int getStartScale() {
        return StartScale;
    }

    @Override
    public int[] getFeatureTableIDs() {
        return featureTableIDs;
    }
}
