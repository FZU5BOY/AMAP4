package com.example.amap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amap.R;

/**
 * Created by Zeashon on 2015/5/10.
 */
public class RegiActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        Button btn = (Button)findViewById(R.id.makeRoad_GoBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegiActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final EditText password01 = (EditText) findViewById(R.id.loginPass01);
        final EditText password02 = (EditText) findViewById(R.id.loginPass02);
        final EditText userName = (EditText) findViewById(R.id.newNickName);
        final EditText userTel = (EditText) findViewById(R.id.loginName);

        Button regbtn = (Button)findViewById(R.id.registerBtn);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().length()<=0 || userTel.getText().length()<=0 || password01.getText().length()<=0 || password02.getText().length()<=0)
                {
                    Toast.makeText(getApplicationContext(), "请填写完整注册信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password01.getText().toString().length()<6 || password01.getText().toString().length()>10)
                {
                    Toast.makeText(getApplicationContext(), "密码应为6-10位的数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password01.getText().toString().equals(password02.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "两次输入密码不同", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "注册成功，登录帐号为你的手机号", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegiActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
