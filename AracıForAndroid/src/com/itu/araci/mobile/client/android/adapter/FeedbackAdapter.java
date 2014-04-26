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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.FeedbackActivity;
import com.itu.araci.mobile.client.android.PlaceActivity;
import com.itu.araci.mobile.client.android.R;
import com.itu.araci.mobile.client.android.model.Feedback;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class FeedbackAdapter extends ArrayAdapter<Feedback> {
	private Activity context;
	List<Feedback> data = null;

	public FeedbackAdapter(Activity context, int resource, List<Feedback> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.feedback_item, parent, true);
		}

		final Feedback item = data.get(position);

		if (item != null) {

			SeekBar feedbackItemInputAmbiance = (SeekBar) row
					.findViewById(R.id.feedbackItemInputAmbiance);
			feedbackItemInputAmbiance.setEnabled(false);
			feedbackItemInputAmbiance.setProgress(item.getAmbiance());
			SeekBar feedbackItemInputFlavour = (SeekBar) row
					.findViewById(R.id.feedbackItemInputFlavour);
			feedbackItemInputFlavour.setEnabled(false);
			feedbackItemInputFlavour.setProgress(item.getFlavour());
			SeekBar feedbackItemInputService = (SeekBar) row
					.findViewById(R.id.feedbackItemInputService);
			feedbackItemInputService.setEnabled(false);
			feedbackItemInputService.setProgress(item.getService());
			SeekBar feedbackItemInputPrice = (SeekBar) row
					.findViewById(R.id.feedbackItemInputPrice);
			feedbackItemInputPrice.setEnabled(false);
			feedbackItemInputPrice.setProgress(item.getPrice());

			TextView feedbackItemValueAmbiance = (TextView) row
					.findViewById(R.id.feedbackItemValueAmbiance);
			feedbackItemValueAmbiance.setText(item.getAmbiance().toString());
			TextView feedbackItemValueFlavour = (TextView) row
					.findViewById(R.id.feedbackItemValueFlavour);
			feedbackItemValueFlavour.setText(item.getFlavour().toString());
			TextView feedbackItemValueService = (TextView) row
					.findViewById(R.id.feedbackItemValueService);
			feedbackItemValueService.setText(item.getService().toString());
			TextView feedbackItemValuePrice = (TextView) row
					.findViewById(R.id.feedbackItemValuePrice);
			feedbackItemValuePrice.setText(item.getPrice().toString());

			TextView feedbackPlaceTextView = (TextView) row
					.findViewById(R.id.feedbackPlaceTextView);
			feedbackPlaceTextView.setText(item.getPlaceName().toString());
			row.findViewById(R.id.feedbackItemRelativeLayout)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent;
							intent = new Intent(context, PlaceActivity.class);
							intent.putExtra("placeId", item.getPlaceId().toString());
							context.startActivity(intent);
						}
					});
			row.findViewById(R.id.feedbackItemRelativeLayout)
					.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							final AlertDialog alertDialog = new AlertDialog.Builder(
									context)
									.setCancelable(true)
									.setTitle("Uyarı")
									.setMessage(
											"Geri besleme bilgisini silmek istediğinize emin misiniz?")
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
															"feedbackId",
															getItem(position)
																	.getId()
																	.toString()));
											try {
												AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
														context,
														HttpUtil.buildUrl(
																"/nativeapp/feedback/delete",
																nameValuePairs,
																context),
														FeedbackActivity.class
																.getDeclaredMethod(
																		"onPostExecuteFeedbackDelete",
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