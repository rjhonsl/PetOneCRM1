package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.santeh.petone.crm.DBase.DB_Query_PetOneCRM;
import com.santeh.petone.crm.R;

/**
 * Created by rjhonsl on 1/7/2016.
 */
public class Activity_Unsynched_Data extends FragmentActivity {

    ImageButton btnTitleLeft;
    LinearLayout llBottom,llClientInfo, llClientUpdates;
    TextView txtClientInfoCount, txtClientUpdatesCount;

    Activity activity;
    Context context;
    DB_Query_PetOneCRM db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsyced_data);
        context = Activity_Unsynched_Data.this;
        activity = this;

        db = new DB_Query_PetOneCRM(this);
        db.open();

        btnTitleLeft = (ImageButton) findViewById(R.id.btn_title_left);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom_postall);
        llClientInfo = (LinearLayout) findViewById(R.id.ll_clientinfo);
        llClientUpdates = (LinearLayout) findViewById(R.id.ll_clientupdates);
        txtClientInfoCount = (TextView) findViewById(R.id.txt_clientinfo_count);
        txtClientUpdatesCount = (TextView) findViewById(R.id.txt_clientupdates_count);


        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llClientInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Activity_Unsynched_ClientInfo.class);
                startActivity(intent);
            }
        });

        llClientUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Activity_Unsynched_ClientUpdates.class);
                startActivity(intent);
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
        db.open();
        txtClientInfoCount.setText(db.getNotPosted_ClientInfo(activity).getCount() + "");
        txtClientUpdatesCount.setText(db.getNotPosted_Updates(activity).getCount() + "");

    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }
}
