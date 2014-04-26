package com.itu.araci.mobile.client.android.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.LikeActivity;
import com.itu.araci.mobile.client.android.PhotoActivity;
import com.itu.araci.mobile.client.android.PlaceActivity;
import com.itu.araci.mobile.client.android.R;
import com.itu.araci.mobile.client.android.model.Like;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class LikeAdapter extends ArrayAdapter<Like> {
	private Activity context;
	List<Like> data = null;

	public LikeAdapter(Activity context, int resource, List<Like> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.like_item, parent, true);
		}

		final Like item = data.get(position);

		if (item != null) {
			TextView likeName = (TextView) row.findViewById(R.id.likeUserText);
			likeName.setText(item.getName());

			TextView likeDate = (TextView) row.findViewById(R.id.likeDateText);
			String typeText = item.getType() == 0 ? "Mekan" : "Fotoğraf";
			likeDate.setText(typeText + " - " + item.getDateAdded());

			ImageView image = (ImageView) row.findViewById(R.id.likeOrDislike);
			image.setImageResource(item.getIsLike() ? R.drawable.like_image
					: R.drawable.dislike_image);
			row.findViewById(R.id.likeRelativeLayout).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent;
							if (item.getType() == 1) {
								intent = new Intent(context,
										PhotoActivity.class);
								intent.putExtra("photoId",
										item.getLikedEntityBase());
							} else {
								intent = new Intent(context,
										PlaceActivity.class);
								intent.putExtra("placeId",
										item.getLikedEntityBase());
							}
							context.startActivity(intent);
						}
					});
			row.findViewById(R.id.likeRelativeLayout).setOnLongClickListener(
					new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							final AlertDialog alertDialog = new AlertDialog.Builder(
									context)
									.setCancelable(true)
									.setTitle("Uyarı")
									.setMessage(
											"Beğeni bilgisini silmek istediğinize emin misiniz?")
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
															"likeId", getItem(
																	position)
																	.getId()
																	.toString()));
											try {
												AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
														context,
														HttpUtil.buildUrl(
																"/nativeapp/likedislike/delete",
																nameValuePairs,
																context),
														LikeActivity.class
																.getDeclaredMethod(
																		"onPostExecuteLikeDelete",
																		cArg),
														null);
												asyncTaskExecuteGet.execute();
											} catch (Exception e) {
												e.printStackTrace();
												Toast.makeText(context,
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
		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}

}