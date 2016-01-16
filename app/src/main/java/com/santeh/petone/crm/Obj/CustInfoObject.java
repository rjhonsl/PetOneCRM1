package com.santeh.petone.crm.Obj;

import com.google.android.gms.maps.model.LatLng;

public class CustInfoObject {
	
	private int id;
	private int ci_id;
	private String cust_latitude;
	private String cust_longtitude;
	private String address;
	private String contact_number;
	private String dateAddedToDB;
	private String assingedArea;
	private String username;
	private String deviceid;
	private String firstname;
	private String lastname;
	private String customerName;
	private String localId;
	private String dbID;
	private String updateID;
	private String updateRemarks;
	private String updateClientId;
	private String updateDateAdded;
	private String updateLocalId;
	private String actID;
	private String actUserID;
	private String actActionDone;
	private String actLat;
	private String actLong;
	private String actDatetime;
	private String actActionType;
	private String actLocalId;


	private String accountlevelDescription;
	private int userlevel;
	private int userid;

	private int isVisited;
	private int isactive;
	private int isPosted;
	private int isAqua_active;
	private int isHogs_active;
	private int isPetOne_active;

	private String custCode;
	private String remarks;
	private String addedBy;

	private LatLng latLng;

	private double weeklyConsumptionInGrams;
	private String survivalrate_per_pond;

	public CustInfoObject() {
	}



	public String getAssingedArea() {
		return assingedArea;
	}

	public void setAssingedArea(String assingedArea) {
		this.assingedArea = assingedArea;
	}


	public String getSurvivalrate_per_pond() {
		return survivalrate_per_pond;
	}

	public void setSurvivalrate_per_pond(String survivalrate_per_pond) {
		this.survivalrate_per_pond = survivalrate_per_pond;
	}

	public int getIsVisited() {
		return isVisited;
	}

	public void setIsVisited(int isVisited) {
		this.isVisited = isVisited;
	}

	public int getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(int userlevel) {
		this.userlevel = userlevel;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAccountlevelDescription() {
		return accountlevelDescription;
	}

	public void setAccountlevelDescription(String accountlevelDescription) {
		this.accountlevelDescription = accountlevelDescription;
	}

	public double getWeeklyConsumptionInGrams() {
		return weeklyConsumptionInGrams;
	}

	public void setWeeklyConsumptionInGrams(double weeklyConsumptionInGrams) {
		this.weeklyConsumptionInGrams = weeklyConsumptionInGrams;
	}

	public int getCi_id() {
		return ci_id;
	}

	public void setCi_id(int ci_id) {
		this.ci_id = ci_id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getDateAddedToDB() {
		return dateAddedToDB;
	}

	public void setDateAddedToDB(String dateAddedToDB) {
		this.dateAddedToDB = dateAddedToDB;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsactive() {
		return isactive;
	}

	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceId(String deviceid) {
		this.deviceid = deviceid;
	}

	public int getIsPosted() {
		return isPosted;
	}

	public void setIsPosted(int isPosted) {
		this.isPosted = isPosted;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public int getIsAqua_active() {
		return isAqua_active;
	}

	public void setIsAqua_active(int isAqua_active) {
		this.isAqua_active = isAqua_active;
	}

	public int getIsHogs_active() {
		return isHogs_active;
	}

	public void setIsHogs_active(int isHogs_active) {
		this.isHogs_active = isHogs_active;
	}

	public int getIsPetOne_active() {
		return isPetOne_active;
	}

	public void setIsPetOne_active(int isPetOne_active) {
		this.isPetOne_active = isPetOne_active;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getCust_latitude() {
		return cust_latitude;
	}

	public void setCust_latitude(String cust_latitude) {
		this.cust_latitude = cust_latitude;
	}

	public String getCust_longtitude() {
		return cust_longtitude;
	}

	public void setCust_longtitude(String cust_longtitude) {
		this.cust_longtitude = cust_longtitude;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public String getDbID() {
		return dbID;
	}

	public void setDbID(String dbID) {
		this.dbID = dbID;
	}

	public String getUpdateID() {
		return updateID;
	}

	public void setUpdateID(String updateID) {
		this.updateID = updateID;
	}

	public String getUpdateRemarks() {
		return updateRemarks;
	}

	public void setUpdateRemarks(String updateRemarks) {
		this.updateRemarks = updateRemarks;
	}

	public String getUpdateClientId() {
		return updateClientId;
	}

	public void setUpdateClientId(String updateClientId) {
		this.updateClientId = updateClientId;
	}

	public String getUpdateDateAdded() {
		return updateDateAdded;
	}

	public void setUpdateDateAdded(String updateDateAdded) {
		this.updateDateAdded = updateDateAdded;
	}

	public String getUpdateLocalId() {
		return updateLocalId;
	}

	public void setUpdateLocalId(String updateLocalId) {
		this.updateLocalId = updateLocalId;
	}

	public String getActID() {
		return actID;
	}

	public void setActID(String actID) {
		this.actID = actID;
	}

	public String getActUserID() {
		return actUserID;
	}

	public void setActUserID(String actUserID) {
		this.actUserID = actUserID;
	}

	public String getActActionDone() {
		return actActionDone;
	}

	public void setActActionDone(String actActionDone) {
		this.actActionDone = actActionDone;
	}

	public String getActLat() {
		return actLat;
	}

	public void setActLat(String actLat) {
		this.actLat = actLat;
	}

	public String getActLong() {
		return actLong;
	}

	public void setActLong(String actLong) {
		this.actLong = actLong;
	}

	public String getActDatetime() {
		return actDatetime;
	}

	public void setActDatetime(String actDatetime) {
		this.actDatetime = actDatetime;
	}

	public String getActActionType() {
		return actActionType;
	}

	public void setActActionType(String actActionType) {
		this.actActionType = actActionType;
	}

	public String getActLocalId() {
		return actLocalId;
	}

	public void setActLocalId(String actLocalId) {
		this.actLocalId = actLocalId;
	}
}
