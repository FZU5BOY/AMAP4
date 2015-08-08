package com.example.amap.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import cn.bmob.im.util.BmobLog;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.example.amap.R;
import com.example.amap.service.LocationService;
import com.example.amap.util.rount.MyPoint;
import com.example.amap.view.HeaderLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于发送位置的界面
 *
 * @ClassName: LocationActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-23 下午3:17:05
 */
	public class LocationActivity extends BaseActivity  {
	// 定位相关
	private final int LOCATION_OK = 1;
	private final int LOCATION_NO_IN_MAP = 2;
	private final int LOCATION_NET_ERROR = 3;
	private final int LOCATION_LOCATION_IP_NOSET = 4;
	private final int LOCATION_LOCATION_IP_ERROR = 5;
	MapView mMapView ;
	MyReceiver receiver;
	private static Handler viewHandler;//用于更新UI
	MapViewHelper mvHelper;//帮助类，某些操作更快捷
	final  int SHOWCURRENTFLOOR = 11;
	final int allfloor =3 ;
	private boolean service_is_run =false;
	public static MyPoint locateMyPoint = null;//当前定位地址
	final String extern = Environment.getExternalStorageDirectory().getPath();
	//tpk文件地址
	final String tpkPath[] = {"/arcgis/b1/b1.tpk", "/arcgis/f1/f1.tpk", "/arcgis/f2/f2.tpk"};
	//geo文件地址
	public static final String GEO_FILENAME[] =
			{"/arcgis/b1/data/b1.geodatabase", "/arcgis/f1/data/f1.geodatabase", "/arcgis/f2/data/f2.geodatabase"};
	int currentFloor = 1;//当前楼层 默认F1
	String geofilename[] = {extern + GEO_FILENAME[0], extern + GEO_FILENAME[1], extern + GEO_FILENAME[2]};
	String floorname[] = {"B1", "F1", "F2"};
	List<TiledLayer> mTileLayers = new ArrayList<>();
	GraphicsLayer loactionGraphicsLayer = new GraphicsLayer();
	static List<FeatureLayer> featureLayers = new ArrayList<>();
	//Point，MyPoint,Location转换
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		viewHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case SHOWCURRENTFLOOR:
						for (int i = 0; i < allfloor; i++) {
							if (i != currentFloor) {
								mMapView.getLayer(i ).setVisible(false);
							} else {
								mMapView.getLayer(i).setVisible(true);
							}
						}
						if (locateMyPoint != null) {
							if (locateMyPoint.z != currentFloor)
								loactionGraphicsLayer.setVisible(false);
							else loactionGraphicsLayer.setVisible(true);
						}
						break;
					case LOCATION_NET_ERROR:
						ShowToast(R.string.location_error_net_tips);
						locateMyPoint = null;
						loactionGraphicsLayer.removeAll();
						break;
					case LOCATION_LOCATION_IP_ERROR:
						ShowToast(R.string.location_error_url_unfind);
						locateMyPoint = null;
						loactionGraphicsLayer.removeAll();
						break;
					case LOCATION_LOCATION_IP_NOSET:
						ShowToast(R.string.location_error_url_no_set);
						locateMyPoint = null;
						loactionGraphicsLayer.removeAll();
						break;
					case LOCATION_NO_IN_MAP:
						ShowToast(R.string.location_error_local_inmap);
						locateMyPoint = null;
						loactionGraphicsLayer.removeAll();
						break;
					case LOCATION_OK:
						loactionGraphicsLayer.removeAll();
						Bundle bundle = msg.getData();
						double ax = bundle.getDouble("ax");
						double ay = bundle.getDouble("ay");
						int az = bundle.getInt("az");
						PictureMarkerSymbol pic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.man));
						Point mapPoint = new Point(LocationToMapX(ax), LocationToMapY(ay));
						Graphic gp = new Graphic(mapPoint, pic);
						MyPoint newMyPoint = new MyPoint(MapToMyPointX(mapPoint.getX()), MapToMyPointY(mapPoint.getY()), az);
						locateMyPoint = newMyPoint;
						if (az != currentFloor) {
							currentFloor = az;
							showcurrentfloor();
						}
						loactionGraphicsLayer.addGraphic(gp);
						mMapView.centerAt(mapPoint, true);
						mMapView.setScale(7000.0);
						mHeaderLayout.getRightImageButton().setEnabled(true);
						break;
					default:
						break;
				}
			}
		};
		initMap();

	}
	public static boolean isServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.example.amap.service.LocationService".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	//create完回调
	private void  startLocation(){
		boolean ser =isServiceRunning(this);
		if(ser){
			service_is_run=true;
			ShowLog("之前就启动service了");
		}
		else {
			startService(new Intent(this, LocationService.class));
		}
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(30);
		filter.addAction("com.example.amap.service.LocationService");
		registerReceiver(receiver, filter);
	}
	private void showcurrentfloor() {
		viewHandler.sendEmptyMessage(SHOWCURRENTFLOOR);
	}
	boolean isfirstLocation =true;
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
					Message msg = new Message();
					msg.what = LOCATION_OK;
					msg.setData(bundle);//mes利用Bundle传递数据
					viewHandler.sendMessage(msg);
					if(isfirstLocation)ShowToast("定位成功");
					break;
				case LOCATION_NET_ERROR:
					if(isfirstLocation)viewHandler.sendEmptyMessage(LOCATION_NET_ERROR);
					break;
				case LOCATION_NO_IN_MAP:
					if(isfirstLocation)viewHandler.sendEmptyMessage(LOCATION_NO_IN_MAP);
					break;
				case LOCATION_LOCATION_IP_ERROR:
					if(isfirstLocation)viewHandler.sendEmptyMessage(LOCATION_LOCATION_IP_ERROR);
					break;
				case LOCATION_LOCATION_IP_NOSET:
					if(isfirstLocation)viewHandler.sendEmptyMessage(LOCATION_LOCATION_IP_NOSET);
					break;
				default:
					break;
			}
			isfirstLocation=false;
		}
	}

	private void initMap() {
		mMapView =  (MapView) findViewById(R.id.map);
		mvHelper = new MapViewHelper(mMapView);
		mMapView.setMapBackground(0xeeeeee, 0xffffff, 0, 0);//设置地图网格，背景样式
		//添加瓦片和绘制图层
		try {
			mTileLayers.add(new ArcGISLocalTiledLayer(extern + tpkPath[0]));
			mTileLayers.add(new ArcGISLocalTiledLayer(extern + tpkPath[1]));
			mTileLayers.add(new ArcGISLocalTiledLayer(extern + tpkPath[2]));
			mMapView.addLayer(mTileLayers.get(0));
			mMapView.addLayer(mTileLayers.get(1));
			mMapView.addLayer(mTileLayers.get(2));
			mMapView.addLayer(loactionGraphicsLayer);
		} catch (Exception e) {
			Log.i("zjx", "未找到地图包");
		}
		Intent intent = getIntent();
		String type = intent.getStringExtra("type");
		if (type.equals("select")) {// 选择发送位置
			initTopBarForBoth("位置", R.drawable.btn_login_selector, "发送",
					new HeaderLayout.onRightImageButtonClickListener() {

						@Override
						public void onClick() {
							// TODO Auto-generated method stub
							gotoChatPage();
						}
					});
			mHeaderLayout.getRightImageButton().setEnabled(false);
			startLocation();
			showcurrentfloor();
		} else {// 查看当前位置
			initTopBarForLeft("位置");
			Bundle b = intent.getExtras();
			int x=b.getInt("x");
			int y=b.getInt("y");
			int z=b.getInt("z");
			MyPoint newMyPoint = new MyPoint(x,y,z);
			currentFloor = z;

			PictureMarkerSymbol pic = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.man));
			Point mapPoint = new Point(x*20.0,-y*20.0);
			Graphic gp = new Graphic(mapPoint, pic);
			loactionGraphicsLayer.addGraphic(gp);
			showcurrentfloor();
			mMapView.centerAt(mapPoint, true);
			mMapView.setScale(7000.0);
		}


	}

	/**
	 * 回到聊天界面
	 * @Title: gotoChatPage
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void gotoChatPage() {
		if(locateMyPoint!=null){
			Intent intent = new Intent();
			intent.putExtra("x", locateMyPoint.x);// x
			intent.putExtra("y", locateMyPoint.y);// y
			intent.putExtra("z", locateMyPoint.z);//z
			intent.putExtra("detail", "福建晋江国际机场" + locateMyPoint.z+"层大厅");
			setResult(RESULT_OK, intent);
			this.finish();
		}else{
			ShowToast("获取室内位置信息失败!");
		}
	}

	@Override
	protected void onPause() {
		mMapView.pause();
		super.onPause();
	}
	@Override
	protected void onResume() {
		mMapView.unpause();
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		mMapView.destroyDrawingCache();
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//结束服务，如果想让服务一直运行就注销此句
		if(!service_is_run){
			try {
				ShowLog("结束 service");
				stopService(new Intent(this, LocationService.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

}
