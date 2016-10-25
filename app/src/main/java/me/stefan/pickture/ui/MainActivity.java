package me.stefan.pickture.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import me.stefan.pickture.R;
import me.stefan.pickturelib.Pickture;
import me.stefan.pickturelib.interf.OperateListenerAdapter;
import me.stefan.pickturelib.widget.PickRecyclerView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "PICKTURE";

    private ArrayList<String> selectedList = new ArrayList<>();

    private PickRecyclerView mPickRecyclerView;

    private Context mContext = MainActivity.this;

    private final int COLUMN = 4, MAX = 18;
    private Pickture mPickture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPickRecyclerView = (PickRecyclerView) findViewById(R.id.__prv);

        findViewById(R.id.__get_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启用图片选择功能
                mPickture.selected(selectedList).create();
                //如果只是使用读取照片功能，可使用下面这个快捷通道，不需要再调用showOn方法去同步参数了
//                Pickture.with(MainActivity.this).column(COLUMN).max(MAX).selected(selectedList).create();
            }
        });

        mPickture = Pickture.with(MainActivity.this).column(COLUMN).max(MAX).selected(selectedList);
        //当需要同步展示到 PickRecyclerView 需要同步基础参数给你的 mPickRecyclerView ，这个方法就是用于同步的
        mPickture.showOn(mPickRecyclerView);

        mPickRecyclerView.setOnOperateListener(new OperateListenerAdapter() {

            @Override
            public void onClickAdd() {
                //启用图片选择功能
                mPickture.selected(selectedList).create();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

//            selectedList.clear();
            // 如果要使用PickRecyclerView ,切忌这里使用 ‘=’ ,需要保持原索引 在mPickture.create(); 启用的时候就能够将当前的照片索引传递过去
            // 否则需要在create()之前需要调用 mPickture.selected(selectedList)
            selectedList = data.getStringArrayListExtra(Pickture.PARAM_PICKRESULT);

            mPickRecyclerView.bind(selectedList);
        }
    }
}
