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
import android.widget.TextView;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.FollowedActivity;
import com.itu.araci.mobile.client.android.FriendProfileActivity;
import com.itu.araci.mobile.client.android.PlaceActivity;
import com.itu.araci.mobile.client.android.R;
import com.itu.araci.mobile.client.android.model.Followed;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class FollowedAdapter extends ArrayAdapter<Followed> {
	private Activity context;
	List<Followed> data = null;

	public FollowedAdapter(Activity context, int resource, List<Followed> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.follower_item, parent, true);
		}

		final Followed item = data.get(position);

		if (item != null) {
			TextView followerInfo = (TextView) row
					.findViewById(R.id.followerInfo);
			followerInfo.setText(item.getFollowerInfo());

			TextView followerIsApproved = (TextView) row
					.findViewById(R.id.followerApproved);
			followerIsApproved.setText(item.getType() == 0 ? "Mekan" : "Kişi");

			TextView followerDate = (TextView) row
					.findViewById(R.id.followerDate);
			followerDate.setText(item.getDateAdded());
			row.findViewById(R.id.followerRelativeLayout).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent;
							if (item.getType() == 1) {
								intent = new Intent(context,
										FriendProfileActivity.class);
								intent.putExtra("userId",
										item.getFollowedEntityBase());
							} else {
								intent = new Intent(context,
										PlaceActivity.class);
								intent.putExtra("placeId",
										item.getFollowedEntityBase());
							}
							context.startActivity(intent);
						}
					});
			row.findViewById(R.id.followerRelativeLayout)
					.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							final AlertDialog alertDialog = new AlertDialog.Builder(
									context)
									.setCancelable(true)
									.setTitle("Uyarı")
									.setMessage(
											"Takip bilgisini silmek istediğinize emin misiniz?")
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
															"followId",
															getItem(position)
																	.getId()
																	.toString()));
											try {
												AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
														context,
														HttpUtil.buildUrl(
																"/nativeapp/followee/delete",
																nameValuePairs,
																context),
														FollowedActivity.class
																.getDeclaredMethod(
																		"onPostExecuteFolloweeDelete",
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