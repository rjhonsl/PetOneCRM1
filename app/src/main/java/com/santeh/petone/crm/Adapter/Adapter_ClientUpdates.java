package com.santeh.petone.crm.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.santeh.petone.crm.Obj.CustInfoObject;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.Helper;

import java.util.List;


public class Adapter_ClientUpdates extends ArrayAdapter<CustInfoObject> {

	Context context;
	LayoutInflater inflater;
	List<CustInfoObject> itemList;
	int positions = 0;
	String tag = "Create New ArrayAdapter";
	private SparseBooleanArray mSelectedItemsIds;

	public Adapter_ClientUpdates(Context context, int resourceId, List<CustInfoObject> items) {
		super(context, resourceId, items);
		this.context = context;
		this.itemList = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d(tag, "Adapter Context");
		mSelectedItemsIds = new SparseBooleanArray();
	}

	private class ViewHolder {
		TextView dateUpdated;
		TextView remarks;
		TextView initials;
	}

	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		positions = position;

		Log.d(tag, "Adapter Getview");
		if (view == null) {

			Log.d(tag, "if null");
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.item_lv_clientupdates, null);

			holder.remarks = (TextView) view.findViewById(R.id.item_user_position);
			holder.dateUpdated = (TextView) view.findViewById(R.id.item_clientupdates_date);
			holder.initials = (TextView) view.findViewById(R.id.item_user_initials);
			view.setTag(holder);
		}
		else
		{
//			Log.d(tag, "if not null");
			holder = (ViewHolder) view.getTag();
		}


		String dateString = Helper.timeConvert.longtoDate_StringFormat(Long.parseLong(itemList.get(position).getDateAddedToDB()));
		String remarks = itemList.get(position).getRemarks()+"";

		holder.remarks.setText(remarks);//reversed this//
		holder.dateUpdated.setText(dateString);
		holder.initials.setText(position+1+"");


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

	public void selectView(int position, boolean value) {
		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);
		notifyDataSetChanged();
	}

}
