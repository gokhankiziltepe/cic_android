package com.itu.araci.mobile.client.android.adapter;

import java.util.List;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itu.araci.mobile.client.android.R;
import com.itu.araci.mobile.client.android.model.WorkingHour;

public class WorkingHourAdapter extends ArrayAdapter<WorkingHour> {
	private Activity context;
	List<WorkingHour> data = null;

	public WorkingHourAdapter(Activity context, int resource,
			List<WorkingHour> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.working_hour_item, parent, false);
		}

		final WorkingHour item = data.get(position);

		if (item != null) {

			TextView workingHourItemText = (TextView) row
					.findViewById(R.id.workingHourItemText);
			workingHourItemText.setText(Html.fromHtml("- <b>" + item.getDay()
					+ "</b>  [" + item.getStart() + "-" + item.getEnd() + "]"));
		}

		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}

}