package com.santeh.petone.crm.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.santeh.petone.crm.DBase.DB_Helper_PetOneCRM;
import com.santeh.petone.crm.DBase.DB_Query_PetOneCRM;
import com.santeh.petone.crm.Obj.Var;
import com.santeh.petone.crm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rjhonsl on 12/9/2015.
 */
public class Helper {


    public static class variables{

        public static String sourceAddress_goDaddy              = "http://santeh-webservice.com/php/android_json_post/";


        public static String URL_LOGIN                                  = sourceAddress_goDaddy + "login.php";
        public static String URL_PHP_RAW_QUERY_POST_INSERT              = sourceAddress_goDaddy + "insertSyncFarmInfo.php";
        public static String URL_SELECT_CLIENTINFO_BY_USERID            = sourceAddress_goDaddy + "petone_select_clientinfo_by_userid.php";
        public static String URL_SELECT_CLIENTUPDATE_BY_USERID          = sourceAddress_goDaddy + "petone_select_clientupdate_by_userid.php";
        public static String URL_SELECT_USERACTIVITY_BY_USERID          = sourceAddress_goDaddy + "petone_select_useractivity_by_userid.php";

        public static String ACTIVITY_CLIENTUPDATES             = "updates";
        public static String ACTIVITY_UNSYCED_CLIENTINFO        = "unsyced_clientinfo";
        public static String ACTIVITY_UNSYCED_CLIENT_UPDATES    = "unsyced_clientupdates";
        public static String ACTIVITY_MapsActivity              = "mapsactivity";



        public static void setGlobalVar_currentUserID(int ID, Activity activity) {
            DB_Query_PetOneCRM db = new DB_Query_PetOneCRM(activity);
            db.open();

            db.updateOneRow(
                    DB_Helper_PetOneCRM.TBL_USERS,
                    DB_Helper_PetOneCRM.CL_USERS_ID,
                    ID + "",
                    DB_Helper_PetOneCRM.CL_USERS_ID + " = "+ID);

            ((Var) activity.getApplication()).setCurrentuser(ID);
        }


        public static int getGlobalVar_currentUserID( Activity activity ){

//
//            DB_Query_PetOneCRM db = new DB_Query_PetOneCRM(activity);
//            db.open();
//
//            String queried  = db.getOneRow(
//                    DB_Helper_PetOneCRM.TBL_USERS,
//                    DB_Helper_PetOneCRM.CL_USERS_ID,
//                    DB_Helper_PetOneCRM.CL_USERS_isloggedin + " = " + 1);
//
            return ((Var) activity.getApplication()).getCurrentuser();
//            return Integer.parseInt(queried);

        }


        public static void setGlobalVar_currentlevel(int lvl, Activity activity){
            ((Var) activity.getApplication()).setCurrentuserLvl(lvl);
        }


        public static int getGlobalVar_currentLevel(Activity activity){
            return  ((Var) activity.getApplication()).getCurrentuserLvl();
        }


        public static String getGlobalVar_currentUserName(Activity activity){
            return  ((Var) activity.getApplication()).getCurrentUserName();
        }

        public static void setGlobalVar_currentUsername(String username, Activity activity ){
            ((Var) activity.getApplication()).setCurrentUserName(username);
        }


        public static String getGlobalVar_currentUserFirstname( Activity activity ){
            return  ((Var) activity.getApplication()).getCurrentUserFirstname();
        }


        public static String getGlobalVar_DateAdded( Activity activity ){
            return  ((Var) activity.getApplication()).getDateAddedToDB();
        }

        public static void setGlobalVar_DateAddedToDb(String date, Activity activity ){
            ((Var) activity.getApplication()).setDateAddedToDB(date);
        }


        public static void setGlobalVar_currentFirstname(String firstname, Activity activity ){
            ((Var) activity.getApplication()).setCurrentUserFirstname(firstname);
        }


        public static String getGlobalVar_currentUserLastname( Activity activity ){
            return  ((Var) activity.getApplication()).getCurrentUserLastname();
        }

        public static void setGlobalVar_currentLastname(String lastname, Activity activity ){
            ((Var) activity.getApplication()).setCurrentUserLastname(lastname);
        }


        public static String getGlobalVar_currentUserPassword(Activity activity){
            return  ((Var) activity.getApplication()).getCurrentPassword();
        }

        public static void setGlobalVar_currentUserpassword(String password, Activity activity ){
            ((Var) activity.getApplication()).setCurrentPassword(password);
        }


        public static String getGlobalVar_currentAssignedArea( Activity activity ){
            return  ((Var) activity.getApplication()).getAssignedArea();
        }

        public static void setGlobalVar_currentAssignedArea(String area, Activity activity ){
            ((Var) activity.getApplication()).setAssignedArea(area);
        }

        public static int getGlobalVar_currentisActive( Activity activity ){
            return  ((Var) activity.getApplication()).getIsactive();
        }

        public static void setGlobalVar_currentIsActive(int isactive, Activity activity ){
            ((Var) activity.getApplication()).setIsactive(isactive);
        }

        public static String getGlobalVar_currentDeviceID(Activity activity){
            return  ((Var) activity.getApplication()).getDeviceID();
        }

        public static void setGlobalVar_deviceID(String deviceID, Activity activity ){
            ((Var) activity.getApplication()).setDeviceID(deviceID);
        }


    }


    public static class timeConvert{

        public static String longtoDateTime_DB_Format(long dateInMillis){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateInMillis);
            return formatter.format(calendar.getTime());
        }


        public static String longtoDateTime_StringFormat(long dateInMillis){
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy hh:mm aa");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateInMillis);
            return formatter.format(calendar.getTime());
        }

        public static String longtoTime_StringFormat(long dateInMillis){
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateInMillis);
            return formatter.format(calendar.getTime());
        }



        public static String longtoDate_StringFormat(long dateInMillis){
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateInMillis);
            return formatter.format(calendar.getTime());
        }

        public static String longtoDate_ShortenedStringFormat(long dateInMillis){
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateInMillis);
            return formatter.format(calendar.getTime());
        }

    }




    public static class map {


        public static float getDifference(LatLng center, LatLng touchLocation ){

            float[] results = new float[1];
            Location.distanceBetween(center.latitude, center.longitude,
                    touchLocation.latitude, touchLocation.longitude, results);
            return results[0];
        }


        public static Marker addMarker(GoogleMap map, LatLng latlng, int iconResID,
                                       final String clientName, String address, String id){

            String snippet = id+"~"+address;
            Marker marker = map.addMarker(new MarkerOptions()
                    .title(clientName)
                    .icon(BitmapDescriptorFactory.fromResource(iconResID))
                    .snippet(snippet)
                    .position(latlng)
                    .draggable(false)
            );
            return marker;
        }

        public  static boolean isLocationEnabled(Context context) {
            int locationMode = 0;
            String locationProviders;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                try {
                    locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }

                return locationMode != Settings.Secure.LOCATION_MODE_OFF;

            }else{
                locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                return !TextUtils.isEmpty(locationProviders);
            }
        }


        public static void moveCameraAnimate(final GoogleMap map, final LatLng latlng, final int zoom) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
        }


        public static CircleOptions addCircle(Activity activity, LatLng latlng, float strokeWidth, int strokeColor, int fillColor, int radius){

            CircleOptions circleOptions = new  CircleOptions()
                    .center(new LatLng(latlng.latitude, latlng.longitude))
                    .radius(radius)
                    .strokeColor(activity.getResources().getColor(strokeColor))
                    .fillColor(fillColor)
                    .strokeWidth(strokeWidth);
            return circleOptions;
        }


        public static void isLocationAvailablePrompt(final Context context, Activity activity){
            LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            if(!gps_enabled) {
                final Dialog d = common.dialogThemedYesNO(activity, "Location services is needed to use this application. Please turn on Location in settings", "GPS Service", "OK", "No", R.color.red);
                Button b1 = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                Button b2 = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);

                b2.setVisibility(View.GONE);
                d.setCancelable(false);
                d.show();

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.hide();
                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(gpsOptionsIntent);

                    }
                });

            }
        }


    }/////////////////END OF MAP//////////////////////




    public static class common {

        public static boolean isNetworkAvailable(Context context) {

            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }


        public static Dialog dialogThemedYesNO(Activity activity, String prompt, String title, String strButton1, String strButton2, int resIdColor){
            final Dialog d = new Dialog(activity);//
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_yesno);//Set the xml view of the dialog
            Button btn1 = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
            Button btn2 = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
            TextView txttitle = (TextView) d.findViewById(R.id.dialog_yesno_title);
            TextView txtprompt = (TextView) d.findViewById(R.id.dialog_yesno_prompt);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.hide();
                }
            });

            txtprompt.setText(prompt);
            txttitle.setText(title);
            txttitle.setBackground(activity.getResources().getDrawable(resIdColor));
            btn1.setText(strButton1);
            btn2.setText(strButton2);
            d.show();

            return d;
        }

        public static Dialog dialogList(Activity activity, String[] options, String title){
            final Dialog d = new Dialog(activity);//
            d.requestWindowFeature(Window.FEATURE_NO_TITLE); //notitle
            d.setContentView(R.layout.dialog_list);//Set the xml view of the dialog

            ListView listview = (ListView) d.findViewById(R.id.dialog_list_listview);
            ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(activity, R.layout.select_dialog_item_material, options); //selected item will look like a spinner set from XML
            listViewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listview.setAdapter(listViewAdapter);

            TextView txtTitle = (TextView) d.findViewById(R.id.dialog_okonly_title);
            txtTitle.setText(title);
            d.show();
            return d;
        }

        public static Dialog dialogThemedList(Activity activity, String[] options, String title, int resIdColor){
            final Dialog d = new Dialog(activity);//
            d.requestWindowFeature(Window.FEATURE_NO_TITLE); //notitle
            d.setContentView(R.layout.dialog__list);//Set the xml view of the dialog

            ListView listview = (ListView) d.findViewById(R.id.dialog_list_listview);
            ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(activity, R.layout.select_dialog_item_material, options); //selected item will look like a spinner set from XML
            listViewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listview.setAdapter(listViewAdapter);

            TextView txtTitle = (TextView) d.findViewById(R.id.dialog_okonly_title);
            txtTitle.setBackground(activity.getResources().getDrawable(resIdColor));
            txtTitle.setText(title);
            d.show();
            return d;
        }


        public static void toastShort(Activity activity, String msg){
            LayoutInflater inflater = activity.getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast,
                    (ViewGroup) activity.findViewById(R.id.toast_layout_root));

            TextView text = (TextView) layout.findViewById(R.id.text);
            Typeface font = Typeface.createFromAsset(activity.getAssets(), "Roboto-Light.ttf");
            text.setTypeface(font);
            text.setText(msg);

            Toast toast = new Toast(activity.getApplicationContext());
            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setMargin(0, 0);
            toast.setView(layout);
            toast.show();
        }

        public static void setCursorOnEnd(EditText edt) {
            edt.setSelection(edt.getText().length());
        }

        public static void toastLong(Activity context, String msg){
            LayoutInflater inflater = context.getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast,
                    (ViewGroup) context.findViewById(R.id.toast_layout_root));

            TextView text = (TextView) layout.findViewById(R.id.text);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
            text.setTypeface(font);
            text.setText(msg);

            Toast toast = new Toast(context.getApplicationContext());
            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setMargin(0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }


        public static Dialog dialogThemedOkOnly(Activity activity, String title, String prompt, String button, int resIdColor){
            final Dialog d = new Dialog(activity);//
            d.requestWindowFeature(Window.FEATURE_NO_TITLE); //notitle
            d.setContentView(R.layout.dialog_okonly);//Set the xml view of the dialog
            TextView txttitle = (TextView) d.findViewById(R.id.dialog_okonly_title);
            TextView txtprompt = (TextView) d.findViewById(R.id.dialog_okonly_prompt);
            Button txtok = (Button) d.findViewById(R.id.btn_dialog_okonly_OK);
            txtok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.hide();
                }
            });
            txtprompt.setMovementMethod(new ScrollingMovementMethod());
            txttitle.setBackground(activity.getResources().getDrawable(resIdColor));
            txtok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.hide();
                }
            });
            txtprompt.setText(prompt);
            txttitle.setText(title+"   ");
            txtok.setText(button);
            d.show();
            return d;
        }


        public static Dialog dialogYesNoWithEditTExt(Activity activity, String prompt, String edtEntry, String title, String strButton1, String strButton2, int resIdColor){
            final Dialog d = new Dialog(activity);//
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_yesno_with_edittext);//Set the xml view of the dialog
            Button btn1 = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
            Button btn2 = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
            EditText edt = (EditText) d.findViewById(R.id.dialog_edttext);
            TextView txttitle = (TextView) d.findViewById(R.id.dialog_yesno_title);
            TextView txtprompt = (TextView) d.findViewById(R.id.dialog_yesno_prompt);

            txtprompt.setText(prompt);
            edt.setText(edtEntry);
            txttitle.setText(title);
            txttitle.setBackground(activity.getResources().getDrawable(resIdColor));
            btn1.setText(strButton1);
            btn2.setText(strButton2);
            d.show();
            return d;
        }


        public static Dialog dialogYesNoWithMultiLineEditText(Activity activity, String prompt, String edtEntry, String title, String strButton1, String strButton2, int resIdColor){
            final Dialog d = new Dialog(activity);//
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_yesno_edittext_multiline);//Set the xml view of the dialog
            Button btn1 = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
            Button btn2 = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
            EditText edt = (EditText) d.findViewById(R.id.dialog_edttext);
            TextView txttitle = (TextView) d.findViewById(R.id.dialog_yesno_title);
            TextView txtprompt = (TextView) d.findViewById(R.id.dialog_yesno_prompt);

            txtprompt.setText(prompt);
            edt.setText(edtEntry);
            txttitle.setText(title);
            txttitle.setBackground(activity.getResources().getDrawable(resIdColor));
            btn1.setText(strButton1);
            btn2.setText(strButton2);
            d.show();
            return d;
        }

        public static void hideKeyboardOnLoad(Activity activity){
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }


        public static String getMacAddress(Context context){
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            return info.getMacAddress();
        }


    }///////////////////////END OF COMMON//////////////////////


    public static class random {

        public static Dialog initProgressDialog(Activity activity){
            Dialog PD = new Dialog(activity);
            PD.requestWindowFeature(Window.FEATURE_NO_TITLE);
            PD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            PD.setCancelable(false);
            PD.setContentView(R.layout.progressdialog);
            return  PD;
        }

        public static String trimFirstAndLast(String string){
            String trimmed = "";

            trimmed = string.substring(1,string.length() );
            return  trimmed = trimmed.substring(0, trimmed.length() - 1);
        }

        public static String[] splitter(String splitter, String strToSplit){
           return strToSplit.split(splitter);
        }


        public static boolean checkSD(Activity activity) {
            Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
            if(isSDPresent)
            {
//			toaster_long(activity, "SDcard available");
            }
            else
            {
//			toaster_long(activity, "SDcard not available");
            }
            return isSDPresent;
        }


    }

    public static class nullcheck{
        public static boolean isGlobalUserIDNull(Activity activity){
            boolean isEmpty = false;
            if(variables.getGlobalVar_currentUserID(activity) <= 0){
                isEmpty = true;
            }
            return  isEmpty;
        }
    }

    public static class activityChooser {

        public static void startActivityClearStack(Activity currentActivity, Class nextActivity) {
            Intent intent = new Intent(currentActivity, nextActivity);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            currentActivity.startActivity(intent);
        }
    }



    public static class DBase{

        public static void cleartablesforRestore(DB_Query_PetOneCRM db, Context context) {
            db = new DB_Query_PetOneCRM(context);
            db.open();

            db.emptyTable(DB_Helper_PetOneCRM.TBL_UPDATES);
            db.emptyTable(DB_Helper_PetOneCRM.TBL_CLIENTINFO);
            db.emptyTable(DB_Helper_PetOneCRM.TBL_USERS);
            db.emptyTable(DB_Helper_PetOneCRM.TBL_USER_ACTIVITY);
        }

    }

}
