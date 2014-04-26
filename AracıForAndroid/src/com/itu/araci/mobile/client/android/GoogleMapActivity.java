package com.itu.araci.mobile.client.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itu.araci.mobile.client.android.thread.AsyncTaskExecuteGet;
import com.itu.araci.mobile.client.android.util.HttpUtil;

public class GoogleMapActivity extends BaseActivity {
	private GoogleMap mMap;
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		final Class<?>[] cArg = new Class[2];
		cArg[0] = String.class;
		cArg[1] = Dialog.class;

		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", getIntent()
					.getExtras().getString("placeId")));
			AsyncTaskExecuteGet asyncTaskExecuteGet = new AsyncTaskExecuteGet(
					this, HttpUtil.buildUrl("/nativeapp/place/get",
							nameValuePairs, GoogleMapActivity.this),
					GoogleMapActivity.class.getDeclaredMethod(
							"onPostExecutePlaceGet", cArg), null);

			asyncTaskExecuteGet.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Cevap işlenemedi",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onPostExecutePlaceGet(String result, Dialog dialog) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (!jsonObject.getString("codeConstant").equals("SUCCESS")) {
				Toast.makeText(this, jsonObject.getString("message"),
						Toast.LENGTH_LONG).show();
				GoogleMapActivity.this.finish();
			} else {
				LatLng latLng = new LatLng(jsonObject.getJSONObject("result")
						.getDouble("addressLatitude"), jsonObject
						.getJSONObject("result").getDouble("addressLongitude"));
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
				TextView addressText = (TextView) GoogleMapActivity.this
						.findViewById(R.id.addressText);
				addressText.setText(jsonObject.getJSONObject("result")
						.getString("addressText"));
				mMap.addMarker(new MarkerOptions().position(latLng));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "Cevap işlenemedi", Toast.LENGTH_LONG).show();
		}
	}
}
