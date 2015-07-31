package com.example.amap.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.amap.R;
import com.example.amap.bean.User;
import com.example.amap.config.BmobConstants;
import com.example.amap.util.CircularImage;
import com.example.amap.util.CommonUtils;

import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Zeashon on 2015/5/10.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    Button regbtn,logbtn;
    EditText et_username, et_password;
    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        //注册退出广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH);
        registerReceiver(receiver, filter);



    }
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && BmobConstants.ACTION_REGISTER_SUCCESS_FINISH.equals(intent.getAction())) {
                finish();
            }
        }

    }
    private void init() {
        CircularImage cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
        cover_user_photo.setImageResource(R.drawable.head);
        CircularImage cover_user_photo_circle = (CircularImage) findViewById(R.id.circle);
        cover_user_photo_circle.setImageResource(R.drawable.circle);
        et_username = (EditText) findViewById(R.id.loginName);
        et_password = (EditText) findViewById(R.id.loginPass);
        logbtn = (Button) findViewById(R.id.loginBtn);
        regbtn = (Button) findViewById(R.id.registerBtn);
        logbtn.setOnClickListener(this);
        regbtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == regbtn) {
            Intent intent = new Intent(LoginActivity.this,
                    RegiActivity.class);
            startActivity(intent);
        } else {
            boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
            if(!isNetConnected){
                ShowToast(R.string.network_tips);
                return;
            }
            login();
        }
    }
    private void login(){
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ShowToast(R.string.toast_error_username_null);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.toast_error_password_null);
            return;
        }

        final ProgressDialog progress = new ProgressDialog(
                LoginActivity.this);
        progress.setMessage("正在登录...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        userManager.login(user,new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        progress.setMessage("正在获取好友列表...");
                    }
                });
                //更新用户的地理位置以及好友的资料
                //updateUserInfos();
                progress.dismiss();
                ShowLog("正在准备进入usermain");
                Intent intent = new Intent(LoginActivity.this,UserMainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int errorcode, String arg0) {
                // TODO Auto-generated method stub
                progress.dismiss();
                BmobLog.i(arg0);
//                ShowToast(arg0);
                ShowToast("用户名或密码错误");
            }
        });

    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
