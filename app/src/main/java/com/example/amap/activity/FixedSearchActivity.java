package com.example.amap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.amap.R;

/**
 * Created by Zeashon on 2015/5/10.
 */
public class FixedSearchActivity extends Activity {
    Button churukou;
    Button dengjikou;
    Button anjian;
    Button shangdian;
    Button atm;
    Button weishengjian;
    Button dianti;
    TextView fix_text1;
    TextView fix_text2;
    TextView fix_text3;
    TextView fix_text4;
    TextView fix_text5;
    TextView fix_text6;
    TextView fix_text7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixed_search);
        Button btn = (Button)findViewById(R.id.makeRoad_GoBack);
        churukou=(Button)findViewById(R.id.t01);
        churukou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentIntent(getResources().getString(R.string.churukou));
            }
        });
        dengjikou=(Button)findViewById(R.id.t03);
        dengjikou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentIntent(getResources().getString(R.string.dengjikou));
            }
        });
        anjian=(Button)findViewById(R.id.t05);
        anjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentIntent(getResources().getString(R.string.anjian));
            }
        });
        shangdian=(Button)findViewById(R.id.t07);
        shangdian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentIntent(getResources().getString(R.string.shangdian));
            }
        });
        atm=(Button)findViewById(R.id.t09);
        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentIntent(getResources().getString(R.string.atm));
            }
        });
        weishengjian=(Button)findViewById(R.id.t11);
        weishengjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentIntent(getResources().getString(R.string.weishengjian));
            }
        });
        dianti=(Button)findViewById(R.id.t13);
        dianti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentIntent(getResources().getString(R.string.dianti));
            }
        });
        fix_text1=(TextView)findViewById(R.id.fix_text1);
        fix_text2=(TextView)findViewById(R.id.fix_text2);
        fix_text3=(TextView)findViewById(R.id.fix_text3);
        fix_text4=(TextView)findViewById(R.id.fix_text4);
        fix_text5=(TextView)findViewById(R.id.fix_text5);
        fix_text6=(TextView)findViewById(R.id.fix_text6);
        fix_text7=(TextView)findViewById(R.id.fix_text7);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FixedSearchActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
//    @Override
//    public void onClick(View v){
//        Log.i("zjx",""+v.getId());
//        switch (v.getId()){
//            case R.id.churukou:sentIntent(getResources().getString(R.string.churukou));break;
//            case R.id.dengjikou:sentIntent(getResources().getString(R.string.dengjikou));break;
//            case R.id.anjian:sentIntent(getResources().getString(R.string.anjian));break;
//            case R.id.shangdian:sentIntent(getResources().getString(R.string.shangdian));break;
//            case R.id.atm:sentIntent(getResources().getString(R.string.atm));break;
//            case R.id.weishengjian:sentIntent(getResources().getString(R.string.weishengjian));break;
//            case R.id.dianti:sentIntent(getResources().getString(R.string.dianti));break;
//            default:
//                Log.i("zjx","other button");
//        }
//    }
    public void sentIntent(String s){
        Intent intent=getIntent();
        intent.putExtra("fix_search",s);
        FixedSearchActivity.this.setResult(2, intent);
        FixedSearchActivity.this.finish();
    }
    @Override
    protected void onPostCreate (Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        boolean islocate=this.getIntent().getBooleanExtra("islocate",false);
        if(!islocate){
            fix_text1.setText("未定位");
            fix_text2.setText("未定位");
            fix_text3.setText("未定位");
            fix_text4.setText("未定位");
            fix_text5.setText("未定位");
            fix_text6.setText("未定位");
            fix_text7.setText("未定位");
        }
        else{
//            距离最近是13m
            double churukoudouble=this.getIntent().getDoubleExtra("churukou",0);
            if(churukoudouble>9999999)fix_text1.setText("在附近未找到");
            else fix_text1.setText("距离最近是"+churukoudouble*10+"m");

            double dengjikoudouble=this.getIntent().getDoubleExtra("dengjikou",0);
            if(dengjikoudouble>9999999)fix_text2.setText("在附近未找到");
            else fix_text2.setText("距离最近是"+dengjikoudouble*10+"m");

            double anjiandouble=this.getIntent().getDoubleExtra("anjian",0);
            if(anjiandouble>9999999)fix_text3.setText("在附近未找到");
            else fix_text3.setText("距离最近是"+anjiandouble*10+"m");

            double shangdiandouble=this.getIntent().getDoubleExtra("shangdian",0);
            if(shangdiandouble>9999999)fix_text4.setText("在附近未找到");
            else fix_text4.setText("距离最近是"+shangdiandouble*10+"m");

            double atmdouble=this.getIntent().getDoubleExtra("atm",0);
            if(atmdouble>9999999)fix_text5.setText("在附近未找到");
            else fix_text5.setText("距离最近是"+atmdouble*10+"m");

            double weishengjiandouble=this.getIntent().getDoubleExtra("weishengjian",0);
            if(weishengjiandouble>9999999)fix_text6.setText("在附近未找到");
            else fix_text6.setText("距离最近是"+weishengjiandouble*10+"m");

            double diantidouble=this.getIntent().getDoubleExtra("dianti",0);
            if(diantidouble>9999999)fix_text7.setText("在附近未找到");
            else fix_text7.setText("距离最近是"+diantidouble*10+"m");
        }
    }
}
