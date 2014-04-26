package com.itu.araci.mobile.client.android.thread;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageTask extends AsyncTask<Object, Object, Drawable> {
	private String url;
	private ImageView imageView;

	public ImageTask(Activity activity, String url, ImageView imageView) {
		super();
		this.url = url;
		this.imageView = imageView;
	}

	@Override
	protected void onPreExecute() {
		imageView
				.setImageResource(android.R.drawable.progress_indeterminate_horizontal);
	}

	@Override
	protected Drawable doInBackground(Object... arg0) {
		URL myUrl;
		try {
			myUrl = new URL(url);
			InputStream inputStream = (InputStream) myUrl.getContent();
			Drawable drawable = Drawable.createFromStream(inputStream, null);
			return drawable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Drawable result) {
		imageView.setImageDrawable(result);
	}
}