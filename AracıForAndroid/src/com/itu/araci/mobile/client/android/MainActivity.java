package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecutePost;
import com.itu.araci.mobile.client.android.util.HttpUtil;
import com.itu.araci.mobile.client.android.util.ValidityUtils;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button homeSignInButton = (Button) this
				.findViewById(R.id.homeSignInButton);
		homeSignInButton
				.setOnClickListener(new HomeSignInButtonOnClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class HomeSignInButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			loginDialog().show();
		}
	}

	private class DialogSignInButtonOnClickListener implements OnClickListener {
		private Dialog parentDialog;

		public DialogSignInButtonOnClickListener(Dialog parentDialog) {
			super();
			this.parentDialog = parentDialog;
		}

		@Override
		public void onClick(View v) {
			EditText emailEditText = (EditText) parentDialog
					.findViewById(R.id.signInEMail);

			EditText passwordEditText = (EditText) parentDialog
					.findViewById(R.id.signInPassword);

			if (ValidityUtils.signInValidity(emailEditText, passwordEditText)) {

				Map<String, String> params = new HashMap<String, String>();
				params.put("j_username", emailEditText.getText().toString());
				params.put("j_password", passwordEditText.getText().toString());

				Class<?>[] cArg = new Class[2];
				cArg[0] = String.class;
				cArg[1] = Dialog.class;
				try {
					AsyncTaskExecutePost asyncTaskExecutePost = new AsyncTaskExecutePost(
							MainActivity.this, HttpUtil.buildUrl(
									"/j_spring_security_check", null,
									MainActivity.this), MainActivity.this
									.getClass().getDeclaredMethod(
											"onPostExecuteSignIn", cArg),
							parentDialog, params);
					asyncTaskExecutePost.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private class DialogSignUpButtonOnClickListener implements OnClickListener {

		private Dialog parentDialog;

		public DialogSignUpButtonOnClickListener(Dialog parentDialog) {
			super();
			this.parentDialog = parentDialog;
		}

		@Override
		public void onClick(View v) {
			parentDialog.dismiss();
			signUpDialog().show();
		}
	}

	public void onPostExecuteSignUp(String result, Dialog parentDialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this,
							"Kayıt başarılı. Lütfen giriş yapınız.",
							Toast.LENGTH_LONG).show();
					parentDialog.dismiss();
					loginDialog().show();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteSignIn(String result, Dialog dialog) {
		try {
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
					Toast.makeText(this, jsonObject.getString("message"),
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_LONG)
							.show();
					Intent intent = new Intent(MainActivity.this,
							UserProfileActivity.class);
					startActivity(intent);
					dialog.dismiss();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	private Dialog loginDialog() {
		Dialog dialog = new Dialog(MainActivity.this, R.style.DialogTheme);
		dialog.setContentView(R.layout.dialog_login);
		dialog.setTitle("Giriş Yap");

		Button dialogSignInButton = (Button) dialog
				.findViewById(R.id.dialogSignInButton);
		dialogSignInButton
				.setOnClickListener(new DialogSignInButtonOnClickListener(
						dialog));

		Button dialogSignUpButton = (Button) dialog
				.findViewById(R.id.dialogSignUpButton);
		dialogSignUpButton
				.setOnClickListener(new DialogSignUpButtonOnClickListener(
						dialog));
		return dialog;
	}

	private Dialog signUpDialog() {
		final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogTheme);
		dialog.setContentView(R.layout.dialog_signup);
		dialog.setTitle("Kayıt Ol");
		final EditText birthDateButton = (EditText) dialog
				.findViewById(R.id.signUpBirthDateEditText);
		birthDateButton.setKeyListener(null);
		birthDateButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					DialogFragment newFragment = new SelectDateFragment(
							birthDateButton);
					newFragment.show(getSupportFragmentManager(), "DatePicker");
				}
			}
		});
		Button signUpButton = (Button) dialog
				.findViewById(R.id.dialogSignUpButton);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ValidityUtils.signUpValidity((EditText) dialog
						.findViewById(R.id.signUpEMail), (EditText) dialog
						.findViewById(R.id.signUpPassword), (EditText) dialog
						.findViewById(R.id.signUpPasswordRepeat),
						(EditText) dialog.findViewById(R.id.signUpName),
						(EditText) dialog.findViewById(R.id.signUpSurname),
						(RadioGroup) dialog.findViewById(R.id.signUpGender),
						(EditText) dialog
								.findViewById(R.id.signUpBirthDateEditText))) {

					String email = ((EditText) dialog
							.findViewById(R.id.signUpEMail)).getText()
							.toString();
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
					nameValuePairs.add(new BasicNameValuePair("email", email));
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
								MainActivity.this, HttpUtil.buildUrl(
										"/nativeapp/user/save", nameValuePairs,
										MainActivity.this), MainActivity.this
										.getClass().getDeclaredMethod(
												"onPostExecuteSignUp", cArg),
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
}
