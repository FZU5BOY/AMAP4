package com.example.amap.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amap.R;
import com.example.amap.util.rount.MyPoint;
import com.example.amap.util.SharePreferenceUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Zeashon on 2015/5/10.
 */
public class ToolsActivity extends Activity implements View.OnClickListener {
    Button btn_logout;
    TextView tv_set_name;
    RelativeLayout rl_switch_notification, rl_switch_voice,
            rl_switch_vibrate;

    ImageView iv_open_notification, iv_close_notification, iv_open_voice,
            iv_close_voice, iv_open_vibrate, iv_close_vibrate;
    SharePreferenceUtil mSharedUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools);
//        mSharedUtil = mApplication.getSpUtil();
        Button btn = (Button) findViewById(R.id.makeRoad_GoBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToolsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button changeIp = (Button) findViewById(R.id.button15);
        changeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = View.inflate(ToolsActivity.this, R.layout.a_editor, null);
                final String FILE_NAME = "ip.txt";
                Dialog abc = new AlertDialog.Builder(ToolsActivity.this)
                        .setView(view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText et = (EditText) view.findViewById(R.id.editText3);
                                String s = et.getText().toString();
                                if (s.length() == 0) {
                                    Toast.makeText(getApplicationContext(), "Input something.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                FileInputStream fis = null;
                                FileOutputStream fos = null;
                                try {
                                    fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                                    String text = et.getText().toString();
                                    fos.write(text.getBytes());
//                                    Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (fos != null) {
                                        try {
                                            fos.flush();
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                try {
                                    fis = openFileInput(FILE_NAME);
                                    if (fis.available() == 0) {
                                        return;
                                    }
                                    byte[] readBytes = new byte[fis.available()];
                                    while (fis.read(readBytes) != -1) {
                                    }
                                    String text = new String(readBytes);
//                                    Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT).show();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("cancel", null)
                        .create();
                abc.show();
                EditText et = (EditText) view.findViewById(R.id.editText3);
                FileInputStream fis = null;
                try {
                    fis = openFileInput(FILE_NAME);
                    if (fis.available() == 0) {
                        return;
                    }
                    byte[] readBytes = new byte[fis.available()];
                    while (fis.read(readBytes) != -1) {
                    }
                    String text = new String(readBytes);
//                    if(text==null||"".equals(text))text="http://192.168.191.1:8001/loc";
                    et.setText(text);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        Button suggest = (Button) findViewById(R.id.button5);
        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = View.inflate(ToolsActivity.this, R.layout.emailtext, null);
                new AlertDialog.Builder(ToolsActivity.this)
                        .setView(view)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("cancel", null)
                        .create()
                        .show();
            }
        });

        Button share = (Button) findViewById(R.id.button4);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String shareMsg =getResources().getString(R.string.imlocation);
                String shareMsg2 ="http://zjx.com/im?";
                 MyPoint sb=MainActivity.locateMyPoint;
               if (sb!=null)shareMsg2+="x="+sb.x+"&y="+sb.y+"&z="+sb.z;
                Log.i("zjx","shareMsg2:"+shareMsg2);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg + "  " + shareMsg2);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(shareIntent.createChooser(shareIntent, getTitle()));
            }
        });
        initView();
//        initData();

    }




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_set);
//        mSharedUtil = mApplication.getSpUtil();
//        initView();
//        initData();
//    }
    private void initView() {
//        initTopBarForOnlyTitle("设置");

//          layout_info = (RelativeLayout) findViewById(R.id.layout_info);
        rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
        rl_switch_voice = (RelativeLayout) findViewById(R.id.rl_switch_voice);
        rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
        rl_switch_notification.setOnClickListener(this);
        rl_switch_voice.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);

        iv_open_notification = (ImageView) findViewById(R.id.iv_open_notification);
        iv_close_notification = (ImageView) findViewById(R.id.iv_close_notification);
        iv_open_voice = (ImageView) findViewById(R.id.iv_open_voice);
        iv_close_voice = (ImageView) findViewById(R.id.iv_close_voice);
        iv_open_vibrate = (ImageView) findViewById(R.id.iv_open_vibrate);
        iv_close_vibrate = (ImageView) findViewById(R.id.iv_close_vibrate);

//        tv_set_name = (TextView) findViewById(R.id.tv_set_name);
        btn_logout = (Button) findViewById(R.id.set_logout);

        // 初始化
        boolean isAllowNotify = true;////mSharedUtil.isAllowPushNotify();

        if (isAllowNotify) {
            iv_open_notification.setVisibility(View.VISIBLE);
            iv_close_notification.setVisibility(View.INVISIBLE);
        } else {
            iv_open_notification.setVisibility(View.INVISIBLE);
            iv_close_notification.setVisibility(View.VISIBLE);
        }
        boolean isAllowVoice = true;//mSharedUtil.isAllowVoice();
        if (isAllowVoice) {
            iv_open_voice.setVisibility(View.VISIBLE);
            iv_close_voice.setVisibility(View.INVISIBLE);
        } else {
            iv_open_voice.setVisibility(View.INVISIBLE);
            iv_close_voice.setVisibility(View.VISIBLE);
        }
        boolean isAllowVibrate = true;//mSharedUtil.isAllowVibrate();
        if (isAllowVibrate) {
            iv_open_vibrate.setVisibility(View.VISIBLE);
            iv_close_vibrate.setVisibility(View.INVISIBLE);
        } else {
            iv_open_vibrate.setVisibility(View.INVISIBLE);
            iv_close_vibrate.setVisibility(View.VISIBLE);
        }
        btn_logout.setOnClickListener(this);
//        layout_info.setOnClickListener(this);

    }

//    private void initData() {
//        tv_set_name.setText(BmobUserManager.getInstance(this)
//                .getCurrentUser().getUsername());
//    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
//            case R.id.layout_info:// 启动到个人资料页面
//                Intent intent2 = new Intent(this, SetInfoActivity.class);
//                intent2.putExtra("user", bmobUserManager.getCurrentUser(User.class));
//                intent2.putExtra("action", "me");
//                startActivity(intent2);
//                break;
//            case R.id.set_logout:
//                mApplication.logout();
//                Intent intent = new Intent(this,
//                        LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.rl_switch_notification:
                if (iv_open_notification.getVisibility() == View.VISIBLE) {
                    iv_open_notification.setVisibility(View.INVISIBLE);
                    iv_close_notification.setVisibility(View.VISIBLE);
                    mSharedUtil.setPushNotifyEnable(false);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    rl_switch_voice.setVisibility(View.GONE);
//                    view1.setVisibility(View.GONE);
//                    view2.setVisibility(View.GONE);
                } else {
                    iv_open_notification.setVisibility(View.VISIBLE);
                    iv_close_notification.setVisibility(View.INVISIBLE);
                    mSharedUtil.setPushNotifyEnable(true);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    rl_switch_voice.setVisibility(View.VISIBLE);
//                    view1.setVisibility(View.VISIBLE);
//                    view2.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.rl_switch_voice:
                if (iv_open_voice.getVisibility() == View.VISIBLE) {
                    iv_open_voice.setVisibility(View.INVISIBLE);
                    iv_close_voice.setVisibility(View.VISIBLE);
                    mSharedUtil.setAllowVoiceEnable(false);
                } else {
                    iv_open_voice.setVisibility(View.VISIBLE);
                    iv_close_voice.setVisibility(View.INVISIBLE);
                    mSharedUtil.setAllowVoiceEnable(true);
                }

                break;
            case R.id.rl_switch_vibrate:
                if (iv_open_vibrate.getVisibility() == View.VISIBLE) {
                    iv_open_vibrate.setVisibility(View.INVISIBLE);
                    iv_close_vibrate.setVisibility(View.VISIBLE);
                    mSharedUtil.setAllowVibrateEnable(false);
                } else {
                    iv_open_vibrate.setVisibility(View.VISIBLE);
                    iv_close_vibrate.setVisibility(View.INVISIBLE);
                    mSharedUtil.setAllowVibrateEnable(true);
                }
                break;

        }
    }

}

