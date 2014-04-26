package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itu.araci.mobile.client.android.adapter.FeedbackAdapter;
import com.itu.araci.mobile.client.android.model.Feedback;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class FeedbackActivity extends BaseActivity {
	final Class<?>[] cArg = new Class[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);

		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		getFeedbacks();
	}

	public void getFeedbacks() {
		try {
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl(
							"/nativeapp/feedback/getByUserIndividualId", null,
							FeedbackActivity.this),
					FeedbackActivity.class.getDeclaredMethod(
							"onPostExecuteFeedbackGet", cArg), null);
			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteFeedbackGet(String result, Dialog parentDialog) {
		RelativeLayout relativeLayout = (RelativeLayout) FeedbackActivity.this
				.findViewById(R.id.feedbackRelativeLayout);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				relativeLayout.setVisibility(View.GONE);
				FeedbackActivity.this.finish();
			} else {
				List<Feedback> feedbackList = new ArrayList<Feedback>();
				JSONArray jsonArray = jsonObject.getJSONArray("result");
				for (int j = 0; j < jsonArray.length(); j++) {
					Feedback feedback = new Feedback();
					feedback.setId(jsonArray.getJSONObject(j).getInt("id"));
					feedback.setAmbiance(jsonArray.getJSONObject(j).getInt(
							"ambiance"));
					feedback.setFlavour(jsonArray.getJSONObject(j).getInt(
							"flavour"));
					feedback.setService(jsonArray.getJSONObject(j).getInt(
							"service"));
					feedback.setPrice(jsonArray.getJSONObject(j)
							.getInt("price"));
					feedback.setPlaceName(jsonArray.getJSONObject(j)
							.getJSONObject("place").getString("name"));
					feedback.setPlaceId(jsonArray.getJSONObject(j)
							.getJSONObject("place").getInt("id"));
					feedbackList.add(feedback);

				}

				if (feedbackList.size() == 0) {
					Toast.makeText(this, "Henüz geri besleme bilginiz yok",
							Toast.LENGTH_LONG).show();
					FeedbackActivity.this.finish();
				} else {
					LinearLayout linearLayout = (LinearLayout) findViewById(R.id.feedbackList);
					linearLayout.removeAllViews();
					final FeedbackAdapter feedbackAdapter = new FeedbackAdapter(
							FeedbackActivity.this, R.layout.feedback_item,
							feedbackList);
					for (int i = 0; i < feedbackAdapter.getCount(); i++) {
						View view = feedbackAdapter.getView(i, null, null);
						linearLayout.addView(view);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}

	public void onPostExecuteFeedbackDelete(String result, Dialog parentDialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Geri besleme bilginiz silindi",
						Toast.LENGTH_LONG).show();
				getFeedbacks();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}
}
