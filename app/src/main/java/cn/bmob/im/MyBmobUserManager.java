package cn.bmob.im;

import android.content.Context;
import android.util.Log;

import com.example.amap.bean.AMapPoint;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.db.BmobDB;
import cn.bmob.im.task.BFindTask;
import cn.bmob.im.task.BQuery;
import cn.bmob.im.task.BTable;
import cn.bmob.im.util.BmobUtils;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/8/10.
 */
public class MyBmobUserManager extends BmobUserManager {
    BmobPushManager<BmobInstallation> bmobPush;
    public static final String COLUMN_NAME_CONTACTS = "contacts";
    public static final String COLUMN_NAME_BLACKLIST = "blacklist";
//    Context context;
    private static volatile MyBmobUserManager INSTANCE;
    private static Object INSTANCE_LOCK = new Object();

    public MyBmobUserManager() {
    }
    @Override
    public void init(Context var1) {
        this.context = var1;
        this.bmobPush = new BmobPushManager(this.context);
    }
    public static MyBmobUserManager getInstance(Context var0) {
        if(INSTANCE == null) {
            Object var1 = INSTANCE_LOCK;
            synchronized(INSTANCE_LOCK) {
                if(INSTANCE == null) {
                    INSTANCE = new MyBmobUserManager();
                }

                INSTANCE.init(var0);
            }
        }

        return INSTANCE;
    }
    //String var3, double var4, double var6,
    public <T> void queryNearAllByListByPage(boolean isUpdate, int pageNum,String location,double x, double y,int z ,boolean containFirennds,  FindListener<T> var11) {
        Myf var12 = new Myf(this, isUpdate, true, this.AllCode(true, 0.0D, location, x, y, z, containFirennds), pageNum);
        new MyBFindTask(this.context, var12, var11);
    }
    public <T> void queryNearAllTotalCount(Class<T> var1, String var2, double x, double y,int z, boolean var7,  CountListener var10) {
        Myg var11 = new Myg(this, this.AllCode(true, 0.0D, var2, x, y ,z, var7));
        new MyBFindTask(this.context, var11, var1, var10);
    }
    // String var4, double var5, double var7,
    private List<BQuery> AllCode(boolean var1, double var2, String location,double x, double y,int z, boolean containFirennds) {
        ArrayList var12 = new ArrayList();
        Object[] var13;
        (var13 = new Object[2])[0] = new AMapPoint(x,y,z);
        if(!var1) {
            var13[1] = Double.valueOf(var2);
        }

        BTable var15 = new BTable(location, var13);
        Log.i("zjx","location:"+var15.toString());
        var12.add(var15);
        BQuery var14;
        if(var1) {
            var14 = new BQuery(5, var12); //判断是搜索所有还是一公里
        } else {
            var14 = new BQuery(6, var12);
        }

        ArrayList var17 = new ArrayList();
        ArrayList var3 = new ArrayList();
        if(!containFirennds) {
            var3.addAll(BmobDB.create(this.context).getAllContactList());
            var3.add(this.getCurrentUser());
        }

        var3.add(this.getCurrentUser());
        Object[] var21;
        (var21 = new Object[1])[0] = BmobUtils.list2Array(var3);
        BTable var19 = new BTable("username", var21);
        var17.add(var19);
        BQuery var18 = new BQuery(3, var17);
        (var3 = new ArrayList()).add(var14);
        var3.add(var18);
        Log.i("zjx", "" + var3);
        return var3;
    }
    public <T> void queryMyKiloMetersListByPage(boolean var1, int var2, String var3, double x, double y,int z, boolean var8, double var9,FindListener<T> var13) {
        d var14 = new d(this, var1, true, this.MyCode(false, var9, var3, x, y, z, var8), var2);
        new MyBFindTask(this.context, var14, var13);
    }
    private List<BQuery> MyCode(boolean var1, double var2, String var4, double x, double y, int z,boolean var9) {
        ArrayList var12 = new ArrayList();
        Object[] var13;
        (var13 = new Object[2])[0] = new AMapPoint(x, y,z);
        if(!var1) {
            var13[1] = Double.valueOf(var2);
        }

        BTable var15 = new BTable(var4, var13);
        var12.add(var15);
        BQuery var14;
        if(var1) {
            var14 = new BQuery(5, var12);
        } else {
            var14 = new BQuery(6, var12);
        }

        ArrayList var17 = new ArrayList();
        ArrayList var3 = new ArrayList();
        if(!var9) {
            var3.addAll(BmobDB.create(this.context).getAllContactList());
            var3.add(this.getCurrentUser());
        }

        var3.add(this.getCurrentUser());
        Object[] var21;
        (var21 = new Object[1])[0] = BmobUtils.list2Array(var3);
        BTable var19 = new BTable("username", var21);
        var17.add(var19);
        BQuery var18 = new BQuery(3, var17);
        (var3 = new ArrayList()).add(var14);
        var3.add(var18);

        return var3;
    }
}
