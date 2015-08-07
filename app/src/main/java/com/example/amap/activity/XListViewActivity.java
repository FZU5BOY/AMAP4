package com.example.amap.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.amap.R;
import com.example.amap.view.xlist.XListView;
import com.example.amap.view.xlist.XListView.IXListViewListener;
import com.example.amap.activity.WriteComActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class XListViewActivity extends Activity implements IXListViewListener {
    private final String TAG = "XListViewActivity";
    private XListView mListView;
    //	private ArrayAdapter<String> mAdapter;
//	private ArrayList<String> items = new ArrayList<String>();
    private Handler mHandler;
    ArrayList<HashMap<String, Object>> listItem;
    private int j = 0;
    MyAdapter mAdapter;
    private boolean marked = false;
//	private static int refreshCnt = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xlistview);
        geneItems();
        mListView = (XListView) findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);
//		List<Map<String, Object>> list = geneItems();
        mAdapter = new MyAdapter(this);
        mListView.setAdapter(mAdapter);
//		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
//		mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
        Typeface icons = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        Button btn1 = (Button) findViewById(R.id.go_here);
        Button btn2 = (Button) findViewById(R.id.mark_place);
        Button btn3 = (Button) findViewById(R.id.write_comm);
        btn1.setTypeface(icons);
        btn2.setTypeface(icons);
        btn3.setTypeface(icons);
//        back to index
        Button btnGoBack = (Button) findViewById(R.id.detail_GoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XListViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button btn2 = (Button) findViewById(R.id.mark_place);
                if (!marked) {
                    btn2.setText(R.string.marked_place);
                    marked = true;
                } else {
                    btn2.setText(R.string.mark_place);
                    marked = false;
                }
            }

        });

        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(XListViewActivity.this, WriteComActivity.class);
                startActivity(intent);
//                finish();//停止当前的Activity,如果不写,则按返回键会跳转回原来的Activity        
            }

        });
    }

    private ArrayList<HashMap<String, Object>> geneItems() {
        listItem = new ArrayList<HashMap<String, Object>>();
        /**为动态数组添加数据*/
//		int j=i+30;
        for (int i = 1; i < 30; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String user = "大帅比" + j;
            map.put("avatar", "dsb");
            map.put("stars", "4");
            map.put("username", user);
            map.put("comm_time", "8月1日");
            map.put("comm_content", "hahahahaha");
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
            String user = "大帅比" + j;
            map.put("avatar", "dsb");
            map.put("stars", "3");
            map.put("username", user);
            map.put("comm_time", "8.1");
            map.put("comm_content", "hahahahaha");
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
                convertView = mInflater.inflate(R.layout.vlist, null);
            }
            ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
            RatingBar stars = (RatingBar) convertView.findViewById(R.id.stars);
            TextView user = (TextView) convertView.findViewById(R.id.username);
            TextView time = (TextView) convertView.findViewById(R.id.comm_time);
            TextView content = (TextView) convertView.findViewById(R.id.comm_content);
            int resID = getResources().getIdentifier(listItem.get(position).get("avatar").toString(), "drawable", "com.example.amap");
            avatar.setImageResource(resID);
            stars.setRating((float) Integer.parseInt(listItem.get(position).get("stars").toString()));
            user.setText(listItem.get(position).get("username").toString());
            time.setText(listItem.get(position).get("comm_time").toString());
            content.setText(listItem.get(position).get("comm_content").toString());
            return convertView;
        }
    }


}