package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.santeh.petone.crm.R;

/**
 * Created by rjhonsl on 1/7/2016.
 */
public class Activity_Settings extends FragmentActivity {

    ImageButton btnTitleLeft;
    LinearLayout llClientInfo, llRestore;

    Activity activity;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = Activity_Settings.this;
        activity = this;


        btnTitleLeft = (ImageButton) findViewById(R.id.btn_title_left);

        llRestore = (LinearLayout) findViewById(R.id.ll_restore);

        llRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        btnTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
