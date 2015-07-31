package com.example.amap.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/** 重载BmobChatUser对象：若还有其他需要增加的属性可在此添加
 * @ClassName: TextUser
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 下午6:15:45
 */
public class AMapPoint extends BmobObject {
	private String amapId =String.valueOf("amapid");
	private Double x = Double.valueOf(0.0D);
	private Double y = Double.valueOf(0.0D);
	private Integer z = Integer.valueOf(0);
	private String detailAddress;
	private int state = 1;
	public AMapPoint() {
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public AMapPoint(double x, double y,Integer z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	public AMapPoint(double x, double y,Integer z,String amapId) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setAmapId(amapId);
	}
	public AMapPoint(double x, double y,Integer z,String amapId,String detailAddress) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setAmapId(amapId);
		this.setDetailAddress(detailAddress);
	}
	public AMapPoint(int state) {
		this.setState(state);
	}
	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Integer getZ() {
		return z;
	}

	public void setZ(Integer z) {
		this.z = z;
	}

	public String getAmapId() {
		return amapId;
	}

	public void setAmapId(String amapId) {
		this.amapId = amapId;
	}

}
