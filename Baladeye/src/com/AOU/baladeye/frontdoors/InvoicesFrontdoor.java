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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.AOU.baladeye.AccountActivity;
import com.AOU.baladeye.LandingActivity;
import com.AOU.baladeye.models.Invoice;

public class InvoicesFrontdoor extends AsyncTask<String, String, List<Invoice>> {

	private Intent intent;
	private AccountActivity activity;
	public static List<Invoice> invoicesList;

	public InvoicesFrontdoor(Intent intent, AccountActivity activity) {
		this.intent = intent;
		this.activity = activity;
	}

	@Override
	protected List<Invoice> doInBackground(String... uri) {

		invoicesList = new ArrayList<Invoice>();
		String responseString;
		try {
			// JSONObject jsonObj = new JSONObject();
			// jsonObj.put("user_id", "1");
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("user_id", "1"));
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
				}
			}

			// Execute the call
			HttpResponse response = httpClient.execute(httpPost);
			responseString = EntityUtils.toString(response.getEntity());
			try {
				JSONArray jsonArray = new JSONArray(responseString);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					String id = object.getString("id");
					String userId = object.getString("user_id");
					String type = object.getString("type");

					String value = object.getString("value");
					String extraDetails = object.getString("extra_details");
					String billDate = object.getString("bill_date");
					int paid = object.getInt("paid");

					Invoice invoices = new Invoice(id, userId, type, value,
							extraDetails, billDate, paid != 0);
					invoicesList.add(invoices);
				}

			} catch (JSONException e) {
				Log.e("InvoicesFrontdoor",
						"Failed to parse json from web response, web response: "
								+ responseString + " error: " + e.toString());
			}
			// } else{
			// //Closes the connection.
			// response.getEntity().getContent().close();
			// throw new IOException(statusLine.getReasonPhrase());
			// }
		} catch (ClientProtocolException e) {
			Log.e("frontdoor exception", "failed to request: " + uri.toString()
					+ " " + e.toString());
		} catch (IOException e) {
			Log.e("frontdoor exception", "failed to request: " + uri.toString()
					+ " " + e.toString());
		}
		return invoicesList;
	}

	@Override
	protected void onPostExecute(final List<Invoice> invoices) {
		super.onPostExecute(invoices);
		// Do anything with response..
		LandingActivity.context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				activity.showProgress(false);
				activity.startActivity(intent);
			}
		});
	}
}
