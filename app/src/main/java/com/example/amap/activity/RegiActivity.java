package com.example.amap.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amap.R;
import com.example.amap.bean.User;
import com.example.amap.config.BmobConstants;
import com.example.amap.util.CircularImage;
import com.example.amap.util.CommonUtils;

import java.util.List;

import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Zeashon on 2015/5/10.
 */
public class RegiActivity extends BaseActivity {
    private EditText password01,password02,nickName,userTel;//tel = username
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        init();
        Button btn = (Button) findViewById(R.id.makeRoad_GoBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegiActivity.this.finish();
            }
        });



        Button regbtn = (Button) findViewById(R.id.registerBtn);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               register();
            }
        });
    }
    private void init(){
        password01 = (EditText) findViewById(R.id.loginPass01);
        password02 = (EditText) findViewById(R.id.loginPass02);
        nickName = (EditText) findViewById(R.id.newNickName);
        userTel = (EditText) findViewById(R.id.loginName);
        CircularImage cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
        cover_user_photo.setImageResource(R.drawable.head);
        CircularImage cover_user_photo_circle = (CircularImage) findViewById(R.id.circle);
        cover_user_photo_circle.setImageResource(R.drawable.circle);

    }
    private void register(){
        String name = nickName.getText().toString().trim();
        String password = password01.getText().toString().trim();
        String pwd_again = password02.getText().toString().trim();
        String tel=userTel.getText().toString();
        if (name.length() <= 0 || tel.length() <= 0 || password.length() <= 0 || pwd_again.length() <= 0) {
            ShowToast(R.string.toast_register_all_error);
            return;
        }
        if (tel.length() !=11) {
            ShowToast(R.string.toast_register_tel_error);
            return;
        }
        if (password.length() < 6 ) {
            ShowToast(R.string.toast_register_pswlenth_error);
            return;
        }

        if (!password.equals(pwd_again)) {
            ShowToast(R.string.toast_register_psw_unequal_error);
            return;
        }
//        Toast.makeText(getApplicationContext(), "注册成功，登录帐号为你的手机号", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(RegiActivity.this, LoginActivity.class);
//        startActivity(intent);


        boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
        if(!isNetConnected){
            ShowToast(R.string.network_tips);
            return;
        }

        final ProgressDialog progress = new ProgressDialog(RegiActivity.this);
        progress.setMessage("正在注册...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        final User bu = new User();
        bu.setNick(name);//昵称
        bu.setUsername(tel);//tel作为帐号
        bu.setPassword(password);
        //将user和设备id进行绑定aa
        bu.setSex(true);
        bu.setDeviceType("android");
        bu.setInstallId(BmobInstallation.getInstallationId(this));
        bu.signUp(RegiActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                progress.dismiss();
                ShowToast("注册成功,手机号即为登录账号");
                // 将设备与username进行绑定
                userManager.bindInstallationForRegister(bu.getUsername());
                //更新地理位置信息
//                updateUserLocation();
                //发广播通知登陆页面退出
                sendBroadcast(new Intent(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH));
                // 启动主页
                Intent intent = new Intent(RegiActivity.this, UserMainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                BmobLog.i(arg1);
                ShowToast("注册失败:" + arg1);
                progress.dismiss();
            }
        });
    }

}
