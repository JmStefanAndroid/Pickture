package me.stefan.pickturelib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.stefan.pickturelib.FolderFragment;
import me.stefan.pickturelib.R;
import me.stefan.pickturelib.domain.Pic;
import me.stefan.pickturelib.domain.PicFolder;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pic} and makes a call to the
 * specified {@link FolderFragment.OnFolderItemClickListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PicFolderAdapter extends AnimRecyclerViewAdapter<PicFolderAdapter.ViewHolder> {

    private final FolderFragment.OnFolderItemClickListener mFolderItemClickListener;
    private List<PicFolder> mFolderList;

    public PicFolderAdapter(ArrayList<PicFolder> items, FolderFragment.OnFolderItemClickListener listener) {
        this.mFolderList = items;
        mFolderItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.__picfolder_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        showItemAnim(holder.mView, position, AnimType.OVERLAPPING);

        holder.bind(mFolderList.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFolderItemClickListener.onFolderItemClick(holder.mPicFolder);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mFolderList == null || mFolderList.size() == 0 ? 0 : mFolderList.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.mView.clearAnimation();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameTv;
        public PicFolder mPicFolder;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameTv = (TextView) view.findViewById(R.id.__picfolder_name_tv);
        }

        public void bind(PicFolder mPicFolder) {
            this.mPicFolder = mPicFolder;
            if (mPicFolder == null) {
                throw new RuntimeException("PicFolderAdapter: you need give a PicFolder to me ,not a null one");
            }
            mNameTv.setText(mPicFolder.getName());
        }

    }
}
