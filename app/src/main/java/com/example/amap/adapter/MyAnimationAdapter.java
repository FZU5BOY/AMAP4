//package com.example.amap.adapter;
//
//import android.animation.Animator;
//import android.animation.ObjectAnimator;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
//
///**
// * Created by Administrator on 2015/5/23.
// */
//public class MyAnimationAdapter extends AnimationAdapter {
//
//    public MyAnimationAdapter(BaseAdapter baseAdapter) {
//        super(baseAdapter);
//    }
//
//    public Animator[] getAnimators(ViewGroup parent, View view) {
//        Animator bottomInAnimator = ObjectAnimator.ofFloat(view, "translationY", 500, 0);
//        Animator rightInAnimator = ObjectAnimator.ofFloat(view, "translationX", parent.getWidth(), 0);
//        return new Animator[] { bottomInAnimator, rightInAnimator };
//    }
//
//    protected long getAnimationDelayMillis() {
//        return 100L;
//    }
//
//    protected long getAnimationDurationMillis() {
//        return 100;
//    }
//}
