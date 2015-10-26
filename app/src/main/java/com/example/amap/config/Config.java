package com.example.amap.config;

import com.example.amap.R;

/** 系统变量
 * @ClassName: Config
 * @Description: TODO
 * @author smile
 * @date 2014-6-17 上午9:40:11
 */
public class Config {

	public static String applicationId = "7d9c8d5335cab36401a5c9873283acf0";//这是Bmob的ApplicationId,用于初始化操作
	public static String ClientID="uK0DxqYT0om1UXa9";
	public static int MainToMipca = 6;
	public static int Main_allfloor = 3;
	public static double Main_SCAMAX=4000.0;//放大的最大比例
	public static String Main_tpkPath[]={"/arcgis/b1/b1.tpk", "/arcgis/f1/f1.tpk", "/arcgis/f2/f2.tpk"};
	public static String Main_floorName[]= {"B1", "F1", "F2"};
	public static String GEO_FILENAME[] = {"/arcgis/b1/data/b1.geodatabase", "/arcgis/f1/data/f1.geodatabase", "/arcgis/f2/data/f2.geodatabase"};
    public static double Real_Map_Size = 400;//总室内长约400m
	public static double SHOP_LENGHT =400.0;//制图时1:20000 而地图显示8cm(大概占一个屏幕) 总室内长约400m 即1:50 20000/50=400 50格400米 1格8米
	public static double GRID_SIZE=20.0;//网格大小
	public static double MAP_PIX_SIZE=1000.0;//地图的像素长度
	public static int CurrentFloorDefault=1;//当前楼层 飞机场为1 农大体育馆为0
	public static int GRID_COUNT = 50;//栅格的行或列的数量= MAP_PIX_SIZE/GRID_SIZE
	public static  int ROUNT_floorShange[] = {R.raw.b1shange, R.raw.f1shange, R.raw.f2shange};
	public static double Location_houseNorth = 30.0;//在地图上 从入口进去的时候指南针的角度 30为预设
	public static String[] MapPackageZip={"b1.zip","f1.zip","f2.zip"};
    public static int featureTableIDs[]={1,0,1};//原机场地图制作的bug
    public static int StartScale=50;
//	public static String applicationId = "7d9c8d5335cab36401a5c9873283acf0";//这是Bmob的ApplicationId,用于初始化操作
//	public static String ClientID="uK0DxqYT0om1UXa9";
//	public static int MainToMipca = 6;
//	public static int Main_allfloor = 1;
//	public static double Main_SCAMAX=4000.0;//放大的最大比例
//	public static String Main_tpkPath[]={"/arcgis/gym/gym.tpk"};
//	public static String Main_floorName[]= {"F1"};
//	public static String GEO_FILENAME[] = {"/arcgis/gym/data/gym.geodatabase"};
//	public static double Real_Map_Size = 100;//总室内长约100m
//	public static double SHOP_LENGHT =1600.0;//制图时1:20000 而地图显示8cm(大概占一个屏幕) 总室内长约100m 即1:12.5 20000/12.5=1600 200格100米 1格0.5米
//	public static double GRID_SIZE=5.0;//网格大小
//	public static double MAP_PIX_SIZE=1000.0;//地图的像素长度
//	public static int CurrentFloorDefault=0;//当前楼层 飞机场为1 农大体育馆为0
//	public static int GRID_COUNT = 200;//栅格的行或列的数量= MAP_PIX_SIZE/GRID_SIZE
//	public static  int ROUNT_floorShange[] = {R.raw.gym};
//	public static double Location_houseNorth = 257.0;//在地图上 从入口进去的时候指南针的角度 30为预设
//	public static String[] MapPackageZip={"gym.zip"};
//	public static int featureTableIDs[]={3};
//	public static int StartScale=14;

}
