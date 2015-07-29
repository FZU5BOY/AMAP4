package com.example.amap.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.amap.R;
import com.example.amap.util.CircularImage;

/**
 * Created by Zeashon on 2015/5/10.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        TextView ln=(TextView) findViewById(R.id.loginName);
//        TelephonyManager phoneMgr=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//        ln.setText(phoneMgr.getLine1Number());
//        Log.i("123",phoneMgr.getLine1Number());
        CircularImage cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
        cover_user_photo.setImageResource(R.drawable.head);
        CircularImage cover_user_photo_circle = (CircularImage) findViewById(R.id.circle);
        cover_user_photo_circle.setImageResource(R.drawable.circle);

//        Button btn = (Button)findViewById(R.id.makeRoad_GoBack);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        Button regbtn = (Button) findViewById(R.id.registerBtn);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegiActivity.class);
                startActivity(intent);
            }
        });
        Button logbtn = (Button) findViewById(R.id.loginBtn);
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PersonActivity.class);
                startActivity(intent);
            }
        });
    }
}
