package com.example.amap;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.example.amap.bean.AMapPoint;
import com.example.amap.util.CollectionUtils;
import com.example.amap.util.SharePreferenceUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 自定义全局Applcation类
 * @ClassName: CustomApplcation
 * @Description: TODO
 * @author smile
 * @date 2014-5-19 下午3:25:00
 */
public class CustomApplcation extends Application {

	public static CustomApplcation mInstance;
//	public LocationClient mLocationClient;
//	public MyLocationListener mMyLocationListener;

	//	public static BmobGeoPoint lastPoint = null;// 上一次定位到的经纬度
	public static AMapPoint lastPoint = null;// 上一次定位到的经纬度
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 是否开启debug模式--默认开启状态
		BmobChat.DEBUG_MODE = true;
		mInstance = this;
		init();
	}

	private void init() {
		mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		initImageLoader(getApplicationContext());
		// 若用户登陆过，则先从好友数据库中取出好友list存入内存中
		if (BmobUserManager.getInstance(getApplicationContext())
				.getCurrentUser() != null) {
			// 获取本地好友user list到内存,方便以后获取好友list
			contactList = CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList());
		}
//		initBaidu();
//		initLocClient();
	}

//	/**
//	 * 初始化百度相关sdk initBaidumap
//	 * @Title: initBaidumap
//	 * @Description: TODO
//	 * @param
//	 * @return void
//	 * @throws
//	 */
//	private void initBaidu() {
//		// 初始化地图Sdk
//		SDKInitializer.initialize(this);
//		// 初始化定位sdk
//		initBaiduLocClient();
//	}
//
//	/**
//	 * 初始化百度定位sdk
//	 * @Title: initBaiduLocClient
//	 * @Description: TODO
//	 * @param
//	 * @return void
//	 * @throws
//	 */
//	private void initLocClient() {
//		mLocationClient = new LocationClient(this.getApplicationContext());
//		mMyLocationListener = new MyLocationListener();
//		mLocationClient.registerLocationListener(mMyLocationListener);
		
//	}

//	/**
//	 * 实现实位回调监听
//	 */
//	public class MyLocationListener implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			// Receive Location
//			double latitude = location.getLatitude();
//			double amapx = location.getLongitude();
//			if (lastPoint != null) {
//				if (lastPoint.getLatitude() == location.getLatitude()
//						&& lastPoint.getLongitude() == location.getLongitude()) {
////					BmobLog.i("两次获取坐标相同");// 若两次请求获取到的地理位置坐标是相同的，则不再定位
//					mLocationClient.stop();
//					return;
//				}
//			}
//			lastPoint = new BmobGeoPoint(amapx, latitude);
//		}
//	}

	/** 初始化ImageLoader */
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"bmobim/Cache");// 获取到缓存的目录地址
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				// 线程池内加载的数量
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(new WeakMemoryCache())
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
						// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
						// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);// 全局初始化此配置
	}

	public static CustomApplcation getInstance() {
		return mInstance;
	}

	// 单例模式，才能及时返回数据
	SharePreferenceUtil mSpUtil;
	public static final String PREFERENCE_NAME = "_sharedinfo";

	public synchronized SharePreferenceUtil getSpUtil() {
		if (mSpUtil == null) {
			String currentId = BmobUserManager.getInstance(
					getApplicationContext()).getCurrentUserObjectId();
			String sharedName = currentId + PREFERENCE_NAME;
			mSpUtil = new SharePreferenceUtil(this, sharedName);
		}
		return mSpUtil;
	}

	NotificationManager mNotificationManager;

	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}

	MediaPlayer mMediaPlayer;

	public synchronized MediaPlayer getMediaPlayer() {
		if (mMediaPlayer == null)
			mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
		return mMediaPlayer;
	}
	public final String PREF_AMAPX = "amapx";// amapx
	private String amapx = "";

	/**
	 * 获取amapx
	 *
	 * @return
	 */
	public String getAmapx() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		amapx = preferences.getString(PREF_AMAPX, "");
		return amapx;
	}

	/**
	 * 设置amapx
	 *
	 * @param
	 */
	public void setAmapx(String lon) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(PREF_AMAPX, lon).commit()) {
			amapx = lon;
		}
	}

	public final String PREF_AMAPY = "amapy";// amapy
	private String amapy = "";

	/**
	 * 获取amapy
	 *
	 * @return
	 */
	public String getAmapy() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		amapy = preferences.getString(PREF_AMAPY, "");
		return amapy;
	}

	/**
	 * 设置amapy
	 *
	 * @param
	 */
	public void setAmapy(String lon) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(PREF_AMAPY, lon).commit()) {
			amapy = lon;
		}
	}

	public final String PREF_AMAPZ = "amapz";// amapz
	private String amapz = "";

	/**
	 * 获取amapz
	 *
	 * @return
	 */
	public String getAmapz() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		amapz = preferences.getString(PREF_AMAPZ, "");
		return amapz;
	}

	/**
	 * 设置amapz
	 *
	 * @param
	 */
	public void setAmapz(String lon) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(PREF_AMAPZ, lon).commit()) {
			amapz = lon;
		}
	}

	private Map<String, BmobChatUser> contactList = new HashMap<String, BmobChatUser>();

	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	public Map<String, BmobChatUser> getContactList() {
		return contactList;
	}

	/**
	 * 设置好友user list到内存中
	 * @param contactList
	 */
	public void setContactList(Map<String, BmobChatUser> contactList) {
		if (this.contactList != null) {
			this.contactList.clear();
		}
		this.contactList = contactList;
	}

	/**
	 * 退出登录,清空缓存数据
	 */
	public void logout() {
		BmobUserManager.getInstance(getApplicationContext()).logout();
		setContactList(null);
		setAmapx(null);
		setAmapy(null);
		setAmapz(null);
	}

}
