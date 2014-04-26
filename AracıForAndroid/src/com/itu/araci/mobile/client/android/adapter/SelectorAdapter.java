package com.itu.araci.mobile.client.android.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itu.araci.mobile.client.android.R;

public class SelectorAdapter extends ArrayAdapter<String> {
	private Activity context;
	List<String> data = null;

	public SelectorAdapter(Activity context, int resource,
			int textViewResourceId, List<String> data) {
		super(context, resource, textViewResourceId, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.spinner_item, parent, false);
		}

		String item = data.get(position);

		if (item != null) {
			TextView myCountry = (TextView) row
					.findViewById(R.id.spinnerItemText);
			if (myCountry != null)
				myCountry.setText(item);

		}

		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}

}