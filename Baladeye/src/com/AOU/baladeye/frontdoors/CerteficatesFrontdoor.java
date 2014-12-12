//package com.AOU.baladeye.frontdoors;
//
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.params.ClientPNames;
//import org.apache.http.client.params.CookiePolicy;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.AOU.baladeye.CityResidentActivity;
//
//public class CerteficatesFrontdoor extends AsyncTask<String, String, Boolean> {
//
//	private Intent intent;
//	private CityResidentActivity activity;
//	private String birthOfDate;
//	private String name;
//	private String email;
//	private String gender;
//	private String mobile;
//	private String image;
//
//	public CerteficatesFrontdoor(CityResidentActivity cityResidentActivity,
//			String birthOfDate, String email, String gender, String name,
//			String mobile, String drawingCache) {
//		this.activity = cityResidentActivity;
//		this.birthOfDate = birthOfDate;
//		this.name = name;
//		this.email = email;
//		this.gender = gender;
//		this.mobile = mobile;
//		this.image = drawingCache;
//	}
//
//	@Override
//	protected Boolean doInBackground(String... uri) {
//		String responseString;
//		try {
//			SharedPreferences sharedPreferences = activity
//					.getSharedPreferences("baladeye", activity.MODE_PRIVATE);
//			String userId = sharedPreferences.getString("user_id", "1");
//			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//			parameters.add(new BasicNameValuePair("name", name));
//			parameters.add(new BasicNameValuePair("email", email));
//			parameters.add(new BasicNameValuePair("dob", birthOfDate));
//			parameters.add(new BasicNameValuePair("gender", gender));
//			parameters.add(new BasicNameValuePair("tel", mobile));
//			parameters.add(new BasicNameValuePair("user", userId));
//
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(uri[0]);
//			// Set the http request
//			httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
//					CookiePolicy.RFC_2109);
//
//			httpPost.setHeader(
//					"Accept",
//					"text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
//			httpPost.setHeader("Content-Type",
//					"application/x-www-form-urlencoded");
//
//			// Variable to keep the http response
//			responseString = null;
//
//			// Set the parameters if exist
//			if (parameters != null && !parameters.isEmpty()) {
//				try {
//					// Set the parameters in the request
//					httpPost.setEntity(new UrlEncodedFormEntity(parameters,
//							HTTP.UTF_8));
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//					return false;
//				}
//			}
//
//			// Execute the call
//			HttpResponse response = httpClient.execute(httpPost);
//			responseString = EntityUtils.toString(response.getEntity());
//			try {
//				JSONObject object = new JSONObject(responseString);
//				String result = object.getString("result");
//				if (result.equals("true")) {
//					return true;
//				} else {
//					return false;
//				}
//
//			} catch (JSONException e) {
//				Log.e("InvoicesFrontdoor",
//						"Failed to parse json from web response, web response: "
//								+ responseString + " error: " + e.toString());
//				return false;
//			}
//
//		} catch (ClientProtocolException e) {
//			Log.e("InformationFrontdoor exception",
//					"failed to request: " + uri.toString() + " " + e.toString());
//			return false;
//		} catch (IOException e) {
//			Log.e("InformationFrontdoor exception",
//					"failed to request: " + uri.toString() + " " + e.toString());
//			return false;
//		}
//		// MultipartEntity entity = new MultipartEntity();
//		//
//		// entity.addPart("fname", new StringBody("xyz"));
//		//
//		// try {
//		//
//		// File file1 = new File("Your image Path");
//		// FileBody bin1 = new FileBody(file1, "images/jpeg");
//		//
//		// entity.addPart("file", bin1);
//		//
//		//
//		// File file2 = new File("Your image Path");
//		// FileBody bin2 = new FileBody(file2, "images/jpeg");
//		//
//		// entity.addPart("file", bin2);
//		//
//		// File file3 = new File("Your image Path");
//		// FileBody bin3 = new FileBody(file3, "images/jpeg");
//		//
//		// entity.addPart("file", bin3);
//		//
//		//
//		//
//		// postMethod.setEntity(entity);
//		// HttpResponse response;
//		// response = client.execute(postMethod);
//		//
//		// String result = EntityUtils.toString(response.getEntity());
//		//
//		// }
//		//
//		// catch (Exception e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//		// uploadFile(image, uri[0]);
//	}
//
//	@Override
//	protected void onPostExecute(final Boolean result) {
//		super.onPostExecute(result);
//		// Do anything with response..
//		activity.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				// activity.showProgress(false);
//				if (result) {
//					Toast.makeText(activity,
//							"لقد تم إرسال طلب شهادة ساكن بنجاح",
//							Toast.LENGTH_LONG).show();
//				} else {
//					Toast.makeText(
//							activity,
//							"حدث خطأ في الإرسال, تأكد من اتصالك بالإنترنت و حاول لاحقاً",
//							Toast.LENGTH_LONG).show();
//
//				}
//			}
//		});
//	}
//
//	int serverResponseCode = 0;
//
//	public int uploadFile(String sourceFileUri, String upLoadServerUri) {
//		String fileName = sourceFileUri;
//
//		HttpURLConnection conn = null;
//		DataOutputStream dos = null;
//		String lineEnd = "\r\n";
//		String twoHyphens = "--";
//		String boundary = "*****";
//		int bytesRead, bytesAvailable, bufferSize;
//		byte[] buffer;
//		int maxBufferSize = 1 * 1024 * 1024;
//		File sourceFile = new File(sourceFileUri);
//		if (!sourceFile.isFile()) {
//			Log.e("uploadFile", "Source File not exist :" + fileName);
//			return 0;
//		} else {
//			try {// open a URL connection to the Servlet
//				FileInputStream fileInputStream = new FileInputStream(
//						sourceFile);
//				URL url = new URL(upLoadServerUri);
//				// Open a HTTP  connection to  the URL
//				conn = (HttpURLConnection) url.openConnection();
//				conn.setDoInput(true); // Allow Inputs
//				conn.setDoOutput(true); // Allow Outputs
//				conn.setUseCaches(false); // Don't use a Cached Copy
//				conn.setRequestMethod("POST");
//				conn.setRequestProperty("Connection", "Keep-Alive");
//				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//				conn.setRequestProperty("Content-Type",
//						"multipart/form-data;boundary=" + boundary);
//				conn.setRequestProperty("uploaded_file", fileName);
//				SharedPreferences sharedPreferences = activity
//						.getSharedPreferences("baladeye", activity.MODE_PRIVATE);
//				String userId = sharedPreferences.getString("user_id", "1");
//				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//				parameters.add(new BasicNameValuePair("name", name));
//				parameters.add(new BasicNameValuePair("email", email));
//				parameters.add(new BasicNameValuePair("dob", birthOfDate));
//				parameters.add(new BasicNameValuePair("gender", gender));
//				parameters.add(new BasicNameValuePair("tel", mobile));
//				parameters.add(new BasicNameValuePair("user", userId));
//
//				dos = new DataOutputStream(conn.getOutputStream());
//				String urlParameters = "name=" + name + "&email=" + email
//						+ "&dob=" + birthOfDate + "&gender=" + gender + "&tel="
//						+ mobile + "&user=" + userId;
//				dos.writeBytes(urlParameters);
//				dos.writeBytes(twoHyphens + boundary + lineEnd);
//				dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="
//						+ fileName + "" + lineEnd);
//				dos.writeBytes(lineEnd);
//				// create a buffer of  maximum size
//				bytesAvailable = fileInputStream.available();
//				bufferSize = Math.min(bytesAvailable, maxBufferSize);
//				buffer = new byte[bufferSize];
//				// read file and write it into form...
//				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//				while (bytesRead > 0) {
//					dos.write(buffer, 0, bufferSize);
//					bytesAvailable = fileInputStream.available();
//					bufferSize = Math.min(bytesAvailable, maxBufferSize);
//					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//				}
//				// send multipart form data necesssary after file data...
//				dos.writeBytes(lineEnd);
//				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//				// Responses from the server (code and message)
//				serverResponseCode = conn.getResponseCode();
//				String serverResponseMessage = conn.getResponseMessage();
//				Log.i("uploadFile", "HTTP Response is : "
//						+ serverResponseMessage + ": " + serverResponseCode);
//				if (serverResponseCode == 200) {
//					activity.runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							Toast.makeText(activity, "File Upload Complete.",
//									Toast.LENGTH_SHORT).show();
//						}
//					});
//				}
//				// close the streams //
//				fileInputStream.close();
//				dos.flush();
//				dos.close();
//			} catch (MalformedURLException ex) {
//				ex.printStackTrace();
//				activity.runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						Toast.makeText(activity, "MalformedURLException",
//								Toast.LENGTH_SHORT).show();
//					}
//				});
//				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
//			} catch (Exception e) {
//				e.printStackTrace();
//				activity.runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						Toast.makeText(activity, "Got Exception : see logcat ",
//								Toast.LENGTH_SHORT).show();
//					}
//				});
//
//				Log.e("Upload file to server Exception",
//						"Exception : " + e.getMessage(), e);
//			}
//			return serverResponseCode;
//		} // End else block
//	}
//
//	private InputStream OpenHttpConnection(String urlString) throws IOException {
//		InputStream in = null;
//		int response = -1;
//
//		URL url = new URL(urlString);
//		URLConnection conn = url.openConnection();
//
//		if (!(conn instanceof HttpURLConnection))
//			throw new IOException("Not an HTTP connection");
//
//		try {
//			HttpURLConnection httpConn = (HttpURLConnection) conn;
//			httpConn.setAllowUserInteraction(false);
//			httpConn.setInstanceFollowRedirects(true);
//			httpConn.setRequestMethod("GET");
//			httpConn.connect();
//			response = httpConn.getResponseCode();
//			if (response == HttpURLConnection.HTTP_OK) {
//				in = httpConn.getInputStream();
//			}
//		} catch (Exception ex) {
//			throw new IOException("Error connecting");
//		}
//		return in;
//	}
//
//}

package com.AOU.baladeye.frontdoors;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.AOU.baladeye.CityResidentActivity;
import com.AOU.baladeye.LandingActivity;

public class CerteficatesFrontdoor extends AsyncTask<String, String, Boolean> {

	private Intent intent;
	private CityResidentActivity activity;
	private String birthOfDate;
	private String name;
	private String email;
	private String gender;
	private String mobile;
	private String image;

	public CerteficatesFrontdoor(CityResidentActivity cityResidentActivity,
			String birthOfDate, String email, String gender, String name,
			String mobile, String drawingCache) {
		this.activity = cityResidentActivity;
		this.birthOfDate = birthOfDate;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.mobile = mobile;
		this.image = drawingCache;
	}

	@Override
	protected Boolean doInBackground(String... uri) {
		String responseString;
		try {
			SharedPreferences sharedPreferences = activity
					.getSharedPreferences("baladeye", activity.MODE_PRIVATE);
			String userId = sharedPreferences.getString("user_id", "1");
			// List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// parameters.add(new BasicNameValuePair("name", name));
			// parameters.add(new BasicNameValuePair("email", email));
			// parameters.add(new BasicNameValuePair("dob", birthOfDate));
			// parameters.add(new BasicNameValuePair("gender", gender));
			// parameters.add(new BasicNameValuePair("tel", mobile));
			// parameters.add(new BasicNameValuePair("user", userId));
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			/* example for setting a HttpMultipartMode */
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addTextBody("name", name);
			builder.addTextBody("email", (email));
			builder.addTextBody("dob", (birthOfDate));
			builder.addTextBody("gender", (gender));
			builder.addTextBody("tel", (mobile));
			builder.addTextBody("user", (userId));

			builder.addTextBody("b_zima_desc", ("baraa"));

			File file1 = new File(image);
			// FileBody bin1 = new FileBody(file1, "images/jpeg");

			builder.addBinaryBody("b_zima", file1);
			HttpEntity entity = builder.build();

			// File file2 = new File("Your image Path");
			// FileBody bin2 = new FileBody(file2, "images/jpeg");
			//
			// entity.addPart("file", bin2);
			//
			// File file3 = new File("Your image Path");
			// FileBody bin3 = new FileBody(file3, "images/jpeg");
			//
			// entity.addPart("file", bin3);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(uri[0]);
			// Set the http request
			httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
					CookiePolicy.RFC_2109);

			httpPost.setHeader(
					"Accept",
					"text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
//			httpPost.setHeader("Content-Type",
//					"application/x-www-form-urlencoded");

			// Variable to keep the http response
			responseString = null;

			// Set the parameters in the request
			httpPost.setEntity(entity);// new UrlEncodedFormEntity(parameters,
			// HTTP.UTF_8));

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
				Log.e("CertificatesFrontdoor",
						"Failed to parse json from web response, web response: "
								+ responseString + " error: " + e.toString());
				return false;
			}
		} catch (ClientProtocolException e) {
			Log.e("CertificatesFrontdoor exception", "failed to request: "
					+ uri[0].toString() + " " + e.toString());
			return false;
		} catch (IOException e) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity,
							"الرجاء فحص اتصالكم بالإنترنت و المحاولة لاحقاً",
							Toast.LENGTH_LONG).show();
				}
			});
			Log.e("CertificatesFrontdoor IO exception", "failed to request: "
					+ uri[0].toString() + " " + e.toString());
			return false;
		}
		// MultipartEntity entity = new MultipartEntity();
		//
		// entity.addPart("fname", new StringBody("xyz"));
		//
		// try {
		//
		// File file1 = new File("Your image Path");
		// FileBody bin1 = new FileBody(file1, "images/jpeg");
		//
		// entity.addPart("file", bin1);
		//
		//
		// File file2 = new File("Your image Path");
		// FileBody bin2 = new FileBody(file2, "images/jpeg");
		//
		// entity.addPart("file", bin2);
		//
		// File file3 = new File("Your image Path");
		// FileBody bin3 = new FileBody(file3, "images/jpeg");
		//
		// entity.addPart("file", bin3);
		//
		//
		//
		// postMethod.setEntity(entity);
		// HttpResponse response;
		// response = client.execute(postMethod);
		//
		// String result = EntityUtils.toString(response.getEntity());
		//
		// }
		//
		// catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// uploadFile(image, uri[0]);
		// return true;
	}

	@Override
	protected void onPostExecute(final Boolean result) {
		super.onPostExecute(result);
		// Do anything with response..
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// activity.showProgress(false);
				if (result) {
					Toast.makeText(activity,
							"لقد تم إرسال طلب شهادة ساكن بنجاح",
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

	// int serverResponseCode = 0;
	//
	// public int uploadFile(String sourceFileUri, String upLoadServerUri) {
	// String fileName = sourceFileUri;
	//
	// HttpURLConnection conn = null;
	// DataOutputStream dos = null;
	// String lineEnd = "\r\n";
	// String twoHyphens = "--";
	// String boundary = "*****";
	// int bytesRead, bytesAvailable, bufferSize;
	// byte[] buffer;
	// int maxBufferSize = 1 * 1024 * 1024;
	// File sourceFile = new File(sourceFileUri);
	// if (!sourceFile.isFile()) {
	// Log.e("uploadFile", "Source File not exist :" + fileName);
	// return 0;
	// } else {
	// try {// open a URL connection to the Servlet
	// FileInputStream fileInputStream = new FileInputStream(
	// sourceFile);
	// URL url = new URL(upLoadServerUri);
	// // Open a HTTP  connection to  the URL
	// conn = (HttpURLConnection) url.openConnection();
	// conn.setDoInput(true); // Allow Inputs
	// conn.setDoOutput(true); // Allow Outputs
	// conn.setUseCaches(false); // Don't use a Cached Copy
	// conn.setRequestMethod("POST");
	// conn.setRequestProperty("Connection", "Keep-Alive");
	// conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	// conn.setRequestProperty("Content-Type",
	// "multipart/form-data;boundary=" + boundary);
	// conn.setRequestProperty("uploaded_file", fileName);
	// SharedPreferences sharedPreferences = activity
	// .getSharedPreferences("baladeye", activity.MODE_PRIVATE);
	// String userId = sharedPreferences.getString("user_id", "1");
	// List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	// parameters.add(new BasicNameValuePair("name", name));
	// parameters.add(new BasicNameValuePair("email", email));
	// parameters.add(new BasicNameValuePair("dob", birthOfDate));
	// parameters.add(new BasicNameValuePair("gender", gender));
	// parameters.add(new BasicNameValuePair("tel", mobile));
	// parameters.add(new BasicNameValuePair("user", userId));
	//
	// dos = new DataOutputStream(conn.getOutputStream());
	// // String urlParameters =
	// // "name="+name+"&email="+email+"&dob="+birthOfDate +
	// // "&gender="+gender+"&tel="+mobile+"&user="+userId;
	// OutputStream os = conn.getOutputStream();
	// BufferedWriter writer = new BufferedWriter(
	// new OutputStreamWriter(os, "UTF-8"));
	// writer.write(getQuery(parameters));
	// dos.writeBytes(twoHyphens + boundary + lineEnd);
	// dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="
	// + fileName + "" + lineEnd);
	// dos.writeBytes(lineEnd);
	// // create a buffer of  maximum size
	// bytesAvailable = fileInputStream.available();
	// bufferSize = Math.min(bytesAvailable, maxBufferSize);
	// buffer = new byte[bufferSize];
	// // read file and write it into form...
	// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	// while (bytesRead > 0) {
	// dos.write(buffer, 0, bufferSize);
	// bytesAvailable = fileInputStream.available();
	// bufferSize = Math.min(bytesAvailable, maxBufferSize);
	// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	// }
	// // send multipart form data necesssary after file data...
	// dos.writeBytes(lineEnd);
	// dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	// // Responses from the server (code and message)
	// serverResponseCode = conn.getResponseCode();
	// String serverResponseMessage = conn.getResponseMessage();
	// Log.i("uploadFile", "HTTP Response is : "
	// + serverResponseMessage + ": " + serverResponseCode);
	// if (serverResponseCode == 200) {
	// // String msg =
	// // "File Upload Completed.\n\n See uploaded file here : \n\n"
	// // +" http://www.androidexample.com/media/uploads/"
	// // +uploadFileName;
	// // messageText.setText(msg);
	// activity.runOnUiThread(new Runnable() {
	// public void run() {
	// Toast.makeText(activity, "File Upload Complete.",
	// Toast.LENGTH_SHORT).show();
	// }
	// });
	//
	// }
	// // close the streams //
	// fileInputStream.close();
	// dos.flush();
	// dos.close();
	// } catch (MalformedURLException ex) {
	// ex.printStackTrace();
	// // Toast.makeText(UploadToServer.this, "MalformedURLException",
	// // Toast.LENGTH_SHORT).show();
	// Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
	// } catch (Exception e) {
	// e.printStackTrace();
	// activity.runOnUiThread(new Runnable() {
	// public void run() {
	// Toast.makeText(activity, "Got Exception : see logcat ",
	// Toast.LENGTH_SHORT).show();
	// }
	// });
	//
	// Log.e("Upload file to server Exception",
	// "Exception : " + e.getMessage(), e);
	// }
	// return serverResponseCode;
	// } // End else block
	// }

	// private String getQuery(List<NameValuePair> params)
	// throws UnsupportedEncodingException {
	// StringBuilder result = new StringBuilder();
	// boolean first = true;
	//
	// for (NameValuePair pair : params) {
	// if (first)
	// first = false;
	// else
	// result.append("&");
	//
	// result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	// result.append("=");
	// result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	// }
	//
	// return result.toString();
	// }
	//
	// private InputStream OpenHttpConnection(String urlString) throws
	// IOException {
	// InputStream in = null;
	// int response = -1;
	//
	// URL url = new URL(urlString);
	// URLConnection conn = url.openConnection();
	//
	// if (!(conn instanceof HttpURLConnection))
	// throw new IOException("Not an HTTP connection");
	//
	// try {
	// HttpURLConnection httpConn = (HttpURLConnection) conn;
	// httpConn.setAllowUserInteraction(false);
	// httpConn.setInstanceFollowRedirects(true);
	// httpConn.setRequestMethod("GET");
	// httpConn.connect();
	// response = httpConn.getResponseCode();
	// if (response == HttpURLConnection.HTTP_OK) {
	// in = httpConn.getInputStream();
	// }
	// } catch (Exception ex) {
	// throw new IOException("Error connecting");
	// }
	// return in;
	// }

}