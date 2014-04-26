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

import com.itu.araci.mobile.client.android.FollowerActivity;
import com.itu.araci.mobile.client.android.FriendProfileActivity;
import com.itu.araci.mobile.client.android.R;
import com.itu.araci.mobile.client.android.model.Follower;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class FollowerAdapter extends ArrayAdapter<Follower> {
	private Activity context;
	List<Follower> data = null;

	public FollowerAdapter(Activity context, int resource, List<Follower> data) {
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

		final Follower item = data.get(position);

		if (item != null) {
			TextView followerInfo = (TextView) row
					.findViewById(R.id.followerInfo);
			followerInfo.setText(item.getFollowerInfo());

			TextView followerIsApproved = (TextView) row
					.findViewById(R.id.followerApproved);
			followerIsApproved.setText(item.getIsApproved() ? "Onaylı"
					: "Onaysız");

			TextView followerDate = (TextView) row
					.findViewById(R.id.followerDate);
			followerDate.setText(item.getDateAdded());

			row.findViewById(R.id.followerRelativeLayout).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									FriendProfileActivity.class);
							intent.putExtra("userId", item.getFollowerId());

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
											"Takipçi durumunu değiştirmek istediğinize emin misiniz?")
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
											nameValuePairs
													.add(new BasicNameValuePair(
															"newState",
															Boolean.toString(!getItem(
																	position)
																	.getIsApproved())));

											try {
												AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
														context,
														HttpUtil.buildUrl(
																"/nativeapp/follower/update",
																nameValuePairs,
																context),
														FollowerActivity.class
																.getDeclaredMethod(
																		"onPostExecuteFollowerStateChange",
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