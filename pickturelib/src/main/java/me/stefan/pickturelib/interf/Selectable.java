package me.stefan.pickturelib.interf;

import java.util.List;

import me.stefan.pickturelib.domain.Pic;

/**
 * Created by Administrator on 2016/8/1.
 * 选择接口
 */
public interface Selectable {

    void toggle(Pic mPic);

    List<Pic> getSelectedPic();

    List<String> getSelectedPicStr();

    List<Pic> getCurrentPicList();

    void clearAllSelection();

    int getSlectedPicSize();

    boolean isSelected(Pic mPic);

    void setHasSelected(List<String> selected);


}
