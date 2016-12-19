package me.stefan.pickturelib;

import java.io.Serializable;
import java.util.List;

import me.stefan.pickturelib.constant.Constant;

/**
 * Created by Stefan on 2016/8/1.
 * Builder类用于参数初始化
 */
public class PickBuilder implements Serializable {

    protected int column;
    protected int max;
    protected boolean hasCamera;
    protected List<String> selectedStrList;



    public PickBuilder() {
        column = Constant.DEFAULT_COLUMN;
        max = Constant.DEFAULT_MAX;
    }


    public int getColumn() {
        return column;
    }

    public int getMax() {
        return max;
    }

    public List<String> getSelectedStrList() {
        return selectedStrList;
    }

    public boolean isHasCamera() {
        return hasCamera;
    }
}
