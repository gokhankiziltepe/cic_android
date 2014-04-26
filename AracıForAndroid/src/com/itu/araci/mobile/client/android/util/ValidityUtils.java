package com.itu.araci.mobile.client.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.itu.araci.mobile.client.android.R;

public class ValidityUtils {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static Boolean signInValidity(EditText emailEditText,
			EditText passwordEditText) {
		Boolean isValid = true;
		if (emailEditText.getText().toString().length() == 0) {
			isValid = false;
			emailEditText.setError("E-posta doldurulmalı");
			showError(emailEditText);
		} else if (emailEditText.getText().toString().length() > 255) {
			isValid = false;
			emailEditText.setError("E-posta uzun dolduruldu");
			showError(emailEditText);
		} else {
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(emailEditText.getText()
					.toString());
			if (!matcher.matches()) {
				isValid = false;
				emailEditText.setError("E-posta biçimi hatalı");
				showError(emailEditText);
			}
		}
		if (passwordEditText.getText().toString().length() == 0) {
			isValid = false;
			passwordEditText.setError("Şifre doldurulmalı");
			showError(passwordEditText);
		} else if (passwordEditText.getText().toString().length() > 255) {
			isValid = false;
			passwordEditText.setError("Şifre uzun dolduruldu");
			showError(passwordEditText);
		} else if (passwordEditText.getText().toString().length() < 4) {
			isValid = false;
			passwordEditText.setError("Şifre kısa dolduruldu");
			showError(passwordEditText);
		}
		return isValid;
	}

	public static Boolean signUpValidity(EditText emailEditText,
			EditText passwordEditText, EditText passwordRepeatEditText,
			EditText nameEditText, EditText surnameEditText,
			RadioGroup radioGroup, EditText birthDateEditText) {
		Boolean isValid = true;
		if (emailEditText != null
				&& emailEditText.getText().toString().length() == 0) {
			isValid = false;
			emailEditText.setError("E-posta doldurulmalı");
			showError(emailEditText);
		} else if (emailEditText != null
				&& emailEditText.getText().toString().length() > 255) {
			isValid = false;
			emailEditText.setError("E-posta uzun dolduruldu");
			showError(emailEditText);
		} else if (emailEditText != null) {
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(emailEditText.getText()
					.toString());
			if (!matcher.matches()) {
				isValid = false;
				emailEditText.setError("E-posta biçimi hatalı");
				showError(emailEditText);
			}
		}
		if (passwordEditText.getText().toString().length() == 0) {
			isValid = false;
			passwordEditText.setError("Şifre doldurulmalı");
			showError(passwordEditText);
		} else if (passwordEditText.getText().toString().length() > 255) {
			isValid = false;
			passwordEditText.setError("Şifre uzun dolduruldu");
			showError(passwordEditText);
		} else if (passwordEditText.getText().toString().length() < 4) {
			isValid = false;
			passwordEditText.setError("Şifre kısa dolduruldu");
			showError(passwordEditText);
		}

		if (!passwordEditText.getText().toString()
				.equals(passwordRepeatEditText.getText().toString())) {
			passwordEditText.setError("Şifre alanları aynı olmalı");
			passwordRepeatEditText.setError("Şifre alanları aynı olmalı");
			showError(passwordEditText);
			showError(passwordRepeatEditText);
		}
		if (nameEditText.getText().toString().length() == 0) {
			isValid = false;
			nameEditText.setError("Ad doldurulmalı");
			showError(nameEditText);
		} else if (nameEditText.getText().toString().length() > 255) {
			isValid = false;
			nameEditText.setError("Ad uzun dolduruldu");
			showError(nameEditText);
		} else if (nameEditText.getText().toString().length() < 2) {
			isValid = false;
			nameEditText.setError("Ad kısa dolduruldu");
			showError(nameEditText);
		}

		if (surnameEditText.getText().toString().length() == 0) {
			isValid = false;
			surnameEditText.setError("Soyadı doldurulmalı");
			showError(surnameEditText);
		} else if (surnameEditText.getText().toString().length() > 255) {
			isValid = false;
			surnameEditText.setError("Soyadı uzun dolduruldu");
			showError(surnameEditText);
		} else if (surnameEditText.getText().toString().length() < 2) {
			isValid = false;
			surnameEditText.setError("Soyadı kısa dolduruldu");
			showError(surnameEditText);
		}

		if (radioGroup.getCheckedRadioButtonId() == -1) {
			isValid = false;
			((RadioButton) radioGroup.getChildAt(0))
					.setError("Cinsiyet seçilmeli");
			showError(((RadioButton) radioGroup.getChildAt(0)));
		}
		if (!(birthDateEditText.getText().toString().length() == 10)) {
			isValid = false;
			birthDateEditText.setError("Doğum tarihi doğru doldurulmalı");
			showError(birthDateEditText);
		}
		return isValid;
	}

	public static Boolean sendCommentValidity(EditText commentEditText) {
		Boolean isValid = true;
		if (commentEditText.getText().toString().length() == 0) {
			isValid = false;
			commentEditText.setError("Yorum metni girilmeli");
			showError(commentEditText);
		} else if (commentEditText.getText().toString().length() > 500) {
			isValid = false;
			commentEditText.setError("E-posta uzun dolduruldu ("
					+ commentEditText.getText().toString().length()
					+ " karakter)");
			showError(commentEditText);
		}

		return isValid;
	}

	private static void showError(View view) {
		Animation shake = AnimationUtils.loadAnimation(view.getContext(),
				R.anim.shake);
		view.startAnimation(shake);
	}
}
