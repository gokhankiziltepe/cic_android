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

import com.itu.araci.mobile.client.android.adapter.LikeAdapter;
import com.itu.araci.mobile.client.android.model.Like;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class LikeActivity extends BaseActivity {
	final Class<?>[] cArg = new Class[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.like);

		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		getLikes();
	}

	public void getLikes() {
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl(
							"/nativeapp/likedislike/getByUserIndividualId",
							null, LikeActivity.this),
					LikeActivity.class.getDeclaredMethod(
							"onPostExecuteLikeGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteLikeGet(String result, Dialog parentDialog) {
		RelativeLayout relativeLayout = (RelativeLayout) LikeActivity.this
				.findViewById(R.id.likeToPhotoRelativeLayout);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				relativeLayout.setVisibility(View.GONE);
			} else {
				List<Like> likeList = new ArrayList<Like>();
				if (!jsonObject.getJSONObject("result").has("placeLikeList")
						&& !jsonObject.getJSONObject("result").has(
								"photoLikeList")) {
					Toast.makeText(this, "Beğeniniz bulunmamaktadır",
							Toast.LENGTH_LONG).show();
					LikeActivity.this.finish();
				}
				if (jsonObject.getJSONObject("result").has("placeLikeList")) {
					JSONArray jsonArray = jsonObject.getJSONObject("result")
							.getJSONArray("placeLikeList");
					for (int j = 0; j < jsonArray.length(); j++) {
						Like like = new Like();
						like.setId(jsonArray.getJSONObject(j)
								.getJSONObject("like").getString("id"));
						like.setDateAdded(jsonArray.getJSONObject(j)
								.getJSONObject("like").getString("dateAdded"));
						like.setIsLike(jsonArray.getJSONObject(j)
								.getJSONObject("like").getBoolean("isLike"));
						if (jsonArray.getJSONObject(j).has("place")) {
							like.setType(0);
							like.setName(jsonArray.getJSONObject(j)
									.getJSONObject("place").getString("name"));
							like.setLikedEntityBase(jsonArray.getJSONObject(j)
									.getJSONObject("place").getString("id"));
						}
						likeList.add(like);
					}
				}
				if (jsonObject.getJSONObject("result").has("photoLikeList")) {
					JSONArray jsonArray2 = jsonObject.getJSONObject("result")
							.getJSONArray("photoLikeList");

					for (int j = 0; j < jsonArray2.length(); j++) {
						Like like = new Like();
						like.setId(jsonArray2.getJSONObject(j)
								.getJSONObject("like").getString("id"));
						like.setDateAdded(jsonArray2.getJSONObject(j)
								.getJSONObject("like").getString("dateAdded"));
						like.setIsLike(jsonArray2.getJSONObject(j)
								.getJSONObject("like").getBoolean("isLike"));
						if (jsonArray2.getJSONObject(j).has("photo")) {
							like.setType(1);
							like.setName("Mekan Resmi");
							like.setLikedEntityBase(jsonArray2.getJSONObject(j)
									.getJSONObject("photo").getString("id"));
						}
						likeList.add(like);
					}
				}
				if (likeList.size() == 0) {
					Toast.makeText(this, "Henüz beğeni bilginiz yok",
							Toast.LENGTH_LONG).show();
					LikeActivity.this.finish();
				} else {
					LinearLayout linearLayout = (LinearLayout) findViewById(R.id.likeToPhotoList);
					linearLayout.removeAllViews();
					final LikeAdapter likeAdapter = new LikeAdapter(
							LikeActivity.this, R.layout.like_item, likeList);
					for (int i = 0; i < likeAdapter.getCount(); i++) {
						View view = likeAdapter.getView(i, null, null);
						linearLayout.addView(view);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteLikeDelete(String result, Dialog parentDialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Beğeni silindi", Toast.LENGTH_LONG)
						.show();
				getLikes();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}
}
