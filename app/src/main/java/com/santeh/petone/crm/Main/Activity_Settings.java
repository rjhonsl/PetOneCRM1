package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.petone.crm.APIs.MyVolleyAPI;
import com.santeh.petone.crm.DBase.DB_Helper_PetOneCRM;
import com.santeh.petone.crm.DBase.DB_Query_PetOneCRM;
import com.santeh.petone.crm.Obj.CustInfoObject;
import com.santeh.petone.crm.Parser.PetOneParser;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rjhonsl on 1/7/2016.
 */
public class Activity_Settings extends FragmentActivity {

    List<CustInfoObject> custInfoList;
    ImageButton btnTitleLeft;
    LinearLayout llClientInfo, llRestore;

    Activity activity;
    Context context;

    DB_Query_PetOneCRM db;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = Activity_Settings.this;
        activity = this;

        db = new DB_Query_PetOneCRM(this);
        pd = new ProgressDialog(context);

        custInfoList = new ArrayList<>();

        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);



        btnTitleLeft = (ImageButton) findViewById(R.id.btn_title_left);

        llRestore = (LinearLayout) findViewById(R.id.ll_restore);

        llRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog d = Helper.common.dialogThemedYesNO(activity, "Restoring the database will remove existing data that is not posted." +
                        "\n\nAre you sure you want to restore data from server?", "Restore", "NO", "YES", R.color.red_material_600);
                Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd.show();
                        restoreFromWeb_ClientInfo(activity, context);
                        d.hide();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });


            }
        });



        btnTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void restoreFromWeb_ClientInfo(final Activity activity, final Context context) {

        pd.setMessage("Restoring information...");
        pd.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_SELECT_CLIENTINFO_BY_USERID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        pd.hide();
                        if (!response.substring(1, 2).equalsIgnoreCase("0")) {

                            custInfoList = PetOneParser.parseFeed(response);
                            if (db.getClientInfoByUserID(Helper.variables.getGlobalVar_currentUserID(activity) + "").getCount() > 0) {
                                db.emptyTable(DB_Helper_PetOneCRM.TBL_CLIENTINFO);
                            }
    
                            for (int i = 0; i < custInfoList.size(); i++) {
                                db.insertClientInfo(
                                        custInfoList.get(i).getLocalId(),
                                        custInfoList.get(i).getCust_latitude(),
                                        custInfoList.get(i).getCust_longtitude(),
                                        custInfoList.get(i).getAddress(),
                                        custInfoList.get(i).getCustomerName(),
                                        custInfoList.get(i).getCustCode(),
                                        custInfoList.get(i).getContact_number(),
                                        custInfoList.get(i).getDateAddedToDB(),
                                        custInfoList.get(i).getAddedBy(),
                                        1
                                )
                                ;
                            }

                            restoreFromWeb_ClientUpdates(activity, context);
                        } else {

                            Helper.common.toastShort(activity, "Restore Failed. Please try again.");
                            Helper.DBase.cleartablesforRestore(db, context);
                            pd.hide();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.common.toastShort(activity, "RESTORE INTERRUPTED. " + error.toString());
                        Helper.DBase.cleartablesforRestore(db, context);
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

                return params;
            }
        };

        MyVolleyAPI api = new MyVolleyAPI();
        api.addToReqQueue(postRequest, context);
    }



    private void restoreFromWeb_ClientUpdates(final Activity activity, final Context context) {
        custInfoList = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_SELECT_CLIENTUPDATE_BY_USERID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (!response.substring(1, 2).equalsIgnoreCase("0")) {

                            custInfoList = PetOneParser.parseFeed(response);

                            if (db.getClientUpdate_by_userID(activity).getCount() > 0) {
                                db.emptyTable(DB_Helper_PetOneCRM.TBL_UPDATES);
                            }

                            for (int i = 0; i < custInfoList.size(); i++) {
                                String splitted[] = custInfoList.get(i).getUpdateClientId().split("-");
                                String clientid = splitted[1];
                                db.insertClientUpdates(
                                        custInfoList.get(i).getUpdateLocalId(),
                                        custInfoList.get(i).getUpdateRemarks(),
                                        clientid,
                                        custInfoList.get(i).getUpdateDateAdded(),
                                        1
                                )
                                ;
                            }

                            restoreFromWeb_UserActivity();




                        } else {
                            Helper.common.toastShort(activity, "Restore Failed. Please try again.");
                            Helper.DBase.cleartablesforRestore(db, context);
                            pd.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.common.toastShort(activity, "Restore Failed. Please try again.");
                        Helper.DBase.cleartablesforRestore(db, context);
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

                return params;
            }
        };

        MyVolleyAPI api = new MyVolleyAPI();
        api.addToReqQueue(postRequest, context);
    }



    private void restoreFromWeb_UserActivity() {
        custInfoList = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_SELECT_USERACTIVITY_BY_USERID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        pd.hide();
                        if (!response.substring(1, 2).equalsIgnoreCase("0")) {

                            custInfoList = PetOneParser.parseFeed(response);

                            if (db.getUserActivity_by_userID(activity).getCount() > 0) {
                                db.emptyTable(DB_Helper_PetOneCRM.TBL_USER_ACTIVITY);
                            }

                            for (int i = 0; i < custInfoList.size(); i++) {



                                db.insertUserActivityData(
                                        custInfoList.get(i).getActLocalId(),
                                        custInfoList.get(i).getActUserID(),
                                        custInfoList.get(i).getActActionDone(),
                                        custInfoList.get(i).getActLat(),
                                        custInfoList.get(i).getActLong(),
                                        custInfoList.get(i).getActDatetime(),
                                        custInfoList.get(i).getActActionType()
                                )
                                ;
                            }

                            Intent intent = new Intent(activity, Activity_LoginScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Helper.common.toastShort(activity, "Restore Successful.");


                        } else {
                            Helper.common.toastShort(activity, "Restore Failed. Please try again.");
                            Helper.DBase.cleartablesforRestore(db, context);
                            pd.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.common.toastShort(activity, "RESTORE INTERRUPTED. " + error.toString());
                        Helper.DBase.cleartablesforRestore(db, context);
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

                return params;
            }
        };

        MyVolleyAPI api = new MyVolleyAPI();
        api.addToReqQueue(postRequest, context);
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
