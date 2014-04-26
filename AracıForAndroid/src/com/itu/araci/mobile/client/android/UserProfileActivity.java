package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.adapter.ImageAdapter;
import com.itu.araci.mobile.client.android.adapter.SelectorAdapter;
import com.itu.araci.mobile.client.android.model.Image;
import com.itu.araci.mobile.client.android.model.User;
import com.itu.araci.mobile.client.android.session.UserSingleton;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;
import com.itu.araci.mobile.client.android.util.ValidityUtils;

public class UserProfileActivity extends BaseActivity {
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.pin, R.string.open, R.string.close) {
			public void onDrawerClosed(View view) {

			}

			public void onDrawerOpened(View drawerView) {

			}
		};
		final Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		List<String> drawerList = new ArrayList<String>();
		drawerList.add("Etkinliklerim");
		drawerList.add("Hesap Güncelle");
		drawerList.add("Çıkış Yap");
		SelectorAdapter selectorAdapter = new SelectorAdapter(this,
				R.layout.spinner_item, R.id.spinnerItemText, drawerList);
		mDrawerList.setAdapter(selectorAdapter);
		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case 0:
					
					break;
				case 1:
					updateDialog().show();
					break;
				case 2:
					try {
						AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
								UserProfileActivity.this, HttpUtil.buildUrl(
										"/j_spring_security_logout", null,
										UserProfileActivity.this),
								UserProfileActivity.class.getDeclaredMethod(
										"onPostExecuteUserLogout", cArg), null);
						asyncTaskExecuteGet.execute();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(),
								"Cevap işlenemedi", Toast.LENGTH_SHORT).show();
					}
					break;
				}
			}

		});

		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/user", null,
							UserProfileActivity.this),
					UserProfileActivity.class.getDeclaredMethod(
							"onPostExecuteUserProfile", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
					Toast.LENGTH_SHORT).show();
		}

		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/user/photos", null,
							UserProfileActivity.this),
					UserProfileActivity.class.getDeclaredMethod(
							"onPostExecuteUserProfilePhotos", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
					Toast.LENGTH_SHORT).show();
		}
		RelativeLayout checkInRelativeLayout = (RelativeLayout) findViewById(R.id.userProfileCheckIn);
		checkInRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserProfileActivity.this,
						CheckInActivity.class);
				startActivity(i);
			}
		});
		RelativeLayout commentRelativeLayout = (RelativeLayout) findViewById(R.id.userProfileComment);
		commentRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserProfileActivity.this,
						CommentActivity.class);
				startActivity(i);
			}
		});
		RelativeLayout followersRelativeLayout = (RelativeLayout) findViewById(R.id.userProfileFollowerRelativeLayout);
		followersRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserProfileActivity.this,
						FollowerActivity.class);
				startActivity(i);
			}
		});
		RelativeLayout followedsRelativeLayout = (RelativeLayout) findViewById(R.id.userProfileFollowedRelativeLayout);
		followedsRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserProfileActivity.this,
						FollowedActivity.class);
				startActivity(i);
			}
		});
		RelativeLayout userProfileFeedback = (RelativeLayout) findViewById(R.id.userProfileFeedback);
		userProfileFeedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserProfileActivity.this,
						FeedbackActivity.class);
				startActivity(i);
			}
		});
		RelativeLayout likeRelativeLayout = (RelativeLayout) findViewById(R.id.userProfileLike);
		likeRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserProfileActivity.this,
						LikeActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onPostExecuteUserProfile(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
					UserProfileActivity.this.finish();
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
					UserSingleton.getInstance().setUser(user);

					TextView textView = (TextView) UserProfileActivity.this
							.findViewById(R.id.userProfileHeaderMessage);
					textView.setText(name + " " + surname);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteUserProfilePhotos(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
					UserProfileActivity.this.finish();
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
						RelativeLayout photosRelativeLayout = (RelativeLayout) UserProfileActivity.this
								.findViewById(R.id.userProfilePhotoRelativeLayout);
						photosRelativeLayout.setVisibility(View.GONE);
					} else {
						ImageAdapter imageAdapter = new ImageAdapter(
								UserProfileActivity.this, R.layout.image_item,
								photos);
						LinearLayout linearLayout = (LinearLayout) UserProfileActivity.this
								.findViewById(R.id.userProfilePhotoHorizontalLinear);
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

	public void onPostExecuteUserLogout(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
					UserProfileActivity.this.finish();
				} else {
					Toast.makeText(this, "Çıkış Yapıldı", Toast.LENGTH_LONG)
							.show();
					UserProfileActivity.this.finish();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	private Dialog updateDialog() {
		final Dialog dialog = new Dialog(UserProfileActivity.this,
				R.style.DialogTheme);
		dialog.setContentView(R.layout.dialog_update);
		dialog.setTitle("Hesap Güncelle");
		final EditText birthDateButton = (EditText) dialog
				.findViewById(R.id.signUpBirthDateEditText);
		birthDateButton.setKeyListener(null);
		birthDateButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DialogFragment newFragment = new SelectDateFragment(birthDateButton);
					newFragment.show(getSupportFragmentManager(), "DatePicker");
				}
			}
		});

		EditText nameEditText = (EditText) dialog.findViewById(R.id.signUpName);
		nameEditText.setText(user.getName());
		EditText surnameEditText = (EditText) dialog
				.findViewById(R.id.signUpSurname);
		surnameEditText.setText(user.getSurname());
		EditText passwordEditText = (EditText) dialog
				.findViewById(R.id.signUpPassword);
		passwordEditText.setText(user.getPassword());
		EditText passwordRepeatEditText = (EditText) dialog
				.findViewById(R.id.signUpPasswordRepeat);
		passwordRepeatEditText.setText(user.getPasswordRepeat());
		if (user.getGender().equalsIgnoreCase("Erkek")) {
			RadioButton maleRadioButton = (RadioButton) dialog
					.findViewById(R.id.signUpGenderMale);
			maleRadioButton.setChecked(true);
			RadioButton femaleRadioButton = (RadioButton) dialog
					.findViewById(R.id.signUpGenderFemale);
			femaleRadioButton.setChecked(false);
		} else {
			RadioButton femaleRadioButton = (RadioButton) dialog
					.findViewById(R.id.signUpGenderFemale);
			femaleRadioButton.setChecked(true);
			RadioButton maleRadioButton = (RadioButton) dialog
					.findViewById(R.id.signUpGenderMale);
			maleRadioButton.setChecked(false);
		}
		EditText birthDateEditText = (EditText) dialog
				.findViewById(R.id.signUpBirthDateEditText);
		birthDateEditText.setText(user.getBirthDate());
		Button signUpButton = (Button) dialog
				.findViewById(R.id.dialogSignUpButton);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ValidityUtils.signUpValidity(null, (EditText) dialog
						.findViewById(R.id.signUpPassword), (EditText) dialog
						.findViewById(R.id.signUpPasswordRepeat),
						(EditText) dialog.findViewById(R.id.signUpName),
						(EditText) dialog.findViewById(R.id.signUpSurname),
						(RadioGroup) dialog.findViewById(R.id.signUpGender),
						(EditText) dialog
								.findViewById(R.id.signUpBirthDateEditText))) {

					String password = ((EditText) dialog
							.findViewById(R.id.signUpPassword)).getText()
							.toString();
					String name = ((EditText) dialog
							.findViewById(R.id.signUpName)).getText()
							.toString();
					String surname = ((EditText) dialog
							.findViewById(R.id.signUpSurname)).getText()
							.toString();
					RadioGroup radioGroup = ((RadioGroup) dialog
							.findViewById(R.id.signUpGender));
					RadioButton radioButton = (RadioButton) dialog
							.findViewById(radioGroup.getCheckedRadioButtonId());
					String gender = radioButton.getText().toString();
					String birthDate = ((EditText) dialog
							.findViewById(R.id.signUpBirthDateEditText))
							.getText().toString();
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("password",
							password));
					nameValuePairs.add(new BasicNameValuePair("name", name));
					nameValuePairs.add(new BasicNameValuePair("surname",
							surname));
					nameValuePairs
							.add(new BasicNameValuePair("gender", gender));
					nameValuePairs.add(new BasicNameValuePair("birthDate",
							birthDate));
					try {
						Class<?>[] cArg = new Class[2];
						cArg[0] = String.class;
						cArg[1] = Dialog.class;

						AsyncTaskExecuteGet asyncTaskExecute = new AsyncTaskExecuteGet(
								UserProfileActivity.this, HttpUtil.buildUrl(
										"/nativeapp/user/update",
										nameValuePairs,
										UserProfileActivity.this),
								UserProfileActivity.this.getClass()
										.getDeclaredMethod(
												"onPostExecuteUpdate", cArg),
								dialog);
						asyncTaskExecute.execute();
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"İstek oluşturulamadı", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});
		return dialog;
	}

	public void onPostExecuteUpdate(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
				} else {

					Toast.makeText(this, "Hesabınız güncellendi",
							Toast.LENGTH_LONG).show();
					UserProfileActivity.this.finish();
					Intent intent = getIntent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					finish();
					startActivity(intent);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}
}
