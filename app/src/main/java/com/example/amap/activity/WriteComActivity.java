package com.example.amap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.example.amap.R;

public class WriteComActivity extends Activity{
    private Button submit_commBtn;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.write_comm);
//        comment event
        Button submitBtn = (Button) findViewById(R.id.submit_comm);
        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HttpClient httpClient = new DefaultHttpClient();
                String url = "http://192.168.191.1/write_comm.php";
                uploadData(url, httpClient);
            }
        });
//        cancel event
        Button btnCancel = (Button) findViewById(R.id.cancel_comm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zjx", "comment canceled,back to shop detail");
                Intent intent = new Intent(WriteComActivity.this, XListViewActivity.class);
                startActivity(intent);
            }
        });
    }
//    data upload
    private void uploadData(String url, HttpClient httpClient) {
        EditText comm_content = (EditText) findViewById(R.id.comm_content);
        RatingBar rate_stars = (RatingBar) findViewById(R.id.rate_stars);
        String commContent = comm_content.getText().toString();
        String rateStars = String.valueOf(rate_stars.getRating());

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("username", "中国"));
        params.add(new BasicNameValuePair("comment", commContent));
        params.add(new BasicNameValuePair("stars", rateStars));

        try {
            HttpPost postMethod = new HttpPost(url);
            postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 将参数填入POST
            // Entity中
            HttpResponse httpResponse = httpClient.execute(postMethod); // 执行POST方法

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //String httpResult = EntityUtils.toString(
                //	httpResponse.getEntity(), "utf-8");
                Toast.makeText(getApplicationContext(), "评论成功！",
                        Toast.LENGTH_SHORT).show();
                finish();
            }else{
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

    }
}
