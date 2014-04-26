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

import com.itu.araci.mobile.client.android.CommentActivity;
import com.itu.araci.mobile.client.android.PhotoActivity;
import com.itu.araci.mobile.client.android.PlaceActivity;
import com.itu.araci.mobile.client.android.R;
import com.itu.araci.mobile.client.android.model.Comment;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class CommentAdapter extends ArrayAdapter<Comment> {
	private Activity context;
	List<Comment> data = null;
	private Boolean isPhotoComment;

	public CommentAdapter(Activity context, int resource, List<Comment> data,
			Boolean isPhotoComment) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
		this.isPhotoComment = isPhotoComment;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.comment_item, parent, true);
		}

		final Comment item = data.get(position);

		if (item != null) {
			TextView commentText = (TextView) row
					.findViewById(R.id.commentText);
			commentText.setText(item.getCommentText());

			TextView commentUser = (TextView) row
					.findViewById(R.id.commentUserText);
			commentUser.setText(item.getOwnerInfo());

			TextView commentDate = (TextView) row
					.findViewById(R.id.commentDateText);
			commentDate.setText(item.getDateAdded());
			if (context instanceof CommentActivity) {
				row.findViewById(R.id.commentRelativeLayout)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent;
								if (isPhotoComment) {
									intent = new Intent(context,
											PhotoActivity.class);
									intent.putExtra("photoId",
											item.getCommentedToEntityBase());
								} else {
									intent = new Intent(context,
											PlaceActivity.class);
									intent.putExtra("placeId",
											item.getCommentedToEntityBase());
								}
								context.startActivity(intent);
							}
						});
				row.findViewById(R.id.commentRelativeLayout)
						.setOnLongClickListener(new OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {
								final AlertDialog alertDialog = new AlertDialog.Builder(
										context)
										.setCancelable(true)
										.setTitle("Uyarı")
										.setMessage(
												"Yorumu silmek istediğinize emin misiniz?")
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
																"commentId",
																getItem(
																		position)
																		.getId()));
												try {
													AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
															context,
															HttpUtil.buildUrl(
																	"/nativeapp/comment/delete",
																	nameValuePairs,
																	context),
															CommentActivity.class
																	.getDeclaredMethod(
																			"onPostExecuteCommentDelete",
																			cArg),
															null);
													asyncTaskExecuteGet
															.execute();
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
		}

		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}

}