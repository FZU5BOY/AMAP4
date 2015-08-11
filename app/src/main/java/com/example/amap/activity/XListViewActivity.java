package com.example.amap.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import com.example.amap.R;
import com.example.amap.view.xlist.XListView;
import com.example.amap.view.xlist.XListView.IXListViewListener;
import com.example.amap.activity.WriteComActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class XListViewActivity extends Activity implements IXListViewListener {
    private final String TAG = "XListViewActivity";
    private XListView mListView;
    // private ArrayAdapter<String> mAdapter;
    // private ArrayList<String> items = new ArrayList<String>();
    private Handler mHandler;
    ArrayList<HashMap<String, Object>> listItem;
    private int addMoreLmtS = 10;
    MyAdapter mAdapter;
    private boolean marked = false;
    String url = "http://192.168.191.1/get_comm.php";

    // private static int refreshCnt = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xlistview);
        HttpClient httpClient = new DefaultHttpClient();
        geneItems(url, httpClient);
        mListView = (XListView) findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);
        // List<Map<String, Object>> list = geneItems();
        mAdapter = new MyAdapter(this);
        mListView.setAdapter(mAdapter);
        // mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
        // mListView.setAdapter(mAdapter);
        // mListView.setPullLoadEnable(false);
        // mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
        Typeface icons = Typeface.createFromAsset(getAssets(),
                "fonts/iconfont.ttf");
        Button btn1 = (Button) findViewById(R.id.go_here);
        Button btn2 = (Button) findViewById(R.id.mark_place);
        Button btn3 = (Button) findViewById(R.id.write_comm);
        btn1.setTypeface(icons);
        btn2.setTypeface(icons);
        btn3.setTypeface(icons);

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
                // finish();//停止当前的Activity,如果不写,则按返回键会跳转回原来的Activity
            }

        });
    }

    private ArrayList<HashMap<String, Object>> geneItems(String url,
                                                         HttpClient httpClient) {
        getComms(url, httpClient, true, "0");
        addMoreLmtS = 10;
        return listItem;
    }

    private ArrayList<HashMap<String, Object>> addItems(String url,
                                                        HttpClient httpClient) {
        // listItem = new ArrayList<HashMap<String,Object>>();
        String lmtS = String.valueOf(addMoreLmtS);
        /** 为动态数组添加数据 */
        getComms(url, httpClient, false, lmtS);
        addMoreLmtS += 10;
        return listItem;
    }

    private ArrayList<HashMap<String, Object>> getComms(String url,
                                                        HttpClient httpClient, boolean newListItem, String lmtS) {
        if (newListItem) {
            listItem = new ArrayList<HashMap<String, Object>>();
        }

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("lmtS", lmtS));

        try {
            HttpPost postMethod = new HttpPost(url);
            postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 将参数填入POST
            // Entity中
            HttpResponse httpResponse = httpClient.execute(postMethod); // 执行POST方法

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String httpResult = EntityUtils.toString(
                        httpResponse.getEntity(), "utf-8");
                try {
                    JSONArray result = new JSONArray(httpResult);
                    if (result.length() == 0) {
                        Toast.makeText(getApplicationContext(), "已加载全部评论",
                                Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    /** 为动态数组添加数据 */
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("avatar", "dsb");
                        map.put("stars", jsonObject.getString("stars"));
                        map.put("username", jsonObject.getString("username"));
                        map.put("comm_time", jsonObject.getString("time"));
                        map.put("comm_content", jsonObject.getString("comment"));
                        listItem.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "网络错误",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return listItem;
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
//        mListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "刷新最新");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // start = ++refreshCnt;
                // items.clear();
                HttpClient httpClient = new DefaultHttpClient();
                geneItems(url, httpClient);
                mAdapter.notifyDataSetChanged();
                // mAdapter = new ArrayAdapter<String>(XListViewActivity.this,
                // R.layout.list_item, items);
                // MyAdapter mAdapter = new MyAdapter(XListViewActivity.this);
                // mListView.setAdapter(mAdapter);
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
                HttpClient httpClient = new DefaultHttpClient();
                addItems(url, httpClient);
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        private MyAdapter(Context context) {
            // 根据context上下文加载布局，这里的是Demo17Activity本身，即this
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
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.vlist, null);
            }
            ImageView avatar = (ImageView) convertView
                    .findViewById(R.id.avatar);
            RatingBar stars = (RatingBar) convertView.findViewById(R.id.stars);
            TextView user = (TextView) convertView.findViewById(R.id.username);
            TextView time = (TextView) convertView.findViewById(R.id.comm_time);
            TextView content = (TextView) convertView
                    .findViewById(R.id.comm_content);
            int resID = getResources().getIdentifier(
                    listItem.get(position).get("avatar").toString(),
                    "drawable", "org.com.cctest");
            avatar.setImageResource(resID);
            stars.setRating((float) Integer.parseInt(listItem.get(position)
                    .get("stars").toString()));
            user.setText(listItem.get(position).get("username").toString());
            time.setText(listItem.get(position).get("comm_time").toString());
            content.setText(listItem.get(position).get("comm_content")
                    .toString());
            return convertView;
        }
    }

}