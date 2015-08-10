package com.example.amap.activity.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amap.CustomApplcation;
import com.example.amap.R;
import com.example.amap.activity.BlackListActivity;
import com.example.amap.activity.FragmentBase;
import com.example.amap.activity.LoginActivity;
import com.example.amap.activity.MainActivity;
import com.example.amap.activity.SetMyInfoActivity;
import com.example.amap.util.CircularImage;
import com.example.amap.util.SharePreferenceUtil;
import com.example.amap.util.rount.MyPoint;
import com.example.amap.view.xlist.XListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import cn.bmob.im.BmobUserManager;
import jcifs.https.Handler;


public class MyCircleFragment extends FragmentBase implements XListView.IXListViewListener  {
    TextView user_set_name;
    ImageView cover_user_photo;
    private android.os.Handler mHandler;
    MyAdapter mAdapter;
    private final String TAG = "PersonActivity";
    private int j = 1;
    ArrayList<HashMap<String, Object>> listItem;
    private XListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.person, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        user_set_name = (TextView) findViewById(R.id.user_name);
        cover_user_photo = (ImageView) findViewById(R.id.cover_user_photo);
        geneItems();
        mListView = (XListView) findViewById(R.id.mycirclexListView);
        mListView.setPullLoadEnable(true);
//		List<Map<String, Object>> list = geneItems();
        mAdapter = new MyAdapter(getActivity());
        mListView.setAdapter(mAdapter);
//		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
//		mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
        mHandler = new android.os.Handler();
        cover_user_photo.setImageResource(R.drawable.head);
    }

    @SuppressLint("NewApi")
    private void initData() {
        user_set_name.setText(BmobUserManager.getInstance(getActivity())
                .getCurrentUser().getUsername().trim());
//        new AnotherTask().execute("My Avata");

    }
//    public Bitmap loadDrawable()
//    {
//        final Handler handler = new Handler()
//        {
//            public void handleMessage(Message message)
//            {
//                imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
//            }
//        };
//        new Thread()
//        {
//            @Override
//            public void run()
//            {
//                if (bmUserImage == null) {
//                    cover_user_photo.setImageResource(R.drawable.head);
//                } else {
//                    cover_user_photo.setImageBitmap(bmUserImage);
//                }
//            }
//        }.start();
//        return null;
//    }
    private class AnotherTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPostExecute(String result) {
            //对UI组件的更新操作
            URL picUrl = null;
            try {
                picUrl = new URL(BmobUserManager.getInstance(getActivity())
                        .getCurrentUser().getAvatar());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Bitmap bmUserImage = null;
            try {
                bmUserImage = BitmapFactory.decodeStream(picUrl.openStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (bmUserImage == null) {
                cover_user_photo.setImageResource(R.drawable.head);
            } else {
                cover_user_photo.setImageBitmap(bmUserImage);
            }
        }
        @Override
        protected String doInBackground(String... params) {
            //耗时的操作
            try
            {
                //线程睡眠5秒，模拟耗时操作，这里面的内容Android系统会自动为你启动一个新的线程执行
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return params[0];
        }
    }
    private ArrayList<HashMap<String, Object>> geneItems() {
        listItem = new ArrayList<HashMap<String, Object>>();
        /**为动态数组添加数据*/
//		int j=i+30;
        for (int i = 1; i < 30; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String user = "天下第"+i*j+"帅";
            map.put("avatar", "cat");
            map.put("likePic", "z_like");
            map.put("likedPic", "z_liked");
//            map.put("stars", "4");
            map.put("username", user);
            map.put("comm_time", "8月1日");
            map.put("comm_content", "来来来，搞个大新闻。");
            listItem.add(map);
        }
        return listItem;
    }

    private ArrayList<HashMap<String, Object>> addItems() {
//		listItem = new ArrayList<HashMap<String,Object>>();
        /**为动态数组添加数据*/
//		int j=i+30;
        for (int i = 1; i < 30; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String user = "天下第"+i+"帅";
            map.put("avatar", "cat");
            map.put("likePic", "z_like");
            map.put("likedPic", "z_liked");
//            map.put("stars", "3");
            map.put("username", user);
            map.put("comm_time", "8月3日");
            map.put("comm_content", "今天去优衣库逛了一圈。就这样。");
            listItem.add(map);
        }
        return listItem;
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
//		mListView.setRefreshTime("刚刚");  不知道为什么合并这里会报错、暂时注释、
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "刷新最新");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//				start = ++refreshCnt;
//				items.clear();
                j++;
                geneItems();
                mAdapter.notifyDataSetChanged();
//				mAdapter = new ArrayAdapter<String>(XListViewActivity.this, R.layout.list_item, items);
//				MyAdapter mAdapter = new MyAdapter(XListViewActivity.this);
//				mListView.setAdapter(mAdapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        Log.i(TAG, "加载更多");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                j++;
                addItems();
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        private MyAdapter(Context context) {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listItem.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.circlelistview, null);
            }
            ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
//            RatingBar stars = (RatingBar) convertView.findViewById(R.id.stars);
            TextView user = (TextView) convertView.findViewById(R.id.username);
            TextView time = (TextView) convertView.findViewById(R.id.comm_time);
            TextView content = (TextView) convertView.findViewById(R.id.comm_content);
            final ImageButton circle_like = (ImageButton) convertView.findViewById(R.id.circle_like);
            int resID = getResources().getIdentifier(listItem.get(position).get("avatar").toString(), "drawable", "com.example.amap");
            int likedPicID = getResources().getIdentifier(listItem.get(position).get("likePic").toString(), "drawable", "com.example.amap");
            avatar.setImageResource(resID);
            circle_like.setImageResource(likedPicID);
//            stars.setRating((float) Integer.parseInt(listItem.get(position).get("stars").toString()));
            user.setText(listItem.get(position).get("username").toString());
            time.setText(listItem.get(position).get("comm_time").toString());
            content.setText(listItem.get(position).get("comm_content").toString());
            circle_like.setTag(position);
            circle_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = Integer.parseInt(v.getTag().toString());
                    int likedPicID = getResources().getIdentifier(listItem.get(posi).get("likedPic").toString(), "drawable", "com.example.amap");
                    circle_like.setImageResource(likedPicID);
                }
            });
            return convertView;
        }
    }
}
