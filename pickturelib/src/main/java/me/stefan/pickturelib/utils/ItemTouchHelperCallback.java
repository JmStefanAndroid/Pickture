package me.stefan.pickturelib.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import me.stefan.pickturelib.adapter.DisplayRecyclerAdapter;

/**
 * Created by Administrator on 2016/10/24.
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private DisplayRecyclerAdapter mAdapter;

    public ItemTouchHelperCallback(DisplayRecyclerAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //如果是ListView样式的RecyclerView
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            //设置拖拽方向为上下左右
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            //不支持侧滑
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {//如果是GridView样式的RecyclerView

            //设置拖拽方向为上下
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            //设置侧滑方向为从左到右和从右到左都可以
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            //将方向参数设置进去
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    /**
     * 当我们拖动item时会回调此方法
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //如果两个item不是一个类型的，我们让他不可以拖拽
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        //回调adapter中的onItemMove方法
        return mAdapter.onItemMove(viewHolder.getLayoutPosition(), target.getLayoutPosition());
    }

    /**
     * 当我们侧滑item时会回调此方法
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //回调adapter中的onItemDismiss方法
        mAdapter.onItemDismiss(viewHolder.getLayoutPosition());
    }

    /**
     * 当状态改变时回调此方法
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //当前状态不是idel（空闲）状态时，说明当前正在拖拽或者侧滑
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            //TODO 改变item的背景颜色
        }
        super.onSelectedChanged(viewHolder, actionState);
    }
}
