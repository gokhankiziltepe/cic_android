package com.itu.araci.mobile.client.android.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

/**
 * Apache HttpClient helper class for performing HTTP requests.
 * 
 * This class is intentionally *not* bound to any Android classes so that it is
 * easier to develop and test. Use calls to this class inside Android AsyncTask
 * implementations (or manual Thread-Handlers) to make HTTP requests
 * asynchronous and not block the UI Thread.
 * 
 * @author ccollins
 * 
 */
public class HttpUtil {

	private static final String CONTENT_TYPE = "Content-Type";
	private static final int POST_TYPE = 1;
	private static final int GET_TYPE = 2;
	private static final String GZIP = "gzip";
	private static final String ACCEPT_ENCODING = "Accept-Encoding";

	public static final String MIME_FORM_ENCODED = "application/x-www-form-urlencoded";
	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String HTTP_RESPONSE = "HTTP_RESPONSE";
	public static final String HTTP_RESPONSE_ERROR = "HTTP_RESPONSE_ERROR";
	public static final String HTTP_PROTOCOL = "http://";
	public static final String PORT = ":8080";
	public static final String CONTEXT_PATH = "/client-native";

	private static final DefaultHttpClient client;
	static {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
		params.setParameter(CoreProtocolPNames.USER_AGENT,
				"Apache-HttpClient/Android");
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		params.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				params, schemeRegistry);
		client = new DefaultHttpClient(cm, params);
		HttpUtil.client.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(final HttpResponse response,
					final HttpContext context) throws HttpException,
					IOException {
				HttpEntity entity = response.getEntity();
				Header contentEncodingHeader = entity.getContentEncoding();
				if (contentEncodingHeader != null) {
					HeaderElement[] codecs = contentEncodingHeader
							.getElements();
					for (int i = 0; i < codecs.length; i++) {
						if (codecs[i].getName().equalsIgnoreCase(HttpUtil.GZIP)) {
							response.setEntity(new GzipDecompressingEntity(
									response.getEntity()));
							return;
						}
					}
				}
			}
		});
	}

	private final ResponseHandler<String> responseHandler;

	public HttpUtil() {
		responseHandler = new BasicResponseHandler();
	}

	public String performGet(final String url) {
		return performRequest(null, url, null, null, null, null,
				HttpUtil.GET_TYPE);
	}

	public String performGet(final String url, final String user,
			final String pass, final Map<String, String> additionalHeaders) {
		return performRequest(null, url, user, pass, additionalHeaders, null,
				HttpUtil.GET_TYPE);
	}

	public String performPost(final String url, final Map<String, String> params) {
		return performRequest(HttpUtil.MIME_FORM_ENCODED, url, null, null,
				null, params, HttpUtil.POST_TYPE);
	}

	public String performPost(final String url, final String user,
			final String pass, final Map<String, String> additionalHeaders,
			final Map<String, String> params) {
		return performRequest(HttpUtil.MIME_FORM_ENCODED, url, user, pass,
				additionalHeaders, params, HttpUtil.POST_TYPE);
	}

	public String performPost(final String contentType, final String url,
			final String user, final String pass,
			final Map<String, String> additionalHeaders,
			final Map<String, String> params) {
		return performRequest(contentType, url, user, pass, additionalHeaders,
				params, HttpUtil.POST_TYPE);
	}

	private String performRequest(final String contentType, final String url,
			final String user, final String pass,
			final Map<String, String> headers,
			final Map<String, String> params, final int requestType) {

		if ((user != null) && (pass != null)) {
			HttpUtil.client.getCredentialsProvider().setCredentials(
					AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
		}

		final Map<String, String> sendHeaders = new HashMap<String, String>();
		if (!sendHeaders.containsKey(HttpUtil.ACCEPT_ENCODING)) {
			sendHeaders.put(HttpUtil.ACCEPT_ENCODING, HttpUtil.GZIP);
		}
		if ((headers != null) && (headers.size() > 0)) {
			sendHeaders.putAll(headers);
		}
		if (requestType == HttpUtil.POST_TYPE) {
			sendHeaders.put(HttpUtil.CONTENT_TYPE, contentType);
		}
		if (sendHeaders.size() > 0) {
			HttpUtil.client.addRequestInterceptor(new HttpRequestInterceptor() {
				public void process(final HttpRequest request,
						final HttpContext context) throws HttpException,
						IOException {
					for (String key : sendHeaders.keySet()) {
						if (!request.containsHeader(key)) {
							request.addHeader(key, sendHeaders.get(key));
						}
					}
				}
			});
		}

		HttpRequestBase method = null;
		if (requestType == HttpUtil.POST_TYPE) {
			method = new HttpPost(url);
			List<NameValuePair> nvps = null;
			if ((params != null) && (params.size() > 0)) {
				nvps = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
			}
			if (nvps != null) {
				try {
					HttpPost methodPost = (HttpPost) method;
					methodPost.setEntity(new UrlEncodedFormEntity(nvps,
							HTTP.UTF_8));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("Error peforming HTTP request: "
							+ e.getMessage(), e);
				}
			}
		} else if (requestType == HttpUtil.GET_TYPE) {
			method = new HttpGet(url);
		}

		return execute(method);
	}

	private synchronized String execute(final HttpRequestBase method) {
		String response = null;
		try {
			response = HttpUtil.client.execute(method, responseHandler);
		} catch (ClientProtocolException e) {
			response = HttpUtil.HTTP_RESPONSE_ERROR + " - "
					+ e.getClass().getSimpleName() + " " + e.getMessage();
		} catch (IOException e) {
			response = HttpUtil.HTTP_RESPONSE_ERROR + " - "
					+ e.getClass().getSimpleName() + " " + e.getMessage();
		}
		return response;
	}

	static class GzipDecompressingEntity extends HttpEntityWrapper {
		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException,
				IllegalStateException {
			InputStream wrappedin = wrappedEntity.getContent();
			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength() {
			return -1;
		}
	}

	public static String buildUrl(String path,
			List<NameValuePair> nameValuePairs, Context context)
			throws UnsupportedEncodingException {
		String url = HTTP_PROTOCOL
				+ PropertyUtils.getProperty(context, "araci.server.url") + PORT
				+ CONTEXT_PATH + path;
		String param = "";
		if (nameValuePairs != null)
			for (int i = 0; i < nameValuePairs.size(); i++) {
				if (i == 0) {
					param += "?";
				} else {
					param += "&";
				}
				param += nameValuePairs.get(i).getName();
				param += "=";
				param += URLEncoder.encode(nameValuePairs.get(i).getValue(),
						"UTF-8");
			}
		return url + param;
	}
}
