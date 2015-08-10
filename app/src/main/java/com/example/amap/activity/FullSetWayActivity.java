package com.example.amap.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.amap.R;

/**
 * Created by Zeashon on 2015/8/8.
 */
public class FullSetWayActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_set_way);
        Button back_btn = (Button) findViewById(R.id.fsw_GoBack);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullSetWayActivity.this.finish();
            }
        });
    }
    public void fsw01OnClick(View v){
        String str=((TextView)findViewById(R.id.content1)).getText().toString();
        Bundle bundle=new Bundle();
        bundle.putString("strList", str);
        Intent intent=getIntent();
        intent.putExtras(bundle);
        this.setResult(3, intent);
        this.finish();
    }
    public void fsw02OnClick(View v){
        String str=((TextView)findViewById(R.id.content2)).getText().toString();
        Bundle bundle=new Bundle();
        bundle.putString("strList", str);
        Intent intent=getIntent();
        intent.putExtras(bundle);
        this.setResult(3, intent);
        this.finish();
    }
    public void fsw03OnClick(View v){
        String str=((TextView)findViewById(R.id.content3)).getText().toString();
        Bundle bundle=new Bundle();
        bundle.putString("strList", str);
        Intent intent=getIntent();
        intent.putExtras(bundle);
        this.setResult(3, intent);
        this.finish();
    }
//    @Override
//    public void onClick(View v) {
//        ShowLog("view:" + v.getId());
//        switch (v.getId()){
//            case R.id.fsw01:{
//                String str=((TextView)findViewById(R.id.content1)).getText().toString();
//                Bundle bundle=new Bundle();
//                bundle.putString("strList", str);
//                Intent intent=getIntent();
//                intent.putExtras(bundle);
//                this.setResult(3, intent);
//                this.finish();
//                break;
//            }
//            case R.id.fsw02:{
//                String str=((TextView)findViewById(R.id.content2)).getText().toString();
//                Bundle bundle=new Bundle();
//                bundle.putString("strList", str);
//                Intent intent=getIntent();
//                intent.putExtras(bundle);
//                this.setResult(3, intent);
//                this.finish();
//                break;
//            }
//            case R.id.fsw03:{
//                String str=((TextView)findViewById(R.id.content3)).getText().toString();
//                Bundle bundle=new Bundle();
//                bundle.putString("strList", str);
//                Intent intent=getIntent();
//                intent.putExtras(bundle);
//                this.setResult(3, intent);
//                this.finish();
//                break;
//            }
//            default:break;
//        }
//    }
}
