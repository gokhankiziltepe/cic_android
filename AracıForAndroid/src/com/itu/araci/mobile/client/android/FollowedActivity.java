package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.adapter.FollowedAdapter;
import com.itu.araci.mobile.client.android.model.Followed;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class FollowedActivity extends BaseActivity {
	final Class<?>[] cArg = new Class[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.followed);

		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		getFolloweds();
	}

	public void getFolloweds() {
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/followee/get", null,
							FollowedActivity.this),
					FollowedActivity.class.getDeclaredMethod(
							"onPostExecuteFolloweeGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteFolloweeGet(String result, Dialog parentDialog) {
		RelativeLayout relativeLayout = (RelativeLayout) FollowedActivity.this
				.findViewById(R.id.followedPlaceRelativeLayout);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				relativeLayout.setVisibility(View.GONE);
			} else {
				List<Followed> followedList = new ArrayList<Followed>();
				if (!jsonObject.getJSONObject("result").has("placeFollowList")
						&& !jsonObject.getJSONObject("result").has(
								"userFollowList")) {

				}
				if (jsonObject.getJSONObject("result").has("placeFollowList")) {
					JSONArray jsonArray = jsonObject.getJSONObject("result")
							.getJSONArray("placeFollowList");
					for (int j = 0; j < jsonArray.length(); j++) {
						Followed followed = new Followed();
						followed.setId(jsonArray.getJSONObject(j)
								.getJSONObject("follow").getInt("id"));
						followed.setDateAdded(jsonArray.getJSONObject(j)
								.getJSONObject("follow").getString("dateAdded"));
						if (jsonArray.getJSONObject(j).has("place")) {
							followed.setType(0);
							followed.setFollowerInfo(jsonArray.getJSONObject(j)
									.getJSONObject("place").getString("name"));
							followed.setFollowedEntityBase(jsonArray
									.getJSONObject(j).getJSONObject("place")
									.getString("id"));
						}
						if (jsonArray.getJSONObject(j).has("userBase")) {
							followed.setType(1);
							followed.setFollowerInfo(jsonArray.getJSONObject(j)
									.getJSONObject("userBase")
									.getString("name")
									+ " "
									+ jsonArray.getJSONObject(j)
											.getJSONObject("userBase")
											.getString("surname"));
							followed.setFollowedEntityBase(jsonArray
									.getJSONObject(j).getJSONObject("userBase")
									.getString("id"));
						}
						followedList.add(followed);
					}
				}
				if (jsonObject.getJSONObject("result").has("userFollowList")) {
					JSONArray jsonArray2 = jsonObject.getJSONObject("result")
							.getJSONArray("userFollowList");

					for (int j = 0; j < jsonArray2.length(); j++) {
						Followed followed = new Followed();
						followed.setId(jsonArray2.getJSONObject(j)
								.getJSONObject("follow").getInt("id"));
						followed.setDateAdded(jsonArray2.getJSONObject(j)
								.getJSONObject("follow").getString("dateAdded"));
						if (jsonArray2.getJSONObject(j).has("place")) {
							followed.setType(0);
							followed.setFollowerInfo(jsonArray2
									.getJSONObject(j).getJSONObject("place")
									.getString("name"));
							followed.setFollowedEntityBase(jsonArray2
									.getJSONObject(j).getJSONObject("place")
									.getString("id"));
						}
						if (jsonArray2.getJSONObject(j).has("userBase")) {
							followed.setType(1);
							followed.setFollowerInfo(jsonArray2
									.getJSONObject(j).getJSONObject("userBase")
									.getString("name")
									+ " "
									+ jsonArray2.getJSONObject(j)
											.getJSONObject("userBase")
											.getString("surname"));
							followed.setFollowedEntityBase(jsonArray2
									.getJSONObject(j).getJSONObject("userBase")
									.getString("id"));
						}
						followedList.add(followed);
					}
				}
				if (followedList.size() == 0) {
					Toast.makeText(this, "Henüz takip etme bilginiz yok",
							Toast.LENGTH_LONG).show();
					FollowedActivity.this.finish();
				} else {
					LinearLayout linearLayout = (LinearLayout) findViewById(R.id.followedPlaceList);
					linearLayout.removeAllViews();
					final FollowedAdapter followedAdapter = new FollowedAdapter(
							FollowedActivity.this, R.layout.follower_item,
							followedList);
					for (int i = 0; i < followedAdapter.getCount(); i++) {
						View view = followedAdapter.getView(i, null, null);
						linearLayout.addView(view);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteFolloweeDelete(String result, Dialog parentDialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Takip bilginiz silindi",
						Toast.LENGTH_LONG).show();
				getFolloweds();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}
}
