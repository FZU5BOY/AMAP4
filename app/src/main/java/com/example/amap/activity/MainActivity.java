package com.example.amap.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.tasks.query.QueryParameters;
import com.example.amap.config.Config;
import com.example.amap.custom.MyToast;
import com.example.amap.R;
import com.example.amap.service.LocationService;
import com.example.amap.util.rount.HeuryCache;
import com.example.amap.util.rount.MyPoint;
import com.example.amap.util.rount.Node;
import com.example.amap.util.rount.PathFinding;
import com.example.amap.util.rount.PoiSearch;
import com.example.amap.util.rount.ShangeUtil;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Future;

import cn.bmob.im.BmobChat;


public class MainActivity extends BaseActivity {
    private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;
    final int HIDECALLOUTANDALLINFO = 12;//handle 消息值
    final int SHOWCURRENTFLOOR = 13;
    final int COMPLETEAAL = 14;
    final int LOCATION_START = 15;
    final int UPDATEGP = 16;
    final int LOCATION_CLOST = 17;
    final int LOCATION_ING = 18;
    final int MAKE_PATH_ALL = 19;
    final int UPDATESCALE = 20;
    final int FINDFIREND = 21;
    private final int LOCATION_OK = 1;
    private final int LOCATION_NO_IN_MAP = 2;
    private final int LOCATION_NET_ERROR = 3;
    private final int LOCATION_LOCATION_IP_NOSET = 4;
    private final int LOCATION_LOCATION_IP_ERROR = 5;
    private boolean flag = true;
    private boolean isFirstLocating = true;
    private boolean isLocating = false;

    private synchronized void setFlag() {
        flag = false;
    }

    //test git
    Graphic main_gp;//临时变量
    int main_fl;//临时变量 handle用
    boolean isloadok = true;//是否加载成功,判断是否正确引入地图包
    final int OFFSET = 0;//设置偏移量，即到达某点的大致范围就算到达;之前设为1感觉效果不是很好
    MapView mMapView = null;//地图
    MapViewHelper mvHelper;//帮助类，某些操作更快捷
    ShangeUtil su = ShangeUtil.getInstance();//栅格工具类
    HeuryCache heuryCache = HeuryCache.getInstance();//h 缓存
    Deque<List<Node>> paths = new ArrayDeque<>();//多点路径规划时用到
    Deque<Integer> pathId = new ArrayDeque<>();
    Deque<Integer> pointId = new ArrayDeque<>();
    Deque<MyPoint> midPoints = new ArrayDeque<>();
    //得到sd卡根
    final String extern = Environment.getExternalStorageDirectory().getPath();
    //tpk文件地址
    final String tpkPath[] = {"/arcgis/b1/b1.tpk", "/arcgis/f1/f1.tpk", "/arcgis/f2/f2.tpk"};
    //geo文件地址
    public static final String GEO_FILENAME[] =
            {"/arcgis/b1/data/b1.geodatabase", "/arcgis/f1/data/f1.geodatabase", "/arcgis/f2/data/f2.geodatabase"};
    int currentFloor = 1;//当前楼层 默认F1
    static int allfloor = 3; //所有楼层
    String rountstart = null;
    String rountend = null;
    ArrayList<String> rountmid = new ArrayList<>();
    Feature startFeature = null;//开始要素
    Feature endFeature = null;//结束要素  主要用来提供feature绘图
    Feature currentFeature = null;//当前要素
    public static MyPoint locateMyPoint = null;//当前定位地址
    MyPoint currentMyPoint = null;//当前所按地址 做为中间变量的目的
    MyPoint startMyPoint = null;//开始地址
    MyPoint endMyPoint = null;//结束地址
    List<Feature> features = new ArrayList<>();
    //各个控件
    Button search_main;
    Button search_video;
    Button font_up;
    Button font_down;
    Button font_middle;
    Button button_dingwei;
    Button shut_photo,bigger_map,smaller_map;
    Button poiDetBtn;
    int floorShange[] = {R.raw.b1shange, R.raw.f1shange, R.raw.f2shange};
    Button clear;
    TextView dingwei,scaleText;
    RelativeLayout allinfo;
    LinearLayout from_here;
    LinearLayout go_there;
    //底部四个
    RelativeLayout fixed;
    RelativeLayout rount;
    RelativeLayout tools;
    RelativeLayout person;
    //图层各个加载配置
    String geofilename[] = {extern + GEO_FILENAME[0], extern + GEO_FILENAME[1], extern + GEO_FILENAME[2]};
    String floorname[] = {"B1", "F1", "F2"};
    List<TiledLayer> mTileLayers = new ArrayList<>();
    GraphicsLayer mGraphicsLayer[] = {new GraphicsLayer(), new GraphicsLayer(), new GraphicsLayer()};
    GraphicsLayer loactionGraphicsLayer = new GraphicsLayer();
    static List<FeatureLayer> featureLayers = new ArrayList<>();
    private MyReceiver receiver = null;
    private MyReceiver2 receiver2 = null;
    View calloutView;//地图的callout
    Timer timer;//timer
    private static List<PoiSearch> ls = null;
    private MyToast toast;//自定义toast控件
    private static Handler viewHandler;//用于更新UI
    NotificationManager nm;//通知
    static final int NOTIFICATION_ID = 0x123;//通知的id
    private SharedPreferences setting;//is first

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//		Log.i("zjx", "main oncreate");
//create地图
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
        setContentView(R.layout.main);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        currentFloor = 1;//默认为F1
        mMapView = (MapView) findViewById(R.id.map);
        mvHelper = new MapViewHelper(mMapView);
        mMapView.setMapBackground(0xeeeeee, 0xffffff, 0, 0);//设置地图网格，背景样式
        //添加瓦片和绘制图层
        try {
            mTileLayers.add(new ArcGISLocalTiledLayer(extern + tpkPath[0]));
            mTileLayers.add(new ArcGISLocalTiledLayer(extern + tpkPath[1]));
            mTileLayers.add(new ArcGISLocalTiledLayer(extern + tpkPath[2]));
            mMapView.addLayer(mTileLayers.get(0));
            mMapView.addLayer(mGraphicsLayer[0]);
            mMapView.addLayer(mTileLayers.get(1));
            mMapView.addLayer(mGraphicsLayer[1]);
            mMapView.addLayer(mTileLayers.get(2));
            mMapView.addLayer(mGraphicsLayer[2]);
            mMapView.addLayer(loactionGraphicsLayer);
        } catch (Exception e) {
            Log.i("zjx", "未找到地图包");
            isloadok = false;
        }
        calloutView = View.inflate(this, R.xml.callout, null); //动态加载view
        //实例化控件
        dingwei = (TextView) findViewById(R.id.dingwei);
        button_dingwei = (Button) findViewById(R.id.button_dingwei);
        mMapView.setOnTouchListener(new TouchListener(MainActivity.this, mMapView));
        mMapView.setOnZoomListener(new OnZoomListener() {
                //缩放之前自动调用的方法
                @Override
                public void preAction(float pivotX, float pivotY, double factor) {
                    ShowLog("pre action");
                }
                //缩放之后自动调用的方法
                @Override
                public void postAction(float pivotX, float pivotY, double factor) {
                    ShowLog("post action");
                    viewHandler.sendEmptyMessage(UPDATESCALE);
                }
        });
        clear = (Button) findViewById(R.id.button_clear);
        mMapView.setMaxScale(6500);//1：6500
        mMapView.setMinScale(20000.0);//1：20000
        mMapView.centerAt(new Point(500.0, -500.0), false);//
        search_main = (Button) findViewById(R.id.search_main);//进入搜索
        search_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zjx", "click to search");
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        scaleText = (TextView)findViewById(R.id.scale_map_num);
        bigger_map =(Button) findViewById(R.id.bigger_map);
        smaller_map=(Button)findViewById(R.id.smaller_map);
        bigger_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.zoomin();
            }
        });
        smaller_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.zoomout();
            }
        });
        fixed = (RelativeLayout) findViewById(R.id.fixed);//寻找附近
        fixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zjx", "click to fixed");
                Intent intent = new Intent(MainActivity.this, FixedSearchActivity.class);
                if (locateMyPoint == null) {
                    intent.putExtra("islocate", false);
                } else {
//					1：50
                    //10000000
                    //9999999
                    intent.putExtra("islocate", true);
                    double churukou = getCloestfeature(getResources().getString(R.string.churukou));
                    intent.putExtra("churukou", churukou);
                    double dengjikou = getCloestfeature(getResources().getString(R.string.dengjikou));
                    intent.putExtra("dengjikou", dengjikou);
                    double anjian = getCloestfeature(getResources().getString(R.string.anjian));
                    intent.putExtra("anjian", anjian);
                    double shangdian = getCloestfeature(getResources().getString(R.string.shangdian));
                    intent.putExtra("shangdian", shangdian);
                    double atm = getCloestfeature(getResources().getString(R.string.atm));
                    intent.putExtra("atm", atm);
                    double weishengjian = getCloestfeature(getResources().getString(R.string.weishengjian));
                    intent.putExtra("weishengjian", weishengjian);
                    double dianti = getCloestfeature(getResources().getString(R.string.dianti));
                    intent.putExtra("dianti", dianti);
                }

                startActivityForResult(intent, 2);
            }
        });
        //点击进入详情页
        poiDetBtn = (Button) findViewById(R.id.poiDet);
        poiDetBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("zjx", "click to shop detail");
                Intent intent = new Intent(MainActivity.this, XListViewActivity.class);
                startActivity(intent);
            }
        });
        person = (RelativeLayout) findViewById(R.id.person);//个人中心
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zjx", "click to tow");
                if (userManager.getCurrentUser() != null) {
                    // 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
                    updateUserInfos();
                    mHandler.sendEmptyMessageDelayed(GO_HOME, 1000);
                } else {
                    mHandler.sendEmptyMessageDelayed(GO_LOGIN, 1000);
                }
//				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//				startActivity(intent);
            }
        });
        tools = (RelativeLayout) findViewById(R.id.tools);//工具
        tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zjx", "click to tow");
                Intent intent = new Intent(MainActivity.this, ToolsActivity.class);
                startActivity(intent);
            }
        });
        rount = (RelativeLayout) findViewById(R.id.rount);//路径规划，把当前的路径传给rount
        rount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zjx", "click to tow");
                Intent intent = new Intent(MainActivity.this, MakeRoadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("islocation", locateMyPoint != null);
                bundle.putString("mstart", rountstart);
                bundle.putString("mend", rountend);
                bundle.putStringArrayList("mmid", rountmid);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });


        allinfo = (RelativeLayout) findViewById(R.id.allinfo);
        font_middle = (Button) findViewById(R.id.font_middle);
        search_video = (Button) findViewById(R.id.search_video);//语音搜索
        search_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zjx", "click video");
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("Name", "video");
                startActivityForResult(intent, 0);
            }
        });
        shut_photo = (Button) findViewById(R.id.shut_photo);//截图
        shut_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShutPhoto(v);
            }
        });
        font_up = (Button) findViewById(R.id.font_up);//上楼按钮
        font_down = (Button) findViewById(R.id.font_down);//下楼按钮
        from_here = (LinearLayout) findViewById(R.id.from_here);
        go_there = (LinearLayout) findViewById(R.id.go_there);
        //从这么出发
        from_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromHere();
            }
        });
        //ui处理
        viewHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HIDECALLOUTANDALLINFO:
                        mMapView.getCallout().hide();
                        allinfo.setVisibility(View.GONE);
                        break;
                    case SHOWCURRENTFLOOR:
                        font_middle.setText(floorname[currentFloor]);
                        // We will spin off the initialization in a new thread
                        for (int i = 0; i < allfloor; i++) {
                            if (i != currentFloor) {
                                mMapView.getLayer(i * 2).setVisible(false);
                                mMapView.getLayer(i * 2 + 1).setVisible(false);
                            } else {
                                mMapView.getLayer(i * 2).setVisible(true);
                                mMapView.getLayer(i * 2 + 1).setVisible(true);
                            }
                        }
                        if (locateMyPoint != null) {
                            if (locateMyPoint.z != currentFloor)
                                loactionGraphicsLayer.setVisible(false);
                            else loactionGraphicsLayer.setVisible(true);
                        }
                        break;
                    case COMPLETEAAL:
                        try {
                            unregisterReceiver(receiver2);
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                        tryClearAllGra();
                        break;
                    case LOCATION_START:
                        dingwei.setText("定位中...");
                        dingwei.setVisibility(View.VISIBLE);
                        locateMyPoint = null;
                        loactionGraphicsLayer.removeAll();
                        break;
                    case LOCATION_ING:
                        dingwei.setVisibility(View.GONE);
//						locateMyPoint = null;
                        loactionGraphicsLayer.removeAll();
                        break;
                    case LOCATION_CLOST:
                        dingwei.setVisibility(View.GONE);
                        locateMyPoint = null;
                        loactionGraphicsLayer.removeAll();
                        break;
                    case LOCATION_NET_ERROR:
                        dingwei.setText(getResources().getText(R.string.location_error_net_tips));
                        dingwei.setVisibility(View.VISIBLE);
                        locateMyPoint = null;
                        loactionGraphicsLayer.removeAll();
                        break;
                    case LOCATION_LOCATION_IP_ERROR:
                        dingwei.setText(getResources().getText(R.string.location_error_url_unfind));
                        dingwei.setVisibility(View.VISIBLE);
                        locateMyPoint = null;
                        loactionGraphicsLayer.removeAll();
                        break;
                    case LOCATION_LOCATION_IP_NOSET:
                        dingwei.setText(getResources().getText(R.string.location_error_url_no_set));
                        dingwei.setVisibility(View.VISIBLE);
                        locateMyPoint = null;
                        loactionGraphicsLayer.removeAll();
                        break;
                    case LOCATION_NO_IN_MAP:
                        dingwei.setText(getResources().getText(R.string.location_error_local_inmap));
                        dingwei.setVisibility(View.VISIBLE);
                        locateMyPoint = null;
                        loactionGraphicsLayer.removeAll();
                        break;
                    case LOCATION_OK:
                        Bundle bundle = msg.getData();
                        double ax = bundle.getDouble("ax");
                        double ay = bundle.getDouble("ay");
                        int az = bundle.getInt("az");
                        PictureMarkerSymbol pic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.man));
                        Point mapPoint = new Point(LocationToMapX(ax), LocationToMapY(ay));

                        Graphic gp = new Graphic(mapPoint, pic);
                        MyPoint newMyPoint = new MyPoint(MapToMyPointX(mapPoint.getX()), MapToMyPointY(mapPoint.getY()), az);
                        //在servicel里面控制了 所以这句基本不会运行
                        if (locateMyPoint != null && newMyPoint.equal(locateMyPoint)) {
                            loactionGraphicsLayer.addGraphic(gp);
                            break;
                        }
                        locateMyPoint = newMyPoint;
                        if (az != currentFloor) {
                            currentFloor = az;
                            showcurrentfloor();
                        }
                        loactionGraphicsLayer.addGraphic(gp);
                        mMapView.centerAt(mapPoint, true);
                        mMapView.setScale(7000.0);
//                        viewHandler.sendEmptyMessage(UPDATESCALE);
                        break;
                    case UPDATEGP:
                        mGraphicsLayer[main_fl].addGraphic(main_gp);
                        break;
                    case MAKE_PATH_ALL:
                        makePathAll(locateMyPoint, midPoints.getFirst(), false);
                        break;
                    case UPDATESCALE:
                        int relScale=(int) ((mMapView.getScale()+1.0)/400.0);
                        scaleText.setText(relScale+"m");
                        break;
                    case FINDFIREND:{

                        }
                        break;
                    default:
                        break;
                }
            }
        };
        //到这儿去，较重要
        go_there.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goThere();
            }
        });


    }
    private void goThere(){
        Log.i("zjx", "go there");
        mMapView.getCallout().hide();
        endFeature = currentFeature;
        endMyPoint = currentMyPoint;
        PictureMarkerSymbol pic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ending));
        Point poi = new Point((double) endFeature.getAttributeValue("pointX"), (double) endFeature.getAttributeValue("pointy"));
        Graphic gp = new Graphic(poi, pic);
        mGraphicsLayer[currentFloor].addGraphic(gp);
        if (endMyPoint != null) {
            if (startMyPoint != null) {
                rountstart = (String) startFeature.getAttributeValue("nickname");
                rountend = (String) endFeature.getAttributeValue("nickname");
                clearMid();
                ClearAllGraphic();
                MakePath mp = new MakePath(getApplicationContext());
                mp.execute(startMyPoint, endMyPoint);
                currentFloor = startMyPoint.z;
                showcurrentfloor();
                Point poi2 = new Point(startMyPoint.x * 20.0, -startMyPoint.y * 20.0);
                mMapView.centerAt(poi2, true);
                mMapView.setScale(7000.0);
//                viewHandler.sendEmptyMessage(UPDATESCALE);
            } else if (locateMyPoint != null) {
                rountstart = getResources().getString(R.string.mylocation);
                rountend = (String) endFeature.getAttributeValue("nickname");
                clearMid();
                ClearAllGraphic();
                MakePath mp = new MakePath(getApplicationContext());
                mp.execute(locateMyPoint, endMyPoint);
                currentFloor = locateMyPoint.z;
                showcurrentfloor();
                Point poi2 = new Point(locateMyPoint.x * 20.0, -locateMyPoint.y * 20.0);
                mMapView.centerAt(poi2, true);
                mMapView.setScale(7000.0);
//                viewHandler.sendEmptyMessage(UPDATESCALE);
                //注册广播
                receiver2 = new MyReceiver2();
                IntentFilter filter = new IntentFilter();
                filter.setPriority(20);
                filter.addAction("com.example.amap.service.LocationService");
                registerReceiver(receiver2, filter);
            }
        }
    }
    private void ShutPhoto(View v) {
        String result = "";
        boolean isshutok = false;
        try {
            result = mapviewshot();
            Toast.makeText(getApplicationContext(), R.string.shut_ok, Toast.LENGTH_SHORT).show();
            isshutok = true;
        } catch (Exception e) {
            Log.e("zjx", "shut e:" + e.toString());
        }
        if (isshutok) {
            Intent intent = new Intent(MainActivity.this
                    , MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(
                    MainActivity.this, 4, intent, 0);
            Notification notify = new Notification.Builder(v.getContext())
                    // 设置打开该通知，该通知自动消失
                    .setAutoCancel(true)
                            // 设置显示在状态栏的通知提示信息
                    .setTicker("有新消息")
                            // 设置通知的图标
                    .setSmallIcon(R.drawable.iconfont_jieping)
                            // 设置通知内容的标题
                    .setContentTitle("新消息:截图成功")
                            // 设置通知内容
                    .setContentText("截图保存在：" + result)
                            // // 设置使用系统默认的声音、默认LED灯
                            // .setDefaults(Notification.DEFAULT_SOUND
                            // |Notification.DEFAULT_LIGHTS)
                            // 设置通知的自定义声音
//						.setSound(Uri.parse("android.resource://com.example.amap/"
//								+ R.raw.msg))
                    .setWhen(System.currentTimeMillis())
                            // 设改通知将要启动程序的Intent
                    .setContentIntent(pi).build();
            // 发送通知
            nm.notify(NOTIFICATION_ID, notify);

        }
    }
    private void fromHere() {
        Log.i("zjx", "from here");
        mMapView.getCallout().hide();
        startFeature = currentFeature;
        startMyPoint = currentMyPoint;
        PictureMarkerSymbol pic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.begining));
        Point poi = new Point((double) startFeature.getAttributeValue("pointX"), (double) startFeature.getAttributeValue("pointy"));
        Graphic gp = new Graphic(poi, pic);
        mGraphicsLayer[currentFloor].addGraphic(gp);
        //如果都非空可规划一条路径
        if (startMyPoint != null && endMyPoint != null) {
            clearMid();
            ClearAllGraphic();
            try {
                rountstart = (String) startFeature.getAttributeValue("nickname");
                rountend = (String) endFeature.getAttributeValue("nickname");
            }
            catch (Exception e){
                e.printStackTrace();
            }
            MakePath mp = new MakePath(from_here.getContext());
            mp.execute(startMyPoint, endMyPoint);
//					makePathAll(startMyPoint, endMyPoint);
        }
    }
    private class MyReceiver2 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            double ax = bundle.getDouble("ax");
            double ay = bundle.getDouble("ay");
            int az = bundle.getInt("az");
            int astate = bundle.getInt("astate");
            Bundle addBundle = getResultExtras(true);
            boolean ischanged = addBundle.getBoolean("ischanged", false);
            if (astate != LOCATION_OK) return;
            Point mapPoint = new Point(LocationToMapX(ax), LocationToMapY(ay));
            MyPoint locateMyPoint2 = new MyPoint(MapToMyPointX(mapPoint.getX()), MapToMyPointY(mapPoint.getY()), az);
            int result;
            if (!ischanged) {
                ShowLog("还在该点");
                result = 0;
            } else {
                MyPoint mi = null;
                try {
                    mi = midPoints.getFirst();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //还有中间点
                if (mi != null) {
                    //当前点与队列中第一个点(第一个目标点)相近
                    if (Math.abs(locateMyPoint.x - mi.x) <= OFFSET && Math.abs(locateMyPoint.y - mi.y) <= OFFSET && locateMyPoint.z == mi.z) {
                        mGraphicsLayer[currentFloor].updateGraphic(pathId.getFirst(), new Graphic(new Polyline(), new SimpleLineSymbol(Color.argb(255, 255, 22, 34), 10, SimpleLineSymbol.STYLE.SOLID)));
                        midPoints.removeFirst();
                        pathId.removeFirst();
                        paths.removeFirst();
                        if (midPoints.size() == 0) {
                            viewHandler.sendEmptyMessage(COMPLETEAAL);
                            ShowToast(R.string.go_end_success);
                            ShowLog("全部走完");
                            result = 55;
                        } else {
                            if (locateMyPoint.z < midPoints.getFirst().z) {
                                ShowToast(R.string.up_floor_ing);
                            } else if (locateMyPoint.z > midPoints.getFirst().z) {
                                ShowToast(R.string.down_floor_ing);
                            }
//						即到达了其中一个中转点，那么给予用户良好的提示，并进行接下来的路径规划
                            else {
                                ShowToast(R.string.go_mid_success);
                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                r.play();
                            }

                            result = 1;
                        }
                    } else {
                        result = 2;
                        ShowLog("正常规划");
                    }
                } else {
                    result = 2;
                    ShowLog("正常规划");
                    //正常规划}
                }
            }

            if (result != 0) {//0为原地不动
                if (result == 55) {
                    viewHandler.sendEmptyMessage(COMPLETEAAL);
                    return;
                }
                Message msg = new Message();
                msg.what = MAKE_PATH_ALL;
                viewHandler.sendMessage(msg);
//				makePathAll(locateMyPoint, midPoints.getFirst(), false);
                showcurrentfloor();
//				Point poi2 = new Point(locateMyPoint.x * 20.0, -locateMyPoint.y * 20.0);
//				mMapView.centerAt(poi2, true);
//				mMapView.setScale(7000.0);
            }
        }
    }
    //获取广播数据
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            double ax = bundle.getDouble("ax");
            double ay = bundle.getDouble("ay");
            int az = bundle.getInt("az");
            int astate = bundle.getInt("astate");
            switch (astate) {
                case LOCATION_OK:
                    viewHandler.sendEmptyMessage(LOCATION_ING);
                    if (isFirstLocating) {
                        ShowToast(R.string.location_success);
                        isFirstLocating = false;
                    }
                    Message msg = new Message();
                    msg.what = LOCATION_OK;
                    msg.setData(bundle);//mes利用Bundle传递数据
                    Point mapPoint = new Point(LocationToMapX(ax), LocationToMapY(ay));
                    MyPoint newMyPoint = new MyPoint(MapToMyPointX(mapPoint.getX()), MapToMyPointY(mapPoint.getY()), az);

                    if (locateMyPoint!=null && !newMyPoint.equal(locateMyPoint)) {//注意是不等号= =
                        Bundle addBundle = new Bundle();
                        addBundle.putBoolean("ischanged", true);
                        setResultExtras(addBundle);
                    }
                    viewHandler.sendMessage(msg);

                    break;
                case LOCATION_NET_ERROR:
//					abortBroadcast();
                    viewHandler.sendEmptyMessage(LOCATION_NET_ERROR);
                    break;
                case LOCATION_NO_IN_MAP:
//					abortBroadcast();
                    viewHandler.sendEmptyMessage(LOCATION_NO_IN_MAP);
                    break;
                case LOCATION_LOCATION_IP_ERROR:
//					abortBroadcast();
                    viewHandler.sendEmptyMessage(LOCATION_LOCATION_IP_ERROR);
                    break;
                case LOCATION_LOCATION_IP_NOSET:
//					abortBroadcast();
                    viewHandler.sendEmptyMessage(LOCATION_LOCATION_IP_NOSET);
                    break;
                default:
//					abortBroadcast();
                    break;
            }
        }
    }

    //清除中间元素
    public void clearMid() {
        midPoints.clear();
        pathId.clear();
        paths.clear();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    startAnimActivity(UserMainActivity.class);
//					finish();
                    break;
                case GO_LOGIN:
                    startAnimActivity(LoginActivity.class);
//					finish();
                    break;
            }
        }
    };

    //上楼点击事件
    public void TurnUP(View source) {
        if (currentFloor + 1 >= allfloor) {
            Log.i("zjx", "tai gao le");
        } else {
            viewHandler.sendEmptyMessage(HIDECALLOUTANDALLINFO);

            currentFloor += 1;
            showcurrentfloor();
        }
    }

    //下楼点击事件
    public void TurnDown(View source) {
        if (currentFloor <= 0) {
        } else {
            viewHandler.sendEmptyMessage(HIDECALLOUTANDALLINFO);
            currentFloor -= 1;
            showcurrentfloor();
        }
    }

    //更新callout内容
    private void updateContent(int drawable, String name) {

        if (calloutView == null)
            return;
        ImageView imageView = (ImageView) calloutView.findViewById(R.id.family_photo);
//		imageView.setImageResource(drawable);

        TextView tv_country = (TextView) calloutView.findViewById(R.id.text_name);
        tv_country.setText(name);
    }

    //寻找最近的扶梯或者电梯
    public Feature findClostedESOrELFeature(MyPoint START_POS, boolean tob1) {
        QueryParameters qParameters = new QueryParameters();
        String whereClause = "name like '%ES%'";
        if (tob1) whereClause = "name >= 'ES1' and name <= 'ES4' ";//如果是去b1楼的话的特别注意
        qParameters.setReturnGeometry(true);
        qParameters.setWhere(whereClause);
        CallbackListener<FeatureResult> callback = new CallbackListener<FeatureResult>() {
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            public void onCallback(FeatureResult featureIterator) {
                Log.i("zjx", "i m callback");
            }
        };
        Feature closeFeature = null;
        Future<FeatureResult> resultFuture = featureLayers.get(START_POS.z).selectFeatures(qParameters, FeatureLayer.SelectionMode.NEW, callback);
        Log.i("zjx", "resultFuture:" + resultFuture);
        try {
            FeatureResult results = resultFuture.get();

            if (results != null) {
                Log.i("zjx", "result no null");
                int min = 10000000;
                Feature feature0 = null;
                for (Object element : results) {
                    if (element instanceof Feature) {
                        feature0 = (Feature) element;
                        int nowsum = (Math.abs(MapToMyPointX(feature0.getAttributeValue("x1")) - START_POS.x) + Math.abs(MapToMyPointY(feature0.getAttributeValue("y1")) - START_POS.y));
                        if (nowsum < min) {
                            closeFeature = feature0;
                            min = nowsum;
                        }
                    }

                }
            }
        } catch (Exception e) {

        }
        return closeFeature;
    }

    //由任意两点制作的路径，跨楼层，是否第一次生成， 并把isfirst传值给makePathDetail
    public void makePathAll(MyPoint START_POS, MyPoint OBJECT_POS, boolean isfirst) {
        allinfo.setVisibility(View.GONE);
        if (endFeature != null) {
            PictureMarkerSymbol endpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ending));
            Point endpoi = new Point((double) endFeature.getAttributeValue("pointX"), (double) endFeature.getAttributeValue("pointy"));
            Graphic gp = new Graphic(endpoi, endpic);
            mGraphicsLayer[endMyPoint.z].addGraphic(gp);
        }
        if (START_POS.z != OBJECT_POS.z) {//两点之间的楼层不一样
            MyPoint esp = null;
            if (isfirst) {//如果是第一次 则需要寻找楼梯，否则不需要
                Feature fea = findClostedESOrELFeature(START_POS, OBJECT_POS.z == 0);
                if (fea == null) {
                    Log.i("zjx", "fea is null");
                }
                esp = new MyPoint(MapToMyPointX(fea.getAttributeValue("x1")), MapToMyPointY(fea.getAttributeValue("y1")));
                if (START_POS.z < OBJECT_POS.z) {
                    for (int i = START_POS.z; i <= OBJECT_POS.z; i++) {
                        PictureMarkerSymbol uppic;
                        if (i != OBJECT_POS.z) {

                            uppic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.shang));
                        } else
                            uppic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.tingzhi));
                        Point uppoi = new Point((double) fea.getAttributeValue("pointX"), (double) fea.getAttributeValue("pointy"));
                        Graphic upgp = new Graphic(uppoi, uppic);
                        mGraphicsLayer[i].addGraphic(upgp);
                    }
                } else {
                    for (int i = START_POS.z; i >= OBJECT_POS.z; i--) {
                        PictureMarkerSymbol downpic;
                        if (i != OBJECT_POS.z) {
                            downpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.xia));
                        } else
                            downpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.tingzhi));
                        Point downpoi = new Point((double) fea.getAttributeValue("pointX"), (double) fea.getAttributeValue("pointy"));
                        Graphic downgp = new Graphic(downpoi, downpic);
                        mGraphicsLayer[i].addGraphic(downgp);
                    }
                }
                //制作两段路径
                makePathDetail(START_POS, new MyPoint(esp.x, esp.y, START_POS.z), START_POS.z, isfirst);

                makePathDetail(new MyPoint(esp.x, esp.y, OBJECT_POS.z), OBJECT_POS, OBJECT_POS.z, isfirst);
            } else {
                //非第一次操作
                esp = midPoints.getFirst();
                makePathDetail(START_POS, esp, START_POS.z, isfirst);
            }


        } else {
            //同一层楼直接继续
            makePathDetail(START_POS, OBJECT_POS, START_POS.z, isfirst);
        }
    }

    //制作详细的路径 参数：起点 终点 操作楼层 是否第一次
    public void makePathDetail(MyPoint START_POS, MyPoint OBJECT_POS, int curfloor, boolean isfirst) {
        {
            ShowLog("启动寻路程序----");
            Date datea = new Date();
            PathFinding astar = null;
            List<Node> mylist = new ArrayList<>();
            try {
                astar = new PathFinding(curfloor);//寻路类
                Log.i("abc",astar.toString());
            } catch (Exception e) {
                Log.i("zjx", "e1:" + e);
            }
            try {
                //若第一次规划（异步任务时去做的） 添加节点和路径数据
                ShowLog("开始寻路----");
                Date dateb = new Date();
                if (isfirst) {
                    midPoints.addLast(OBJECT_POS);
                    mylist = astar.searchPath(START_POS, OBJECT_POS);
                    paths.addLast(mylist);
                }
                //不是第一次规划
                else {
                    //如果是走第一段
                    if (midPoints.size() > 0 && OBJECT_POS.equal(midPoints.getFirst())) {
                        //那么就需要进行走偏等提示
                        Log.i("zjx", "first point:" + midPoints.getFirst().toString());
                        int sub = isInPath(START_POS);//判断该点在首条路径的位置
                        int path_lenth = paths.getFirst().size();//首条路径的长度
                        //在原路径在 截取后半段即可
                        if (sub != -1) {
                            mylist = paths.getFirst().subList(sub, path_lenth);//重新切割list得到新的路径
                            Log.i("zjx", "不需要重新规划");
                            paths.removeFirst();
                            paths.addFirst(mylist);
                        }
                        //偏移路径重新规划
                        else {
                            mylist = astar.searchPath(START_POS, OBJECT_POS);
                            MyToast.makeText(getApplicationContext(), R.string.deviate_path, 0.7).show();
                            paths.removeFirst();
                            paths.addFirst(mylist);
                        }

                    }
                    //规划的路不是第一段，为后面的
                    else mylist = astar.searchPath(START_POS, OBJECT_POS);

                }
                Date datec = new Date();
                ShowLog("寻找路径成功，耗时" + (datec.getTime() - dateb.getTime()) + "ms");
                ShowLog("从加载map到寻路成功，耗时" + (datec.getTime() - datea.getTime()) + "ms");
                ShowLog("midpoint.size:" + midPoints.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //mylist空的情况即路径规划失败，可能为无法到达的点
            if (mylist != null) {
                Polyline polyline = new Polyline();
                int ls=mylist.size();
                for (int i = 0; i < ls; i++) {
                    Node aaa=mylist.get(i);
                    MyPoint pos = new MyPoint(aaa.X,aaa.Y);
                    Point p = new Point((pos.x * 20.0 + 10.0), -pos.y * 20.0 - 10.0);
                    if (i == 0) polyline.startPath(p);
                    else polyline.lineTo(p);
                    //放入cache
//                    if(i>8){
//                        int len=ls-i-1;
//                        heuryCache.hashMap.put(aaa.X*100+aaa.Y,len);
//                    }
                }
                SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(Color.argb(255, 255, 22, 34), 10, SimpleLineSymbol.STYLE.SOLID);
                Graphic graphic1 = new Graphic(polyline, simpleLineSymbol);
                //如果是第一次规划
                if (isfirst) {
                    //uid表示每段规划路径的标识
                    int uid = mGraphicsLayer[curfloor].addGraphic(graphic1);
                    pathId.addLast(uid);
                    Log.i("zjx", "uid:" + uid);
                } else {
                    ShowLog(pathId.toString());
                    try {
                        mGraphicsLayer[curfloor].updateGraphic(pathId.getFirst(), graphic1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    //得到该点在list的位置
    public int isInPath(MyPoint myPoint) {
        for (int i = 0; i < paths.getFirst().size(); i++) {
            if (myPoint.x == paths.getFirst().get(i).X && myPoint.y == paths.getFirst().get(i).Y) {
                return i;
            }
        }
        return -1;
    }

    //清除所有元素，线程
    public void tryClearAllGra() {
        startFeature = null;
        endFeature = null;
        currentFeature = null;
        endMyPoint = null;
        startMyPoint = null;
        currentMyPoint = null;
        rountstart = null;
        rountend = null;
        rountmid.clear();
        clearMid();
        ClearAllGraphic();

    }

    public void ClearAllGra(View source) {
//        tryClearAllGra();
        viewHandler.sendEmptyMessage(COMPLETEAAL);
    }


    //清除绘制图层
    public void ClearAllGraphic() {
        for (int i = 0; i < allfloor; i++) {
            mGraphicsLayer[i].removeAll();
        }
        mMapView.getCallout().hide();
        allinfo.setVisibility(View.GONE);
    }
    @Override
    protected void onStart(){
        super.onStart();
        ShowLog("onStart");
    }
    //Point(Map 0~1000 -1000~0)，MyPoint(0~50),Location(模拟定位 0~1)转换
    public int MapToMyPointX(Object x) {
        return (int) ((double) x / 20.0);
    }

    public int MapToMyPointY(Object y) {
        return (int) (-(double) y / 20.0);
    }

    public double LocationToMapX(double x) {
        return x * 1000.0;
    }

    public double LocationToMapY(double y) {
        return (y - 1.0) * 1000.0;
    }

    public double MapToLocationX(double x) {
        return x / 1000.0;
    }

    public double MapToLocationY(double y) {
        return y / 1000.0 + 1;
    }

    //定位
    public void GetLocation(View source) throws MalformedURLException {
        if (flag) {
            setFlag();
            if (!isLocating) {
                viewHandler.sendEmptyMessage(LOCATION_START);
                startService(new Intent(this, LocationService.class));
                receiver = new MyReceiver();
                IntentFilter filter = new IntentFilter();
                filter.setPriority(30);
                filter.addAction("com.example.amap.service.LocationService");
                registerReceiver(receiver, filter);
                isLocating = true;
            } else {
                viewHandler.sendEmptyMessage(LOCATION_CLOST);
                unregisterReceiver(receiver);
                //结束服务，如果想让服务一直运行就注销此句
                stopService(new Intent(this, LocationService.class));
                isLocating = false;
                isFirstLocating = true;
            }
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    flag = true;
                }
            }, 2000);
        }


    }




    //异步类，其实可被替代，用于初期的路径规划
    class MakePath extends AsyncTask<MyPoint, Integer, String> {
        //		ProgressDialog pdialog;
        Context mContext;

        public MakePath(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected String doInBackground(MyPoint... params) {

            MyPoint START_POS = params[0];
            MyPoint OBJECT_POS = params[1];
            if(START_POS==null||OBJECT_POS==null)return "fail";
            if (START_POS.z != OBJECT_POS.z) {
                Feature fea = findClostedESOrELFeature(START_POS, OBJECT_POS.z == 0);
                if (fea == null) {
                    Log.i("zjx", "fea is null");
                    return null;
                }
                MyPoint esp = new MyPoint(MapToMyPointX(fea.getAttributeValue("x1")), MapToMyPointY(fea.getAttributeValue("y1")));
                if (START_POS.z < OBJECT_POS.z) {
                    for (int i = START_POS.z; i <= OBJECT_POS.z; i++) {
                        PictureMarkerSymbol uppic;
                        if (i != OBJECT_POS.z) {
                            uppic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.shang));
                        } else
                            uppic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.tingzhi));
                        Point uppoi = new Point((double) fea.getAttributeValue("pointX"), (double) fea.getAttributeValue("pointy"));
                        Graphic upgp = new Graphic(uppoi, uppic);
                        mGraphicsLayer[i].addGraphic(upgp);
                    }
                } else {
                    for (int i = START_POS.z; i >= OBJECT_POS.z; i--) {
                        PictureMarkerSymbol downpic;
                        if (i != OBJECT_POS.z) {
                            downpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.xia));
                        } else
                            downpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.tingzhi));
                        Point downpoi = new Point((double) fea.getAttributeValue("pointX"), (double) fea.getAttributeValue("pointy"));
                        Graphic downgp = new Graphic(downpoi, downpic);
                        mGraphicsLayer[i].addGraphic(downgp);
                    }
                }
                makePathDetail(START_POS, new MyPoint(esp.x, esp.y, START_POS.z), START_POS.z, true);
                makePathDetail(new MyPoint(esp.x, esp.y, OBJECT_POS.z), OBJECT_POS, OBJECT_POS.z, true);
            } else {
                makePathDetail(START_POS, OBJECT_POS, START_POS.z, true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }

        @Override
        protected void onPreExecute() {

            if (startFeature != null) {
                PictureMarkerSymbol startpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.begining));
                Point poi = new Point((double) startFeature.getAttributeValue("pointX"), (double) startFeature.getAttributeValue("pointy"));
                Graphic gp = new Graphic(poi, startpic);
                mGraphicsLayer[startMyPoint.z].addGraphic(gp);
            }
            if (endFeature != null) {
                PictureMarkerSymbol endpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ending));
                Point endpoi = new Point((double) endFeature.getAttributeValue("pointX"), (double) endFeature.getAttributeValue("pointy"));
                Graphic gp = new Graphic(endpoi, endpic);
                mGraphicsLayer[endMyPoint.z].addGraphic(gp);
            }
            else if (endMyPoint != null) {
                PictureMarkerSymbol pic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ending));
                Point poi = new Point((double) (endMyPoint.x*20.0), (double)  (-endMyPoint.y*20.0));
                Graphic gp = new Graphic(poi, pic);
                mGraphicsLayer[endMyPoint.z].addGraphic(gp);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    //展示当前地图，隐藏其他楼层地图
    private void showcurrentfloor() {
        viewHandler.sendEmptyMessage(SHOWCURRENTFLOOR);
    }

    //初始化数据图层
    private void initializeRoutingAndGeocoding() {
        for (int i = 0; i < allfloor; i++) {
            Geodatabase geodatabase = null;
            try {
                geodatabase = new Geodatabase(geofilename[i]);
                List<GeodatabaseFeatureTable> table = geodatabase.getGeodatabaseTables();
                Log.i("zjx", "list:" + table);

                GeodatabaseFeatureTable mytable;
                if (i == 1) mytable = geodatabase.getGeodatabaseFeatureTableByLayerId(0);
                else mytable = geodatabase.getGeodatabaseFeatureTableByLayerId(1);

                Log.i("zjx", "mytable:" + mytable);
                featureLayers.add(new FeatureLayer(mytable));
                // Attempt to load the local geocoding and routing data
                mMapView.addLayer(featureLayers.get(i));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        trygetDataName();
    }

    //监听
    class TouchListener extends MapOnTouchListener {

        @Override
        public void onLongPress(MotionEvent point) {
            // Our long press will clear the screen
//			ClearAllGra(clear);


        }

        @Override
        public boolean onSingleTap(MotionEvent point) {
            try {
                mMapView.getCallout().hide();
                currentFeature = null;
                currentMyPoint = null;
                Point mapPoint = mMapView.toMapPoint(point.getX(), point.getY());
                Log.i("zjx", "mapPoint:" + mapPoint.toString());
                int index = 0;
                if (mapPoint != null) {
                    for (Layer layer : mMapView.getLayers()) {
                        if (layer instanceof FeatureLayer) {
                            if (index == currentFloor) {
                                FeatureLayer fLayer = (FeatureLayer) layer;
                                // Get the Graphic at location x,y
                                final long[] ids = fLayer.getFeatureIDs(point.getX(), point.getY(), 10);
                                if (ids == null || ids.length == 0) {
                                    Log.i("zjx", "id is null");
                                    allinfo.setVisibility(View.GONE);
                                    break;
                                }
                                Log.i("zjx", "the id0 is" + ids[0]);
                                Feature g = fLayer.getFeature(ids[0]);
                                currentFeature = g;
                                currentMyPoint = new MyPoint(MapToMyPointX(g.getAttributeValue("x1")), MapToMyPointY(g.getAttributeValue("y1")), currentFloor);
                                Log.i("zjx", "feature:" + g.getAttributes());
                                TextView text = (TextView) findViewById(R.id.poiname);
                                text.setText(g.getAttributeValue("nickname").toString());
                                Point poi = new Point((double) g.getAttributeValue("pointX"), (double) g.getAttributeValue("pointy"));
                                mMapView.centerAt(poi, true);
                                mMapView.setScale(7000.0);
//                                viewHandler.sendEmptyMessage(UPDATESCALE);
                                updateContent(R.drawable.ic_1, g.getAttributeValue("nickname").toString());
                                Callout mapCallout = mMapView.getCallout();
                                mapCallout.setCoordinates(poi);
                                mapCallout.setContent(calloutView);
                                mapCallout.show();
                                allinfo.setVisibility(View.VISIBLE);

                                break;
                            } else {
                                index++;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.i("zjx", "id e:" + e);
            }
            return true;
        }

        //后期拓展
        @Override
        public boolean onDoubleTap(MotionEvent point) {

            mMapView.zoomin();
            ShowLog("scale:" + mMapView.getScale());
            return true;
        }

        public TouchListener(Context context, MapView view) {
            super(context, view);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    //得到数据
    public static void trygetDataName() {
        ls = new ArrayList<>();
        QueryParameters qParameters = new QueryParameters();
//		qParameters.setGeometry(mMapView.getExtent());
        CallbackListener<FeatureResult> callback = new CallbackListener<FeatureResult>() {
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            public void onCallback(FeatureResult featureIterator) {
                //...
            }


        };
        for (int i = 0; i < allfloor; i++) {
            Future<FeatureResult> resultFuture = featureLayers.get(i).selectFeatures(qParameters, FeatureLayer.SelectionMode.NEW, callback);
            try {
                FeatureResult results = resultFuture.get();
                if (results != null) {
                    for (Object element : results) {
                        if (element instanceof Feature) {
                            Feature feature = (Feature) element;
                            String name = ((String) feature.getAttributeValue("nickname")).trim();
                            if ((!"".equals(name)) && name != null) {
                                ls.add(new PoiSearch(name, R.drawable.iconfont_search));
                            }
                        }
                    }
                }

            } catch (Exception e) {
            }

        }
    }

    public static List<PoiSearch> getDataName() {
        if (ls != null) return ls;
        trygetDataName();
        return ls;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    private boolean findFeatureCurrentFirst(Future<FeatureResult> resultFuture,int i){
        Log.i("zjx", "resultFuture:" + resultFuture);
        try {
            FeatureResult results = resultFuture.get();
            if (results != null) {
                ShowLog("results no null");
                Feature feature = null;
                Point poi = null;
                for (Object element : results) {
                    ShowLog("the element:" + element);
                    if (element instanceof Feature) {
                        feature = (Feature) element;
                        ShowLog("Feature feature = (Feature) element;:" + element);
                        // turn feature into graphic
//							Random r = new Random();
                        int color = Color.rgb(100, 100, 100);
                        SimpleFillSymbol sfs = new SimpleFillSymbol(color);
//							sfs.setAlpha(75);
                        Graphic graphic = new Graphic(feature.getGeometry(),
                                sfs);
                        // add graphic to layer
                        mGraphicsLayer[i].addGraphic(graphic);
                        PictureMarkerSymbol pic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.begining));
                        poi = new Point((double) feature.getAttributeValue("pointX"), (double) feature.getAttributeValue("pointy"));
                        Graphic gp = new Graphic(poi, pic);
                        mGraphicsLayer[i].addGraphic(gp);

                    }

                }
                if (feature != null) {
                    currentFloor=i;
                    showcurrentfloor();
                    mMapView.centerAt(poi, true);
                    mMapView.setScale(7000.0);
//                        viewHandler.sendEmptyMessage(UPDATESCALE);
                    //个人感觉搜索还是不要显示的好
                    TextView text = (TextView) findViewById(R.id.poiname);
                    text.setText(feature.getAttributeValue("nickname").toString());
                    allinfo.setVisibility(View.VISIBLE);
                    currentFeature = feature;
                    currentMyPoint = new MyPoint(MapToMyPointX(feature.getAttributeValue("x1")), MapToMyPointY(feature.getAttributeValue("y1")), i);
                    return true;
                }
            }

        } catch (Exception e) {
            Log.i("zjx", "e:" + e);
        }
        return false;
    }
    //回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Bundle bundle = null;
        try {
            bundle = intent.getExtras();//防止空返回
        } catch (Exception e) {
            return;
        }


        if (requestCode == 0 && resultCode == 0) {//search
            tryClearAllGra();
            String search = bundle.getString("searchkey");
            Log.i("zjx", "the search=" + search);
            QueryParameters qParameters = new QueryParameters();
            String whereClause = "nickname like '%" + search + "%'";
            qParameters.setReturnGeometry(true);
            qParameters.setWhere(whereClause);
            CallbackListener<FeatureResult> callback = new CallbackListener<FeatureResult>() {
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                public void onCallback(FeatureResult featureIterator) {
                    Log.i("zjx", "i m callback");
                }
            };
            boolean isok=findFeatureCurrentFirst(featureLayers.get(currentFloor).selectFeatures(qParameters, FeatureLayer.SelectionMode.NEW, callback),currentFloor);
            if(!isok){
                for(int i=0;i<allfloor;i++){
                    if(i==currentFloor)continue;
                    boolean isok2=findFeatureCurrentFirst(featureLayers.get(i).selectFeatures(qParameters, FeatureLayer.SelectionMode.NEW, callback),i);
                    if(isok2)break;

                }
            }
            return;
        } else if (requestCode == 1 && resultCode == 1) {//rount
            tryClearAllGra();
            String mstart = bundle.getString("start");
            String mend = bundle.getString("end");
            Log.i("zjx", "mstart:" + mstart + " mend" + mend);
            ArrayList<String> mids = bundle.getStringArrayList("mids");
            MyPoint ms = null;
            MyPoint me = null;
            boolean canmakepath = true;
            boolean fromlocate = true;
            if (mstart.equals(getResources().getString(R.string.mylocation))) {
                ms = locateMyPoint;
                if (locateMyPoint == null) {
                    canmakepath = false;
                    Log.i("zjx", "user edit");
                    //防止用户自己输入我的位置时候却没定位}
                }
            } else {
                ms = getonlyfeature(mstart);
                fromlocate = false;
                if (ms == null) canmakepath = false;
            }
            if (canmakepath) {
                me = getonlyfeature(mend);
                if (me == null) canmakepath = false;
            }
            if (canmakepath) {
                MyPoint mid1 = null, mid2 = null;
                List<MyPoint> midlist = new ArrayList<>();
                midlist.add(ms);
                for (int i = 0; i < mids.size(); i++) {
                    MyPoint mid = getonlyfeature(mids.get(i));
                    if (mid != null) {
                        midlist.add(mid);
                    }
                }
                midlist.add(me);

                if (fromlocate) {
                    for (int i = 0; i < midlist.size() - 1; i++) {
                        Log.i("zjx", "s:" + midlist.get(i).toString() + "******e:" + midlist.get(i + 1).toString());
//						MakePath mp = new MakePath(getApplicationContext());
//						mp.execute(midlist.get(i), midlist.get(i+1));
                        PictureMarkerSymbol endpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.begining));
                        if (i + 1 == midlist.size() - 1)
                            endpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ending));
                        Point endpoi = new Point(LocationToMapX(midlist.get(i + 1).x), LocationToMapY(midlist.get(i + 1).y));
//						Graphic gp = new Graphic(endpoi,endpic);
                        main_gp = new Graphic(endpoi, endpic);
                        main_fl = midlist.get(i + 1).z;
//						mGraphicsLayer[midlist.get(i+1).z].addGraphic(gp);
                        viewHandler.sendEmptyMessage(UPDATEGP);
                        makePathAll(midlist.get(i), midlist.get(i + 1), true);


                    }
                    Point poi2 = new Point(locateMyPoint.x * 20.0, -locateMyPoint.y * 20.0);
                    mMapView.centerAt(poi2, true);
                    mMapView.setScale(7000.0);

                    receiver2 = new MyReceiver2();
                    IntentFilter filter = new IntentFilter();
                    filter.setPriority(20);
                    filter.addAction("com.example.amap.service.LocationService");
                    registerReceiver(receiver2, filter);
                } else {

                    for (int i = 0; i < midlist.size() - 1; i++) {
                        Log.i("zjx", "s:" + midlist.get(i).toString() + "******e:" + midlist.get(i + 1).toString());
//						MakePath mp = new MakePath(getApplicationContext());
//						mp.execute(midlist.get(i), midlist.get(i+1));

                        PictureMarkerSymbol endpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.begining));
                        if (i + 1 == midlist.size() - 1)
                            endpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ending));
                        Point endpoi = new Point(LocationToMapX(midlist.get(i + 1).x), LocationToMapY(midlist.get(i + 1).y));
//						Graphic gp = new Graphic(endpoi,endpic);
//						mGraphicsLayer[midlist.get(i+1).z].addGraphic(gp);
                        main_gp = new Graphic(endpoi, endpic);
                        main_fl = midlist.get(i + 1).z;
//						mGraphicsLayer[midlist.get(i+1).z].addGraphic(gp);
                        viewHandler.sendEmptyMessage(UPDATEGP);
                        makePathAll(midlist.get(i), midlist.get(i + 1), true);
//

                    }
                    PictureMarkerSymbol endpic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.begining));

                    Point poi2 = new Point(ms.x * 20.0, -ms.y * 20.0);
//					Graphic gp = new Graphic(poi2,endpic);
//					mGraphicsLayer[ms.z].addGraphic(gp);
                    main_gp = new Graphic(poi2, endpic);
                    main_fl = ms.z;
//						mGraphicsLayer[midlist.get(i+1).z].addGraphic(gp);
                    viewHandler.sendEmptyMessage(UPDATEGP);
                    mMapView.centerAt(poi2, true);
                    mMapView.setScale(7000.0);
//                    viewHandler.sendEmptyMessage(UPDATESCALE);
                }

            } else {
                MyToast.makeText(getApplicationContext(), R.string.makepath_error, 1.5).show();

            }
        } else if (requestCode == 2 && resultCode == 2) {
            String fix_search = intent.getStringExtra("fix_search");
            Log.i("zjx", "fix:" + fix_search);
            tryClearAllGra();
            QueryParameters qParameters = new QueryParameters();
            String whereClause = "nickname like '%" + fix_search + "%'";
            qParameters.setReturnGeometry(true);
            qParameters.setWhere(whereClause);
            CallbackListener<FeatureResult> callback = new CallbackListener<FeatureResult>() {
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                public void onCallback(FeatureResult featureIterator) {
                    Log.i("zjx", "i m callback");
                }
            };
            Future<FeatureResult> resultFuture = featureLayers.get(currentFloor).selectFeatures(qParameters, FeatureLayer.SelectionMode.NEW, callback);
            Log.i("zjx", "resultFuture:" + resultFuture);
            double min = 1000000;
            int minpid = -1;
            try {
                FeatureResult results = resultFuture.get();
                if (results != null) {
                    Feature mfeature = null;
                    Point poi = null;
                    for (Object element : results) {
                        if (element instanceof Feature) {
                            Feature feature = (Feature) element;
                            int color = Color.rgb(100, 100, 100);
                            SimpleFillSymbol sfs = new SimpleFillSymbol(color);
                            Graphic graphic = new Graphic(feature.getGeometry(),
                                    sfs);
                            mGraphicsLayer[currentFloor].addGraphic(graphic);
                            PictureMarkerSymbol pic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.begining));
                            poi = new Point((double) feature.getAttributeValue("pointX"), (double) feature.getAttributeValue("pointy"));
                            Graphic gp = new Graphic(poi, pic);
                            int pid = mGraphicsLayer[currentFloor].addGraphic(gp);
                            MyPoint ms = new MyPoint(MapToMyPointX(feature.getAttributeValue("x1")), MapToMyPointY(feature.getAttributeValue("y1")), currentFloor);
                            double now = Math.abs(ms.x - locateMyPoint.x) + Math.abs(ms.y - locateMyPoint.y);
                            if (now < min) {
                                min = now;
                                mfeature = feature;
                                minpid = pid;
                            }
                        }
                    }
                    if (mfeature != null) {

                        PictureMarkerSymbol pic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.iconfont_nearest));
                        poi = new Point((double) mfeature.getAttributeValue("pointX"), (double) mfeature.getAttributeValue("pointy"));
                        Graphic gp = new Graphic(poi, pic);
                        mMapView.centerAt(poi, true);
                        mMapView.setScale(7000.0);
//                        viewHandler.sendEmptyMessage(UPDATESCALE);
                        mGraphicsLayer[currentFloor].updateGraphic(minpid, gp);
                        TextView text = (TextView) findViewById(R.id.poiname);
                        text.setText(mfeature.getAttributeValue("nickname").toString());
                        allinfo.setVisibility(View.VISIBLE);
                        currentFeature = mfeature;
                        currentMyPoint = new MyPoint(MapToMyPointX(mfeature.getAttributeValue("x1")), MapToMyPointY(mfeature.getAttributeValue("y1")), currentFloor);
                    }
                }
            } catch (Exception e) {
                Log.i("zjx", "e:" + e);
            }
            return;
        }
    }

    //根据搜索名得到唯一的要素点
    public MyPoint getonlyfeature(String msearch) {
        MyPoint ms = null;
        QueryParameters qParameters = new QueryParameters();
        Log.i("zjx", "where 的name=" + msearch);
        String whereClause = "nickname='" + msearch + "'";
        qParameters.setReturnGeometry(true);
        qParameters.setWhere(whereClause);
        CallbackListener<FeatureResult> callback = new CallbackListener<FeatureResult>() {
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            public void onCallback(FeatureResult featureIterator) {
                Log.i("zjx", "i m callback");
            }
        };
        Future<FeatureResult> resultFuture = featureLayers.get(currentFloor).selectFeatures(qParameters, FeatureLayer.SelectionMode.NEW, callback);
        try {
            FeatureResult results = resultFuture.get();
            if (results != null) {
                for (Object element : results) {
                    if (element instanceof Feature) {
                        Feature feature = (Feature) element;
                        ms = new MyPoint(MapToMyPointX(feature.getAttributeValue("x1")), MapToMyPointY(feature.getAttributeValue("y1")), currentFloor);
                        return ms;
                    }

                }
            }
        } catch (Exception e) {
        }
        for (int i = 0; i < allfloor; i++) {
            if (i == currentFloor) continue;//少操作
            resultFuture = featureLayers.get(i).selectFeatures(qParameters, FeatureLayer.SelectionMode.NEW, callback);
            try {
                FeatureResult results = resultFuture.get();
                if (results != null) {
                    for (Object element : results) {
                        if (element instanceof Feature) {
                            Feature feature = (Feature) element;
                            ms = new MyPoint(MapToMyPointX(feature.getAttributeValue("x1")), MapToMyPointY(feature.getAttributeValue("y1")), i);
                            return ms;
                        }

                    }
                }
            } catch (Exception e) {
            }
        }

        return ms;
    }

    //根据搜索名得到最近的要素距离(此处采用曼哈顿，与zhenshi数据较拟合)
    public double getCloestfeature(String msearch) {
        QueryParameters qParameters = new QueryParameters();
        String whereClause = "nickname like '%" + msearch + "%'";
        qParameters.setReturnGeometry(true);
        qParameters.setWhere(whereClause);
        CallbackListener<FeatureResult> callback = new CallbackListener<FeatureResult>() {
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            public void onCallback(FeatureResult featureIterator) {
                Log.i("zjx", "i m callback");
            }
        };
        Future<FeatureResult> resultFuture = featureLayers.get(currentFloor).selectFeatures(qParameters, FeatureLayer.SelectionMode.NEW, callback);
        double min = 10000000;
        try {
            FeatureResult results = resultFuture.get();
            if (results != null) {
                for (Object element : results) {
                    if (element instanceof Feature) {
                        Feature feature = (Feature) element;
                        MyPoint ms = new MyPoint(MapToMyPointX(feature.getAttributeValue("x1")), MapToMyPointY(feature.getAttributeValue("y1")), currentFloor);
                        double now = Math.abs(ms.x - locateMyPoint.x) + Math.abs(ms.y - locateMyPoint.y);
                        if (now < min) min = now;
                    }

                }
            }
        } catch (Exception e) {
        }


        return min;
    }

    @Override
    protected void onDestroy() {
        featureLayers.clear();
        mMapView.destroyDrawingCache();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            unregisterReceiver(receiver2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //结束服务，如果想让服务一直运行就注销此句
        try {
            stopService(new Intent(this, LocationService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    //create完回调
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            initializeRoutingAndGeocoding();
        } catch (Exception e) {
            Log.i("zjx", "未找到地图包");
            isloadok = false;
        }
        try {
            if (isloadok) {
//			GetLocation(button_dingwei);
            } else {
                toast.makeText(getApplicationContext(), R.string.no_find_map, 2.5).show();
            }
        } catch (Exception e) {
            Log.i("zjx", "e:" + e);
        }
        showcurrentfloor();
        processExtraData();

//		GetSearchPOITask getSearchPOITask=new GetSearchPOITask(this);//在这边另开任务会和initializeRoutingAndGeocoding冲突
// 可能还没加载完
        //所以我们把要做的放initializeRoutingAndGeocoding里面去
//		getSearchPOITask.execute();
    }
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processExtraData();
    }

    private void processExtraData(){
        ShowLog("processExtraData");
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
//            Bundle bundle=intent.getExtras();
            String fromActivity = bundle.getString("fromActivity");
            if("LocationActivity".equals(fromActivity)) {
//                viewHandler.sendEmptyMessage(COMPLETEAAL); //handler 里面去取消receiver2的注册 会出错
                tryClearAllGra();//所以要这样写才对 <-
                Message msg = new Message();
                msg.setData(bundle);
                msg.what = FINDFIREND;
                viewHandler.handleMessage(msg);
                Bundle bundle2 = msg.getData();
                int x=bundle2.getInt("x");
                int y=bundle2.getInt("y");
                int z=bundle2.getInt("z");
                ShowLog("try start to find firend");
                ShowLog("start to find firend");
                Log.i("zjx", "go there 2");
                mMapView.getCallout().hide();
                endMyPoint = new MyPoint(x,y,z);
                PictureMarkerSymbol pic5 = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ending));
                Point poi5 = new Point((double) (endMyPoint.x*20.0), (double)  (-endMyPoint.y*20.0));
                Graphic gp5 = new Graphic(poi5, pic5);
                mGraphicsLayer[endMyPoint.z].addGraphic(gp5);
                if (locateMyPoint == null) {
//                    receiver = new MyReceiver();
//                    IntentFilter filter = new IntentFilter();
//                    filter.setPriority(30);
//                    filter.addAction("com.example.amap.service.LocationService");
//                    registerReceiver(receiver, filter);
//                    isLocating = true;
//                    try {
//                        MakePath mp = new MakePath(MainActivity.this);
//                        mp.execute(locateMyPoint, endMyPoint);
//                    }
//                    catch(Exception e){
//                        e.printStackTrace();
//                    }

                    Point poi = new Point(x*20.0,-y*20.0);
                    currentFloor=z;
                    showcurrentfloor();
                    mMapView.centerAt(poi, true);
                    mMapView.setScale(7000.0);
                    ShowToast("您尚未开启定位，请开启定位后重试");
                }
                if (locateMyPoint != null) {
                    rountstart = getResources().getString(R.string.mylocation);
                    MakePath mp = new MakePath(MainActivity.this);
                    mp.execute(locateMyPoint, endMyPoint);
                    currentFloor = locateMyPoint.z;
                    showcurrentfloor();
                    Point poi2 = new Point(locateMyPoint.x * 20.0, -locateMyPoint.y * 20.0);
                    mMapView.centerAt(poi2, true);
                    mMapView.setScale(7000.0);
                    //注册广播
//                    if(receiver2==null)receiver2 = new MyReceiver2();
                    receiver2 = new MyReceiver2();
                    IntentFilter filter = new IntentFilter();
                    filter.setPriority(5);
                    filter.addAction("com.example.amap.service.LocationService");
                    registerReceiver(receiver2, filter);

                }
            }
        }
        //use the data received here

    }
    @Override
    protected void onStop() {

        super.onStop();
//		mMapView.pause();
//		ClearTimeThread();
//		mMapView.pause();

    }

    @Override
    protected void onPause() {

        super.onPause();
//		mMapView.pause();
//		ClearTimeThread();
//		mMapView.pause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ShowLog("onRestart");
    }

    @Override
    protected void onResume() {


        ShowLog("onResume");
        super.onResume();

    }

    //得到mapview的bitmap
    private Bitmap getViewBitmap(MapView v) {

        v.clearFocus();
        v.setPressed(false);
        //能画缓存就返回false
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = null;
        while (cacheBitmap == null) {
            cacheBitmap = v.getDrawingMapCache(0, 0, v.getWidth(), v.getHeight());
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    // 为发送通知的按钮的点击事件定义事件处理方法
//截图
    private String mapviewshot() {
        System.out.println("进入截屏方法");
        Date date = new Date();
        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String timeString = dateformat1.format(date);
        String path = "Arcgis/screenshot";
        String externalPath = Environment.getExternalStorageDirectory().toString();
        String filename = externalPath + "/" + path + "/" + timeString;
        File file_2 = new File(externalPath + "/" + path);
        if (!file_2.exists()) {
            Log.i("zjx", "path 文件夹 不存在--开始创建");
            file_2.mkdirs();
        }
        filename = getfilepath(filename);//判断是否有同一秒内的截图，有就改名字
        //存储于sd卡上
        Log.i("zjx", "获得的filename--" + filename);
        Bitmap bitmap = getViewBitmap(mMapView);
        File file = new File(filename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }

    //截图文件路径
    private String getfilepath(String filename) {
        String filestr = filename + ".png";
        File file = new File(filestr);
        if (file.exists()) {
            filename = getfilepath(filename + "_1");
        } else {
            filename = filestr;
        }
        Log.i("zjx", "getfilename函数返回----" + filename);
        return filename;
    }

    private boolean doubleBackToExitPressedOnce = false;

    //后退按钮事件重写
    @Override
    public void onBackPressed() {

        if (allinfo.getVisibility() == View.VISIBLE) {
            allinfo.setVisibility(View.GONE);
            mMapView.getCallout().hide();
            return;
        }
        if (doubleBackToExitPressedOnce) {//按两次
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
//		Toast.makeText(this,R.string.doubleclick, Toast.LENGTH_SHORT).show();
        toast.makeText(getApplicationContext(), R.string.doubleclick, 1).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
