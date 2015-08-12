package com.example.amap.activity.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amap.CustomApplcation;
import com.example.amap.R;
import com.example.amap.activity.BlackListActivity;
import com.example.amap.activity.FragmentBase;
import com.example.amap.activity.LoginActivity;
import com.example.amap.activity.MainActivity;
import com.example.amap.activity.NaviActivity;
import com.example.amap.activity.SetMyInfoActivity;
import com.example.amap.util.SharePreferenceUtil;
import com.example.amap.util.rount.MyPoint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.im.BmobUserManager;


public class SettingsFragment extends FragmentBase implements OnClickListener {

    Button btn_logout,btnGoback,changeIp;
    TextView tv_set_name;
    RelativeLayout layout_info, rl_switch_notification, rl_switch_voice,
            rl_switch_vibrate, layout_blacklist;

    ImageView iv_open_notification, iv_close_notification, iv_open_voice,
            iv_close_voice, iv_open_vibrate, iv_close_vibrate;

    View view1, view2;
    SharePreferenceUtil mSharedUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mSharedUtil = mApplication.getSpUtil();
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initView();
//        initData();
    }

    private void initView() {
//		initTopBarForOnlyTitle("设置");
        //黑名单列表
        layout_blacklist = (RelativeLayout) findViewById(R.id.layout_blacklist);

        layout_info = (RelativeLayout) findViewById(R.id.layout_info);
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
        view1 = (View) findViewById(R.id.view1);
        view2 = (View) findViewById(R.id.view2);

        tv_set_name = (TextView) findViewById(R.id.tv_set_name);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        // 初始化
        boolean isAllowNotify = mSharedUtil.isAllowPushNotify();

        if (isAllowNotify) {
            iv_open_notification.setVisibility(View.VISIBLE);
            iv_close_notification.setVisibility(View.INVISIBLE);
        } else {
            iv_open_notification.setVisibility(View.INVISIBLE);
            iv_close_notification.setVisibility(View.VISIBLE);
        }
        boolean isAllowVoice = mSharedUtil.isAllowVoice();
        if (isAllowVoice) {
            iv_open_voice.setVisibility(View.VISIBLE);
            iv_close_voice.setVisibility(View.INVISIBLE);
        } else {
            iv_open_voice.setVisibility(View.INVISIBLE);
            iv_close_voice.setVisibility(View.VISIBLE);
        }
        boolean isAllowVibrate = mSharedUtil.isAllowVibrate();
        if (isAllowVibrate) {
            iv_open_vibrate.setVisibility(View.VISIBLE);
            iv_close_vibrate.setVisibility(View.INVISIBLE);
        } else {
            iv_open_vibrate.setVisibility(View.INVISIBLE);
            iv_close_vibrate.setVisibility(View.VISIBLE);
        }
        btn_logout.setOnClickListener(this);
        layout_info.setOnClickListener(this);
        layout_blacklist.setOnClickListener(this);
        changeIp = (Button) findViewById(R.id.button15);
        changeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = View.inflate(getActivity(), R.layout.a_editor, null);
                final String FILE_NAME = "ip.txt";
                Dialog abc = new AlertDialog.Builder(getActivity())
                        .setView(view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText et = (EditText) view.findViewById(R.id.editText3);
                                String s = et.getText().toString();
                                if (s.length() == 0) {
                                    Toast.makeText(getActivity(), "Input something.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                FileInputStream fis = null;
                                FileOutputStream fos = null;
                                try {
                                    fos = getActivity().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
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
                                    fis = getActivity().openFileInput(FILE_NAME);
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
                    fis = getActivity().openFileInput(FILE_NAME);
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
                final View view = View.inflate(getActivity(), R.layout.emailtext, null);
                new AlertDialog.Builder(getActivity())
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
                String shareMsg = getResources().getString(R.string.imlocation);
                String shareMsg2 = "http://zjx.com/im?";
                MyPoint sb = MainActivity.locateMyPoint;
                if (sb != null) shareMsg2 += "x=" + sb.x + "&y=" + sb.y + "&z=" + sb.z;
                Log.i("zjx", "shareMsg2:" + shareMsg2);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg + "  " + shareMsg2);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(shareIntent.createChooser(shareIntent, getActivity().getTitle()));
            }
        });

//        Button btnNavi = (Button) findViewById(R.id.go_navi_btn_set);
//        btnNavi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), NaviActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void initData() {
        tv_set_name.setText(BmobUserManager.getInstance(getActivity())
                .getCurrentUser().getUsername());
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.layout_blacklist:// 启动到黑名单页面
                startAnimActivity(new Intent(getActivity(), BlackListActivity.class));
                break;
            case R.id.layout_info:// 启动到个人资料页面
                Intent intent = new Intent(getActivity(), SetMyInfoActivity.class);
                intent.putExtra("from", "me");
                startActivity(intent);
                break;
            case R.id.btn_logout:
                CustomApplcation.getInstance().logout();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.rl_switch_notification:
                if (iv_open_notification.getVisibility() == View.VISIBLE) {
                    iv_open_notification.setVisibility(View.INVISIBLE);
                    iv_close_notification.setVisibility(View.VISIBLE);
                    mSharedUtil.setPushNotifyEnable(false);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    rl_switch_voice.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                } else {
                    iv_open_notification.setVisibility(View.VISIBLE);
                    iv_close_notification.setVisibility(View.INVISIBLE);
                    mSharedUtil.setPushNotifyEnable(true);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    rl_switch_voice.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
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
