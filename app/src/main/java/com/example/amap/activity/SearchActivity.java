package com.example.amap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amap.adapter.ListAdapter;
import com.example.amap.R;
import com.example.amap.util.PoiSearch;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchActivity extends Activity {
    private RecognizerDialog rd;
    List<PoiSearch> poiSearch = new ArrayList<PoiSearch>() ;
    EditText editinput;
    ListView listview;
    ListAdapter adapter;
    Button search_btn;
    Button search_video;
    final String FILE_NAME = "history.txt";
    protected static final String TAG = "zjx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("zjx","searchactivity create");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_poi);
        editinput = (EditText)findViewById(R.id.search_edit);
        listview = (ListView)findViewById(R.id.listView);
        Button btn = (Button) findViewById(R.id.makeRoad_GoBack);
        search_btn = (Button) findViewById(R.id.button3);
        search_video=(Button)findViewById(R.id.search_video);
        search_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReconigizerDialog();
            }
        });
        rd = new RecognizerDialog(this ,"appid=50e1b967");
        poiSearch = MainActivity.getDataName();
        adapter = new ListAdapter(getApplicationContext(), poiSearch);
//        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
//        animationAdapter.setAbsListView(listview);
        listview.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i("zjx", "你点击了ListView条目" + arg2);//在LogCat中输出信息
                Log.i("zjx", "" + ((PoiSearch) listview.getItemAtPosition(arg2)).getName());
                String search_value=((PoiSearch) listview.getItemAtPosition(arg2)).getName();
                Bundle bundle=new Bundle();
                bundle.putSerializable("searchkey", search_value);
                Intent intent=getIntent();
                intent.putExtras(bundle);
                SearchActivity.this.setResult(0, intent);
                SearchActivity.this.finish();

            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myText = editinput.getText().toString();
                //空字符提示、结束函数不做改动
                if (myText.length() == 0) {
                    return;
                }
//                writeHis();
                // 跳转到MainActivity
                String search_value=editinput.getText().toString();
                Bundle bundle=new Bundle();
                bundle.putSerializable("searchkey", search_value);
                Intent intent=getIntent();
                intent.putExtras(bundle);
                SearchActivity.this.setResult(0,intent);
                SearchActivity.this.finish();

            }
        });
        editinput.addTextChangedListener(new watcher());
        //监听回车键
        editinput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
                String myText = editinput.getText().toString();
                //空字符提示、结束函数不做改动
                if (myText.length() == 0) {
                    return false;
                }
                Bundle bundle=new Bundle();
                bundle.putSerializable("searchkey", myText);
                Intent intent=getIntent();
                intent.putExtras(bundle);
                SearchActivity.this.setResult(0,intent);
                SearchActivity.this.finish();
//                writeHis();
                //清空输入框
                return true;
            }
        });
    }
    @Override
    protected void onPostCreate (Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

        Log.i("zjx", "oncreate ok");
        if("video".equals(this.getIntent().getStringExtra("Name"))){
            showReconigizerDialog();
            return;
        }
        editinput.setText(this.getIntent().getStringExtra("currentsearch"));

    }
    private void showReconigizerDialog() {
        //setEngine(String engine,String params,String grammar);
        /**
         * 识别引擎选择，目前支持以下五种
         “sms”：普通文本转写
         “poi”：地名搜索
         “vsearch”：热词搜索
         “vsearch”：热词搜索
         “video”：视频音乐搜索
         “asr”：命令词识别

         params	引擎参数配置列表
         附加参数列表，每项中间以逗号分隔，如在地图搜索时可指定搜索区域：“area=安徽省合肥市”，无附加参数传null
         */
        rd.setEngine("poi", null, null);

        //设置采样频率，默认是16k，android手机一般只支持8k、16k.为了更好的识别，直接弄成16k即可。
        rd.setSampleRate(SpeechConfig.RATE.rate16k);

        final StringBuilder sb = new StringBuilder();
        Log.i(TAG, "识别准备开始.............");

        //设置识别后的回调结果
        rd.setListener(new RecognizerDialogListener() {
            @Override
            public void onResults(ArrayList<RecognizerResult> result, boolean isLast) {
                for (RecognizerResult recognizerResult : result) {
                    sb.append(recognizerResult.text);
                    Log.i(TAG, "识别一条结果为::"+recognizerResult.text);
                }
            }
            @Override
            public void onEnd(SpeechError error) {
                Log.i(TAG, "识别完成.............");
                editinput.setText(sb.toString());

            }
        });

        editinput.setText(""); //先设置为空，等识别完成后设置内容
        rd.show();
    }

    class watcher implements TextWatcher{

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0) {

                search_btn.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            if (count == 0) {
                search_btn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            String aa = s.toString();
            Pattern p = Pattern.compile(aa);
            List<PoiSearch> we = new ArrayList<PoiSearch>();
            for(int i=0;i< poiSearch.size();i++){
                PoiSearch pp = poiSearch.get(i);
                Matcher matcher = p.matcher(pp.getName());
                if(matcher.find()){
                    we.add(pp);
                }
            }
            adapter = new ListAdapter(getApplicationContext(), we);
            listview.setAdapter(adapter);
        }

    }

}