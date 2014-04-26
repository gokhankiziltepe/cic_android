package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.adapter.CheckInAdapter;
import com.itu.araci.mobile.client.android.model.CheckIn;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class CheckInActivity extends BaseActivity {
	final Class<?>[] cArg = new Class[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_in);

		cArg[0] = String.class;
		cArg[1] = Dialog.class;
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/checkin/get", null,
							CheckInActivity.this),
					CheckInActivity.class.getDeclaredMethod(
							"onPostExecuteCheckInGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteCheckInGet(String result, Dialog parentDialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				List<CheckIn> checkIns = new ArrayList<CheckIn>();
				for (int j = 0; j < jsonObject.getJSONArray("result").length(); j++) {
					CheckIn checkIn = new CheckIn();
					checkIn.setId(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("id"));
					checkIn.setDateAdded(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("dateAdded"));
					checkIn.setPlaceId(jsonObject.getJSONArray("result")
							.getJSONObject(j).getString("placeId"));
					checkIn.setPlaceName(jsonObject.getJSONArray("result")
							.getJSONObject(j).getJSONObject("place")
							.getString("name"));
					checkIns.add(checkIn);
				}

				if (checkIns.size() == 0) {
					Toast.makeText(this, "Henüz check-in yapılmamış",
							Toast.LENGTH_LONG).show();
					CheckInActivity.this.finish();
				} else {
					ListView listView = (ListView) findViewById(R.id.checkInList);
					final CheckInAdapter checkInAdapter = new CheckInAdapter(
							CheckInActivity.this, R.layout.check_in_item,
							checkIns);
					listView.setAdapter(checkInAdapter);
					listView.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, final int arg2, long arg3) {
							final AlertDialog alertDialog = new AlertDialog.Builder(
									CheckInActivity.this)
									.setCancelable(true)
									.setTitle("Uyarı")
									.setMessage(
											"Yer bildirimini silmek istediğinize emin misiniz?")
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
															"checkInId",
															checkInAdapter
																	.getItem(
																			arg2)
																	.getId()));
											try {
												AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
														CheckInActivity.this,
														HttpUtil.buildUrl(
																"/nativeapp/checkin/delete",
																nameValuePairs,
																CheckInActivity.this),
														CheckInActivity.class
																.getDeclaredMethod(
																		"onPostExecutePlaceCheckInDelete",
																		cArg),
														null);
												asyncTaskExecuteGet.execute();
											} catch (Exception e) {
												e.printStackTrace();
												Toast.makeText(
														CheckInActivity.this,
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

			}

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecutePlaceCheckInDelete(String result,
			Dialog parentDialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Yer bildirimi bilginiz silindi",
						Toast.LENGTH_LONG).show();
				try {
					AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
							this, HttpUtil.buildUrl("/nativeapp/checkin/get",
									null, CheckInActivity.this),
							CheckInActivity.class.getDeclaredMethod(
									"onPostExecuteCheckInGet", cArg), null);
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
}
