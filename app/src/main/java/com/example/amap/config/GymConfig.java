package com.example.amap.config;

import com.example.amap.R;

/**
 * Created by Administrator on 2016/5/18.
 */
public class GymConfig implements BaseMapConfig{
    private static class LazyHolder{
        private static final GymConfig mInstance=new GymConfig();
    }
    private GymConfig(){}
    public static final GymConfig getInstance(){
        return LazyHolder.mInstance;
    }
    private String applicationId = "7d9c8d5335cab36401a5c9873283acf0";//这是Bmob的ApplicationId,用于初始化操作
    private String ClientID="uK0DxqYT0om1UXa9";
    private int MainToMipca = 6;
    private int Main_allfloor = 1;
    private double Main_SCAMAX=4000.0;//放大的最大比例
    private String Main_tpkPath[]={"/arcgis/gym/gym.tpk"};
    private String Main_floorName[]= {"F1"};
    private String GEO_FILENAME[] = {"/arcgis/gym/data/gym.geodatabase"};
    private double Real_Map_Size = 100;//总室内长约100m
    private double SHOP_LENGHT =1600.0;//制图时1:20000 而地图显示8cm(大概占一个屏幕) 总室内长约100m 即1:12.5 20000/12.5=1600 200格100米 1格0.5米
    private double GRID_SIZE=5.0;//网格大小
    private double MAP_PIX_SIZE=1000.0;//地图的像素长度
    private int CurrentFloorDefault=0;//当前楼层 飞机场为1 农大体育馆为0
    private int GRID_COUNT = 200;//栅格的行或列的数量= MAP_PIX_SIZE/GRID_SIZE
    private  int ROUNT_floorShange[] = {R.raw.gym};
    private double Location_houseNorth = 257.0;//在地图上 从入口进去的时候指南针的角度 30为预设
    private String[] MapPackageZip={"gym.zip"};
    private int featureTableIDs[]={3};
    private int StartScale=14;
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
