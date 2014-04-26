package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.adapter.FollowerAdapter;
import com.itu.araci.mobile.client.android.model.Follower;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class FollowerActivity extends BaseActivity {
	final Class<?>[] cArg = new Class[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.follower);

		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		getFollowers();
	}

	public void getFollowers() {
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/follower/get", null,
							FollowerActivity.this),
					FollowerActivity.class.getDeclaredMethod(
							"onPostExecuteFollowerGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteFollowerGet(String result, Dialog parentDialog) {
		RelativeLayout relativeLayout = (RelativeLayout) FollowerActivity.this
				.findViewById(R.id.followerRelativeLayout);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				relativeLayout.setVisibility(View.GONE);
			} else {
				List<Follower> followers = new ArrayList<Follower>();
				for (int j = 0; j < jsonObject.getJSONArray("result").length(); j++) {
					Follower follower = new Follower();
					follower.setId(jsonObject.getJSONArray("result")
							.getJSONObject(j).getInt("id"));
					follower.setFollowerId(jsonObject.getJSONArray("result")
							.getJSONObject(j)
							.getJSONObject("followerUserIndividual")
							.getJSONObject("userBase").getString("id"));
					follower.setFollowerInfo(jsonObject.getJSONArray("result")
							.getJSONObject(j)
							.getJSONObject("followerUserIndividual")
							.getJSONObject("userBase").getString("name")
							+ " "
							+ jsonObject.getJSONArray("result")
									.getJSONObject(j)
									.getJSONObject("followerUserIndividual")
									.getJSONObject("userBase")
									.getString("surname"));
					follower.setDateAdded(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("dateAdded"));
					follower.setIsApproved(jsonObject.getJSONArray("result")
							.getJSONObject(j).getBoolean("approved"));
					followers.add(follower);
				}

				if (followers.size() == 0) {
					Toast.makeText(this, "Henüz takipçiniz yok",
							Toast.LENGTH_LONG).show();
					FollowerActivity.this.finish();
				} else {
					LinearLayout linearLayout = (LinearLayout) findViewById(R.id.followersList);
					linearLayout.removeAllViews();
					final FollowerAdapter followerAdapter = new FollowerAdapter(
							FollowerActivity.this, R.layout.follower_item,
							followers);
					for (int i = 0; i < followerAdapter.getCount(); i++) {
						View view = followerAdapter.getView(i, null, null);
						linearLayout.addView(view);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteFollowerStateChange(String result,
			Dialog parentDialog) {
		RelativeLayout relativeLayout = (RelativeLayout) FollowerActivity.this
				.findViewById(R.id.followerRelativeLayout);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				relativeLayout.setVisibility(View.GONE);
			} else {
				Toast.makeText(this, "Takipçi durumunuz değiştirildi",
						Toast.LENGTH_LONG).show();
				getFollowers();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}
}
