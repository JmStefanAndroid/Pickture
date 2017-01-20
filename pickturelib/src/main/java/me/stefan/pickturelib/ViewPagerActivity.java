package me.stefan.pickturelib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import me.stefan.pickturelib.constant.Constant;
import me.stefan.pickturelib.widget.HackyViewPager;

public class ViewPagerActivity extends AppCompatActivity {
    private static ArrayList<String> sDrawables=new ArrayList<>();
    private static RequestManager mGlideRequestManager;
    private static int scWidth, scHeight, tartgetPos;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        init();
    }

    protected void setupToolbar(Toolbar mToolbar, boolean homeIconVisible) {
        if (null == getSupportActionBar()) {
            setSupportActionBar(mToolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(homeIconVisible);
    }


    private void init() {
        ViewPager mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(mToolbar, true);

        SamplePagerAdapter adapter=new SamplePagerAdapter();


        Intent mIntent = getIntent();
        sDrawables = mIntent.getStringArrayListExtra(Constant.VIEW_PAGER_PATH);
        mViewPager.setAdapter(adapter);

        tartgetPos = mIntent.getIntExtra(Constant.VIEW_PAGER_POS, 0);
        mViewPager.setCurrentItem(tartgetPos);
        mGlideRequestManager = Glide.with(this);

        scWidth = getResources().getDisplayMetrics().widthPixels;
        scHeight = getResources().getDisplayMetrics().heightPixels;

        final TextView mIndicatorTv= (TextView) findViewById(R.id.indicator_tv);

        mIndicatorTv.setText(getString(R.string.__unv2_select, tartgetPos+1, sDrawables.size()));

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicatorTv.setText(getString(R.string.__unv2_select, position + 1, sDrawables.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    static class SamplePagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return sDrawables.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            mGlideRequestManager
                    .load(sDrawables.get(position))
                    .crossFade()
                    .thumbnail(0.5f)
                    .error(R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(imageView);
            // Now just add PhotoView to ViewPager and return it
            container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}