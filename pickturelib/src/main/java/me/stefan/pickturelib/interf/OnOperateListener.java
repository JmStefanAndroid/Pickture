package me.stefan.pickturelib.interf;

/**
 * <h4>desc:</h4>
 * <p>图片操作回调listner</p>
 * <h3>注：</h3>
 * <p/>
 * Created by stefan on 2016/10/17 11:05
 */
public interface OnOperateListener {
    /**
     * 单击一张PIC
     *
     * @param picPath  图片资源
     * @param position 图片位置
     */
    void onItemClicked(String picPath, int position);

    /**
     * 长按一张PIC
     *
     * @param picPath  图片资源
     * @param position 图片位置
     */
    void onItemLongClicked(String picPath, int position);

    /**
     * 删除一张PIC之后
     *
     * @param picPath  图片资源
     */
    void onRemoved(String picPath);


    void onClickAdd();

}
