package com.itu.araci.mobile.client.android.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.EventActivity;
import com.itu.araci.mobile.client.android.PlaceActivity;
import com.itu.araci.mobile.client.android.R;
import com.itu.araci.mobile.client.android.model.Event;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class EventAdapter extends ArrayAdapter<Event> {
	private Activity context;
	private Boolean isUserProfile;
	List<Event> data = null;

	public EventAdapter(Activity context, int resource, List<Event> data,
			Boolean isUserProfile) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
		this.isUserProfile = isUserProfile;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.event_item, parent, true);
		}

		final Event item = data.get(position);

		if (item != null) {
			TextView eventDate = (TextView) row.findViewById(R.id.eventDate);
			eventDate.setText(item.getStartDate() + " - " + item.getEndDate());
			TextView eventName = (TextView) row.findViewById(R.id.eventName);
			eventName.setText(item.getName());
			TextView eventPlace = (TextView) row.findViewById(R.id.eventPlace);
			eventPlace.setText(item.getPlaceName());
			row.findViewById(R.id.eventItemRelativeLayout).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent;
							intent = new Intent(context, PlaceActivity.class);
							intent.putExtra("eventId", item.getId().toString());
							context.startActivity(intent);
						}
					});
			if (isUserProfile)
				row.findViewById(R.id.eventItemRelativeLayout)
						.setOnLongClickListener(new OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {
								final AlertDialog alertDialog = new AlertDialog.Builder(
										context)
										.setCancelable(true)
										.setTitle("Uyarı")
										.setMessage(
												"Etkinlik bilgisini silmek istediğinize emin misiniz?")
										.create();
								final Class<?>[] cArg = new Class[2];
								cArg[0] = String.class;
								cArg[1] = Dialog.class;
								alertDialog.setButton(Dialog.BUTTON_POSITIVE,
										"Evet",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
												nameValuePairs
														.add(new BasicNameValuePair(
																"eventId",
																getItem(
																		position)
																		.getId()
																		.toString()));
												try {
													AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
															context,
															HttpUtil.buildUrl(
																	"/nativeapp/event/delete",
																	nameValuePairs,
																	context),
															EventActivity.class
																	.getDeclaredMethod(
																			"onPostExecuteEventDelete",
																			cArg),
															null);
													asyncTaskExecuteGet
															.execute();
												} catch (Exception e) {
													e.printStackTrace();
													Toast.makeText(context,
															"Cevap işlenemedi",
															Toast.LENGTH_LONG)
															.show();
												}
											}
										});
								alertDialog.setButton(Dialog.BUTTON_NEGATIVE,
										"Hayır",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												alertDialog.dismiss();
											}
										});
								alertDialog.show();
								return true;
							}
						});
		}
		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}

}