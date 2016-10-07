package me.stefan.pickturelib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import me.stefan.pickturelib.domain.Pic;
import me.stefan.pickturelib.interf.OnPickListener;

/**
 * 照片展示Ac
 */
public class PicktureActivity extends AppCompatActivity implements OnPickListener {
    PicktureFragment mPicktureFragment;
    PickBuilder pickBuilder = new PickBuilder();

    private Toolbar mToolbar;
    private Button mCommintBtn;
    private int curSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);


        if (getIntent().hasExtra(Pickture.PARAM_BUILDER))
            pickBuilder = (PickBuilder) getIntent().getSerializableExtra(Pickture.PARAM_BUILDER);

        initView();
        loadFrag();

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCommintBtn = (Button) findViewById(R.id.commit);
        mToolbar.setTitle("图片");
        setupToolbar(mToolbar, true);

        curSize = pickBuilder.getSelectedStrList().size();
        mCommintBtn.setText(getString(R.string.__unv2_select, curSize, pickBuilder.getMax()));
        mCommintBtn.setEnabled(curSize > 0);
        mCommintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_OK, new Intent()
                        .putStringArrayListExtra(Pickture.PARAM_PICKRESULT,
                                (ArrayList<String>) mPicktureFragment.getPicAdapter().getSelectedPicStr()));
                finish();
            }
        });
    }

    protected void setupToolbar(Toolbar mToolbar, boolean homeIconVisible) {
        if (null == getSupportActionBar()) {
            setSupportActionBar(mToolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(homeIconVisible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    /**
     * 加载照片frag
     */
    private void loadFrag() {

        mPicktureFragment = PicktureFragment.newInstance(pickBuilder);
        getSupportFragmentManager().beginTransaction().replace(R.id.image_grid, mPicktureFragment).commitAllowingStateLoss();
    }

    @Override
    public boolean onItemClicked(Pic pic, int position, boolean isPreSelected) {
        boolean canOperate = isPreSelected ? curSize < pickBuilder.getMax() : curSize > 0;
        if (canOperate) {
            curSize = isPreSelected ? ++curSize : --curSize;

            mCommintBtn.setEnabled(curSize > 0);
            mCommintBtn.setText(getString(R.string.__unv2_select, curSize, pickBuilder.getMax()));

            return true;
        } else {
            Toast.makeText(PicktureActivity.this, getString(R.string.__max_limit, pickBuilder.getMax()), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onItemLongClicked(Pic pic, int position) {

    }
}
