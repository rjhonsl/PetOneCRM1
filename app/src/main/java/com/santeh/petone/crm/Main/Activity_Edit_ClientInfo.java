package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.santeh.petone.crm.DBase.DB_Helper_PetOneCRM;
import com.santeh.petone.crm.DBase.DB_Query_PetOneCRM;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.Helper;
import com.santeh.petone.crm.Utils.Logging;

/**
 * Created by rjhonsl on 10/7/2015.
 */
public class Activity_Edit_ClientInfo extends FragmentActivity{

    String clientID;
    double lat=0, lng=0;
    int y, m, d;

    public static final String DATEPICKER_TAG = "datepicker";
    DatePickerDialog datePickerDialog;

    Activity activity;
    Context context;

    DB_Query_PetOneCRM db;

    ImageButton btnOK, btnBack, btnDelete;
    EditText edtCustomerCode, edtClientName, edtContactNumber, edtAddress;
    TextView txtnote, txttitle;


    private String strClientName;
    private String strCustCode;
    private String strContactNumber;
    private String strAddress;
    private String strDateAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clientinfo);

        activity = this;
        context = Activity_Edit_ClientInfo.this;

        btnOK = (ImageButton) findViewById(R.id.btn_ok);
        btnBack = (ImageButton) findViewById(R.id.btn_title_left);
        btnDelete = (ImageButton) findViewById(R.id.btn_delete);

        db = new DB_Query_PetOneCRM(this);
        db.open();

        clientID = "";
        if (getIntent().hasExtra("id")){
            clientID = getIntent().getStringExtra("id");
            Cursor cur = db.getClientByClientID(clientID);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    strClientName = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME));
                    strCustCode = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE));
                    strContactNumber = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER));
                    strAddress = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS));
                    strDateAdded = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded));
                }
            }
        }


        Helper.common.hideKeyboardOnLoad(activity);
        if (getIntent().hasExtra("lat")){lat= getIntent().getDoubleExtra("lat", 0);}
        if (getIntent().hasExtra("long")){lng= getIntent().getDoubleExtra("long", 0);}

        txttitle = (TextView) findViewById(R.id.title);
        txtnote = (TextView) findViewById(R.id.txt_note);
        edtCustomerCode = (EditText) findViewById(R.id.edt_custCode);
        edtClientName = (EditText) findViewById(R.id.edt_clientName);
        edtContactNumber = (EditText) findViewById(R.id.edt_contactNumber);
        edtAddress = (EditText) findViewById(R.id.edtAddress);

        edtCustomerCode.setText(strCustCode);
        edtClientName.setText(strClientName);
        edtContactNumber.setText(strContactNumber);
        edtAddress.setText(strAddress);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = Helper.common.dialogThemedYesNO(activity, "Are you sure you want to remove this client information? \n\n(NOTE) All updates associated with this record will also be removed", "Delete", "NO", "YES", R.color.red_material_500);
                Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                        if (db.deleteClientInfoByID(clientID)) { //deletes client info
                            db.deleteClientUpdatesByClientID(clientID); //deletes associated records
                            Logging.userAction(activity, context, Logging.ACTION_DELETE, clientID + "", DB_Helper_PetOneCRM.TBL_CLIENTINFO, Logging.TYPE_USER);


                            Helper.common.toastShort(activity, "Client has been removed.");
                            Intent intent = new Intent(activity, MapsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("fromActivity", "addcustomerinfo");
                            startActivity(intent);
                            finish(); // call this to finish the current activity
                        }

                    }
                });
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtCustomerCode.getText().toString().equalsIgnoreCase("") &&
                        !edtClientName.getText().toString().equalsIgnoreCase("") &&
                        !edtContactNumber.getText().toString().equalsIgnoreCase("") &&
                        !edtAddress.getText().toString().equalsIgnoreCase("")) {
                    boolean isExisting = db.isCustCodeExisting(edtCustomerCode.getText().toString());

                    if(edtCustomerCode.getText().toString().equalsIgnoreCase(strCustCode)) {
                        Log.d("update", "TRUE custcode equals current");
                        updateClientInfo();
                    }else if (isExisting) {
                        Log.d("update", "TRUE custcode exist in db");
                        Helper.common.dialogThemedOkOnly(activity, "Oops", "Customer Code already exist. \n\nYou could only have one input Customer Code once.", "OK", R.color.darkgreen_800);
                    } else {
                        Log.d("update", "TRUE custcode is new custcode");
                        updateClientInfo();
                    }
                } else {
                    Helper.common.dialogThemedOkOnly(activity, "Warning", "You must complete fields with '*' to continue.", "OK", R.color.red);
                }
            }

            private void updateClientInfo() {
                final Dialog d = Helper.common.dialogThemedYesNO(activity, "Save Client Information to device storage?", "Save", "NO", "YES", R.color.blue);
                Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.updateClientInfo(clientID, edtAddress.getText().toString(), edtClientName.getText().toString(), edtCustomerCode.getText().toString(),
                                edtContactNumber.getText().toString());

                        Logging.userAction(activity, context, Logging.ACTION_EDIT, clientID + "", DB_Helper_PetOneCRM.TBL_CLIENTINFO, Logging.TYPE_USER);
                        finish();

                    }
                });
            }
        });
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

