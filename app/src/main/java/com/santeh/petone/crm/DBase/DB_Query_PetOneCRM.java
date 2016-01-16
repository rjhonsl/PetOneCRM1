package com.santeh.petone.crm.DBase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.santeh.petone.crm.Utils.Helper;


public class DB_Query_PetOneCRM {

	private static final String LOGTAG = "GPS";
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase db;

	/********************************************
	 * 				DEFAULTS					*
	 ********************************************/
	public DB_Query_PetOneCRM(Context context){
		//Log.d("DBSource", "db connect");
		dbhelper = new DB_Helper_PetOneCRM(context);
		//opens the db connection
	}

	public void open(){
		//Log.d("DBSource", "db open");
		db = dbhelper.getWritableDatabase();
	}
	public void close(){
		//Log.d("DBSource", "db close");
		dbhelper.close();
	}



	/********************************************
	 * 				INSERTS						*
	 * 	returns index/rowNum of inserted values *
	 ********************************************/


	public long insertClientInfo(String lat, String lng, String address, String clientName, String custCode, String contactNumber, String dateAdded, String addedBy){

		ContentValues values = new ContentValues();
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_LAT, lat);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_LNG, lng);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS, address);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME, clientName);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE, custCode);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER, contactNumber);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded, System.currentTimeMillis());
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby, addedBy);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_IsPosted, 0);

		return  db.insert(DB_Helper_PetOneCRM.TBL_CLIENTINFO, null, values);
	}


	public long insertClientInfo(String id, String lat, String lng, String address, String clientName, String custCode, String contactNumber, String dateAdded, String addedBy, int isPosted){

		ContentValues values = new ContentValues();
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_ID, id);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_LAT, lat);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_LNG, lng);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS, address);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME, clientName);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE, custCode);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER, contactNumber);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded, dateAdded);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby, addedBy);
		values.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_IsPosted, isPosted);

		return  db.insert(DB_Helper_PetOneCRM.TBL_CLIENTINFO, null, values);
	}

	public long insertClientUpdates(String remarks, String clientId){

		ContentValues values = new ContentValues();
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID, clientId);
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_REMARKS, remarks);
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED, System.currentTimeMillis());
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_isposted, 0);

		return  db.insert(DB_Helper_PetOneCRM.TBL_UPDATES, null, values);
	}


	public long insertClientUpdates(String id, String remarks, String clientId, String dateAdded, int isposted){

		ContentValues values = new ContentValues();
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_ID, id);
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID, clientId);
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_REMARKS, remarks);
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED, dateAdded);
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_isposted, isposted);

		return  db.insert(DB_Helper_PetOneCRM.TBL_UPDATES, null, values);
	}

	public long insertAdminAccount(Context context){
		ContentValues values = new ContentValues();
		values.put(DB_Helper_PetOneCRM.CL_USERS_userlvl, 0);
		values.put(DB_Helper_PetOneCRM.CL_USERS_username, "jhonar10");
		values.put(DB_Helper_PetOneCRM.CL_USERS_password, "10");
		values.put(DB_Helper_PetOneCRM.CL_USERS_firstName, "Jhonar");
		values.put(DB_Helper_PetOneCRM.CL_USERS_lastName, "Lajom");
		values.put(DB_Helper_PetOneCRM.CL_USERS_deviceid, Helper.common.getMacAddress(context));
		values.put(DB_Helper_PetOneCRM.CL_USERS_dateAdded, System.currentTimeMillis());
		values.put(DB_Helper_PetOneCRM.CL_USERS_isactive, 1);

		return  db.insert(DB_Helper_PetOneCRM.TBL_USERS, null, values);
	}

	public long insertUserActivityData(int userid, String actiondone, String lat, String lng, String millis, String actionType){
		ContentValues values = new ContentValues();
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_USERID, userid);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONDONE, actiondone);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LAT, lat);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LNG, lng);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_DATETIME, millis);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONTYPE, actionType);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_isPosted, "0");

		return  db.insert(DB_Helper_PetOneCRM.TBL_USER_ACTIVITY, null, values);
	}


	public long insertUserActivityData(String id, String userid, String actiondone, String lat, String lng, String millis, String actionType){
		ContentValues values = new ContentValues();
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ID, id);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_USERID, userid);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONDONE, actiondone);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LAT, lat);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LNG, lng);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_DATETIME, millis);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONTYPE, actionType);
		values.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_isPosted, "1");

		return  db.insert(DB_Helper_PetOneCRM.TBL_USER_ACTIVITY, null, values);
	}




	public void insertUserAccountInfo(int userid, int userlvl, String firstname, String lastname, String username, String password, String deviceID, String dateAdded, int isActive){
		ContentValues values = new ContentValues();
		values.put(DB_Helper_PetOneCRM.CL_USERS_ID, userid);
		values.put(DB_Helper_PetOneCRM.CL_USERS_userlvl, userlvl);
		values.put(DB_Helper_PetOneCRM.CL_USERS_lastName, lastname);
		values.put(DB_Helper_PetOneCRM.CL_USERS_firstName, firstname);
		values.put(DB_Helper_PetOneCRM.CL_USERS_username, username);
		values.put(DB_Helper_PetOneCRM.CL_USERS_password, password);
		values.put(DB_Helper_PetOneCRM.CL_USERS_deviceid, deviceID);
		values.put(DB_Helper_PetOneCRM.CL_USERS_dateAdded, dateAdded );
		values.put(DB_Helper_PetOneCRM.CL_USERS_isactive, isActive);
		db.insert(DB_Helper_PetOneCRM.TBL_USERS, null, values);
	}



	/********************************************
	 * 				VALIDATIONS					*
	 ********************************************/


	/********************************************
	 * 				SELECTS						*
	 ********************************************/

	public boolean isUserExisting(String userID){
		String query = "SELECT * FROM "+DB_Helper_PetOneCRM.TBL_USERS+" WHERE "+DB_Helper_PetOneCRM.CL_USERS_ID+" = ?;";
		String[] params = new String[] {userID};
		Cursor cur = db.rawQuery(query, params);
		boolean isExisting = false;

		if (cur.getCount() > 0) {
			isExisting = true;
		}

		return isExisting;
	}

	public Cursor getNotPosted_ClientInfo(Activity activity){

		String query = "SELECT * FROM "+DB_Helper_PetOneCRM.TBL_CLIENTINFO+" WHERE "
				+ DB_Helper_PetOneCRM.CL_CLIENTINFO_IsPosted + " = 0 AND "
				+ DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby + " = " + Helper.variables.getGlobalVar_currentUserID(activity)
				;
		String[] params = new String[] {};
		return db.rawQuery(query, params);
	}


	public Cursor getNotPosted_Updates(Activity activity){

		String query = "SELECT * FROM [SALES.PETONE.CRM.UPDATES] "
				+ "INNER JOIN [SALES.PETONE.CRM.CLIENTINFO] ON "
				+ "[SALES.PETONE.CRM.CLIENTINFO].ci_customerId = [SALES.PETONE.CRM.UPDATES].updates_clientid "
				+ "WHERE [SALES.PETONE.CRM.CLIENTINFO].ci_addedby = "+ Helper.variables.getGlobalVar_currentUserID(activity)  + " AND "
				+ DB_Helper_PetOneCRM.CL_UPDATES_isposted + " = 0 "
				+ "ORDER BY " + DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME + " ASC"
				;
		String[] params = new String[] {};
		return db.rawQuery(query, params);
	}

	public Cursor getNotPosted_userActivity(Activity activity){

		String query = "SELECT * FROM[SALES.PETONE.CRM.USERS.ACTIVITY] "
		+ " WHERE [SALES.PETONE.CRM.USERS.ACTIVITY] = "+ Helper.variables.getGlobalVar_currentUserID(activity);
		String[] params = new String[] {};
		return db.rawQuery(query, params);
	}

	public int getUser_Count() {
		String query = "SELECT * FROM "+ DB_Helper_PetOneCRM.TBL_USERS+";";
		String[] params = new String[] {};
		Cursor cur = db.rawQuery(query, params);
		return cur.getCount();
	}



	public boolean isCustCodeExisting(String custCode){
		boolean isexisting = false;
		String query = "SELECT * FROM "+ DB_Helper_PetOneCRM.TBL_CLIENTINFO +" WHERE "
				+ DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE + " = ? "
				;
		String[] params = new String[] {custCode};
		Cursor cur = db.rawQuery(query, params);
		if (cur!=null){
			if (cur.getCount() > 0){
				isexisting = true;
			}
		}
		return  isexisting;
	}



	public boolean isClientUpdatePosted(String clientID){
		boolean isexisting = false;
		String query = "SELECT * FROM "+ DB_Helper_PetOneCRM.TBL_UPDATES +" WHERE "
				+ DB_Helper_PetOneCRM.CL_UPDATES_ID + " = ? "
				+ " AND "
				+ DB_Helper_PetOneCRM.CL_UPDATES_isposted + " = 1 "
				;

		String[] params = new String[] {clientID};
		Cursor cur = db.rawQuery(query, params);
		if (cur!=null){
			if (cur.getCount() > 0){
				isexisting = true;
			}
		}
		return  isexisting;
	}


	public boolean isClientInfoPosted(String clientID){
		boolean isexisting = false;
		String query = "SELECT * FROM "+ DB_Helper_PetOneCRM.TBL_CLIENTINFO +" WHERE "
				+ DB_Helper_PetOneCRM.CL_CLIENTINFO_ID + " = ? "
				+ " AND "
				+ DB_Helper_PetOneCRM.CL_CLIENTINFO_IsPosted + " = 1 "
				;

		String[] params = new String[] {clientID};
		Cursor cur = db.rawQuery(query, params);
		if (cur!=null){
			if (cur.getCount() > 0){
				isexisting = true;
			}
		}
		return  isexisting;
	}

	public Cursor getClientInfoByUserID(String userID){
		String query = "SELECT * FROM "+ DB_Helper_PetOneCRM.TBL_CLIENTINFO+" WHERE "
				+ DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby + " = ? "
				;


		String[] params = new String[] {userID};
		return db.rawQuery(query, params);
	}



	public Cursor getClientByClientID(String clientID){
		String query = "SELECT * FROM "+ DB_Helper_PetOneCRM.TBL_CLIENTINFO+" WHERE "
				+ DB_Helper_PetOneCRM.CL_CLIENTINFO_ID + " = ? "
				;


		String[] params = new String[] {clientID};
		return db.rawQuery(query, params);
	}



	public Cursor getUserIdByLogin(String username, String password, String deviceid){
		String query = "SELECT * FROM "+ DB_Helper_PetOneCRM.TBL_USERS+" WHERE "
				+ DB_Helper_PetOneCRM.CL_USERS_username + " = ? AND "
				+ DB_Helper_PetOneCRM.CL_USERS_password + " = ? AND "
				+ DB_Helper_PetOneCRM.CL_USERS_deviceid + " = ? "
				;
		String[] params = new String[] {username, password, deviceid };
		return db.rawQuery(query, params);
	}


	public Cursor getClientUpdateByID(String clientID){
		String query = "SELECT * FROM "+ DB_Helper_PetOneCRM.TBL_UPDATES+" WHERE "
				+ DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID + " = ? "
//				+ "ORDER BY " + DB_Helper_PetOneCRM.CL_UPDATES_ID + " DESC"
				;


		String[] params = new String[] {clientID};
		return db.rawQuery(query, params);
	}

	public Cursor getClientUpdate_by_userID(Activity activity) {
		String query = "SELECT * FROM [SALES.PETONE.CRM.UPDATES] "
				+ "INNER JOIN [SALES.PETONE.CRM.CLIENTINFO] ON "
				+ "[SALES.PETONE.CRM.CLIENTINFO].ci_customerId = [SALES.PETONE.CRM.UPDATES].updates_clientid "
				+ "WHERE [SALES.PETONE.CRM.CLIENTINFO].ci_addedby = "+ Helper.variables.getGlobalVar_currentUserID(activity)  + " "
				+ "ORDER BY " + DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME + " ASC"
				;


		return db.rawQuery(query, null);
	}



	public Cursor getUserActivity_by_userID(Activity activity) {
		String query = "SELECT * FROM `"+Helper.random.trimFirstAndLast(DB_Helper_PetOneCRM.TBL_USER_ACTIVITY)+"` "
				+ "ORDER BY " + DB_Helper_PetOneCRM.CL_USER_ACTIVITY_DATETIME + " ASC"
				;


		return db.rawQuery(query, null);
	}



	public String getSQLStringForInsert_UNPOSTED_CustomerINFO(Activity activity, int[] selectedID) {


		String whereSelected = "";
		for (int i = 0; i < selectedID.length; i++) {
			String condition = "OR";
			if (selectedID.length-1 == i) {
				condition = "AND";
			}else{
				condition = "OR";
			}
			whereSelected = whereSelected +  DB_Helper_PetOneCRM.CL_CLIENTINFO_ID + " = "+ selectedID[i] + " " + condition + " ";
		}


		String sqlString = "" +
				"INSERT INTO `"+Helper.random.trimFirstAndLast(DB_Helper_PetOneCRM.TBL_CLIENTINFO)+"` " +
				"(`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_ID +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_LAT +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_LNG +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_CLIENTINFO_localid+"`)  VALUES ";


		String query = "SELECT * FROM " + DB_Helper_PetOneCRM.TBL_CLIENTINFO + " WHERE "
				+ whereSelected
				+ DB_Helper_PetOneCRM.CL_CLIENTINFO_IsPosted + " = 0 AND " +
				DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby + " = " + Helper.variables.getGlobalVar_currentUserID(activity);


		String[] params = new String[]{};
		Cursor cur = db.rawQuery(query, null);
		String ci_id="", ci_lat="", ci_long="", ci_address="", ci_clientName="", ci_custcode="", ci_contactNumber="", ci_dateadded="", ci_addedby="", ci_localid="";
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				ci_id = Helper.variables.getGlobalVar_currentUserID(activity) + "-" + cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ID)).replaceAll("'", "\\'");
				ci_lat = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_LAT)).replaceAll("'", "\\'");
				ci_long = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_LNG)).replaceAll("'", "\\'");
				ci_address = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS)).replaceAll("'", "\\'");
				ci_clientName = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME)).replaceAll("'", "\\'");
				ci_custcode = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE)).replaceAll("'", "\\'");
				ci_contactNumber = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER)).replaceAll("'", "\\'");
				ci_dateadded = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded)).replaceAll("'", "\\'");
				ci_addedby = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby)).replaceAll("'", "\\'");
				ci_localid = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ID)).replaceAll("'", "\\'");

				sqlString = sqlString +
						"( '"+ci_id+"',  " +
						"'"+ci_lat+"',  " +
						"'"+ci_long+"',  " +
						"'"+ci_address+"',  " +
						"'"+ci_clientName+"',  " +
						"'"+ci_custcode+"',  " +
						"'"+ci_contactNumber+"',  " +
						"'"+ci_dateadded+"',  " +
						"'"+ci_addedby+"',  " +
						"'"+ci_localid+"' ),";
			}
		}

		String strSql = sqlString.substring(0, sqlString.length() - 1);
		strSql = strSql + " " +
				" ON DUPLICATE KEY UPDATE " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_ID+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_ID+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_LAT+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_LAT+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_LNG+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_LNG+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_CLIENTINFO_localid+" = VALUES("+DB_Helper_PetOneCRM.CL_CLIENTINFO_localid+")"
		;

		return strSql;
	}



	public String getSQLStringForInsert_UNPOSTED_Activities(Activity activity) {


		String sqlString = "" +
				"INSERT INTO `"+Helper.random.trimFirstAndLast(DB_Helper_PetOneCRM.TBL_USER_ACTIVITY)+"` " +
				"(`"+ DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ID +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_USER_ACTIVITY_USERID +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONDONE +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LAT +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LNG +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_USER_ACTIVITY_DATETIME+"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONTYPE+"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_USER_ACTIVITY_localID+"`)  VALUES ";


		String query = "SELECT * FROM `SALES.PETONE.CRM.USERS.ACTIVITY` " +
				"WHERE " +
				"user_act_isposted = 0";


		Cursor cur = db.rawQuery(query, null);
		String user_id = "", user_userid ="", user_actiondone ="", user_lat="", user_long="", user_datetime ="",user_actiontype="", user_localId;
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				user_id = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ID)).replaceAll("'", "\\'");
				user_userid = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_USERID)).replaceAll("'", "\\'");
				user_actiondone = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONDONE)).replaceAll("'", "\\'");
				user_lat = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LAT)).replaceAll("'", "\\'");
				user_long = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LNG)).replaceAll("'", "\\'");
				user_datetime = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_DATETIME)).replaceAll("'", "\\'");
				user_actiontype = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONTYPE)).replaceAll("'", "\\'");
				user_localId = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ID)).replaceAll("'", "\\'");

				sqlString = sqlString +
						"( '"+user_id+"-"+user_userid+"',  " +
						"'"+user_userid+"',  " +
						"'"+user_actiondone+"',  " +
						"'"+user_lat+"',  " +
						"'"+user_long+"',  " +
						"'"+user_datetime+"',  " +
						"'"+user_actiontype+"',  " +
						"'"+user_localId+"' ),";
			}
		}

		String strSql = sqlString.substring(0, sqlString.length() - 1);
		strSql = strSql + " " +
				" ON DUPLICATE KEY UPDATE " +
				" 	 "+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ID+" = VALUES("+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ID+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_USERID+" = VALUES("+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_USERID+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONDONE+" = VALUES("+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_ACTIONDONE+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LAT+" = VALUES("+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LAT+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LNG+" = VALUES("+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_LNG+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_DATETIME+" = VALUES("+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_DATETIME+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_localID+" = VALUES("+DB_Helper_PetOneCRM.CL_USER_ACTIVITY_localID+") "
		;

		return strSql;
	}



	public String getSQLStringForInsert_UNPOSTED_CustomerUPDATES(Activity activity, int[] selectedID) {


		String whereSelected = "";
		for (int i = 0; i < selectedID.length; i++) {
			String condition = "OR";
			if (selectedID.length-1 == i) {
				condition = "AND";
			}else{
				condition = "OR";
			}
			whereSelected = whereSelected +  DB_Helper_PetOneCRM.CL_UPDATES_ID + " = "+ selectedID[i] + " " + condition + " ";
		}

		String sqlString = "" +
				"INSERT INTO `"+Helper.random.trimFirstAndLast(DB_Helper_PetOneCRM.TBL_UPDATES)+"` " +
				"(`"+ DB_Helper_PetOneCRM.CL_UPDATES_ID +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_UPDATES_REMARKS +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED +"`, " +
				"`"+ DB_Helper_PetOneCRM.CL_UPDATES_localID+"`)  VALUES ";


		String query = "SELECT * FROM [SALES.PETONE.CRM.UPDATES] "
				+ "INNER JOIN [SALES.PETONE.CRM.CLIENTINFO] ON "
				+ "[SALES.PETONE.CRM.CLIENTINFO].ci_customerId = [SALES.PETONE.CRM.UPDATES].updates_clientid "
				+ "WHERE "
				+ whereSelected
				+ " [SALES.PETONE.CRM.CLIENTINFO].ci_addedby = "+ Helper.variables.getGlobalVar_currentUserID(activity)
				+ " "
//				+ "ORDER BY " + DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME + " ASC"
				;


		Log.d("query", query);


		String[] params = new String[]{};
		Cursor cur = db.rawQuery(query, null);
		String upd_id= "", upd_remarks="", upd_clientid="", upd_dateAdded="", upd_localid="";

		Log.d("query", "before if");
		if (cur != null) {
			Log.d("query", "before if not null");
			if (cur.getCount() > 0) {
				Log.d("query", "before if not zero " + cur.getCount());
				while (cur.moveToNext()) {
					upd_id = Helper.variables.getGlobalVar_currentUserID(activity) + "-" + cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_ID)).replaceAll("'", "\\'");
					upd_remarks = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_REMARKS)).replaceAll("'", "\\'");
					upd_clientid = Helper.variables.getGlobalVar_currentUserID(activity)+"-"+cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID)).replaceAll("'", "\\'");
					upd_dateAdded = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED)).replaceAll("'", "\\'");
					upd_localid = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_UPDATES_ID)).replaceAll("'", "\\'");

					sqlString = sqlString +
							"( '"+upd_id+"',  " +
							"'"+upd_remarks+"',  " +
							"'"+upd_clientid+"',  " +
							"'"+upd_dateAdded+"',  " +
							"'"+upd_localid+"' ),";
				}
			}
		}

		Log.d("query", "after conditions");

		String strSql = sqlString.substring(0, sqlString.length() - 1);
		strSql = strSql + " " +
				" ON DUPLICATE KEY UPDATE " +
				" 	 "+DB_Helper_PetOneCRM.CL_UPDATES_ID+" = VALUES("+DB_Helper_PetOneCRM.CL_UPDATES_ID+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_UPDATES_REMARKS+" = VALUES("+DB_Helper_PetOneCRM.CL_UPDATES_REMARKS+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID+" = VALUES("+DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED+" = VALUES("+DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED+"), " +
				" 	 "+DB_Helper_PetOneCRM.CL_UPDATES_localID+" = VALUES("+DB_Helper_PetOneCRM.CL_UPDATES_localID+")"
		;

		return strSql;
	}




	/********************************************
	 * 				DELETES						*
	 ********************************************/

	public boolean deleteClientInfoByID(String rowId) {
		String where = DB_Helper_PetOneCRM.CL_CLIENTINFO_ID+ "=" + rowId;
		return db.delete(DB_Helper_PetOneCRM.TBL_CLIENTINFO, where, null) != 0;
	}


	public boolean deleteClientUpdatesByClientID(String rowId) {
		String where = DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID+ "=" + rowId;
		return db.delete(DB_Helper_PetOneCRM.TBL_UPDATES, where, null) != 0;
	}



	public boolean deleteClientUpdateById(String rowId) {
		String where = DB_Helper_PetOneCRM.CL_UPDATES_ID+ "=" + rowId;
		return db.delete(DB_Helper_PetOneCRM.TBL_UPDATES, where, null) != 0;
	}

	public void emptyTable(String tableName) {

		String query = "DELETE FROM "+tableName;
		String[] params = new String[]{};
		db.rawQuery(query, null);

	}



	/********************************************
	 * 				UDPATES						*
	 ********************************************/

	public int updateClientUpdates( String indexid, String remarks) {
		String where = DB_Helper_PetOneCRM.CL_UPDATES_ID + " = " + indexid;
		ContentValues newValues = new ContentValues();
		newValues.put(DB_Helper_PetOneCRM.CL_UPDATES_REMARKS, remarks);
		return 	db.update(DB_Helper_PetOneCRM.TBL_UPDATES, newValues, where, null);
	}



	public int updateClientInfo( String indexid, String address, String clientName, String custCode, String contactNumber) {
		String where = DB_Helper_PetOneCRM.CL_CLIENTINFO_ID + " = " + indexid;

		ContentValues newValues = new ContentValues();
		newValues.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS, address);
		newValues.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME, clientName);
		newValues.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE, custCode);
		newValues.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER, contactNumber);

		return 	db.update(DB_Helper_PetOneCRM.TBL_CLIENTINFO, newValues, where, null);
	}


	public int updateRowOneUser(String userid, String lvl, String firstname, String lastname, String username, String password, String deviceid, String dateAdded) {
		String where = DB_Helper_PetOneCRM.CL_USERS_ID + " = " + userid;
		ContentValues newValues = new ContentValues();
		newValues.put(DB_Helper_PetOneCRM.CL_USERS_userlvl, lvl);
		newValues.put(DB_Helper_PetOneCRM.CL_USERS_firstName, firstname);
		newValues.put(DB_Helper_PetOneCRM.CL_USERS_lastName, lastname);
		newValues.put(DB_Helper_PetOneCRM.CL_USERS_username, username);
		newValues.put(DB_Helper_PetOneCRM.CL_USERS_password, password);
		newValues.put(DB_Helper_PetOneCRM.CL_USERS_deviceid, deviceid);
		newValues.put(DB_Helper_PetOneCRM.CL_USERS_dateAdded, dateAdded);
		return 	db.update(DB_Helper_PetOneCRM.TBL_USERS, newValues, where, null);
	}

	public int updateUnPostedToPosted_BySelectedItems_CLIENTINFO(Activity activity, int[] selectedID) {

		String whereSelected = "";
		for (int i = 0; i < selectedID.length; i++) {
			String condition = "OR";
			if (selectedID.length-1 == i) {
				condition = "AND";
			}else{
				condition = "OR";
			}
			whereSelected = whereSelected +  DB_Helper_PetOneCRM.CL_CLIENTINFO_ID + " = "+ selectedID[i] + " " + condition + " ";
		}

		String where = whereSelected + DB_Helper_PetOneCRM.CL_CLIENTINFO_IsPosted + " = 0 AND "
				+ DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby + " = " + Helper.variables.getGlobalVar_currentUserID(activity)
				;

		ContentValues newValues = new ContentValues();
		newValues.put(DB_Helper_PetOneCRM.CL_CLIENTINFO_IsPosted, 1);

		return 	db.update(DB_Helper_PetOneCRM.TBL_CLIENTINFO, newValues, where, null);
	}

	public int updateUnPostedToPosted_All_userActivities() {


		String where = DB_Helper_PetOneCRM.CL_USER_ACTIVITY_isPosted + " = 0 ";

		ContentValues newValues = new ContentValues();
		newValues.put(DB_Helper_PetOneCRM.CL_USER_ACTIVITY_isPosted, 1);

		return 	db.update(DB_Helper_PetOneCRM.TBL_USER_ACTIVITY, newValues, where, null);
	}


	public int updateUnPostedToPosted_BySelectedItems_CLIENTUPDATES(Activity activity, int[] selectedID) {

		String whereSelected = "";
		for (int i = 0; i < selectedID.length; i++) {
			String condition = "OR";
			if (selectedID.length-1 == i) {
				condition = "";
			}else{
				condition = "OR";
			}
			whereSelected = whereSelected +  DB_Helper_PetOneCRM.CL_UPDATES_ID + " = "+ selectedID[i] + " " + condition + " ";
		}

		String where = whereSelected
				;

		ContentValues newValues = new ContentValues();
		newValues.put(DB_Helper_PetOneCRM.CL_UPDATES_isposted, 1);

		return 	db.update(DB_Helper_PetOneCRM.TBL_UPDATES, newValues, where, null);
	}






}
