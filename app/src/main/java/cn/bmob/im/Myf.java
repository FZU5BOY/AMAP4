package cn.bmob.im;

import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.task.BRequest;

/**
 * Created by Administrator on 2015/8/10.
 */
final class Myf extends BRequest {
    Myf(BmobUserManager var1, boolean var2, boolean var3, List var4, int var5) {
        super(var2, true, var4, var5);
    }
}