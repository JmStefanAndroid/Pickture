package me.stefan.pickturelib.interf;

/**
 * This adapter class provides empty implementations of the methods from {@link me.stefan.pickturelib.interf.OnOperateListener}.
 * Any custom listener that cares only about a subset of the methods of this listener can
 * simply subclass this adapter class instead of implementing the interface directly.
 */
public abstract class OperateListenerAdapter implements OnOperateListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemClicked(String picPath, int position) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemLongClicked(String picPath, int position) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRemoved(String picPath) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClickAdd() {

    }
}
