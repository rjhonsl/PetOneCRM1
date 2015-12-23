package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.santeh.petone.crm.Adapter.Adapter_ClientUpdates;
import com.santeh.petone.crm.DBase.DB_Helper_AquaCRM;
import com.santeh.petone.crm.DBase.DB_Query_AquaCRM;
import com.santeh.petone.crm.Obj.CustInfoObject;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjhonsl on 12/21/2015.
 */
public class Activity_ClientUpdates extends FragmentActivity {

    ImageButton btnAddUpdate, btnClientDetails;
    Activity activity;
    Context context;

    DB_Query_AquaCRM db;
    String clientID;

    ListView lv_clientUpdates;
    Adapter_ClientUpdates adapterClientUpdates;
    List<CustInfoObject> updateList;
    String strClientName, strCustCode, strContactNumber, strAddress, strDateAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_updates);

        activity = this;
        context = Activity_ClientUpdates.this;
        db = new DB_Query_AquaCRM(this);
        db.open();


        clientID = "";

        if (getIntent().hasExtra("id")){
            clientID = getIntent().getStringExtra("id");
            Cursor cur = db.getClientInfoByUserID(clientID);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    strClientName = cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_CLIENT_NAME));
                    strCustCode = cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_CUSTCODE));
                    strContactNumber = cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_C_NUMBER));
                    strAddress = cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_ADDRESS));
                    strDateAdded = cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_dateAdded));
                }
            }
        }


        btnAddUpdate = (ImageButton) findViewById(R.id.btn_addupdate);
        btnClientDetails = (ImageButton) findViewById(R.id.btn_viewClientDetails);
        lv_clientUpdates = (ListView) findViewById(R.id.listview_clientUpdates);

        btnClientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String compile = "Customer Code:\n"+strCustCode+"\n\n"+
                                "Client Name:\n"+strClientName+"\n\n"+
                                "Contact Number:\n"+strContactNumber + "\n\n"+
                                "Address:\n"+strAddress +"\n\n" +
                                "Date Added:\n"+Helper.timeConvert.longtoDateTime_StringFormat(Long.parseLong(strDateAdded));

                Helper.common.dialogThemedOkOnly(activity, "Client Info", compile, "OK", R.color.deepteal_400);
            }
        });


        btnAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = Helper.common.dialogYesNoWithMultiLineEditText(activity, "Describe your client visit.", "", "Updates", "CANCEL", "OK", R.color.blue);
                Button btnOK = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                Button btnCancel = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                final EditText edtRemarks = (EditText) d.findViewById(R.id.dialog_edttext);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int id = (int) db.insertClientUpdates(edtRemarks.getText().toString(), clientID);
                        d.hide();
                        CustInfoObject singleUpdate = new CustInfoObject();

                        singleUpdate.setDateAddedToDB(System.currentTimeMillis()+"");
                        singleUpdate.setRemarks(edtRemarks.getText().toString());
                        singleUpdate.setId(id);

                        adapterClientUpdates.add(singleUpdate);
//                        getClientUpdates();
                    }
                });
            }
        });

        getClientUpdates();

    }

    private void getClientUpdates() {
        updateList = new ArrayList<CustInfoObject>();
        Cursor cur = db.getClientUpdateByID(clientID);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                CustInfoObject updates = new CustInfoObject();

                updates.setDateAddedToDB(cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_UPDATES_DATEADDED)));
                updates.setRemarks(cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_UPDATES_REMARKS)));
                updates.setId(cur.getInt(cur.getColumnIndex(DB_Helper_AquaCRM.CL_UPDATES_ID)));

                updateList.add(updates);
            }
            showClientUpdates();
        }
    }

    private void showClientUpdates(){
        adapterClientUpdates =  new Adapter_ClientUpdates(context, R.layout.item_lv_clientupdates, updateList);
        lv_clientUpdates.setAdapter(adapterClientUpdates);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }
}
