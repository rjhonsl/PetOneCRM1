package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.petone.crm.APIs.MyVolleyAPI;
import com.santeh.petone.crm.Adapter.Adapter_UnsynchedClientUpdates;
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
public class Activity_Unsynched_ClientUpdates extends FragmentActivity {


    ImageButton btntitleLeft;
    ListView lvUnsyncedClientUpdate;
    DB_Query_PetOneCRM db;
    LinearLayout ll_bottom_sync;

    Activity activity;
    Context context;

    Adapter_UnsynchedClientUpdates adapter_unsynchedClientUpdates;
    List<CustInfoObject> clientUpdateList;
    int[] selectedItems[];
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsyced_clientupdates);

        /** INITIALIZE VALUES AND OBJECTS **/
        clientUpdateList = new ArrayList<>();
        activity = this;
        context = Activity_Unsynched_ClientUpdates.this;

        db = new DB_Query_PetOneCRM(this);
        db.open();

        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(true);

        btntitleLeft = (ImageButton) findViewById(R.id.btn_title_left);
        lvUnsyncedClientUpdate = (ListView) findViewById(R.id.lv_unsycned_clientupdate);
        ll_bottom_sync = (LinearLayout) findViewById(R.id.ll_bottom_postall);

        /** END OF INITIALIZING VALUES **/

        showAllUnsycnedClientInfo();



        lvUnsyncedClientUpdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

//                String strClientName, strCustCode, strContactNumber, strAddress, strDateAdded;
//
//                strClientName = clientUpdateList.get(position).getCustomerName();
//                strCustCode = clientUpdateList.get(position).getCustCode();
//                strContactNumber = clientUpdateList.get(position).getContact_number();
//                strAddress = clientUpdateList.get(position).getAddress();
//                strDateAdded = clientUpdateList.get(position).getDateAddedToDB();
//
//                final String compile = "Customer Code:\n" + strCustCode + "\n\n" +
//                        "Client Name:\n" + strClientName + "\n\n" +
//                        "Contact Number:\n" + strContactNumber + "\n\n" +
//                        "Address:\n" + strAddress + "\n\n" +
//                        "Date Added:\n" + Helper.timeConvert.longtoDateTime_StringFormat(Long.parseLong(strDateAdded));
//
//                final Handler handler1 = new Handler();
//                handler1.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Helper.common.dialogThemedOkOnly(activity, "Client Info", compile, "OK", R.color.teal_400);
//                    }
//                }, 250);


            }
        });

        lvUnsyncedClientUpdate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog d = Helper.common.dialogYesNoWithMultiLineEditText(activity, "Enter new remark", clientUpdateList.get(position).getRemarks(), "Edit", "CANCEL", "OK", R.color.red_material_600);
                final EditText editText = (EditText) d.findViewById(R.id.dialog_edttext);
                Button btnNo = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                Button btnYes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.updateClientUpdates(clientUpdateList.get(position).getId() + "", editText.getText().toString());
                        d.hide();

                        refreshListView();
                        lvUnsyncedClientUpdate.smoothScrollToPosition(position);


                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });


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

                if (clientUpdateList != null){
                    if (clientUpdateList.size() > 0){
                        boolean[] checkedPositions = adapter_unsynchedClientUpdates.getCheckedPositions();

                        int selectedidcounter= adapter_unsynchedClientUpdates.getCheckedCount();
                        final int[] selectedid = new int[selectedidcounter];

                        if (selectedidcounter>0){
                            String positions = "positions :";

                            int counter = 0;
                            for (int i = 0; i < checkedPositions.length; i++) {
                                if (checkedPositions[i]) {
                                    selectedid[counter] = clientUpdateList.get(i).getId();
                                    counter++;
                                }
                            }




                            final Dialog d = Helper.common.dialogThemedYesNO(activity,
                                    "The data you will sync is final and unchangeable. " +
                                    "\n\n"+
                                    "Are you sure you want to sync (" + selectedidcounter + ") items on our server? ", "Prompt", "NO", "YES", R.color.red_material_600);
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



//                            String whereSelected = "";
//                            for (int i = 0; i < selectedid.length; i++) {
//                                String condition = "OR";
//                                if (selectedid.length-1 == i) {
//                                    condition = "AND";
//                                }else{
//                                    condition = "OR";
//                                }
//                                whereSelected = whereSelected +  DB_Helper_PetOneCRM.CL_UPDATES_ID + " = "+ selectedid[i] + " " + condition + " ";
//                            }

//                            Helper.common.dialogThemedOkOnly(activity, "Query",
////                                    whereSelected,
//                                    db.getSQLStringForInsert_UNPOSTED_CustomerUPDATES(activity, selectedid),
//                                    "OK", R.color.red);
////                            Log.d("query", db.getSQLStringForInsert_UNPOSTED_CustomerUPDATES(activity, selectedid));

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
        Cursor cur = db.getNotPosted_Updates(activity);
        if (cur.getCount() > 0) {
            clientUpdateList = new ArrayList<>();
            while (cur.moveToNext()) {
                CustInfoObject infoobj = new CustInfoObject();

                infoobj.setId(cur.getInt(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_ID)));
                infoobj.setCi_id(cur.getInt(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID)));
                infoobj.setIsPosted(cur.getInt(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_isposted)));
                infoobj.setDateAddedToDB(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED)));
                infoobj.setRemarks(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_REMARKS)));

                infoobj.setCustomerName(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME)));

                clientUpdateList.add(infoobj);
            }
            adapter_unsynchedClientUpdates = new Adapter_UnsynchedClientUpdates(context, R.layout.item_lv_unsynced_clientupdate, clientUpdateList);
            lvUnsyncedClientUpdate.setAdapter(adapter_unsynchedClientUpdates);

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
                            db.updateUnPostedToPosted_BySelectedItems_CLIENTUPDATES(activity, selectedID);

                            refreshListView();
                            pd.hide();
                            Helper.common.toastShort(activity, "Sync Success");

                        } else {
                            Helper.common.toastShort(activity, "SYNC INTERRUPTED. Please try again");
                            pd.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.common.toastShort(activity, "SYNC INTERRUPTED. "+ error.toString() );
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

                String qquery = db.getSQLStringForInsert_UNPOSTED_CustomerUPDATES(activity, selectedID) + "";
                params.put("sql",qquery );
                Log.d("query2", qquery);

                return params;
            }
        };

        MyVolleyAPI api = new MyVolleyAPI();
        api.addToReqQueue(postRequest, context);
    }

    private void refreshListView() {
        if (adapter_unsynchedClientUpdates !=null){
            adapter_unsynchedClientUpdates.clear();
        }
        showAllUnsycnedClientInfo();
    }
}
