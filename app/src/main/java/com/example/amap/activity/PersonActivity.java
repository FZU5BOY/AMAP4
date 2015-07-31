package com.example.amap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.amap.R;
import com.example.amap.util.CircularImage;

/**
 * Created by Zeashon on 2015/5/10.
 */
public class PersonActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person);
        Button btn = (Button)findViewById(R.id.makeRoad_GoBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        CircularImage cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
        cover_user_photo.setImageResource(R.drawable.head);
        CircularImage cover_user_photo_circle = (CircularImage) findViewById(R.id.circle);
        cover_user_photo_circle.setImageResource(R.drawable.circle);
        RelativeLayout setbtn = (RelativeLayout)findViewById(R.id.user_set);
        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
