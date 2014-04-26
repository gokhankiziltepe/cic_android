package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.adapter.CommentAdapter;
import com.itu.araci.mobile.client.android.adapter.ImageAdapter;
import com.itu.araci.mobile.client.android.adapter.PhoneAdapter;
import com.itu.araci.mobile.client.android.adapter.WorkingHourAdapter;
import com.itu.araci.mobile.client.android.model.Comment;
import com.itu.araci.mobile.client.android.model.Image;
import com.itu.araci.mobile.client.android.model.Place;
import com.itu.araci.mobile.client.android.model.WorkingHour;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;
import com.itu.araci.mobile.client.android.util.ValidityUtils;

public class PlaceActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place);
		final Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", getIntent()
					.getExtras().getString("placeId")));
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/place/get",
							nameValuePairs, PlaceActivity.this),
					PlaceActivity.class.getDeclaredMethod(
							"onPostExecutePlaceGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
					Toast.LENGTH_SHORT).show();
		}

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("entityBaseId",
					getIntent().getExtras().getString("placeId")));
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/photo/entity/get",
							nameValuePairs, PlaceActivity.this),
					PlaceActivity.class.getDeclaredMethod(
							"onPostExecutePlacePhotoGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
					Toast.LENGTH_SHORT).show();
		}

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", String
					.valueOf(getIntent().getExtras().getString("placeId"))));
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/likedislike/get",
							nameValuePairs, PlaceActivity.this),
					PlaceActivity.class.getDeclaredMethod(
							"onPostExecutePlaceLikeDislike", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}

		RelativeLayout workingHourRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.placeWorkingHourRelativeLayout);
		workingHourRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Dialog dialog = new Dialog(PlaceActivity.this,
						R.style.DialogTheme);
				dialog.setContentView(R.layout.working_hour);
				dialog.setTitle("Çalışma Saatleri");

				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("placeId", String
							.valueOf(getIntent().getExtras().getString(
									"placeId"))));
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							PlaceActivity.this, HttpUtil.buildUrl(
									"/nativeapp/workinghour/get",
									nameValuePairs, PlaceActivity.this),
							PlaceActivity.class.getDeclaredMethod(
									"onPostExecutePlaceWorkingHourGet", cArg),
							dialog);
					asyncTaskExecuteGet.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
							Toast.LENGTH_SHORT).show();
				}

				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("placeId", String
							.valueOf(getIntent().getExtras().getString(
									"placeId"))));
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							PlaceActivity.this, HttpUtil.buildUrl(
									"/nativeapp/phone/get", nameValuePairs,
									PlaceActivity.this),
							PlaceActivity.class.getDeclaredMethod(
									"onPostExecutePlacePhoneGet", cArg), dialog);
					asyncTaskExecuteGet.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		RelativeLayout placeCheckInRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.placeCheckInRelativeLayout);
		placeCheckInRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final AlertDialog alertDialog = new AlertDialog.Builder(
						PlaceActivity.this)
						.setCancelable(true)
						.setTitle("Uyarı")
						.setMessage(
								"Yer bildirimi yapmak istediğinize emin misiniz?")
						.create();

				alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Evet",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
								nameValuePairs.add(new BasicNameValuePair(
										"placeId", getIntent().getExtras()
												.getString("placeId")));
								try {
									AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
											PlaceActivity.this,
											HttpUtil.buildUrl(
													"/nativeapp/checkin",
													nameValuePairs,
													PlaceActivity.this),
											PlaceActivity.class
													.getDeclaredMethod(
															"onPostExecutePlaceCheckIn",
															cArg), null);
									asyncTaskExecuteGet.execute();
								} catch (Exception e) {
									e.printStackTrace();
									Toast.makeText(PlaceActivity.this,
											"Cevap işlenemedi",
											Toast.LENGTH_LONG).show();
								}
							}

						});
				alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Hayır",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								alertDialog.dismiss();
							}
						});
				alertDialog.show();
			}
		});
		RelativeLayout placeFollowRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.placeFollowRelativeLayout);
		placeFollowRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final AlertDialog alertDialog = new AlertDialog.Builder(
						PlaceActivity.this)
						.setCancelable(true)
						.setTitle("Uyarı")
						.setMessage(
								"Mekanı takip etmek istediğinize emin misiniz?")
						.create();

				alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Evet",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
								nameValuePairs.add(new BasicNameValuePair(
										"placeId", getIntent().getExtras()
												.getString("placeId")));
								try {
									AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
											PlaceActivity.this,
											HttpUtil.buildUrl(
													"/nativeapp/follow",
													nameValuePairs,
													PlaceActivity.this),
											PlaceActivity.class
													.getDeclaredMethod(
															"onPostExecutePlaceFollow",
															cArg), null);
									asyncTaskExecuteGet.execute();
								} catch (Exception e) {
									e.printStackTrace();
									Toast.makeText(PlaceActivity.this,
											"Cevap işlenemedi",
											Toast.LENGTH_LONG).show();
								}
							}

						});
				alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Hayır",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								alertDialog.dismiss();
							}
						});
				alertDialog.show();
			}
		});
		RelativeLayout placeLocationRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.placeLocationRelativeLayout);
		placeLocationRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PlaceActivity.this,
						GoogleMapActivity.class);
				i.putExtra("placeId",
						getIntent().getExtras().getString("placeId"));
				startActivity(i);
			}
		});

		RelativeLayout placeFeedbackRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.placeFeedbackRelativeLayout);
		placeFeedbackRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(PlaceActivity.this,
						R.style.DialogTheme);
				dialog.setContentView(R.layout.dialog_feedback);
				dialog.setTitle("Geribildirim Yap");
				final SeekBar ambianceSeekBar = (SeekBar) dialog
						.findViewById(R.id.feedbackInputAmbiance);
				ambianceSeekBar
						.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onProgressChanged(SeekBar seekBar,
									int progress, boolean fromUser) {
								TextView textView = (TextView) dialog
										.findViewById(R.id.feedbackValueAmbiance);
								textView.setText(String.valueOf(progress));

							}
						});
				final SeekBar priceSeekBar = (SeekBar) dialog
						.findViewById(R.id.feedbackInputPrice);
				priceSeekBar
						.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onProgressChanged(SeekBar seekBar,
									int progress, boolean fromUser) {

								TextView textView = (TextView) dialog
										.findViewById(R.id.feedbackValuePrice);
								textView.setText(String.valueOf(progress));

							}
						});
				final SeekBar flavourSeekBar = (SeekBar) dialog
						.findViewById(R.id.feedbackInputFlavour);
				flavourSeekBar
						.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onProgressChanged(SeekBar seekBar,
									int progress, boolean fromUser) {

								TextView textView = (TextView) dialog
										.findViewById(R.id.feedbackValueFlavour);
								textView.setText(String.valueOf(progress));

							}
						});
				final SeekBar serviceSeekBar = (SeekBar) dialog
						.findViewById(R.id.feedbackInputService);
				serviceSeekBar
						.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onProgressChanged(SeekBar seekBar,
									int progress, boolean fromUser) {

								TextView textView = (TextView) dialog
										.findViewById(R.id.feedbackValueService);
								textView.setText(String.valueOf(progress));

							}
						});

				Button sendFeedbackButton = (Button) dialog
						.findViewById(R.id.feedbackSendButton);
				sendFeedbackButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer ambiance = ambianceSeekBar.getProgress();
						Integer price = priceSeekBar.getProgress();
						Integer service = serviceSeekBar.getProgress();
						Integer flavour = flavourSeekBar.getProgress();
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("placeId",
								getIntent().getExtras().getString("placeId")));
						nameValuePairs.add(new BasicNameValuePair("ambiance",
								ambiance.toString()));
						nameValuePairs.add(new BasicNameValuePair("price",
								price.toString()));
						nameValuePairs.add(new BasicNameValuePair("service",
								service.toString()));
						nameValuePairs.add(new BasicNameValuePair("flavour",
								flavour.toString()));
						try {
							AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
									PlaceActivity.this,
									HttpUtil.buildUrl("/nativeapp/feedback",
											nameValuePairs, PlaceActivity.this),
									PlaceActivity.class.getDeclaredMethod(
											"onPostExecutePlaceFeedback", cArg),
									dialog);
							asyncTaskExecuteGet.execute();
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(PlaceActivity.this,
									"Cevap işlenemedi", Toast.LENGTH_LONG)
									.show();
						}
					}
				});
				dialog.show();
			}
		});
		ImageButton likeImageButton = (ImageButton) this
				.findViewById(R.id.placeLikeButton);
		likeImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", getIntent()
						.getExtras().getString("placeId")));
				nameValuePairs.add(new BasicNameValuePair("isLike", "true"));
				try {
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							PlaceActivity.this, HttpUtil.buildUrl(
									"/nativeapp/likedislike", nameValuePairs,
									PlaceActivity.this), PlaceActivity.class
									.getDeclaredMethod(
											"onPostExecutePlaceDoLikeDislike",
											cArg), null);
					asyncTaskExecuteGet.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(PlaceActivity.this, "Cevap işlenemedi",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		ImageButton dislikeImageButton = (ImageButton) this
				.findViewById(R.id.placeDislikeButton);
		dislikeImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", getIntent()
						.getExtras().getString("placeId")));
				nameValuePairs.add(new BasicNameValuePair("isLike", "false"));
				try {
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							PlaceActivity.this, HttpUtil.buildUrl(
									"/nativeapp/likedislike", nameValuePairs,
									PlaceActivity.this), PlaceActivity.class
									.getDeclaredMethod(
											"onPostExecutePlaceDoLikeDislike",
											cArg), null);
					asyncTaskExecuteGet.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(PlaceActivity.this, "Cevap işlenemedi",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", getIntent()
					.getExtras().getString("placeId")));
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/comment/get",
							nameValuePairs, PlaceActivity.this),
					PlaceActivity.class.getDeclaredMethod(
							"onPostExecutePlaceCommentGet", cArg), null);
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
							.getExtras().getString("placeId")));
					nameValuePairs.add(new BasicNameValuePair("commentText",
							commentEditText.getText().toString()));
					final Class<?>[] cArg = new Class[2];
					cArg[0] = String.class;
					cArg[1] = Dialog.class;

					try {
						AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
								PlaceActivity.this, HttpUtil.buildUrl(
										"/nativeapp/comment/save",
										nameValuePairs, PlaceActivity.this),
								PlaceActivity.class.getDeclaredMethod(
										"onPostExecutePlaceCommentSave", cArg),
								null);
						asyncTaskExecuteGet.execute();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(PlaceActivity.this, "Cevap işlenemedi",
								Toast.LENGTH_LONG).show();
					}

				}
			}
		});

	}

	public void onPostExecutePlaceGet(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				PlaceActivity.this.finish();
			} else {
				Place place = new Place();
				place.setName(jsonObject.getJSONObject("result").getString(
						"name"));
				place.setDescription(jsonObject.getJSONObject("result")
						.getString("description"));
				place.setCapacity(jsonObject.getJSONObject("result").getInt(
						"capacity"));

				TextView placeHeader = (TextView) findViewById(R.id.placeHeaderMessage);
				placeHeader.setText(place.getName());

				TextView placeDescription = (TextView) findViewById(R.id.placeDescriptionText);
				placeDescription.setText(place.getDescription());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePlacePhotoGet(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				PlaceActivity.this.finish();
			} else {
				List<Image> photos = new ArrayList<Image>();
				for (int j = 0; j < jsonObject.getJSONArray("result").length(); j++) {
					Image image = new Image();
					image.setId(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("id"));
					image.setPath(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("path"));
					String timestamp = jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("dateAdded");

					image.setTimestamp(timestamp);
					photos.add(image);
				}
				if (photos.size() == 0) {
					RelativeLayout photosRelativeLayout = (RelativeLayout) PlaceActivity.this
							.findViewById(R.id.placePhotoRelativeLayout);
					photosRelativeLayout.setVisibility(View.GONE);
				} else {
					ImageAdapter imageAdapter = new ImageAdapter(
							PlaceActivity.this, R.layout.image_item, photos);
					LinearLayout linearLayout = (LinearLayout) PlaceActivity.this
							.findViewById(R.id.placePhotoHorizontalLinear);
					for (int j = 0; j < photos.size(); j++) {
						linearLayout.addView(imageAdapter
								.getView(j, null, null));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePlaceWorkingHourGet(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				List<WorkingHour> workingHours = new ArrayList<WorkingHour>();
				for (int j = 0; j < jsonObject.getJSONArray("result").length(); j++) {
					WorkingHour workingHour = new WorkingHour();
					workingHour.setDay(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("day"));
					workingHour.setStart(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("start"));
					workingHour.setEnd(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("end"));

					workingHours.add(workingHour);
				}
				if (workingHours.size() == 0) {
					RelativeLayout workingHoursRelativeLayout = (RelativeLayout) dialog
							.findViewById(R.id.workingHourRelativeLayout);
					workingHoursRelativeLayout.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(),
							"Çalışma saati kaydı bulunamadı", Toast.LENGTH_LONG)
							.show();
				} else {
					WorkingHourAdapter workingHourAdapter = new WorkingHourAdapter(
							PlaceActivity.this, R.layout.working_hour_item,
							workingHours);
					LinearLayout linearLayout = (LinearLayout) dialog
							.findViewById(R.id.workingHourList);
					for (int j = 0; j < workingHours.size(); j++) {
						linearLayout.addView(workingHourAdapter.getView(j,
								null, null));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePlacePhoneGet(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				List<String> phones = new ArrayList<String>();
				for (int j = 0; j < jsonObject.getJSONArray("result").length(); j++) {
					String phoneText = jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("phoneText");
					phones.add(phoneText);
				}
				if (phones.size() == 0) {
					RelativeLayout phoneRelativeLayout = (RelativeLayout) dialog
							.findViewById(R.id.phoneRelativeLayout);
					phoneRelativeLayout.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(),
							"Telefon kaydı bulunamadı", Toast.LENGTH_LONG)
							.show();
				} else {
					PhoneAdapter phoneAdapter = new PhoneAdapter(
							PlaceActivity.this, R.layout.working_hour_item,
							phones);
					LinearLayout linearLayout = (LinearLayout) dialog
							.findViewById(R.id.phoneList);
					for (int j = 0; j < phones.size(); j++) {
						linearLayout.addView(phoneAdapter
								.getView(j, null, null));
					}
				}
				dialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePlaceLikeDislike(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				TextView textView = (TextView) PlaceActivity.this
						.findViewById(R.id.placeDislikeText);

				TextView textView2 = (TextView) PlaceActivity.this
						.findViewById(R.id.placeLikeText);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
					textView.setText("0");
					textView2.setText("0");
				} else {
					textView2.setText(String.valueOf(jsonObject.getJSONObject(
							"result").getInt("likeCount")));
					textView.setText(String.valueOf(jsonObject.getJSONObject(
							"result").getInt("dislikeCount")));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePlaceDoLikeDislike(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);

			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("id", getIntent()
							.getExtras().getString("placeId")));
					final Class<?>[] cArg = new Class[2];
					cArg[0] = String.class;
					cArg[1] = Dialog.class;
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							this, HttpUtil.buildUrl(
									"/nativeapp/likedislike/get",
									nameValuePairs, PlaceActivity.this),
							PlaceActivity.class.getDeclaredMethod(
									"onPostExecutePlaceLikeDislike", cArg),
							null);
					asyncTaskExecuteGet.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG)
							.show();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePlaceCheckIn(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);

			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Yer bildirimi eklendi", Toast.LENGTH_LONG)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePlaceFollow(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);

			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Takip bilgisi eklendi", Toast.LENGTH_LONG)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePlaceCommentGet(String result, Dialog dialog) {
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
				LinearLayout commentsRelativeLayout = (LinearLayout) PlaceActivity.this
						.findViewById(R.id.commentLayout);
				commentsRelativeLayout.removeAllViews();
				if (comments.size() == 0) {
					commentsRelativeLayout.setVisibility(View.GONE);
				} else {
					commentsRelativeLayout.setVisibility(View.VISIBLE);
					CommentAdapter commentAdapter = new CommentAdapter(
							PlaceActivity.this, R.layout.comment_item,
							comments, null);
					Log.e("size", comments.size() + "");
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

	public void onPostExecutePlaceCommentSave(String result, Dialog dialog) {
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
						.getExtras().getString("placeId")));
				final Class<?>[] cArg = new Class[2];
				cArg[0] = String.class;
				cArg[1] = Dialog.class;
				try {
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							this, HttpUtil.buildUrl("/nativeapp/comment/get",
									nameValuePairs, PlaceActivity.this),
							PlaceActivity.class.getDeclaredMethod(
									"onPostExecutePlaceCommentGet", cArg), null);
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

	public void onPostExecutePlaceFeedback(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Geri beslemeniz kaydedildi",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		} finally {
			dialog.dismiss();
		}
	}
}
