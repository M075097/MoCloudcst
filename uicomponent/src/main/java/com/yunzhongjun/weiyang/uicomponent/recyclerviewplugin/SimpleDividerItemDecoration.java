package com.yunzhongjun.weiyang.uicomponent.recyclerviewplugin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yunzhongjun.weiyang.uicomponent.R;


public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private boolean isNeedLastDivider;
    private Drawable mDivider;

    public SimpleDividerItemDecoration(Context context) {
        this(context, true);
    }

    public SimpleDividerItemDecoration(Context context, boolean isNeedLastDivider) {
        mDivider = context.getResources().getDrawable(R.drawable.recyclerview_decorate_linedivider);
        this.isNeedLastDivider = isNeedLastDivider;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == childCount - 1 && !isNeedLastDivider) {
                continue;
            }
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int bottom = child.getBottom() + params.bottomMargin;
            int top = bottom - mDivider.getIntrinsicHeight();
            //int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}