package com.itu.araci.mobile.client.android.thread;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.util.HttpUtil;

public class AsyncTaskExecuteGet extends AsyncTask<Object, Object, String> {
	private Activity activity;
	private ProgressDialog dialog;
	private String url;
	private Method method;
	private Dialog parentDialog;

	public AsyncTaskExecuteGet(Activity activity, String url, Method method, Dialog dialog) {
		super();
		this.activity = activity;
		this.url = url;
		this.method = method;
		this.parentDialog = dialog;
	}

	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(activity);
		dialog.setIndeterminate(true);
		dialog.setMessage("Gönderiliyor");
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected String doInBackground(Object... arg0) {
		HttpUtil httpUtil = new HttpUtil();
		String response = httpUtil.performGet(url);
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		try {
			method.invoke(activity, result, parentDialog);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity, "Cevap işlenemedi", Toast.LENGTH_SHORT)
					.show();
		}
		dialog.dismiss();
	}
}