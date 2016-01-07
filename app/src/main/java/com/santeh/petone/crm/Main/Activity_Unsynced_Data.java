package com.santeh.petone.crm.Main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.santeh.petone.crm.R;

/**
 * Created by rjhonsl on 1/7/2016.
 */
public class Activity_Unsynced_Data extends FragmentActivity {

    ImageButton btnTitleLeft;
    LinearLayout llBottom,llClientInfo, llClientUpdates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsyced_data);


        btnTitleLeft = (ImageButton) findViewById(R.id.btn_title_left);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom_postall);
        llClientInfo = (LinearLayout) findViewById(R.id.ll_clientinfo);
        llClientUpdates = (LinearLayout) findViewById(R.id.ll_clientupdates);


        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llClientInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llClientUpdates.setOnClickListener(new View.OnClickListener() {
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
}
