package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.petone.crm.APIs.MyVolleyAPI;
import com.santeh.petone.crm.Adapter.Adapter_UnsynchedClientInfo;
import com.santeh.petone.crm.DBase.DB_Helper_PetOneCRM;
import com.santeh.petone.crm.DBase.DB_Query_PetOneCRM;
import com.santeh.petone.crm.Obj.CustInfoObject;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    ProgressDialog pd;

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

        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(true);

        btntitleLeft = (ImageButton) findViewById(R.id.btn_title_left);
        lvUnsyncedClientInfo = (ListView) findViewById(R.id.lv_unsycned_clientinfo);
        ll_bottom_sync = (LinearLayout) findViewById(R.id.ll_bottom_postall);

        /** END OF INITIALIZING VALUES **/

        showAllUnsycnedClientInfo();



        lvUnsyncedClientInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                String strClientName, strCustCode, strContactNumber, strAddress,strDateAdded;

                strClientName = clientInfoList.get(position).getCustomerName();
                strCustCode = clientInfoList.get(position).getCustCode();
                strContactNumber = clientInfoList.get(position).getContact_number();
                strAddress = clientInfoList.get(position).getAddress();
                strDateAdded = clientInfoList.get(position).getDateAddedToDB();

                final String compile = "Customer Code:\n" + strCustCode + "\n\n" +
                        "Client Name:\n" + strClientName + "\n\n" +
                        "Contact Number:\n" + strContactNumber + "\n\n" +
                        "Address:\n" + strAddress + "\n\n" +
                        "Date Added:\n" + Helper.timeConvert.longtoDateTime_StringFormat(Long.parseLong(strDateAdded));

                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Helper.common.dialogThemedOkOnly(activity, "Client Info", compile, "OK", R.color.teal_400);
                    }
                }, 250);


            }
        });

        lvUnsyncedClientInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, Activity_Edit_ClientInfo.class);
                intent.putExtra("activity", Helper.variables.ACTIVITY_UNSYCED_CLIENTINFO);
                intent.putExtra("id", clientInfoList.get(position).getCi_id()+"");
                startActivity(intent);
                return true;
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

                        int selectedidcounter= adapter_unsynchedClientInfo.getCheckedCount();
                        final int[] selectedid = new int[selectedidcounter];

                        if (selectedidcounter>0){
                            String positions = "positions :";

                            int counter = 0;
                            for (int i = 0; i < checkedPositions.length; i++) {
                                if (checkedPositions[i]) {
                                    selectedid[counter] = clientInfoList.get(i).getCi_id();
                                    counter++;
                                }
                            }

                            final Dialog d = Helper.common.dialogThemedYesNO(activity, "The data you will sync is final and unchangeable. " +
                                    "\n\nAre you sure you want to sync (" + selectedidcounter + ") items on our server?", "Prompt", "NO", "YES", R.color.red_material_600);
                            Button btnYes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                            Button btnNo = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);

                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Sync_clientInfo(db, activity, context, selectedid);
                                    d.hide();
                                }
                            });

                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    d.hide();
                                }
                            });



//
//                            Helper.common.dialogThemedOkOnly(activity, "Query",
////                                    whereSelected,
//                                    db.getSQLStringForInsert_UNPOSTED_CustomerINFO(activity, selectedid),
//                                    "OK", R.color.red);
//                            Log.d("query", db.getSQLStringForInsert_UNPOSTED_CustomerINFO(activity, selectedid));

                        }else{
                                Helper.common.toastShort(activity, "No item selected!");
                        }
//
                    }else{
                        Helper.common.toastShort(activity, "No item to sync!");
                    }
                }else{
                    Helper.common.toastShort(activity, "No item to sync!");
                }
            }
        });

    }

    private void showAllUnsycnedClientInfo() {
        Cursor cur = db.getNotPosted_ClientInfo(activity);
        if (cur.getCount() > 0) {
            clientInfoList = new ArrayList<>();
            while (cur.moveToNext()) {
                CustInfoObject infoobj = new CustInfoObject();

                infoobj.setCustomerName(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME)));
                infoobj.setAddress(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS)));
                infoobj.setCi_id(cur.getInt(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ID)));
                infoobj.setContact_number(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER)));
                infoobj.setCustCode(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE)));
                infoobj.setDateAddedToDB(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded)));

                clientInfoList.add(infoobj);
            }
            adapter_unsynchedClientInfo = new Adapter_UnsynchedClientInfo(context, R.layout.item_lv_unsynced_clientinfo, clientInfoList);
            lvUnsyncedClientInfo.setAdapter(adapter_unsynchedClientInfo);

        }
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



    private void Sync_clientInfo(final DB_Query_PetOneCRM db, final Activity activity, final Context context, final int[] selectedID) {

        pd.setMessage("Syncing...");
        pd.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_PHP_RAW_QUERY_POST_INSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!response.substring(1, 2).equalsIgnoreCase("0")) {
//                                startSynchingDB_PondInfo();
                            db.updateUnPostedToPosted_BySelectedItems_CLIENTINFO(activity, selectedID);

                            if (adapter_unsynchedClientInfo!=null){
                                adapter_unsynchedClientInfo.clear();
                            }
                            Helper.common.toastShort(activity, "Sync Success");
                            showAllUnsycnedClientInfo();
                            sync_activities();
                            pd.hide();


                        } else {
                            Helper.common.toastShort(activity, "SYNC INTERRUPTED. Please try again");
                            pd.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.common.toastShort(activity, "SYNC INTERRUPTED. Please try syncing again.");
                        pd.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", Helper.variables.getGlobalVar_currentUserName(activity));
                params.put("password", Helper.variables.getGlobalVar_currentUserPassword(activity));
                params.put("deviceid", Helper.common.getMacAddress(context));
                params.put("userid", Helper.variables.getGlobalVar_currentUserID(activity) + "");
                params.put("userlvl", Helper.variables.getGlobalVar_currentLevel(activity) + "");

                params.put("sql", db.getSQLStringForInsert_UNPOSTED_CustomerINFO(activity, selectedID) + "");

                return params;
            }
        };

        MyVolleyAPI api = new MyVolleyAPI();
        api.addToReqQueue(postRequest, context);
    }


    private void sync_activities() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_PHP_RAW_QUERY_POST_INSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!response.substring(1, 2).equalsIgnoreCase("0")) {
//                                startSynchingDB_PondInfo();
                            db.updateUnPostedToPosted_All_userActivities();
                            Log.d("sync activities", "success");
                        } else {
                            Log.d("sync activities", "failed with reply from server: "+response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("sync activities", "failed with local error");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", Helper.variables.getGlobalVar_currentUserName(activity));
                params.put("password", Helper.variables.getGlobalVar_currentUserPassword(activity));
                params.put("deviceid", Helper.common.getMacAddress(context));
                params.put("userid", Helper.variables.getGlobalVar_currentUserID(activity) + "");
                params.put("userlvl", Helper.variables.getGlobalVar_currentLevel(activity) + "");

                params.put("sql", db.getSQLStringForInsert_UNPOSTED_Activities(activity) + "");

                return params;
            }
        };

        MyVolleyAPI api = new MyVolleyAPI();
        api.addToReqQueue(postRequest, context);
    }
}
