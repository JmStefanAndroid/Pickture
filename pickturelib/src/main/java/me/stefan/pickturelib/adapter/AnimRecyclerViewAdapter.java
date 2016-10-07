package me.stefan.pickturelib.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import me.stefan.pickturelib.R;


/**
 * <h4>desc:</h4>
 * <p>
 * 用于动画实现的父类
 * 目前支持 横向 纵向 交叉 以及 自定义动画
 * </p>
 * <h3>注：</h3>
 * 自定义动画： AnimType.CUSTOM
 * 并且 {@link #setCustomAnimListner(onCustomAnimListner customAnimListner)}
 *
 * @author stefan
 *         2016/9/1
 */
public class AnimRecyclerViewAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {

    private static final int VERTICAL_DELAY = 87;
    private static final int HORIZONTAL_DELAY = 135;
    private int mLastPosition = -1;

    protected enum AnimType {
        HORIZIONTAL_RIGHT, HORIZIONTAL_LEFT, VERTICAL_SYNCHRONIZATION, VERTICAL_SLOW, OVERLAPPING, CUSTOM;
    }

    private AnimType mType;

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }


    @Override
    public void onBindViewHolder(T holder, int position) {
    }


    @Override
    public int getItemCount() {
        return 0;
    }


    public void showItemAnim(final View view, final int position, AnimType animType) {
        mType = animType;
        final Context context = view.getContext();
        if (position > mLastPosition) {
            switch (mType) {
                case HORIZIONTAL_RIGHT:
                    horizontalScroll(view, position, context, R.anim.slide_in_right, true);
                    break;

                case HORIZIONTAL_LEFT:
                    horizontalScroll(view, position, context, R.anim.slide_in_left, true);
                    break;

                case VERTICAL_SYNCHRONIZATION:

                    view.setTranslationY(100);
                    view.setAlpha(0.f);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.animate()
                                    .translationY(0).alpha(1.f)
                                    .setStartDelay(VERTICAL_DELAY)
                                    .setInterpolator(new DecelerateInterpolator(2.f))
                                    .setDuration(300)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                        }
                                    })
                                    .start();
                        }
                    }, VERTICAL_DELAY);
                    break;
                case VERTICAL_SLOW:

                    view.setTranslationY(100);
                    view.setAlpha(0.f);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.animate()
                                    .translationY(0).alpha(1.f)
//                                    .setStartDelay(VERTICAL_DELAY * (position))
                                    .setInterpolator(new DecelerateInterpolator(2.f))
                                    .setDuration(300)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                        }
                                    })
                                    .start();
                        }
                    }, VERTICAL_DELAY * position);
                    break;
                case OVERLAPPING://交叉
                    horizontalScroll(view, position, context, position % 2 == 0 ? R.anim.slide_in_left : R.anim.slide_in_right, false);
                    break;
                case CUSTOM:
                    if (customAnimListner != null)
                        customAnimListner.showItemAnim(view, position);
                    break;
            }


            mLastPosition = position;
        }
    }


    /**
     * 水平滚动
     *
     * @param view
     * @param position
     * @param context
     * @param AnimId   动画资源id
     * @param hasDelay 是否延迟
     */
    private void horizontalScroll(final View view, int position, final Context context, final int AnimId, boolean hasDelay) {
        if (hasDelay) {
            view.setAlpha(0);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    createAnim(view, context, AnimId);
                }
            }, HORIZONTAL_DELAY * position);
        } else {
            createAnim(view, context, AnimId);
        }
    }

    private void createAnim(final View view, Context context, int AnimId) {
        Animation animation = AnimationUtils.loadAnimation(context,
                AnimId);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setAlpha(1);
            }


            @Override
            public void onAnimationEnd(Animation animation) {
            }


            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);
    }

    /**
     * 支持自定义动画的事件
     */
    public interface onCustomAnimListner {
        void showItemAnim(final View view, final int position);
    }

    onCustomAnimListner customAnimListner;


    public void setCustomAnimListner(onCustomAnimListner customAnimListner) {
        this.customAnimListner = customAnimListner;
    }
}