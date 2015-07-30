package com.example.amap.util.rount;

/**
 * Created by Administrator on 2015/5/4.
 */
public class MyPoint {
    public int x;
    public int y;
    public int z;
    public MyPoint(int x,int y){
        this.x=x;
        this.y=y;
    }
    public MyPoint(int x,int y,int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public String toString(){
        return "MyPoint[x:"+x+",y:"+y+",z:"+z+"]";
    }
    public boolean equal(MyPoint a){
        if(this.x==a.x&&this.y==a.y&&this.z==a.z)return true;
        return false;
    }
}
