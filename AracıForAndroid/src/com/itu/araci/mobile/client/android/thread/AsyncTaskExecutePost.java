package com.itu.araci.mobile.client.android.thread;

import java.lang.reflect.Method;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.util.HttpUtil;

public class AsyncTaskExecutePost extends AsyncTask<Object, Object, String> {
	private Activity activity;
	private ProgressDialog dialog;
	private String url;
	private Method method;
	private Dialog parentDialog;
	private Map<String, String> params;

	public AsyncTaskExecutePost(Activity activity, String url, Method method,
			Dialog dialog, Map<String, String> params) {
		super();
		this.activity = activity;
		this.url = url;
		this.method = method;
		this.parentDialog = dialog;
		this.params = params;
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
		String response = httpUtil.performPost(url, params);
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