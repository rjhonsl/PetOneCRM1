package com.santeh.petone.crm.DBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

	public long insertClientUpdates(String remarks, String clientId){

		ContentValues values = new ContentValues();
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID, clientId);
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_REMARKS, remarks);
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED, System.currentTimeMillis());
		values.put(DB_Helper_PetOneCRM.CL_UPDATES_isposted, 0);

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



}
