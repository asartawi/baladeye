package com.AOU.baladeye.frontdoors;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.AOU.baladeye.ComplaintsActivity;
import com.AOU.baladeye.LandingActivity;

public class ComplaintsFrontdoor extends
AsyncTask<String, String, Boolean> {

private ComplaintsActivity activity;
private String content;
private String name;
private String type;
private String place;
private String mobile;
private Bitmap image;

public ComplaintsFrontdoor(ComplaintsActivity activity,
	String content, String type, String place, String name,
	String mobile, Bitmap drawingCache) {
this.activity = activity;
this.content = content;
this.name = name;
this.type = type;
this.place = place;
this.mobile = mobile;
this.image = drawingCache;
}

@Override
protected Boolean doInBackground(String... uri) {
String responseString;
try {
	SharedPreferences sharedPreferences = activity.getSharedPreferences(
			"baladeye", activity.MODE_PRIVATE);
	String userId = sharedPreferences.getString("user_id", "1");
	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	parameters.add(new BasicNameValuePair("name", name));
	parameters.add(new BasicNameValuePair("location", place));
	parameters.add(new BasicNameValuePair("content", content));
	parameters.add(new BasicNameValuePair("type", type));
	parameters.add(new BasicNameValuePair("telephone", mobile));
	parameters.add(new BasicNameValuePair("user", userId));


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
						+ responseString + " error: "
						+ e.toString());
		return false;
	}

} catch (ClientProtocolException e) {
	 Log.e("InformationFrontdoor exception", "failed to request: " +
	 uri.toString()
	 + " " + e.toString());
	return false;
} catch (IOException e) {
	activity.runOnUiThread(new Runnable() {
		public void run() {
			Toast.makeText(activity,
					"الرجاء فحص اتصالكم بالإنترنت و المحاولة لاحقاً",
					Toast.LENGTH_LONG).show();
		}
	});
	 Log.e("InformationFrontdoor exception", "failed to request: " +
			 uri.toString()
			 + " " + e.toString());
			return false;
}
//HttpClient httpclient = new DefaultHttpClient();
//HttpResponse response;
//String responseString = null;
//
//try {
//	response = httpclient.execute(new HttpPost(uri[0]));
//	StatusLine statusLine = response.getStatusLine();
//	if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		 image	.compress(CompressFormat.JPEG, 75, out);
//            byte[] data = out.toByteArray();
//            
//		response.getEntity().writeTo(out);
//		out.close();
//		responseString = out.toString();
//		try {
//			JSONArray jsonArray = new JSONArray(responseString);
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject object = jsonArray.getJSONObject(i);
//				String title = object.getString("title");
//				String picUrl = object.getString("pic");
//				String content = object.getString("content");
//				Bitmap bitmap = DownloadImage(picUrl);
//				News news = new News(title, content, picUrl, bitmap);
//			}
//
//		} catch (JSONException e) {
//			Log.e("NewsFrontdoor",
//					"Failed to parse json from web response, web response: "
//							+ responseString + " error: "
//							+ e.toString());
//		}
//	} else {
//		// Closes the connection.
//		response.getEntity().getContent().close();
//		throw new IOException(statusLine.getReasonPhrase());
//	}
//} catch (ClientProtocolException e) {
//	Log.e("frontdoor exception", "failed to request: " + uri.toString()
//			+ " " + e.toString());
//} catch (IOException e) {
//	Log.e("frontdoor exception", "failed to request: " + uri.toString()
//			+ " " + e.toString());
//	return false
//}
//return true;
}

@Override
protected void onPostExecute(final Boolean result) {
super.onPostExecute(result);
// Do anything with response..
LandingActivity.context.runOnUiThread(new Runnable() {

	@Override
	public void run() {
//		activity.showProgress(false);
		if(result){
			Toast.makeText(activity, "لقد تم إرسال الشكوى بنجاح", Toast.LENGTH_LONG).show();
			activity.finish();
		}else{
			Toast.makeText(activity, "حدث خطأ في الإرسال, تأكد من اتصالك بالإنترنت و حاول لاحقاً", Toast.LENGTH_LONG).show();

		}
	}
});
}

private InputStream OpenHttpConnection(String urlString) throws IOException {
InputStream in = null;
int response = -1;

URL url = new URL(urlString);
URLConnection conn = url.openConnection();

if (!(conn instanceof HttpURLConnection))
	throw new IOException("Not an HTTP connection");

try {
	HttpURLConnection httpConn = (HttpURLConnection) conn;
	httpConn.setAllowUserInteraction(false);
	httpConn.setInstanceFollowRedirects(true);
	httpConn.setRequestMethod("GET");
	httpConn.connect();
	response = httpConn.getResponseCode();
	if (response == HttpURLConnection.HTTP_OK) {
		in = httpConn.getInputStream();
	}
} catch (Exception ex) {
	throw new IOException("Error connecting");
}
return in;
}
}