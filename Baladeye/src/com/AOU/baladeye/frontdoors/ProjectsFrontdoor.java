package com.AOU.baladeye.frontdoors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.AOU.baladeye.LandingActivity;
import com.AOU.baladeye.models.News;
import com.AOU.baladeye.models.Project;

public class ProjectsFrontdoor extends AsyncTask<String, String, List<Project>>{

	private Intent intent;
	private LandingActivity activity;
	public static List<Project> projectsList;
	public ProjectsFrontdoor(Intent intent, LandingActivity activity){
		this.intent = intent;
		this.activity = activity;
	}
    @Override
    protected List<Project> doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        projectsList= new ArrayList<Project>();

        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                try{
    			JSONArray jsonArray = new JSONArray(responseString);
    			for (int i = 0; i < jsonArray.length(); i++) {
    				JSONObject object = jsonArray.getJSONObject(i);
    				String id = object.getString("id");
    				String title = object.getString("title");
    				String picUrl = object.getString("pic");
    				String content = object.getString("content");
    				Bitmap bitmap = DownloadImage(picUrl);
    				Project news = new Project(id, title, picUrl, content, bitmap);
    				projectsList.add(news);
    			}
    			
    		} catch (JSONException e) {
    			Log.e("ProjectsFrontdoor",
    					"Failed to parse json from web response, web response: "
    							+ responseString + " error: " + e.toString());
    		}
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.e("frontdoor exception", "failed to request: " + uri.toString() + " " + e.toString());
        } catch (IOException e) {
            Log.e("frontdoor exception", "failed to request: " + uri.toString() + " " + e.toString());
        }
        return projectsList;
    }

    @Override
    protected void onPostExecute(final List<Project> projects) {
        super.onPostExecute(projects);
        //Do anything with response..
        LandingActivity.context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				activity.showProgress(false);
				activity.startActivity(intent);
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

	private Bitmap DownloadImage(String URL) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bitmap;
	}
}