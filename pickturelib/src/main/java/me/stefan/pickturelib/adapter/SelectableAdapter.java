package me.stefan.pickturelib.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.stefan.pickturelib.domain.Pic;
import me.stefan.pickturelib.domain.PicFolder;
import me.stefan.pickturelib.interf.Selectable;

/**
 * Created by Stefan on 2016/8/1.
 * 处理选中取消逻辑
 */
public class SelectableAdapter<T extends RecyclerView.ViewHolder> extends AnimRecyclerViewAdapter<T> implements Selectable {
    protected List<Pic> mList;
    protected List<Pic> mSelectedList = new ArrayList<>();
    protected List<PicFolder> mFolderList;
    protected List<String> mSelectedStrList = new ArrayList<>();
    private int mCurrentFolderIndex;


    /**
     * 切换Pic的选中状态
     *
     * @param mPic
     */
    @Override
    public void toggle(Pic mPic) {
        if (mSelectedStrList == null) {
            throw new RuntimeException("mSelectedStrList is not avaliable!");
        }
        if (mSelectedList.contains(mPic)) {
            mSelectedList.remove(mPic);
            mSelectedStrList.remove(mPic.getPath());
        } else {
            mSelectedList.add(mPic);
            mSelectedStrList.add(mPic.getPath());
        }

    }


    /**
     * 获取选中的图片
     *
     * @return
     */
    @Override
    public List<Pic> getSelectedPic() {
        return mSelectedList;
    }

    @Override
    public List<String> getSelectedPicStr() {
        return mSelectedStrList;
    }

    @Override
    public List<Pic> getCurrentPicList() {
        return mFolderList.get(mCurrentFolderIndex).getPics();
    }

    /**
     * 清空所有选中
     */
    @Override
    public void clearAllSelection() {
        if (mSelectedStrList != null)
            mSelectedStrList.clear();
        if (mSelectedList != null)
            mSelectedList.clear();

    }

    /**
     * 获取已选中数量
     *
     * @return
     */
    @Override
    public int getSlectedPicSize() {
        return mSelectedList == null ? 0 : mSelectedList.size();
    }

    /**
     * 判断是否被选中
     *
     * @param mPic
     * @return
     */
    @Override
    public boolean isSelected(Pic mPic) {
        if (mSelectedStrList != null && mSelectedStrList.contains(mPic.getPath())) {
            return true;
        }
        return false;
    }

    /**
     * 设置已选中
     * @param selected
     */
    @Override
    public void setHasSelected(List<String> selected) {
        if (selected != null && mFolderList != null && selected.size() != 0) {
            for (Pic pic : mFolderList.get(0).getPics()) {
                if (selected.contains(pic.getPath())) {
                    mSelectedList.add(pic);
                    mSelectedStrList.add(pic.getPath());
                }
            }

        }
    }

    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.mCurrentFolderIndex = currentDirectoryIndex;
    }


}
