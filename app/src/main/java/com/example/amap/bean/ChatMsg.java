package com.example.amap.bean;

import android.content.Context;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.v3.datatype.BmobRelation;

/** 重载BmobChatUser对象：若还有其他需要增加的属性可在此添加
 * @ClassName: TextUser
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 下午6:15:45
 */
public class ChatMsg extends BmobMsg {

	public static BmobMsg createLocationSendMsg(Context var0, String var1, String var2, int x,int y,int z) {
		StringBuilder var7;
		(var7 = new StringBuilder()).append(var2).append("&").append(x).append("&").append(y).append("&").append(z);
		return createSendMessage(var0, var1, var7.toString(), Integer.valueOf(1), Integer.valueOf(3));
	}
}
