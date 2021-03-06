package com.santeh.petone.crm.Main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.santeh.petone.crm.APIs.MyVolleyAPI;
import com.santeh.petone.crm.DBase.DB_Helper_PetOneCRM;
import com.santeh.petone.crm.DBase.DB_Query_PetOneCRM;
import com.santeh.petone.crm.Obj.CustInfoObject;
import com.santeh.petone.crm.Parser.AccountsParser;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.FusedLocation;
import com.santeh.petone.crm.Utils.Helper;
import com.santeh.petone.crm.Utils.Logging;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rjhonsl on 9/4/2015.
 */
public class Activity_LoginScreen extends Activity{

    EditText txtusername, txtpassword;
    TextView txtappname1, txtappname2, txtshowpassword, txtforgot, txtrequestaccount, txttester, txtprogressdialog_message, lblusername, lblpassword;

    CheckBox chkshowpasword;
    ImageButton btnLogin;

    LatLng curlatlng;
    String longitude = " ", latitude=" ";

    Activity activity; Context context;
    Dialog PD;
    ProgressDialog mProgressDialog;

    String versionName, fileDir = "/sdcard/Download/", fulladdress, latestVersionName, versionFile;
    int versionCode;
//    List<CustInfoObject> listaccounts = new ArrayList<>();
    FusedLocation fusedLocation;
    PackageInfo pInfo = null;

    DB_Helper_PetOneCRM dbHelper;
    DB_Query_PetOneCRM db;

    float filesize;
    List<CustInfoObject> listaccounts = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        activity = this;
        context = Activity_LoginScreen.this;

        dbHelper = new DB_Helper_PetOneCRM(this);
        db = new DB_Query_PetOneCRM(this);
        db.open();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red_material_800));
        }

        fusedLocation = new FusedLocation(context, activity);
        fusedLocation.buildGoogleApiClient(context);

//        if (db.getUser_Count() < 1) {
//            long a =  db.insertAdminAccount(context);
//            Helper.common.toastLong(activity, "users: " + a );
//        }


        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (pInfo !=null){
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        }


        PD =  Helper.random.initProgressDialog(activity);
        txtprogressdialog_message = (TextView) PD.findViewById(R.id.progress_message);

        initViews();

        Typeface font_roboto = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");


        Helper.common.hideKeyboardOnLoad(activity);

        btnLogin.requestFocus();
        txtappname1.setTypeface(font_roboto);
        txtappname2.setTypeface(font_roboto);
        txtpassword.setTypeface(font_roboto);
        txtusername.setTypeface(font_roboto);


        txttester.setText(
                Helper.common.getMacAddress(context)
                        + "\n" +
                        "update: " + versionCode + "    V." + versionName
        );


        txtshowpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (chkshowpasword.isChecked()) {
                    chkshowpasword.setChecked(false);
                } else {
                    chkshowpasword.setChecked(true);
                }
                toggle_showpassword();
            }
        });


        if (txtpassword.isFocused() || !txtpassword.getText().toString().equalsIgnoreCase("")){
            txtpassword.setVisibility(View.VISIBLE);
        }

        txtusername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    if (!txtusername.getText().toString().equalsIgnoreCase("") || hasFocus){
                        txtusername.setVisibility(View.VISIBLE);

                        lblusername.setAlpha(0);
                        lblusername.setVisibility(View.VISIBLE);
                        lblusername.animate()
                                .translationY(0)
                                .alpha(255)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        txtusername.setHint("");
                                    }
                                });

                    }
                    else{
                        lblusername.animate()
                                .alpha(0)
                                .translationY(lblpassword.getHeight())
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        lblusername.setVisibility(View.GONE);
                                        txtusername.setHint("Username...");
                                    }
                                });
                    }
            }
        });

        txtpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!txtpassword.getText().toString().equalsIgnoreCase("") || hasFocus){

                    lblpassword.setAlpha(0);
                    lblpassword.setVisibility(View.VISIBLE);
                    lblpassword.animate()
                            .translationY(0)
                            .alpha(255)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    txtpassword.setHint("");
                                }
                            });

                }
                else{
                    lblpassword.animate()
                            .alpha(0)
                            .translationY(lblpassword.getHeight())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    lblpassword.setVisibility(View.GONE);
                                    txtpassword.setHint("Password...");
                                }
                            });
                }
            }
        });


        txtrequestaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ((txtpassword.getText().toString().equalsIgnoreCase("") || txtpassword.getText().toString().trim().equalsIgnoreCase(""))) &&
                        (txtusername.getText().toString().equalsIgnoreCase("") || txtusername.getText().toString().trim().equalsIgnoreCase(""))){
                    Helper.common.toastShort(activity, "Username and Password is needed to continue");
                }else if(txtpassword.getText().toString().equalsIgnoreCase("") || txtpassword.getText().toString().trim().equalsIgnoreCase("")){
                    Helper.common.toastShort(activity, "Password is needed to continue");
                }else if(txtusername.getText().toString().equalsIgnoreCase("") || txtusername.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Helper.common.toastShort(activity,"Username is needed to continue");
                }else
                {
                    login();
                }
            }
        });


        chkshowpasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_showpassword();
            }
        });
    }

    private void initViews() {

        txtusername = (EditText) findViewById(R.id.txt_loginscreen_username);
        txtpassword = (EditText) findViewById(R.id.txt_loginscreen_password);
        txtappname1 = (TextView) findViewById(R.id.txt_loginscreen_apptitle1);
        txtappname2 = (TextView) findViewById(R.id.txt_loginscreen_apptitle2);
        txtshowpassword = (TextView) findViewById(R.id.txt_loginscreen_showpassword);
        txtforgot = (TextView) findViewById(R.id.txtforgot_password);
        txtrequestaccount = (TextView) findViewById(R.id.txt_requestaccount);
        lblpassword = (TextView) findViewById(R.id.lbl_login_password);
        lblusername = (TextView) findViewById(R.id.lbl_login_username);
        txttester = (TextView) findViewById(R.id.txttester);

        btnLogin = (ImageButton) findViewById(R.id.btn_login);

        chkshowpasword = (CheckBox) findViewById(R.id.chk_loginscreen_showpassword);

        txtusername.getBackground().setColorFilter(getResources().getColor(R.color.yellow_300), PorterDuff.Mode.SRC_IN);
        txtpassword.getBackground().setColorFilter(getResources().getColor(R.color.yellow_300), PorterDuff.Mode.SRC_IN);

        txtusername.setText("jhonar10");
        txtpassword.setText("10");

    }

    private void toggle_showpassword() {
        if (chkshowpasword.isChecked()){
            txtpassword.setTransformationMethod(null);
        }else{
            txtpassword.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    public void login() {

        fusedLocation.connectToApiClient();
        Helper.map.isLocationAvailablePrompt(context, activity);

        if(Helper.common.isNetworkAvailable(activity)) { // if network was available
            updatingUserDB();
            txtprogressdialog_message.setText("Logging in...");
            PD.show();

        }else{//if there was no network
            if ( db.getUser_Count()  <=  0 ) { //if user db was null
                //require user to connect to internet
                if(!Helper.common.isNetworkAvailable(activity)) {
                    final Dialog d = Helper.common.dialogThemedOkOnly(activity, "Warning", "Your device should be connected to the internet when logging in for the first time!", "OK", R.color.red_material_500);
                    Button ok = (Button) d.findViewById(R.id.btn_dialog_okonly_OK);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.hide();
                        }
                    });
                }
                else{
                    txtprogressdialog_message.setText("Preparing app please wait...");
                    PD.show();
                    updatingUserDB();
                }
            }else { //if there is an existing account in local db
                txtprogressdialog_message.setText("Logging in...");
                PD.show();
                Cursor cur =  db.getUserIdByLogin(txtusername.getText().toString(), txtpassword.getText().toString(), Helper.common.getMacAddress(context));
                if (cur.getCount() > 0 ){
                    for (int i = 0; i < cur.getCount() ; i++) {
                        while (cur.moveToNext()) {
                            Helper.variables.setGlobalVar_currentlevel(cur.getInt(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USERS_userlvl)), activity);
                            Helper.variables.setGlobalVar_currentUserID(cur.getInt(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USERS_ID)), activity);
                            Helper.variables.setGlobalVar_currentFirstname(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USERS_firstName)), activity);
                            Helper.variables.setGlobalVar_currentLastname(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USERS_lastName)), activity);
                            Helper.variables.setGlobalVar_currentUsername(txtusername.getText().toString(),activity);
                            Helper.variables.setGlobalVar_currentUserpassword(txtpassword.getText().toString(),activity);
                            Helper.variables.setGlobalVar_currentAssignedArea("n/a", activity);
                            Helper.variables.setGlobalVar_DateAddedToDb(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USERS_dateAdded)), activity);
                            Helper.variables.setGlobalVar_currentIsActive(cur.getInt(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USERS_isactive)), activity);
                        }

                        if (Helper.variables.getGlobalVar_currentisActive(activity) == 1){

                            final Intent intent = new Intent(Activity_LoginScreen.this, MapsActivity.class);
                            intent.putExtra("userid", Helper.variables.getGlobalVar_currentUserID(activity));
                            intent.putExtra("userlevel", Helper.variables.getGlobalVar_currentLevel(activity));
                            intent.putExtra("username", Helper.variables.getGlobalVar_currentUserName(activity));
                            intent.putExtra("firstname",Helper.variables.getGlobalVar_currentUserFirstname(activity));
                            intent.putExtra("lastname", Helper.variables.getGlobalVar_currentUserLastname(activity));
                            intent.putExtra("userdescription", Helper.variables.getGlobalVar_currentAssignedArea(activity));
                            intent.putExtra("fromActivity", "login");
                            intent.putExtra("lat", fusedLocation.getLastKnowLocation().latitude + "");
                            intent.putExtra("long", fusedLocation.getLastKnowLocation().longitude + "");

                            db.updateActiveUser(Helper.variables.getGlobalVar_currentUserID(activity) + "", System.currentTimeMillis() + "");

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PD.hide();
                                    startActivity(intent);
                                    Logging.userAction(activity, context, Logging.ACTION_LOGIN, Logging.TYPE_USER);
                                }
                            }, 800);
                        }else{
                            Helper.common.toastShort(activity, "Account is not available. Please contact admin for further support.");
                            PD.hide();
                        }
                    }
                }else{
                    Helper.common.toastShort(activity, "Wrong account credentials. Please try again");
                    PD.hide();
                }
            }
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        Helper.map.isLocationAvailablePrompt(context, activity);
        fusedLocation.connectToApiClient();
        db.open();


        //creates new folder
        File folder = new File(Environment.getExternalStorageDirectory() + "/.ptn/local");
        boolean success = true;
        boolean check = Helper.random.checkSD(activity);
        if (check) {
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            else
            {
                Log.d("mkDirs", "exist");
            }

            if (success) {
                Log.d("mkDirs", "SUCCESS");
            } else {
                Log.d("mkDirs", "FAIL");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }


    @Override
    public void onBackPressed() {
        exitApp();
    }

    private void exitApp() {
        final Dialog d = Helper.common.dialogThemedYesNO(activity, "Do you wish to exit the app?", "EXIT", "YES", "NO", R.color.red);
        d.show();
        Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
        Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.hide();
                finishAffinity();
                Intent setIntent = new Intent(Intent.ACTION_MAIN);
                setIntent.addCategory(Intent.CATEGORY_HOME);
                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(setIntent);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.hide();
            }
        });
    }




    private void updatingUserDB() {
        PD.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        if (response.substring(1, 2).equalsIgnoreCase("0")) {
                            Helper.common.toastLong(activity, "Username and password does not seem to match.");
                            PD.dismiss();
                        } else {

                            listaccounts = AccountsParser.parseFeed(response);
                            PD.dismiss();

                            if (listaccounts.get(0).getIsPetOne_active() == 1){ //if this account is active on petone app

                                //get parsed value from web then insert it to global variable
                                Intent intent = new Intent(Activity_LoginScreen.this, MapsActivity.class);
                                Helper.variables.setGlobalVar_currentlevel(listaccounts.get(0).getUserlevel(), activity);
                                Helper.variables.setGlobalVar_currentUserID(listaccounts.get(0).getUserid(), activity);
                                Helper.variables.setGlobalVar_currentFirstname(listaccounts.get(0).getFirstname(), activity);
                                Helper.variables.setGlobalVar_currentLastname(listaccounts.get(0).getLastname(), activity);
                                Helper.variables.setGlobalVar_currentUsername(txtusername.getText().toString(), activity);
                                Helper.variables.setGlobalVar_currentUserpassword(txtpassword.getText().toString(), activity);
                                Helper.variables.setGlobalVar_currentAssignedArea(listaccounts.get(0).getAssingedArea(), activity);
                                Helper.variables.setGlobalVar_DateAddedToDb(listaccounts.get(0).getDateAddedToDB(), activity);
                                Helper.variables.setGlobalVar_currentIsActive(listaccounts.get(0).getIsactive(), activity);
                                Helper.variables.setGlobalVar_deviceID(listaccounts.get(0).getDeviceid(), activity);


                                //if user is already existing then
                                if (db.isUserExisting(Helper.variables.getGlobalVar_currentUserID(activity) + "")) {
                                    Log.d("LOCAL DB", "UPDATING USER");
                                    int x = db.updateRowOneUser(
                                            Helper.variables.getGlobalVar_currentUserID(activity) + "",
                                            Helper.variables.getGlobalVar_currentLevel(activity) + "",
                                            Helper.variables.getGlobalVar_currentUserFirstname(activity) + "",
                                            Helper.variables.getGlobalVar_currentUserLastname(activity) + "",
                                            Helper.variables.getGlobalVar_currentUserName(activity) + "",
                                            Helper.variables.getGlobalVar_currentUserPassword(activity) + "",
                                            Helper.variables.getGlobalVar_currentDeviceID(activity) + "",
                                            Helper.variables.getGlobalVar_DateAdded(activity) + "");
                                    Log.d("LOCAL DB", "UPDATE FINISHED" + x);
                                }else{
                                    db.insertUserAccountInfo(
                                            Helper.variables.getGlobalVar_currentUserID(activity),
                                            Helper.variables.getGlobalVar_currentLevel(activity),
                                            Helper.variables.getGlobalVar_currentUserFirstname(activity),
                                            Helper.variables.getGlobalVar_currentUserLastname(activity),
                                            Helper.variables.getGlobalVar_currentUserName(activity),
                                            Helper.variables.getGlobalVar_currentUserPassword(activity),
                                            Helper.variables.getGlobalVar_currentDeviceID(activity),
                                            Helper.variables.getGlobalVar_DateAdded(activity),
                                            Helper.variables.getGlobalVar_currentisActive(activity),
                                            1,
                                            System.currentTimeMillis()+""
                                    );
                                }

                                intent.putExtra("userid", listaccounts.get(0).getUserid());
                                intent.putExtra("userlevel", listaccounts.get(0).getUserlevel());
                                intent.putExtra("username", listaccounts.get(0).getUsername());
                                intent.putExtra("firstname", listaccounts.get(0).getFirstname());
                                intent.putExtra("lastname", listaccounts.get(0).getLastname());
                                intent.putExtra("userdescription", listaccounts.get(0).getAccountlevelDescription());
                                intent.putExtra("fromActivity", "login");
                                intent.putExtra("lat",fusedLocation.getLastKnowLocation().latitude+"");
                                intent.putExtra("long",fusedLocation.getLastKnowLocation().longitude+"");

                                db.updateActiveUser(listaccounts.get(0).getUserid()+"", System.currentTimeMillis()+"");


                                Logging.userAction(activity, context, Logging.ACTION_LOGIN, Logging.TYPE_USER);
                                startActivity(intent);
                            }else{
                                Helper.common.toastLong(activity, "The account you are trying to use is not available for this application.");
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Helper.common.dialogThemedOkOnly(activity, "Error", error.toString(), "OK", R.color.red);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceid", Helper.common.getMacAddress(activity));
                params.put("username", txtusername.getText().toString());
                params.put("password", txtpassword.getText().toString());

                return params;
            }
        };

        // Adding request to request queue
        MyVolleyAPI api = new MyVolleyAPI();
        api.addToReqQueue(postRequest, Activity_LoginScreen.this);

    }

}//end of class
