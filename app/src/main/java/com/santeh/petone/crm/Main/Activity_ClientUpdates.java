package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    Activity activity;
    Context context;

    DB_Query_AquaCRM db;
    String clientID;

    LinearLayout llEmptyUpdates, llListviewHOlder;
    ImageButton btnAddUpdate, btnClientDetails, btnTitleLeft, btnTitleRight;
    TextView txtTitle;
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
            Cursor cur = db.getClientByClientID(clientID);
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


        llEmptyUpdates = (LinearLayout) findViewById(R.id.ll_empty_update);
        llListviewHOlder = (LinearLayout) findViewById(R.id.ll_listviewHolder);
        btnAddUpdate = (ImageButton) findViewById(R.id.btn_addupdate);
        btnTitleLeft = (ImageButton) findViewById(R.id.btn_title_left);
        btnTitleRight = (ImageButton) findViewById(R.id.btn_title_right);
        txtTitle = (TextView) findViewById(R.id.title);
        btnClientDetails = (ImageButton) findViewById(R.id.btn_viewClientDetails);
        lv_clientUpdates = (ListView) findViewById(R.id.listview_clientUpdates);

        txtTitle.setText(strClientName);

        getClientUpdates();

        showEmptyUpdateImage();


        btnTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.isClientInfoPosted(clientID)) {
                    Helper.common.toastShort(activity, "You can't modify information that is already posted. Please contact admin for further assistance.");
                }else{
                    Intent intent = new Intent(context, Activity_Edit_ClientInfo.class);
                    intent.putExtra("id", clientID);
                    startActivity(intent);
                }

            }
        });

        btnClientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String compile = "Customer Code:\n" + strCustCode + "\n\n" +
                        "Client Name:\n" + strClientName + "\n\n" +
                        "Contact Number:\n" + strContactNumber + "\n\n" +
                        "Address:\n" + strAddress + "\n\n" +
                        "Date Added:\n" + Helper.timeConvert.longtoDateTime_StringFormat(Long.parseLong(strDateAdded));

                Helper.common.dialogThemedOkOnly(activity, "Client Info", compile, "OK", R.color.teal_400);
            }
        });

        btnTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

                        singleUpdate.setDateAddedToDB(System.currentTimeMillis() + "");
                        singleUpdate.setRemarks(edtRemarks.getText().toString());
                        singleUpdate.setId(id);

                        updateList.add(singleUpdate);


                        if (adapterClientUpdates != null)
                        {
                            adapterClientUpdates.clear();
                        }

                        getClientUpdates();

                    }
                });
            }
        });


        lv_clientUpdates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String date = Helper.timeConvert.longtoDate_StringFormat(Long.parseLong(updateList.get(position).getDateAddedToDB()));
                String time = Helper.timeConvert.longtoTime_StringFormat(Long.parseLong(updateList.get(position).getDateAddedToDB()));
                Helper.common.dialogThemedOkOnly(activity, date, updateList.get(position).getRemarks()+"\n\n"+time, "OK", R.color.red);
            }
        });

        lv_clientUpdates.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position1, long id) {
//                Helper.common.dialogThemedOkOnly(activity, "Longpressed", view.getId() + " was long pressed " + id + " " + position,"OK", R.color.red_material_600);
                boolean isposted = db.isClientUpdatePosted(updateList.get(position1).getId()+"");
                if (isposted) {
                    Helper.common.dialogThemedOkOnly(activity, "Warning", "You can't modify uploaded updates", "OK", R.color.red_material_400);
                }else{
                    final String[] options = {"Edit","Delete"};
                    final Dialog d = Helper.common.dialogList(activity, options, "Options");

                    ListView lv = (ListView) d.findViewById(R.id.dialog_list_listview);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            d.hide();

                            if (position == 0) {

                                final Dialog d2 = Helper.common.dialogYesNoWithMultiLineEditText(activity, "Input your changes.", updateList.get(position).getRemarks(), "Updates",
                                        "CANCEK", "UPDATE", R.color.lightBlue_400);
                                Button btnOK = (Button) d2.findViewById(R.id.btn_dialog_yesno_opt2);
                                Button btnCancel = (Button) d2.findViewById(R.id.btn_dialog_yesno_opt1);
                                final EditText edtRemarks = (EditText) d2.findViewById(R.id.dialog_edttext);
                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        d2.hide();
                                    }
                                });

                                btnOK.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (edtRemarks.getText().toString().equalsIgnoreCase("")) {
                                            d2.hide();
                                            Helper.common.toastShort(activity, "You can't leave remarks blank.");
                                        } else {
                                            d2.hide();
                                            db.updateClientUpdates(updateList.get(position).getId() + "", edtRemarks.getText().toString());
                                            adapterClientUpdates.clear();
                                            getClientUpdates();
                                        }
                                    }
                                });


                            } else if (position == 1) {
                                final Dialog d1 = Helper.common.dialogThemedYesNO(activity, "Are you sure you want to DELETE this record?", "Delete", "NO", "YES", R.color.red_material_500);
                                Button yes  = (Button) d1.findViewById(R.id.btn_dialog_yesno_opt2);
                                Button no   = (Button) d1.findViewById(R.id.btn_dialog_yesno_opt1);

                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        db.deleteClientUpdateById(updateList.get(position1).getId() + "");
                                        adapterClientUpdates.clear();
                                        getClientUpdates();
                                        d1.hide();
                                    }
                                });

                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        d1.hide();
                                    }
                                });
                            }
                        }
                    });
                }
                return true;
            }
        });
    }

    private void showEmptyUpdateImage() {
        Cursor cur = db.getClientByClientID(clientID);
        int count = cur.getCount();
        if (count > 0) {
            llEmptyUpdates.setVisibility(View.GONE);
            llListviewHOlder.setVisibility(View.VISIBLE);
        }else{
            llEmptyUpdates.setVisibility(View.VISIBLE);
            llListviewHOlder.setVisibility(View.GONE);
        }
        Helper.common.toastShort(activity, count+"");
    }

    private void getClientUpdates() {
        updateList = new ArrayList<>();
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
//        Helper.common.toastShort(activity, updateList.size()+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
//        showEmptyUpdateImage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }
}
