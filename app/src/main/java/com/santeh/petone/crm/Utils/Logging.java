package com.santeh.petone.crm.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.santeh.petone.crm.DBase.DB_Query_PetOneCRM;

/**
 * Created by rjhonsl on 9/10/2015.
 */
public class Logging {

    static boolean isRecorded = false;
    static FusedLocation fusedLocation;
    static DB_Query_PetOneCRM db;

    public static String ACTION_ADD = "ADD";
    public static String ACTION_EDIT = "EDIT";
    public static String ACTION_DELETE = "DELETE";

    public static String ACTION_LOGIN = " LOGIN";

    public static String TYPE_ADMIN_TESTING = "ADMIN_TEST";
    public static String TYPE_USER = " USER ";

    /** Action Related to Database **/
    public static boolean userAction(final Activity activity, final Context context, final String userDBAction, String rowID , String tableName, final String actionType) {
        fusedLocation = new FusedLocation(context, activity);
        fusedLocation.buildGoogleApiClient(context);
        fusedLocation.connectToApiClient();
        db = new DB_Query_PetOneCRM(context);
        final String action = userDBAction + " " + rowID + " on " + tableName;



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logging.InsertUserActivity(Helper.variables.getGlobalVar_currentUserID(activity) + "",
                        action, actionType,
                        fusedLocation.getLastKnowLocation().latitude + "", fusedLocation.getLastKnowLocation().longitude + "");
            }
        }, 500);

        return true;
    }

    /** Action NOT Related to Database **/
    public static boolean userAction(final Activity activity, final Context context, final String userDBAction, final String actionType) {
        fusedLocation = new FusedLocation(context, activity);
        fusedLocation.buildGoogleApiClient(context);
        fusedLocation.connectToApiClient();
        db = new DB_Query_PetOneCRM(context);
        final String action = userDBAction;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logging.InsertUserActivity(Helper.variables.getGlobalVar_currentUserID(activity) + "",
                        action, actionType,
                        fusedLocation.getLastKnowLocation().latitude + "", fusedLocation.getLastKnowLocation().longitude + "");
            }
        }, 500);

        return true;
    }



    private static boolean InsertUserActivity(final String userid, final String action, final String actiontype, final String latitude, final String longitude){
        db.open();
        db.insertUserActivityData(Integer.parseInt(userid), action, fusedLocation.getLastKnowLocation().latitude+"", fusedLocation.getLastKnowLocation().longitude+"", System.currentTimeMillis()+"", actiontype);

//        if (Helper.variables.getGlobalVar_currentLevel(activity) != 4 && Helper.isNetworkAvailable(context)){
//            StringRequest postRequest = new StringRequest(Request.Method.POST,
//                    Helper.variables.URL_INSERT_USER_ACTIVITY,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            if (!response.substring(1,2).equalsIgnoreCase("0")) {
//                                Log.d("USER LOGGING", "Activity logging for user " + userid + " SUCEEDED. " + response);
//                                isRecorded = true;
//                            }
//                            else{
//                                Log.d("USER LOGGING", "Activity logging for user "+userid+" FAILED. " + response);
////                                InsertUserActivity(activity, context, userid, action, actiontype, latitude, longitude);
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("USER LOGGING", "Activity logging for user "+userid+" FAILED. " + error);
//                    InsertUserActivity(activity, context, userid, action, actiontype, latitude, longitude);
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//
//                    params.put("userid", String.valueOf(userid));
//                    params.put("action", String.valueOf(action));
//                    params.put("actiontype", String.valueOf(actiontype));
//                    params.put("latitude", fusedLocation.getLastKnowLocation().latitude+"");
//                    params.put("longitude", fusedLocation.getLastKnowLocation().longitude+"");
//
//                    return params;
//                }
//            };
//
//            // Adding request to request queue
//            MyVolleyAPI api = new MyVolleyAPI();
//            api.addToReqQueue(postRequest, context);
//        }
        return isRecorded;
    }
}