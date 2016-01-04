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

import java.util.List;


public class Adapter_MapsActivity extends ArrayAdapter<CustInfoObject> {

	Context context;
	LayoutInflater inflater;
	List<CustInfoObject> itemList;
	int positions = 0;
	String tag = "Create New ArrayAdapter";
	private SparseBooleanArray mSelectedItemsIds;

	public Adapter_MapsActivity(Context context, int resourceId, List<CustInfoObject> items) {
		super(context, resourceId, items);
		this.context = context;
		this.itemList = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d(tag, "Adapter Context");
		mSelectedItemsIds = new SparseBooleanArray();
	}

	private class ViewHolder {
		TextView customerName;
	}

	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		positions = position;

		Log.d(tag, "Adapter Getview");
		if (view == null) {

			Log.d(tag, "if null");
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.item_lv_mapsactivity, null);

			holder.customerName = (TextView) view.findViewById(R.id.item_maps_activity_customerName);
			view.setTag(holder);
		}
		else
		{
//			Log.d(tag, "if not null");
			holder = (ViewHolder) view.getTag();
		}


		String remarks = itemList.get(position).getCustomerName()+"";

		holder.customerName.setText(remarks);//reversed this//


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
