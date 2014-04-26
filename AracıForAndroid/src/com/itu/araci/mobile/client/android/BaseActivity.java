package com.itu.araci.mobile.client.android;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class BaseActivity extends FragmentActivity {
	public class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		private EditText editText;

		public SelectDateFragment(EditText editText) {
			super();
			this.editText = editText;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm + 1, dd, editText);
		}
	}

	public class SelectTimeFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {
		private EditText editText;

		public SelectTimeFragment(EditText editText) {
			super();
			this.editText = editText;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR);
			int minute = calendar.get(Calendar.MINUTE);
			return new TimePickerDialog(getActivity(), this, hour, minute, true);
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			populateSetTime(hourOfDay, minute, editText);
		}
	}

	private void populateSetTime(int hourOfDay, int minute, EditText editText) {
		editText.setText((hourOfDay < 10 ? "0" + String.valueOf(hourOfDay)
				: hourOfDay)
				+ ":"
				+ (minute < 10 ? "0" + String.valueOf(minute) : minute));
	}

	public void populateSetDate(int year, int month, int day, EditText editText) {
		editText.setText((day < 10 ? "0" + String.valueOf(day) : day) + "/"
				+ (month < 10 ? "0" + String.valueOf(month) : month) + "/"
				+ year);
	}
}
