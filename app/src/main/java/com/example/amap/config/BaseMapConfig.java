package com.example.amap.config;

import com.example.amap.R;

/**
 * Created by Administrator on 2016/5/18.
 */
public interface BaseMapConfig {
    public String getApplicationId();
    public String getClientID();
    public int getMainToMipca();
    public  int getMain_allfloor();
    public  double getMain_SCAMAX() ;
    public  String[] getMain_tpkPath();
    public  String[] getMain_floorName();
    public  double getReal_Map_Size() ;
    public  String[] getGeoFilename();
    public  double getShopLenght() ;
    public  double getGridSize() ;
    public  double getMapPixSize();
    public  int getGridCount() ;
    public  int getCurrentFloorDefault() ;
    public  int[] getROUNT_floorShange() ;
    public  double getLocation_houseNorth();
    public  String[] getMapPackageZip() ;
    public  int getStartScale();
    public  int[] getFeatureTableIDs();


}
