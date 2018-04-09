package com.yunzhongjun.weiyang.uicomponent.recyclerviewplugin;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wb-zyl208210 on 2016/8/31.
 * 用来处理RecyclerView的条目点击事件，封装出longPress动作和SingleClick动作，以便调用，此处配合按需求开启相关条目的拖曳事件
 * ps：点击实现背景变色（类似lsitview的效果），需要设置相关ItemView的clickable 为 true ；并设置其背景资源为selector即可
 */
public abstract class OnSimpleRecyclerViewItemClicklistener implements RecyclerView.OnItemTouchListener {
    RecyclerView recyclerView;
    private final GestureDetectorCompat mGestureDetectorCompat;

    public OnSimpleRecyclerViewItemClicklistener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new OnRecyclerViewItemGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        //        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class OnRecyclerViewItemGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }


        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (recyclerView == null) {
                return false;
            }
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(child);
                onItemClick(childViewHolder);
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (recyclerView == null) {
                return;
            }
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(child);
                onItemLongPress(childViewHolder);
            }
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

    }

    protected abstract void onItemClick(RecyclerView.ViewHolder childViewHolder);

    public abstract void onItemLongPress(RecyclerView.ViewHolder childViewHolder);
}
