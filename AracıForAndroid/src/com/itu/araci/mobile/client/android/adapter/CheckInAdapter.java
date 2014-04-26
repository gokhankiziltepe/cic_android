package com.itu.araci.mobile.client.android.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itu.araci.mobile.client.android.PlaceActivity;
import com.itu.araci.mobile.client.android.R;
import com.itu.araci.mobile.client.android.model.CheckIn;

public class CheckInAdapter extends ArrayAdapter<CheckIn> {
	private Activity context;
	List<CheckIn> data = null;

	public CheckInAdapter(Activity context, int resource, List<CheckIn> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.check_in_item, parent, false);
		}

		final CheckIn item = data.get(position);

		if (item != null) {

			TextView checkInPlaceName = (TextView) row
					.findViewById(R.id.checkInPlaceName);
			checkInPlaceName.setText(Html.fromHtml("<u>" + item.getPlaceName()
					+ "</u>"));
			checkInPlaceName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(context, PlaceActivity.class);
					i.putExtra("placeId", item.getPlaceId());
					context.startActivity(i);
				}
			});

			TextView checkInDate = (TextView) row
					.findViewById(R.id.checkInPlaceDate);
			checkInDate.setText(item.getDateAdded());

		}
		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}

}