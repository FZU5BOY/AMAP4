package com.example.amap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amap.R;
import com.example.amap.util.rount.PoiSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/18.
 */
public class ListAdapter extends BaseAdapter  {
    private List<PoiSearch> poiSearch = new ArrayList<PoiSearch>();
    Context ct;
    private LayoutInflater inflater;
    public ListAdapter(Context ct,List<PoiSearch> poiSearch) {
        // TODO Auto-generated constructor stub
        this.poiSearch = poiSearch;
        this.ct = ct;
        inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return poiSearch.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return poiSearch.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    static class ViewHolder {

        TextView text;

        ImageView icon;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        PoiSearch p = poiSearch.get(position);
        if(convertView==null){
            convertView = inflater.inflate(R.layout.simple_item,parent,false);
            holder = new ViewHolder();
            holder.text=(TextView)convertView.findViewById(R.id.title);
            holder.icon = (ImageView)convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
//        ImageView tv1=(ImageView)convertView.findViewById(R.id.img);
//        TextView tv2=(TextView)convertView.findViewById(R.id.title);
//        tv1.setImageResource(p.getImg());
//        tv2.setText(p.getName());
        holder.text.setText(p.getName());
        holder.icon.setImageResource(p.getImg());
        //http://www.2cto.com/kf/201108/101092.html
        return convertView;
    }



}