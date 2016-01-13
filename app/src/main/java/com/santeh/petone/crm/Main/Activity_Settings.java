package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.petone.crm.APIs.MyVolleyAPI;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = Activity_Settings.this;
        activity = this;

        db = new DB_Query_PetOneCRM(this);


        btnTitleLeft = (ImageButton) findViewById(R.id.btn_title_left);

        llRestore = (LinearLayout) findViewById(R.id.ll_restore);

        llRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreFromDB_ClientInfo(activity, context);
            }
        });



        btnTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void restoreFromDB_ClientInfo(final Activity activity, final Context context) {

        final ProgressDialog pd = new ProgressDialog(context);
        custInfoList = new ArrayList<>();
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage("Syncing...");
        pd.show();



        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_SELECT_CLIENTINFO_BY_USERID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        pd.hide();
                        if (!response.substring(1, 2).equalsIgnoreCase("0")) {
                            custInfoList = PetOneParser.parseFeed(response);
                            Helper.common.toastShort(activity, custInfoList.size()+"" );
                        } else {
                            Helper.common.toastShort(activity, "FAiL");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.common.toastShort(activity, "SYNC INTERRUPTED.");
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
