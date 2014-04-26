package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.adapter.EventAdapter;
import com.itu.araci.mobile.client.android.model.Event;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class EventActivity extends BaseActivity {
	final Class<?>[] cArg = new Class[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event);

		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		Button eventAddButton = (Button) findViewById(R.id.eventAddButton);
		eventAddButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(EventActivity.this,
						EventAddActivity.class);
				startActivity(i);
			}
		});

		getEvents();
	}

	public void getEvents() {
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl(
							"/nativeapp/event/getByUserIndividualId", null,
							EventActivity.this),
					EventActivity.class.getDeclaredMethod(
							"onPostExecuteEventGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteEventGet(String result, Dialog parentDialog) {
		RelativeLayout relativeLayout = (RelativeLayout) EventActivity.this
				.findViewById(R.id.eventRelativeLayout);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				relativeLayout.setVisibility(View.GONE);
			} else {
				List<Event> eventList = new ArrayList<Event>();
				JSONArray jsonArray = jsonObject.getJSONArray("result");
				for (int j = 0; j < jsonArray.length(); j++) {
					Event event = new Event();
					event.setId(jsonArray.getJSONObject(j).getString("id"));
					event.setDescription(jsonArray.getJSONObject(j).getString(
							"description"));
					event.setEndDate(jsonArray.getJSONObject(j).getString(
							"endTime"));
					event.setStartDate(jsonArray.getJSONObject(j).getString(
							"startTime"));
					event.setName(jsonArray.getJSONObject(j).getString("name"));
					event.setPlaceName(jsonArray.getJSONObject(j)
							.getJSONObject("place").getString("name"));
					eventList.add(event);

				}

				if (eventList.size() == 0) {
					Toast.makeText(this, "Henüz etkinlik bilginiz yok",
							Toast.LENGTH_LONG).show();
				} else {
					LinearLayout linearLayout = (LinearLayout) findViewById(R.id.eventList);
					linearLayout.removeAllViews();
					final EventAdapter eventAdapter = new EventAdapter(
							EventActivity.this, R.layout.event_item, eventList,
							true);
					for (int i = 0; i < eventAdapter.getCount(); i++) {
						View view = eventAdapter.getView(i, null, null);
						linearLayout.addView(view);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteEventDelete(String result, Dialog parentDialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Etkinlik bilginiz silindi",
						Toast.LENGTH_LONG).show();
				getEvents();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}
}
