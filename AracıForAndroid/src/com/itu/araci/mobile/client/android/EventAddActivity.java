package com.itu.araci.mobile.client.android;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class EventAddActivity extends BaseActivity {
	final Class<?>[] cArg = new Class[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_add);

		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		final EditText startDate = (EditText) findViewById(R.id.eventAddStartDate);
		startDate.setKeyListener(null);
		startDate.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DialogFragment newFragment = new SelectDateFragment(
							startDate);
					newFragment.show(getSupportFragmentManager(), "DatePicker");
				}
			}
		});
		final EditText endDate = (EditText) findViewById(R.id.eventAddEndDate);
		endDate.setKeyListener(null);
		endDate.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DialogFragment newFragment = new SelectDateFragment(endDate);
					newFragment.show(getSupportFragmentManager(), "DatePicker");
				}
			}
		});
		final EditText startTime = (EditText) findViewById(R.id.eventAddStartTime);
		startTime.setKeyListener(null);
		startTime.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DialogFragment newFragment = new SelectTimeFragment(
							startTime);
					newFragment.show(getSupportFragmentManager(), "TimePicker");
				}
			}
		});
		final EditText endTime = (EditText) findViewById(R.id.eventAddEndTime);
		endTime.setKeyListener(null);
		endTime.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DialogFragment newFragment = new SelectTimeFragment(endTime);
					newFragment.show(getSupportFragmentManager(), "TimePicker");
				}
			}
		});
	}
}
