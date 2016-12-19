package me.stefan.pickturelib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;
import java.util.ArrayList;

import me.stefan.pickturelib.PickBuilder;
import me.stefan.pickturelib.R;
import me.stefan.pickturelib.domain.Pic;
import me.stefan.pickturelib.domain.PicFolder;
import me.stefan.pickturelib.interf.OnPickListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pic} and makes a call to the
 * specified {@link OnPickListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PicRecyclerViewAdapter extends SelectableAdapter<PicRecyclerViewAdapter.ViewHolder> {

    private final OnPickListener mPickListener;
    private final int mImageWh;
    private RequestManager mRequestManager;
    private PickBuilder mPickBuilder;
    private final int[] mSelectedIcSour = new int[]{R.drawable.__ic_pic_unselected, R.drawable.__ic_pic_selected};

    public PicRecyclerViewAdapter(RequestManager requestManager, PickBuilder pickBuilder, ArrayList<PicFolder> items,
                                  OnPickListener listener, int imageWh) {
        this.mFolderList = items;
        mPickBuilder = pickBuilder;
        mPickListener = listener;
        mImageWh = imageWh;
        mRequestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.__pic_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        showItemAnim(holder.mView, position, AnimType.OVERLAPPING);

        if (position == 0 && mPickBuilder.isHasCamera()) {//hasCamera
            holder.showCamera();
        } else {
            if (mPickBuilder.isHasCamera())
                position--;
            final Pic pic = getCurrentPicList().get(position);

            holder.bind(pic);

            final boolean isSelected = isSelected(pic);
            if (isSelected) {
                //selected
                holder.mPSelected.setSelected(true);
                holder.mPSelected.setImageResource(mSelectedIcSour[1]);
                holder.mPicView.setAlpha(0.5f);
                holder.mPicView.setSelected(true);
            } else {
                //unselect
                holder.mPSelected.setSelected(false);
                holder.mPSelected.setImageResource(mSelectedIcSour[0]);
                holder.mPicView.setAlpha(1f);
                holder.mPicView.setSelected(false);
            }

            holder.mPicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPickListener == null) return;
                    holder.mPSelected.performClick();
                }
            });
            holder.mPSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mLockTag) return;
                    holder.mLockTag = true;
                    if (mPickListener == null) {
                        return;
                    }
                    int pos = holder
                            .getAdapterPosition();
                    if (mPickListener.onItemClicked(holder.mPic, pos, !isSelected)) {
                        toggle(holder.mPic);

                        notifyItemChanged(pos);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mFolderList == null || mFolderList.size() == 0 ?
                (mFolderList == null ? 0 : mPickBuilder.isHasCamera() ? 1 : 0) :
                (mPickBuilder.isHasCamera() ? getCurrentPicList().size() + 1 : getCurrentPicList().size());
    }

    /**
     * holder被回收时，清除相应缓存
     *
     * @param holder
     */
    @Override
    public void onViewRecycled(ViewHolder holder) {
        Glide.clear(holder.mPicView);
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.mView.clearAnimation();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPicView;
        public final ImageView mPSelected;
        public Pic mPic;
        //in order to avoid err of frequent clicks
        public boolean mLockTag;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPicView = (ImageView) view.findViewById(R.id.__pic_p_iv);
            mPSelected = (ImageView) view.findViewById(R.id.__pic_p_selected_iv);
        }

        public void bind(Pic mPic) {
            this.mPic = mPic;
            if (mPic == null) {
                throw new RuntimeException("PicRecyclerViewAdapter: you need give a pic to me ,not a null one");
            }
            mRequestManager
                    .load(new File(mPic.getPath()))
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.5f)
                    .override(mImageWh, mImageWh)
//                    .placeholder(R.drawable.asv)
                    .error(R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(mPicView);
            mLockTag = false;
        }

        public void showCamera() {
            mPicView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mRequestManager
                    .load(R.drawable.__ic_takephoto)
                    .into(mPicView);
            mLockTag = false;
            mPSelected.setVisibility(View.GONE);
            mPicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPickListener != null)
                        mPickListener.onCameraClick();
                }
            });
        }

    }
}
