package com.example.amap.custom;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.amap.R;

public class MyToast {
	private WindowManager wdm;
	private double time;
	private View mView;
	private WindowManager.LayoutParams params;
	private Timer timer;

	private MyToast(Context context, CharSequence text, double time){
		wdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		timer = new Timer();

		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		mView = toast.getView();

		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
//		params.windowAnimations = toast.getView().getAnimation().INFINITE;
		params.windowAnimations = R.style.toast_anim_view;//设置进入退出动画效果
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.setTitle("Toast");
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.gravity = Gravity.CENTER_HORIZONTAL;
		params.y = 250;

		this.time = time;
	}

	public static MyToast makeText(Context context,  int resId, double time){
		MyToast toastCustom = new MyToast(context, context.getResources().getText(resId), time);
		return toastCustom;
	}

	public void show(){
		wdm.addView(mView, params);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				wdm.removeView(mView);
			}
		}, (long)(time * 1000));
	}

	public void cancel(){
		wdm.removeView(mView);
		timer.cancel();
	}
}
