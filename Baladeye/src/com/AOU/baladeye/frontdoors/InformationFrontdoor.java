package com.AOU.baladeye.frontdoors;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.AOU.baladeye.InformationActivity;
import com.AOU.baladeye.LandingActivity;

public class InformationFrontdoor extends AsyncTask<String, String, Boolean> {

	private InformationActivity activity;
	private String name;
	private String desc;
	private String userId;

	public InformationFrontdoor(InformationActivity activity, String name,
			String desc, String userId) {
		this.activity = activity;
		this.name = name;
		this.desc = desc;
		this.userId = userId;
	}

	@Override
	protected Boolean doInBackground(String... uri) {
		String responseString;
		try {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("name", name));
			parameters.add(new BasicNameValuePair("desc", desc));
			parameters.add(new BasicNameValuePair("user_id", userId));

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(uri[0]);

			// Set the http request
			httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
					CookiePolicy.RFC_2109);
			// httpPost = new HttpPost(webServiceUrl + methodName);

			httpPost.setHeader(
					"Accept",
					"text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");

			// Variable to keep the http response
			responseString = null;

			// Set the parameters if exist
			if (parameters != null && !parameters.isEmpty()) {
				try {
					// Set the parameters in the request
					httpPost.setEntity(new UrlEncodedFormEntity(parameters,
							HTTP.UTF_8));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return false;
				}
			}

			// Execute the call
			HttpResponse response = httpClient.execute(httpPost);
			responseString = EntityUtils.toString(response.getEntity());
			try {
				JSONObject object = new JSONObject(responseString);
				String result = object.getString("result");
				if (result.equals("true")) {
					return true;
				} else {
					return false;
				}

			} catch (JSONException e) {
				Log.e("InvoicesFrontdoor",
						"Failed to parse json from web response, web response: "
								+ responseString + " error: " + e.toString());
				return false;
			}

		} catch (ClientProtocolException e) {
			Log.e("InformationFrontdoor exception",
					"failed to request: " + uri.toString() + " " + e.toString());
			return false;
		} catch (IOException e) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity,
							"الرجاء فحص اتصالكم بالإنترنت و المحاولة لاحقاً",
							Toast.LENGTH_LONG).show();
				}
			});
			Log.e("InformationFrontdoor exception",
					"failed to request: " + uri.toString() + " " + e.toString());
			return false;
		}
	}

	@Override
	protected void onPostExecute(final Boolean result) {
		super.onPostExecute(result);
		// Do anything with response..
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				activity.showProgress(false);
				if (result) {
					Toast.makeText(activity, "لقد تم إرسال استعلامك بنجاح",
							Toast.LENGTH_LONG).show();
					activity.finish();
				} else {
					Toast.makeText(
							activity,
							"حدث خطأ في الإرسال, تأكد من اتصالك بالإنترنت و حاول لاحقاً",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
