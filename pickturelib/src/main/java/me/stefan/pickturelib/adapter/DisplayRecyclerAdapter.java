package me.stefan.pickturelib.adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import me.stefan.pickturelib.R;
import me.stefan.pickturelib.interf.OnMoveAndSwipedListener;
import me.stefan.pickturelib.interf.OnOperateListener;
import me.stefan.pickturelib.widget.PickRecyclerView;
import me.stefan.pickturelib.widget.SquareRelativeLayout;

/**
 * Created by Stefan on 2016/10/17.
 * 用于展示选择的照片Recycler的Adapter
 */
public class DisplayRecyclerAdapter extends RecyclerView.Adapter<DisplayRecyclerAdapter.ViewHolder> implements OnMoveAndSwipedListener {
    private final RequestManager mRequestManager;
    private ArrayList<String> imagePathList = null;
    private OnOperateListener mOnOperateListener;
    private final int max;
    PickRecyclerView mPickRecyclerView;
    private boolean showflag;

    public DisplayRecyclerAdapter(RequestManager mRequestManager, ArrayList<String> imagePathList,
                                  int max, PickRecyclerView mPickRecyclerView, OnOperateListener mOnOperateListener
    ) {
        this.mRequestManager = mRequestManager;
        this.imagePathList = imagePathList;
        this.max = max;
        this.mOnOperateListener = mOnOperateListener;
        this.mPickRecyclerView = mPickRecyclerView;
    }

    @Override
    public int getItemCount() {
        return imagePathList == null ? 0 : hasAddDrawble() ? (imagePathList.size() + 1) : imagePathList.size();
    }

    /**
     * 判断是否显示最后一张的添加图
     *
     * @return
     */
    public boolean hasAddDrawble() {
        boolean has = false;
        if (max > imagePathList.size()) has = true;
        return has;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (showflag) {
            holder.mDeleteSqrl.setVisibility(View.VISIBLE);
            holder.mDeleteSqrl.animate().scaleX(1).scaleY(1).setInterpolator(new OvershootInterpolator()).start();
        } else {
            holder.mDeleteSqrl.setVisibility(View.GONE);
        }
        if (hasAddDrawble() && position == imagePathList.size()) {//最后一张添加的图
            holder.mDeleteSqrl.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnOperateListener != null)
                        mOnOperateListener.onClickAdd();
                }
            });

            mRequestManager
                    .load(R.drawable.asy)
                    .fitCenter()
                    .thumbnail(0.5f)
//                .override(mImageWh, mImageWh)
                    .into(holder.mPicIv);
        } else {
            holder.mDeleteSqrl.setVisibility(View.VISIBLE);
            final int pos = holder.getLayoutPosition();
            final String path = imagePathList.get(pos);

            holder.bind(path);

            mRequestManager
                    .load(new File(path))
                    .centerCrop()
                    .thumbnail(0.5f)
//                .override(mImageWh, mImageWh)
                    .placeholder(R.drawable.asv)
                    .error(R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(holder.mPicIv);

            holder.mDeleteSqrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnOperateListener != null && mPickRecyclerView != null) {
                        mOnOperateListener.onRemoved(holder.path);
                        mPickRecyclerView.remove(holder.path);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnOperateListener != null)
                        mOnOperateListener.onItemClicked(path, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnOperateListener != null && (!hasAddDrawble() || hasAddDrawble() && position != imagePathList.size()))
                        mOnOperateListener.onItemLongClicked(path, pos);
                    holder.visibleDelete();
                    return true;
                }
            });
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (showflag) {
                        //如果按下
                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                            //如果判断为最后一张添加的图，不做拖动响应
                            if (hasAddDrawble() && holder.getLayoutPosition() == imagePathList.size())
                                return false;
                            //回调RecyclerListFragment中的startDrag方法
                            //让mItemTouchHelper执行拖拽操作
                            mPickRecyclerView.startDrag(holder);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * holder被回收时，清除相应缓存
     *
     * @param holder
     */
    @Override
    public void onViewRecycled(ViewHolder holder) {
        Glide.clear(holder.mPicIv);
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.__display_item, parent, false);
        return new ViewHolder(view);
    }


    /**
     * 删除指定的Item
     */
    public void removeData(int position) {
        imagePathList.remove(position);

        //通知RecyclerView控件某个Item已经被删除
        notifyItemRemoved(position);
        //如果不需要动画可直接使用下句代替；
//        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (toPosition >= imagePathList.size() || fromPosition >= imagePathList.size())
            return false;

        //数据机构上实现from到to的插入
        if (fromPosition < toPosition) {
            //分别把中间所有的item的位置重新交换
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(imagePathList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(imagePathList, i, i - 1);
            }
        }

        //交换RecyclerView列表中item的位置
        notifyItemMoved(fromPosition, toPosition);

        Log.d(this.getClass().getSimpleName(), "from:" + fromPosition + "--->" + "to:" + toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int pos) {
        mPickRecyclerView.remove(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mPicIv;
        final SquareRelativeLayout mDeleteSqrl;
        public String path;

        public ViewHolder(View itemView) {
            super(itemView);
            mPicIv = (ImageView) itemView.findViewById(R.id.__display_iv);
            mDeleteSqrl = (SquareRelativeLayout) itemView.findViewById(R.id.__display_delete_sqrl);
        }

        public void bind(String path) {
            this.path = path;
        }

        /**
         * 显示删除照片的布局
         */
        public void visibleDelete() {
            notifyDataSetChanged();
            showflag = true;
        }
    }

    public void setOnOperateListener(OnOperateListener mOnOperateListener) {
        this.mOnOperateListener = mOnOperateListener;
    }


}
