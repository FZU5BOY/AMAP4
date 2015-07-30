package com.example.amap.util.rount;

/**
 * Created by Administrator on 2015/5/18.
 */
public class PoiSearch {
    private String name ;
    private int img;


    public String getName() {
        return name;
    }



    public void setName(String name) {
        name = name;
    }



    public int getImg() {
        return img;
    }



    public void setImg(int img) {
        this.img = img;
    }



    public PoiSearch(String name, int img){
        super();
        this.name = name;
        this.img = img;
    }
}