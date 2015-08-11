package cn.bmob.im;

import android.content.Context;
import android.util.Log;

import com.example.amap.bean.AMapPoint;

import java.util.Collection;
import java.util.List;

import cn.bmob.im.task.BQuery;
import cn.bmob.im.task.BRequest;
import cn.bmob.im.task.BTable;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/8/10.
 */
public class MyBFindTask<T> extends MyBmobQuery<T> {
    public MyBFindTask(Context var1, BRequest var2, FindListener<T> var3) {
        this.setCachePolicy(CachePolicy.NETWORK_ONLY);
        if(var2.getCachePolicy() != null) {
            this.setCachePolicy(var2.getCachePolicy());
        } else {
            this.setLimit(var2.getLimitLength());
        }

        if(var2.getEqualList() != null && var2.getEqualList().size() > 0) {
            int var4 = var2.getEqualList().size();

            for(int i = 0; i < var4; ++i) {
                BQuery var6;
                List var7;
                int var8;
                if((var8 = (var7 = (var6 = (BQuery)var2.getEqualList().get(i)).getTable()).size()) > 0) {
                    Log.i("zjx","var2.getEqualList():"+var2.getEqualList());
                    for(int var9 = 0; var9 < var8; ++var9) {
                        Log.i("zjx","var7.getType():"+var6.getType());
                        BTable var10 = (BTable)var7.get(var9);
                        if(var6.getType() == 0) {
                            this.addWhereEqualTo(var10.getTableFiled(), var10.getTableFiledValue()[0]);
                        } else if(var6.getType() == 1) {
                            this.addWhereNotEqualTo(var10.getTableFiled(), var10.getTableFiledValue()[0]);
                        } else if(var6.getType() == 3) {
                            this.addWhereNotContainedIn(var10.getTableFiled(), (Collection)var10.getTableFiledValue()[0]);
                        } else if(var6.getType() == 2) {
                            this.addWhereContains(var10.getTableFiled(), (String)var10.getTableFiledValue()[0]);
                        } else if(var6.getType() == 4) {
                            this.addWhereRelatedTo(var10.getTableFiled(), (BmobPointer)var10.getTableFiledValue()[0]);
                        } else if(var6.getType() == 5) {
                            this.addWhereNear(var10.getTableFiled(), (AMapPoint)var10.getTableFiledValue()[0]);
                        } else if(var6.getType() == 6) {
                            this.addWhereWithinKilometers(var10.getTableFiled(), (AMapPoint)var10.getTableFiledValue()[0], ((Double)var10.getTableFiledValue()[1]).doubleValue());
                        }
                    }
                }
            }
        }

        if(var2.isLoadMore()) {
            this.setSkip(var2.getSkipPage() * var2.getLimitLength());
        }

        if(var2.getOrderBy() != null && !var2.getOrderBy().equals("")) {
            this.order(var2.getOrderBy());
        }

        this.findObjects(var1, var3);
    }
    public MyBFindTask(Context var1, BRequest var2, Class<T> var3, CountListener var4) {
        this.setCachePolicy(CachePolicy.NETWORK_ONLY);
        if(var2.getEqualList() != null && var2.getEqualList().size() > 0) {
            int var5 = var2.getEqualList().size();

            for(int var6 = 0; var6 < var5; ++var6) {
                BQuery var7;
                List var8;
                int var9;
                if((var9 = (var8 = (var7 = (BQuery)var2.getEqualList().get(var6)).getTable()).size()) > 0) {
                    for(int var10 = 0; var10 < var9; ++var10) {
                        BTable var11 = (BTable)var8.get(var10);
                        Log.i("zjx","var7.getType():"+var7.getType());
                        if(var7.getType() == 0) {
                            this.addWhereEqualTo(var11.getTableFiled(), var11.getTableFiledValue()[0]);
                        } else if(var7.getType() == 1) {
                            this.addWhereNotEqualTo(var11.getTableFiled(), var11.getTableFiledValue()[0]);
                        } else if(var7.getType() == 5) {
                            this.addWhereNear(var11.getTableFiled(), (AMapPoint) var11.getTableFiledValue()[0]);
                        } else if(var7.getType() == 3) {
                            this.addWhereNotContainedIn(var11.getTableFiled(), (Collection) var11.getTableFiledValue()[0]);
                        } else if(var7.getType() == 2) {
                            this.addWhereContains(var11.getTableFiled(), (String) var11.getTableFiledValue()[0]);
                        } else if(var7.getType() == 6) {
                            this.addWhereWithinKilometers(var11.getTableFiled(), (AMapPoint) var11.getTableFiledValue()[0], ((Double) var11.getTableFiledValue()[1]).doubleValue());
                        } else if(var7.getType() == 4) {
                            this.addWhereRelatedTo(var11.getTableFiled(), (BmobPointer)var11.getTableFiledValue()[0]);
                        }
                    }
                }
            }
        }

        this.order(var2.getOrderBy());
        this.count(var1, var3, var4);
    }
}
