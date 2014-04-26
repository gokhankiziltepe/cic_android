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

import com.itu.araci.mobile.client.android.adapter.CommentAdapter;
import com.itu.araci.mobile.client.android.model.Comment;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class CommentActivity extends BaseActivity {
	final Class<?>[] cArg = new Class[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);

		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		getPhotoComments();
		getPlaceComments();
	}

	public void getPhotoComments() {
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/comment/photo/get",
							null, CommentActivity.this),
					CommentActivity.class.getDeclaredMethod(
							"onPostExecuteCommentPhotoGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void getPlaceComments() {
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/comment/place/get",
							null, CommentActivity.this),
					CommentActivity.class.getDeclaredMethod(
							"onPostExecuteCommentPlaceGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteCommentPhotoGet(String result, Dialog parentDialog) {
		RelativeLayout relativeLayout = (RelativeLayout) CommentActivity.this
				.findViewById(R.id.commentToPhotoRelativeLayout);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				relativeLayout.setVisibility(View.GONE);
			} else {
				List<Comment> comments = new ArrayList<Comment>();
				for (int j = 0; j < jsonObject.getJSONArray("result").length(); j++) {
					Comment comment = new Comment();
					comment.setId(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("id"));
					comment.setCommentText("\""
							+ jsonObject.getJSONArray("result")
									.getJSONObject(j).getString("commentText")
							+ "\"");
					comment.setDateAdded(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("dateAdded"));
					comment.setCommentedToEntityBase(jsonObject
							.getJSONArray("result").getJSONObject(j)
							.getString("commentedToEntityBaseId"));
					comment.setOwnerInfo(jsonObject.getJSONArray("result")
							.getJSONObject(j)
							.getJSONObject("commentedByUserBase")
							.getString("name")
							+ " "
							+ jsonObject.getJSONArray("result")
									.getJSONObject(j)
									.getJSONObject("commentedByUserBase")
									.getString("surname"));
					comments.add(comment);
				}

				if (comments.size() == 0) {
					Toast.makeText(this, "Henüz fotoğraf yorumunuz yok",
							Toast.LENGTH_LONG).show();
					relativeLayout.setVisibility(View.GONE);
				} else {
					LinearLayout linearLayout = (LinearLayout) findViewById(R.id.commentToPhotoList);
					linearLayout.removeAllViews();
					final CommentAdapter commentAdapter = new CommentAdapter(
							CommentActivity.this, R.layout.comment_item,
							comments, true);
					for (int i = 0; i < commentAdapter.getCount(); i++) {
						View view = commentAdapter.getView(i, null, null);
						linearLayout.addView(view);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteCommentPlaceGet(String result, Dialog parentDialog) {
		RelativeLayout relativeLayout = (RelativeLayout) CommentActivity.this
				.findViewById(R.id.commentToPlaceRelativeLayout);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				relativeLayout.setVisibility(View.GONE);
			} else {
				List<Comment> comments = new ArrayList<Comment>();
				for (int j = 0; j < jsonObject.getJSONArray("result").length(); j++) {
					Comment comment = new Comment();
					comment.setId(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("id"));
					comment.setCommentText("\""
							+ jsonObject.getJSONArray("result")
									.getJSONObject(j).getString("commentText")
							+ "\"");
					comment.setDateAdded(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("dateAdded"));
					comment.setCommentedToEntityBase(jsonObject
							.getJSONArray("result").getJSONObject(j)
							.getString("commentedToEntityBaseId"));
					comment.setOwnerInfo(jsonObject.getJSONArray("result")
							.getJSONObject(j)
							.getJSONObject("commentedByUserBase")
							.getString("name")
							+ " "
							+ jsonObject.getJSONArray("result")
									.getJSONObject(j)
									.getJSONObject("commentedByUserBase")
									.getString("surname"));
					comments.add(comment);
				}

				if (comments.size() == 0) {
					Toast.makeText(this, "Henüz mekan yorumunuz yok",
							Toast.LENGTH_LONG).show();
					relativeLayout.setVisibility(View.GONE);
				} else {
					LinearLayout linearLayout = (LinearLayout) findViewById(R.id.commentToPlaceList);
					linearLayout.removeAllViews();
					final CommentAdapter commentAdapter = new CommentAdapter(
							CommentActivity.this, R.layout.comment_item,
							comments, false);
					for (int i = 0; i < commentAdapter.getCount(); i++) {
						View view = commentAdapter.getView(i, null, null);
						linearLayout.addView(view);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteCommentDelete(String result, Dialog parentDialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Yorum bilginiz silindi",
						Toast.LENGTH_LONG).show();
				getPhotoComments();
				getPlaceComments();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}
}
