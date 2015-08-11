package com.example.amap.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.amap.R;
import com.example.amap.util.UnzipAssets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Zeashon on 2015/5/10.
 */
public class NaviActivity extends Activity {

    private ViewPager viewPager;
    private PagerTitleStrip pageTitle;

    private PagerTitleStrip pageTab;
    private List<View> views;
    private List<String> titles;
    private ImageView[] imageViews;
    private SharedPreferences setting;
    private String extern = Environment.getExternalStorageDirectory().getPath();
    public String OUTPUT_DIRECTORY = extern + "/arcgis";
    boolean iszipok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setting = this.getSharedPreferences("check", MODE_PRIVATE);
        boolean fristload = setting.getBoolean("fristload", true);
        if (!fristload) {
            Intent intent = new Intent(NaviActivity.this, SplashScreen.class);
            startActivity(intent);
            NaviActivity.this.finish();
        }

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        super.onCreate(savedInstanceState);
        if (fristload) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.navi);
            Button useapp = (Button) findViewById(R.id.useapp);
            useapp.setOnClickListener(new View.OnClickListener() {
                ProgressDialog dialog = new ProgressDialog(NaviActivity.this);

                @Override
                public void onClick(View v) {
//                dialog.setTitle("提示");
                    dialog.setMessage("初次使用，正在导入地图文件！");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();//显示对话框
                    new Thread() {
                        public void run() {
                            //在新线程中以同名覆盖方式解压
                            try {
                                while (!iszipok) {
                                    Thread.sleep(100);
                                }
                                dialog.setMessage("导入成功...");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            dialog.cancel();//解压完成后关闭对话框
                            setting = getApplicationContext().getSharedPreferences("check", MODE_PRIVATE);
                            setting.edit().putBoolean("fristload", false).commit();
                            Intent intent = new Intent(NaviActivity.this, MainActivity.class);
                            startActivity(intent);
                            NaviActivity.this.finish();
                        }
                    }.start();

                }
            });

            Button loginPageBtn = (Button) findViewById(R.id.loginBtn_navi);
            loginPageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread() {
                        public void run() {
                            //在新线程中以同名覆盖方式解压
                            try {
                                while (!iszipok) {
                                    Thread.sleep(100);
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            setting = getApplicationContext().getSharedPreferences("check", MODE_PRIVATE);
                            setting.edit().putBoolean("fristload", false).commit();
                            Intent intent = new Intent(NaviActivity.this, LoginActivity.class);
                            startActivity(intent);
                            NaviActivity.this.finish();
                        }
                    }.start();

                }
            });

            Button regPageBtn = (Button) findViewById(R.id.registerBtn_navi);
            regPageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread() {
                        public void run() {
                            //在新线程中以同名覆盖方式解压
                            try {
                                while (!iszipok) {
                                    Thread.sleep(100);
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            setting = getApplicationContext().getSharedPreferences("check", MODE_PRIVATE);
                            setting.edit().putBoolean("fristload", false).commit();
                            Intent intent = new Intent(NaviActivity.this, RegiActivity.class);
                            startActivity(intent);
                            NaviActivity.this.finish();
                        }
                    }.start();
                }
            });

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            //使用pageTitleStrip时标签无点击响应
            //pageTitle = (PagerTitleStrip)findViewById(R.id.pagertitle);
            pageTab = (PagerTitleStrip) findViewById(R.id.pagertitle);

            views = new ArrayList<View>();


            LayoutInflater layoutInflater = getLayoutInflater();
            View view1 = layoutInflater.inflate(R.layout.view1, null);
            View view2 = layoutInflater.inflate(R.layout.view2, null);
            View view3 = layoutInflater.inflate(R.layout.view3, null);

            views.add(view1);
            views.add(view2);
            views.add(view3);


            imageViews = new ImageView[3];
            imageViews[0] = (ImageView) findViewById(R.id.dot0);
            imageViews[1] = (ImageView) findViewById(R.id.dot1);
            imageViews[2] = (ImageView) findViewById(R.id.dot2);

            titles = new ArrayList<String>();
            titles.add("tab1");
            titles.add("tab2");
            titles.add("tab3");

            PagerAdapter pageAdapter = new PagerAdapter() {


                @Override
                public void destroyItem(View container, int position, Object object) {
                    // TODO Auto-generated method stub
                    ((ViewPager) container).removeView(views.get(position));
                }

                @Override
                public Object instantiateItem(View container, int position) {
                    // TODO Auto-generated method stub
                    ((ViewPager) container).addView(views.get(position));
                    return views.get(position);
                }

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    // TODO Auto-generated method stub
                    return arg0 == arg1;
                }

                @Override
                public int getCount() {
                    // TODO Auto-generated method stub
                    return views.size();
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    // TODO Auto-generated method stub
                    return titles.get(position);
                }
            };

            viewPager.setAdapter(pageAdapter);
//        public onPageScrolled(int arg0,float arg1,int arg2)
            viewPager.setOnPageChangeListener(new NavigationPageChangeListener());

            UnZip unZip = new UnZip(getApplicationContext());
            unZip.execute("fff");
        }
    }

    class UnZip extends AsyncTask<String, Integer, String> {
        //		ProgressDialog pdialog;
        Context mContext;

        public UnZip(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Date a = new Date();
                UnzipAssets.unZip(NaviActivity.this, "b1.zip", OUTPUT_DIRECTORY, true);
                UnzipAssets.unZip(NaviActivity.this, "f1.zip", OUTPUT_DIRECTORY, true);
                UnzipAssets.unZip(NaviActivity.this, "f2.zip", OUTPUT_DIRECTORY, true);
                Date b = new Date();
                iszipok = true;
                Log.i("zjx", "the time is:" + (b.getTime() - a.getTime()));

            } catch (Exception e) {
                Log.i("zjx", "" + e.toString());
                Log.i("zjx", "解压失败");
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {


        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    private class NavigationPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            Button useapp = (Button) findViewById(R.id.useapp);
            LinearLayout login_reg_btn_layout = (LinearLayout) findViewById(R.id.login_reg_btn_layout_navi);
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[position]
                        .setBackgroundResource(R.drawable.iconfont_dotb);
                // 不是当前选中的page，其小圆点设置为未选中的状态
                if (position != i) {
                    imageViews[i].setBackgroundResource(R.drawable.iconfont_dot);
                }
                if (position == (imageViews.length - 1)) {
                    useapp.setVisibility(View.VISIBLE);
                    login_reg_btn_layout.setVisibility(View.VISIBLE);
                } else {
                    useapp.setVisibility(View.GONE);
                    login_reg_btn_layout.setVisibility(View.GONE);
                }
            }
        }
    }

}


