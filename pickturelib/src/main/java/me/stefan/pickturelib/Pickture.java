package me.stefan.pickturelib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by Stefan on 2016/8/1.
 * 调用API
 * --------------------------------------------
 * |API| {@link PickBuilder} 公开的使用途径
 * --------------------------------------------
 */

public class Pickture {

    public static final String PARAM_BUILDER = "PARAM_BUILDER";
    public static final String PARAM_PICKRESULT = "PARAM_PICKRESULT";

    private static PickBuilder mBuilder ;
    private static Pickture mInstance ;

    public static final int CODE_REQUEST = 0X999;

    public static class PicktureSingle {
        public static Pickture mInstance = new Pickture();

    }

    public static Pickture with(Context mContext) {
        mInstance = PicktureSingle.mInstance;
        mBuilder=new PickBuilder();
        mBuilder.mContext = mContext;
        return mInstance;
    }

    /**
     * 设置列
     *
     * @param colum 列
     * @return {@link PickBuilder}
     */
    public Pickture colum(int colum) {
        mBuilder.colum = colum;
        return mInstance;
    }

    /**
     * 设置最大选择数
     *
     * @param max 最大选择数
     * @return {@link PickBuilder}
     */
    public Pickture max(int max) {
        mBuilder.max = max;
        return mInstance;
    }

    public Pickture hasCamera(boolean hasCamera) {
        mBuilder.hasCamera = hasCamera;
        return mInstance;
    }

    /**
     * 设置已选中的list
     *
     * @param selectedStrList 已选中的list
     * @return {@link PickBuilder}
     */
    public Pickture selected(List<String> selectedStrList) {
        mBuilder.selectedStrList = selectedStrList;
        return mInstance;
    }


    /**
     * 跳转到PicktureActivity
     */
    public void create() {
        if (mBuilder.mContext != null)
            ((Activity) mBuilder.mContext).startActivityForResult(new Intent(mBuilder.mContext, PicktureActivity.class).putExtra(PARAM_BUILDER, mBuilder), CODE_REQUEST);
    }

}
