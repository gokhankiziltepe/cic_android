package com.itu.araci.mobile.client.android.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itu.araci.mobile.client.android.PhotoActivity;
import com.itu.araci.mobile.client.android.R;
import com.itu.araci.mobile.client.android.cache.ImageLoader;
import com.itu.araci.mobile.client.android.model.Image;
import com.itu.araci.mobile.client.android.util.PropertyUtils;

public class ImageAdapter extends ArrayAdapter<Image> {
	private Activity context;
	List<Image> data = null;

	public ImageAdapter(Activity context, int resource, List<Image> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.image_item, parent, false);
		}

		final Image item = data.get(position);

		if (item != null) {
			ImageView imageView = (ImageView) row
					.findViewById(R.id.userImageItem);
			TextView textView = (TextView) row
					.findViewById(R.id.userImageItemText);
			ImageLoader imageLoader = new ImageLoader(context);
			imageLoader.DisplayImage(
					item.getPath().replace(
							"localhost:80",
							PropertyUtils.getProperty(context,
									"araci.server.url")), imageView);
			textView.setText(item.getTimestamp());
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, PhotoActivity.class);
					intent.putExtra("photoId", item.getId());
					context.startActivity(intent);
				}
			});
		}

		return row;
	}
}