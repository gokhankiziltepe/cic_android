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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.adapter.CommentAdapter;
import com.itu.araci.mobile.client.android.cache.ImageLoader;
import com.itu.araci.mobile.client.android.model.Comment;
import com.itu.araci.mobile.client.android.model.Image;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;
import com.itu.araci.mobile.client.android.util.ValidityUtils;

public class PhotoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", getIntent().getExtras()
				.getString("photoId")));
		final Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/photo/get",
							nameValuePairs, PhotoActivity.this),
					PhotoActivity.class.getDeclaredMethod(
							"onPostExecutePhotoGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}

		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/likedislike/get",
							nameValuePairs, PhotoActivity.this),
					PhotoActivity.class.getDeclaredMethod(
							"onPostExecutePhotoLikeDislike", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}

		ImageButton likeImageButton = (ImageButton) this
				.findViewById(R.id.photoLikeButton);
		likeImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", getIntent()
						.getExtras().getString("photoId")));
				nameValuePairs.add(new BasicNameValuePair("isLike", "true"));
				try {
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							PhotoActivity.this, HttpUtil.buildUrl(
									"/nativeapp/likedislike", nameValuePairs,
									PhotoActivity.this), PhotoActivity.class
									.getDeclaredMethod(
											"onPostExecutePhotoDoLikeDislike",
											cArg), null);
					asyncTaskExecuteGet.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(PhotoActivity.this, "Cevap işlenemedi",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		ImageButton dislikeImageButton = (ImageButton) this
				.findViewById(R.id.photoDislikeButton);
		dislikeImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", getIntent()
						.getExtras().getString("photoId")));
				nameValuePairs.add(new BasicNameValuePair("isLike", "false"));
				try {
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							PhotoActivity.this, HttpUtil.buildUrl(
									"/nativeapp/likedislike", nameValuePairs,
									PhotoActivity.this), PhotoActivity.class
									.getDeclaredMethod(
											"onPostExecutePhotoDoLikeDislike",
											cArg), null);
					asyncTaskExecuteGet.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(PhotoActivity.this, "Cevap işlenemedi",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/comment/get",
							nameValuePairs, PhotoActivity.this),
					PhotoActivity.class.getDeclaredMethod(
							"onPostExecutePhotoCommentGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}

		Button commentSendButton = (Button) this
				.findViewById(R.id.commentSendButton);
		commentSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText commentEditText = (EditText) findViewById(R.id.commentEditText);
				if (ValidityUtils.sendCommentValidity(commentEditText)) {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("id", getIntent()
							.getExtras().getString("photoId")));
					nameValuePairs.add(new BasicNameValuePair("commentText",
							commentEditText.getText().toString()));
					final Class<?>[] cArg = new Class[2];
					cArg[0] = String.class;
					cArg[1] = Dialog.class;

					try {
						AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
								PhotoActivity.this, HttpUtil.buildUrl(
										"/nativeapp/comment/save",
										nameValuePairs, PhotoActivity.this),
								PhotoActivity.class.getDeclaredMethod(
										"onPostExecutePhotoCommentSave", cArg),
								null);
						asyncTaskExecuteGet.execute();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(PhotoActivity.this, "Cevap işlenemedi",
								Toast.LENGTH_LONG).show();
					}

				}
			}
		});
	}

	public void onPostExecutePhotoGet(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
					PhotoActivity.this.finish();
				} else {
					Image image = new Image();
					image.setPath(jsonObject.getJSONObject("result")
							.getString("path")
							.replace("localhost:80", "192.168.2.5"));
					image.setTimestamp(jsonObject.getJSONObject("result")
							.getString("dateAdded"));
					ImageLoader imageLoader = new ImageLoader(
							PhotoActivity.this);
					ImageView imageView = (ImageView) PhotoActivity.this
							.findViewById(R.id.photoId);
					imageLoader.DisplayImage(image.getPath(), imageView);
					TextView textView = (TextView) PhotoActivity.this
							.findViewById(R.id.photoDateText);
					textView.setText(image.getTimestamp());
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePhotoLikeDislike(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
					PhotoActivity.this.finish();
				} else {
					TextView textView = (TextView) PhotoActivity.this
							.findViewById(R.id.photoLikeText);
					textView.setText(String.valueOf(jsonObject.getJSONObject(
							"result").getInt("likeCount")));
					TextView textView2 = (TextView) PhotoActivity.this
							.findViewById(R.id.photoDislikeText);
					textView2.setText(String.valueOf(jsonObject.getJSONObject(
							"result").getInt("dislikeCount")));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePhotoDoLikeDislike(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
				} else {
					try {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("id",
								getIntent().getExtras().getString("photoId")));
						final Class<?>[] cArg = new Class[2];
						cArg[0] = String.class;
						cArg[1] = Dialog.class;
						AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
								this, HttpUtil.buildUrl(
										"/nativeapp/likedislike/get",
										nameValuePairs, PhotoActivity.this),
								PhotoActivity.class.getDeclaredMethod(
										"onPostExecutePhotoLikeDislike", cArg),
								null);
						asyncTaskExecuteGet.execute();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(this, "Cevap işlenemedi",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePhotoCommentGet(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				List<Comment> comments = new ArrayList<Comment>();
				for (int j = 0; j < jsonObject.getJSONArray("result").length(); j++) {
					Comment comment = new Comment();

					comment.setCommentText("\""
							+ jsonObject.getJSONArray("result")
									.getJSONObject(j).getString("commentText")
							+ "\"");
					comment.setDateAdded(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("dateAdded"));
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
				LinearLayout commentsRelativeLayout = (LinearLayout) PhotoActivity.this
						.findViewById(R.id.commentLayout);
				commentsRelativeLayout.removeAllViews();
				if (comments.size() == 0) {
					commentsRelativeLayout.setVisibility(View.GONE);
				} else {
					commentsRelativeLayout.setVisibility(View.VISIBLE);
					CommentAdapter commentAdapter = new CommentAdapter(
							PhotoActivity.this, R.layout.comment_item,
							comments, null);
					for (int k = 0; k < comments.size(); k++) {
						commentsRelativeLayout.addView(commentAdapter.getView(
								k, null, null));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePhotoCommentSave(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				EditText commentEditText = (EditText) findViewById(R.id.commentEditText);
				commentEditText.setText("");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", getIntent()
						.getExtras().getString("photoId")));
				final Class<?>[] cArg = new Class[2];
				cArg[0] = String.class;
				cArg[1] = Dialog.class;
				try {
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							this, HttpUtil.buildUrl("/nativeapp/comment/get",
									nameValuePairs, PhotoActivity.this),
							PhotoActivity.class.getDeclaredMethod(
									"onPostExecutePhotoCommentGet", cArg), null);
					asyncTaskExecuteGet.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG)
							.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}
}
