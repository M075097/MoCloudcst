package com.yunzhongjun.weiyang.uicomponent.recyclerviewplugin;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wb-zyl208210 on 2016/9/1.
 * 用来指定recyclerView的条目间距
 * 用法：RecyclerView.addItemDecoration(new SpaceItemDecoration(int space));
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int bottom;
    private int right;
    private int top;
    private int left;
    private int space;

    public SpaceItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public SpaceItemDecoration(int space) {
        this.left = space;
        this.top = space;
        this.right = space;
        this.bottom = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        //        if (parent.getChildPosition(view) != 0) {
        outRect.top = this.top;
        outRect.bottom = this.bottom;
        outRect.right = this.right;
        outRect.left = this.left;
        //        }
    }
}