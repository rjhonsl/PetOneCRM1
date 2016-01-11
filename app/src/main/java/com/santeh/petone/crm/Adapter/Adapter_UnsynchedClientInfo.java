package com.santeh.petone.crm.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.santeh.petone.crm.Obj.CustInfoObject;
import com.santeh.petone.crm.R;

import java.util.List;


public class Adapter_UnsynchedClientInfo extends ArrayAdapter<CustInfoObject> {

	Context context;
	LayoutInflater inflater;
	List<CustInfoObject> itemList;
	boolean[] isUpload;
	int positions = 0;
	String tag = "Create New ArrayAdapter";
	private SparseBooleanArray mSelectedItemsIds;

	public Adapter_UnsynchedClientInfo(Context context, int resourceId, List<CustInfoObject> items) {
		super(context, resourceId, items);
		this.context = context;
		this.itemList = items;
		isUpload = new boolean[itemList.size()];
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d(tag, "Adapter Context");
		mSelectedItemsIds = new SparseBooleanArray();

	}

	private class ViewHolder {
		TextView txtClientName;
		TextView txtAddress;
		CheckBox chkPostThis;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		positions = position;

		Log.d(tag, "Adapter Getview");
		if (view == null) {

			Log.d(tag, "if null");
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.item_lv_unsynced_clientinfo, null);

			holder.txtAddress = (TextView) view.findViewById(R.id.item_clientinfo_address);
			holder.txtClientName = (TextView) view.findViewById(R.id.item_clientinfo_name);
			holder.chkPostThis = (CheckBox) view.findViewById(R.id.chk_upload);
			isUpload[position] = false;
			view.setTag(holder);
		}
		else
		{
//			Log.d(tag, "if not null");
			holder = (ViewHolder) view.getTag();
		}

		holder.chkPostThis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					isUpload[position] = isChecked;
			}
		});




		holder.chkPostThis.setChecked(isUpload[position]);

		String clientName = itemList.get(position).getCustomerName();
		String address = itemList.get(position).getAddress()+"";

		holder.txtAddress.setText(address);//reversed this//
		holder.txtClientName.setText(clientName);


		return view;
	}

	@Override
	public void remove(CustInfoObject object) {
		itemList.remove(object);
		notifyDataSetChanged();
	}

	public void toggleSelection(int position) {
		selectView(position, !mSelectedItemsIds.get(position));
	}

	public boolean[] getCheckedPositions(){
		return isUpload;
	}


	public int getCheckedCount(){
		int selectedidcounter = 0;
		for (int i = 0; i < isUpload.length; i++) {
			if (isUpload[i]) {
				selectedidcounter++;
			}
		}

		return selectedidcounter;
	}

	public void selectView(int position, boolean value) {
		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);
		notifyDataSetChanged();
	}

}
