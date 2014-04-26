package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.adapter.EventAdapter;
import com.itu.araci.mobile.client.android.adapter.ImageAdapter;
import com.itu.araci.mobile.client.android.model.Event;
import com.itu.araci.mobile.client.android.model.Image;
import com.itu.araci.mobile.client.android.model.User;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class FriendProfileActivity extends BaseActivity {
	private User user;
	final Class<?>[] cArg = new Class[2];
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_profile);

		cArg[0] = String.class;
		cArg[1] = Dialog.class;
		nameValuePairs.add(new BasicNameValuePair("userId", getIntent()
				.getExtras().getString("userId")));
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/friend",
							nameValuePairs, FriendProfileActivity.this),
					FriendProfileActivity.class.getDeclaredMethod(
							"onPostExecuteFriendProfile", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
					Toast.LENGTH_SHORT).show();
		}

		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/friend/photos",
							nameValuePairs, FriendProfileActivity.this),
					FriendProfileActivity.class.getDeclaredMethod(
							"onPostExecuteFriendProfilePhotos", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
					Toast.LENGTH_SHORT).show();
		}

		getEvents();
	}

	public void onPostExecuteFriendProfile(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
					FriendProfileActivity.this.finish();
				} else {
					Integer id = jsonObject.getJSONObject("result")
							.getInt("id");
					String name = jsonObject.getJSONObject("result")
							.getJSONObject("userBase").getString("name");
					String surname = jsonObject.getJSONObject("result")
							.getJSONObject("userBase").getString("surname");
					String password = jsonObject.getJSONObject("result")
							.getJSONObject("userBase").getString("password");
					String passwordRepeat = jsonObject.getJSONObject("result")
							.getJSONObject("userBase").getString("password");
					String gender = jsonObject.getJSONObject("result")
							.getString("gender");
					String birthDate = jsonObject.getJSONObject("result")
							.getString("birthDate");
					user = new User();
					user.setId(id);
					user.setBirthDate(birthDate);
					user.setGender(gender);
					user.setName(name);
					user.setPassword(password);
					user.setPasswordRepeat(passwordRepeat);
					user.setSurname(surname);

					TextView textView = (TextView) FriendProfileActivity.this
							.findViewById(R.id.friendProfileHeaderMessage);
					textView.setText(name + " " + surname);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteFriendProfilePhotos(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
					FriendProfileActivity.this.finish();
				} else {
					List<Image> photos = new ArrayList<Image>();
					for (int j = 0; j < jsonArray.getJSONObject(i)
							.getJSONArray("result").length(); j++) {
						Image image = new Image();
						image.setId(jsonArray.getJSONObject(i)
								.getJSONArray("result").getJSONObject(j)
								.getString("id"));
						image.setPath(jsonArray.getJSONObject(i)
								.getJSONArray("result").getJSONObject(j)
								.getString("path"));
						String timestamp = jsonArray.getJSONObject(i)
								.getJSONArray("result").getJSONObject(j)
								.getString("dateAdded");

						image.setTimestamp(timestamp);
						photos.add(image);
					}
					if (photos.size() == 0) {
						RelativeLayout photosRelativeLayout = (RelativeLayout) FriendProfileActivity.this
								.findViewById(R.id.friendProfilePhotoRelativeLayout);
						photosRelativeLayout.setVisibility(View.GONE);
					} else {
						ImageAdapter imageAdapter = new ImageAdapter(
								FriendProfileActivity.this,
								R.layout.image_item, photos);
						LinearLayout linearLayout = (LinearLayout) FriendProfileActivity.this
								.findViewById(R.id.friendProfilePhotoHorizontalLinear);
						for (int j = 0; j < photos.size(); j++) {
							linearLayout.addView(imageAdapter.getView(j, null,
									null));
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void getEvents() {
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/event/getByFriendId",
							nameValuePairs, FriendProfileActivity.this),
					FriendProfileActivity.class.getDeclaredMethod(
							"onPostExecuteEventGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteEventGet(String result, Dialog parentDialog) {
		RelativeLayout relativeLayout = (RelativeLayout) FriendProfileActivity.this
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
					Toast.makeText(this, "Henüz etkinlik bilgisi yok",
							Toast.LENGTH_LONG).show();
				} else {
					LinearLayout linearLayout = (LinearLayout) findViewById(R.id.eventList);
					linearLayout.removeAllViews();
					final EventAdapter eventAdapter = new EventAdapter(
							FriendProfileActivity.this, R.layout.event_item,
							eventList, false);
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
}
