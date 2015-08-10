package cn.bmob.im;

import android.text.TextUtils;

import com.example.amap.bean.AMapPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import g.acknowledge;

/**
 * Created by Administrator on 2015/8/10.
 */
public class MyBmobQuery<T> extends BmobQuery<T> {
    private JSONObject m = new JSONObject();
    public BmobQuery<T> addWhereNear(String key, AMapPoint point) {
        this.Code(key, "$nearSphere", point);
        return this;
    }
    public BmobQuery<T> addWhereWithinKilometers(String key, AMapPoint point, double maxDistance) {
        this.Code(key, "$maxDistanceInKilometers", point, maxDistance);
        return this;
    }
    private void Code(String var1, String var2, AMapPoint var3, double var4) {
        this.Code(var1, "$nearSphere", var3);
        this.Code(var1, var2, Double.valueOf(var4));
    }
    private void Code(String var1, String var2, Object var3) {
        try {
            BmobUser var4;
            JSONObject var5;
            BmobObject var8;
            if(TextUtils.isEmpty(var2)) {
                if(var3 instanceof BmobUser) {
                    var4 = (BmobUser)var3;
                    (var5 = new JSONObject()).put("__type", "Pointer");
                    var5.put("objectId", var4.getObjectId());
                    var5.put("className", "_User");
                    this.m.put(var1, var5);
                } else if(var3 instanceof BmobObject) {
                    var8 = (BmobObject)var3;
                    (var5 = new JSONObject()).put("__type", "Pointer");
                    var5.put("objectId", var8.getObjectId());
                    var5.put("className", var8.getClass().getSimpleName());
                    this.m.put(var1, var5);
                } else {
                    this.m.put(var1, var3);
                }
            } else {
                if(var3 instanceof AMapPoint) {
                    var3 = new JSONObject(acknowledge.I(var3));
                } else if(var3 instanceof BmobUser) {
                    var4 = (BmobUser)var3;
                    (var5 = new JSONObject()).put("__type", "Pointer");
                    var5.put("objectId", var4.getObjectId());
                    var5.put("className", "_User");
                    var3 = var5;
                } else if(var3 instanceof BmobObject) {
                    var8 = (BmobObject)var3;
                    (var5 = new JSONObject()).put("__type", "Pointer");
                    var5.put("objectId", var8.getObjectId());
                    var5.put("className", var8.getClass().getSimpleName());
                    var3 = var5;
                } else if(var3 instanceof BmobDate) {
                    var3 = new JSONObject(acknowledge.I(var3));
                } else if(var3 instanceof ArrayList) {
                    ArrayList var9 = (ArrayList)var3;
                    JSONArray var13 = new JSONArray();
                    JSONObject var10;
                    (var10 = new JSONObject()).put("__type", "aMapPoint");
                    var10.put("x", ((AMapPoint)var9.get(0)).getX());
                    var10.put("y", ((AMapPoint)var9.get(0)).getY());
                    JSONObject var6;
                    (var6 = new JSONObject()).put("__type", "aMapPoint");
                    var6.put("x", ((AMapPoint)var9.get(1)).getX());
                    var6.put("y", ((AMapPoint)var9.get(1)).getY());
                    var13.put(var10);
                    var13.put(var6);
                    (var10 = new JSONObject()).put("$box", var13);
                    var3 = var10;
                }

                JSONObject var11 = null;
                Object var12;
                if(this.m.has(var1) && (var12 = this.m.get(var1)) instanceof JSONObject) {
                    var11 = (JSONObject)var12;
                }

                if(var11 == null) {
                    var11 = new JSONObject();
                }

                var11.put(var2, var3);
                this.m.put(var1, var11);
            }
        } catch (JSONException var7) {
            var7.printStackTrace();
        }
    }
}
