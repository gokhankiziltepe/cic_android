package com.itu.araci.mobile.client.android.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class PropertyUtils {
	public static String getProperty(Context baseActivity, String propName) {
		Properties prop = new Properties();
		InputStream is = null;
		try {
			is = baseActivity.getApplicationContext().getAssets()
					.open("araci.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty(propName);
	}
}