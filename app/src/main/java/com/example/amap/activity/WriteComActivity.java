package com.example.amap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.amap.R;

public class WriteComActivity extends Activity{
    private Button submit_commBtn;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.write_comm);
        submit_commBtn = (Button) findViewById(R.id.submit_comm);
        submit_commBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zjx", "comment finished,click to shop detail");
                Intent intent = new Intent(WriteComActivity.this, XListViewActivity.class);
                startActivity(intent);
            }
        });
    }  
      
} 