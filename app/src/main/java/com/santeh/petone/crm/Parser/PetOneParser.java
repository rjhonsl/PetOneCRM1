package com.santeh.petone.crm.Parser;


import com.santeh.petone.crm.DBase.DB_Helper_PetOneCRM;
import com.santeh.petone.crm.Obj.CustInfoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjhonsl on 9/9/2015.
 */
public class PetOneParser {


    public static List<CustInfoObject> parseFeed(String content) {

        try {
            JSONArray ar = new JSONArray(content);
            List<CustInfoObject> custInfoObjectList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                CustInfoObject custInfoObject = new CustInfoObject();


                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME)) {
                    custInfoObject.setCustomerName(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME));
                }else {
                    custInfoObject.setCustomerName("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_ID)) {
                    custInfoObject.setDbID(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_ID));
                } else {
                    custInfoObject.setDbID("");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_LAT)) {
                    custInfoObject.setCust_latitude(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_LAT));
                } else {
                    custInfoObject.setCust_latitude("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_LNG)) {
                    custInfoObject.setCust_longtitude(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_LNG));
                } else {
                    custInfoObject.setCust_longtitude("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS)) {
                    custInfoObject.setAddress(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS));
                } else {
                    custInfoObject.setAddress("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE)) {
                    custInfoObject.setCustCode(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_CUSTCODE));
                } else {
                    custInfoObject.setCustCode("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER)) {
                    custInfoObject.setContact_number(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_C_NUMBER));
                } else {
                    custInfoObject.setContact_number("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby)) {
                    custInfoObject.setAddedBy(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_addedby));
                } else {
                    custInfoObject.setAddedBy("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_localid)) {
                    custInfoObject.setLocalId(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_localid));
                } else {
                    custInfoObject.setLocalId("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded)) {
                    custInfoObject.setDateAddedToDB(obj.getString(DB_Helper_PetOneCRM.CL_CLIENTINFO_dateAdded));
                } else {
                    custInfoObject.setDateAddedToDB("0");
                }




                if (obj.has(DB_Helper_PetOneCRM.CL_UPDATES_ID)) {
                    custInfoObject.setUpdateID(obj.getString(DB_Helper_PetOneCRM.CL_UPDATES_ID));
                } else {
                    custInfoObject.setUpdateID("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_UPDATES_REMARKS)) {
                    custInfoObject.setUpdateRemarks(obj.getString(DB_Helper_PetOneCRM.CL_UPDATES_REMARKS));
                } else {
                    custInfoObject.setUpdateRemarks("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID)) {
                    custInfoObject.setUpdateClientId(obj.getString(DB_Helper_PetOneCRM.CL_UPDATES_CLIENTID));
                } else {
                    custInfoObject.setUpdateClientId("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED)) {
                    custInfoObject.setUpdateDateAdded(obj.getString(DB_Helper_PetOneCRM.CL_UPDATES_DATEADDED));
                } else {
                    custInfoObject.setUpdateDateAdded("0");
                }

                if (obj.has(DB_Helper_PetOneCRM.CL_UPDATES_localID)) {
                    custInfoObject.setUpdateLocalId(obj.getString(DB_Helper_PetOneCRM.CL_UPDATES_localID));
                } else {
                    custInfoObject.setUpdateLocalId("0");
                }






                custInfoObjectList.add(custInfoObject);
            }

            return custInfoObjectList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
