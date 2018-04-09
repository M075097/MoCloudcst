package com.yunzhongjun.weiyang.uicomponent.recyclerviewplugin;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.List;

/**
 * Created by wb-zyl208210 on 2016/8/31.
 * 用来支持条目拖曳交换效果，并交换数据集合的相关条目，支持长按条目背景高亮恢复
 * 用法：mItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallBack(itemDataList));//放入数据集合引用
 * mItemTouchHelper.attachToRecyclerView(RecyclerView控件);
 */
public class SimpleItemTouchHelperCallBack extends ItemTouchHelper.Callback {
    private List addedDataList;
    private List dataList;

    public SimpleItemTouchHelperCallBack(List dataList, List addedDataList) {
        this.dataList = dataList;
        this.addedDataList = addedDataList;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;
        int swipFlags;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            dragFlags = ItemTouchHelper.UP |
                    ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT |
                    ItemTouchHelper.RIGHT;
            swipFlags = 0;
        } else {
            dragFlags = ItemTouchHelper.UP |
                    ItemTouchHelper.DOWN;
            swipFlags = 0;
        }

        return makeMovementFlags(dragFlags, swipFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (addedDataList != null && addedDataList.size() > 0 && toPosition != 0 && toPosition < addedDataList.size() + 1) {
            if (dataList != null) {
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(dataList, i, i + 1);//操作position需要计算数据角标
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(dataList, i, i - 1);
                    }
                }
            }

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //滑动暂不处理
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {//选中时的回调
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            //viewHolder.itemView.setBackground(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wisehome_itemcardstytle_pressedbackground));//选中时给灰色背景色
            viewHolder.itemView.setBackgroundColor(Color.GRAY);//选中时给灰色背景色
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {//清除
        super.clearView(recyclerView, viewHolder);
        //viewHolder.itemView.setBackground(recyclerView.getContext().getResources().getDrawable(R.drawable.wisehome_itemcardstytle_background));//设回为原有背景色
        viewHolder.itemView.setBackgroundColor(Color.WHITE);//设回为原有背景色
    }

    @Override
    public boolean isLongPressDragEnabled() {//重写返回false 所有条目都不支持长按事件，另再添加各条目的点击长按处理
        return false;
    }
}
