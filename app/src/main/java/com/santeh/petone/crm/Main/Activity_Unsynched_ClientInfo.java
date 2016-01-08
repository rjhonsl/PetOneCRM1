package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.santeh.petone.crm.Adapter.Adapter_UnsynchedClientInfo;
import com.santeh.petone.crm.DBase.DB_Helper_PetOneCRM;
import com.santeh.petone.crm.DBase.DB_Query_PetOneCRM;
import com.santeh.petone.crm.Obj.CustInfoObject;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjhonsl on 1/7/2016.
 */
public class Activity_Unsynched_ClientInfo extends FragmentActivity {

    ImageButton btntitleLeft;
    ListView lvUnsyncedClientInfo;
    DB_Query_PetOneCRM db;
    LinearLayout ll_bottom_sync;

    Activity activity;
    Context context;

    Adapter_UnsynchedClientInfo adapter_unsynchedClientInfo;
    List<CustInfoObject> clientInfoList;
    int[] selectedItems[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsyced_clientinfo);


        /** INITIALIZE VALUES AND OBJECTS **/
        clientInfoList = new ArrayList<>();
        activity = this;
        context = Activity_Unsynched_ClientInfo.this;

        db = new DB_Query_PetOneCRM(this);
        db.open();

        btntitleLeft = (ImageButton) findViewById(R.id.btn_title_left);
        lvUnsyncedClientInfo = (ListView) findViewById(R.id.lv_unsycned_clientinfo);
        ll_bottom_sync = (LinearLayout) findViewById(R.id.ll_bottom_postall);

        /** END OF INITIALIZING VALUES **/

        Cursor cur = db.getNotPosted_ClientInfo(activity);
        if (cur.getCount() > 0) {
            clientInfoList = new ArrayList<>();
            while (cur.moveToNext()) {
                CustInfoObject infoobj = new CustInfoObject();

                infoobj.setCustomerName(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME)));
                infoobj.setAddress(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS)));
                infoobj.setCi_id(cur.getInt(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ID)));

                clientInfoList.add(infoobj);
            }
            adapter_unsynchedClientInfo = new Adapter_UnsynchedClientInfo(context, R.layout.item_lv_unsynced_clientupdates, clientInfoList);
            lvUnsyncedClientInfo.setAdapter(adapter_unsynchedClientInfo);

        }



        lvUnsyncedClientInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Helper.common.toastShort(activity, position+"");
            }
        });


        btntitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_bottom_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clientInfoList != null){
                    if (clientInfoList.size() > 0){
                        boolean[] checkedPositions = adapter_unsynchedClientInfo.getCheckedPositions();
                        String positions = "positions " + checkedPositions.length + " :";
                        for (int i = 0; i < checkedPositions.length; i++) {
                            if (checkedPositions[i]){
                                positions = positions + "," + i;
                            }
                        }
                        Helper.common.toastShort(activity, positions);
                    }
                }

            }
        });





    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
    }
}
