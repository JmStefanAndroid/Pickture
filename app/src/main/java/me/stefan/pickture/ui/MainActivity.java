package me.stefan.pickture.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.stefan.pickture.R;
import me.stefan.pickturelib.Pickture;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "PICKTURE";

    private List<String> selectedList = new ArrayList<>();

    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.__get_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pickture.with(MainActivity.this).colum(2).max(6).selected(selectedList).create();
            }
        });
        container = (LinearLayout) findViewById(R.id.__container);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            container.removeAllViews();

            selectedList = data.getStringArrayListExtra(Pickture.PARAM_PICKRESULT);
            for (String path : selectedList) {
                Log.i(TAG, path);

                TextView textView = new TextView(MainActivity.this);
                textView.setMinHeight(46);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setText(path);
                container.addView(textView);
            }

        }
    }
}
