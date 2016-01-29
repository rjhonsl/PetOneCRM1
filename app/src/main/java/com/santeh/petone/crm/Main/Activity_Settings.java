package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.santeh.petone.crm.Utils.SimpleFileDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rjhonsl on 1/7/2016.
 */
public class Activity_Settings extends FragmentActivity {

    List<CustInfoObject> custInfoList;
    ImageButton btnTitleLeft;
    LinearLayout llClientInfo, llRestoreDB, llexport, llimport;

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

        llRestoreDB = (LinearLayout) findViewById(R.id.ll_restore);
        llexport = (LinearLayout) findViewById(R.id.ll_export_local);
        llimport = (LinearLayout) findViewById(R.id.ll_import_local);


        llimport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog d = Helper.common.dialogThemedYesNO(activity,
                        "All data will be restored to the state of when the backup was done. All existing data after the date will not be restored.\n\nAre you sure you want to retore db? ",
                        "Restore", "NO", "YES", R.color.red_material_600);
                Button btnyes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                Button btnno  = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);

                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });

                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                        if (Helper.random.checkSD(activity)) {
                            /////////////////////////////////////////////////////////////////////////////////////////////////
                            //Create FileOpenDialog and register a callback
                            /////////////////////////////////////////////////////////////////////////////////////////////////
                            SimpleFileDialog FileOpenDialog = new SimpleFileDialog(activity, ".db",
                                    new SimpleFileDialog.SimpleFileDialogListener() {
                                        String m_chosen;

                                        @Override
                                        public void onChosenDir(String chosenDir) {
                                            // The code in this function will be executed when the dialog OK button is pushed
                                            m_chosen = chosenDir;
                                            try {
                                                File sd = Environment.getExternalStorageDirectory();//gets external Directory/address
                                                if (sd.canWrite()) {
                                                    String backupDBPath = "/data/data/com.santeh.petone.crm/databases/petone.db";//database internal storage path
                                                    File currentDB = new File(backupDBPath);
                                                    File backupDB = new File(m_chosen);

                                                    if (currentDB.exists()) {
                                                        FileChannel src = new FileInputStream(backupDB).getChannel();
                                                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                                                        dst.transferFrom(src, 0, src.size());
                                                        src.close();
                                                        dst.close();
                                                        Toast.makeText(getApplicationContext(), "Restore was successful", Toast.LENGTH_LONG).show();
                                                        Intent i = getBaseContext().getPackageManager()
                                                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(i);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Helper.common.toastLong(activity, "Failed to Restore: " + String.valueOf(e));
                                            }

                                        }
                                    });

                            //You can change the default filename using the public variable "Default_File_Name"
                            FileOpenDialog.Default_File_Name = "";
                            FileOpenDialog.chooseFile_or_Dir();
                            /////////////////////////////////////////////////////////////////////////////////////////////////
                        } else {
                            Helper.common.toastLong(activity, "External storage not available");
                        }
                    }
                });


            }
        });

        llexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog d = Helper.common.dialogThemedYesNO(activity,
                        "Create local backup? ",
                        "Backup", "NO", "YES", R.color.red_material_600);
                Button btnyes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                Button btnno  = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);

                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                    }
                });

                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();

                        if(Helper.random.checkSD(activity)){
                            final String inFileName = "/data/data/com.santeh.petone.crm/databases/petone.db";//current database to be exported
                            File dbFile = new File(inFileName);
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(dbFile);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            //gets time for naming sequence
                            Date d = new Date();
                            CharSequence s  = DateFormat.format("MMM-dd-yyyy hhmmAA", d.getTime());
                            String curDate = String.valueOf(s);

                            String outFileName = Environment.getExternalStorageDirectory()+"/.ptn/local/" + curDate+".db";//output file name

                            // Open the empty db as the output stream
                            OutputStream output = null;
                            try {
                                output = new FileOutputStream(outFileName);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            // Transfer bytes from the inputfile to the outputfile
                            byte[] buffer = new byte[1024];
                            int length;
                            try {
                                while ((length = fis.read(buffer))>0){
                                    output.write(buffer, 0, length);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Close the streams
                            try {
                                output.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Helper.common.toastLong(activity, "Back up Successfull: \n" + curDate);
                        }
                        else{
                            Helper.common.toastLong(activity, "External Storage not available!");
                        }

                    }
                });

            }
        });

        llRestoreDB.setOnClickListener(new View.OnClickListener() {
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
