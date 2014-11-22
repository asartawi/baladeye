package com.AOU.baladeye;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.AOU.baladeye.frontdoors.InvoicesFrontdoor;
import com.AOU.baladeye.frontdoors.NewsFrontdoor;
import com.AOU.baladeye.frontdoors.ProjectsFrontdoor;

public class LandingActivity extends Activity implements Progress {
private  Button aboutBtn;
private  Button contactBtn;
private  Button newsBtn;
private Button projectsBtn;
private Button accountBtn;
private View mProgressView;
private View mainView;

public static Activity context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);
		context = this;
		aboutBtn = (Button)findViewById(R.id.about_btn);
		mProgressView = findViewById(R.id.landing_progress);
		mainView = findViewById(R.id.landing_main_content);
		aboutBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LandingActivity.this, AboutActivity.class);
				startActivity(intent);				
			}
		});
		
		contactBtn = (Button)findViewById(R.id.contact_btn);
		contactBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LandingActivity.this, ContactActivity.class);
				startActivity(intent);				
			}
		});
		
		newsBtn = (Button)findViewById(R.id.news_btn);
		newsBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent  = new Intent(LandingActivity.this, NewsActivity.class);
				showProgress(true);
				new NewsFrontdoor(intent, LandingActivity.this).execute("http://www.go-social.me/i-salfit/api/news.php");
			}
		});
		
		projectsBtn = (Button)findViewById(R.id.projects_btn);
		projectsBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent  = new Intent(LandingActivity.this, ProjectsActivity.class);
				showProgress(true);
				new ProjectsFrontdoor(intent, LandingActivity.this).execute("http://go-social.me/i-salfit/api/projects.php");
			}
		});
		
		accountBtn = (Button)findViewById(R.id.account_btn);
		accountBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent  = new Intent(LandingActivity.this, AccountActivity.class);
				startActivity(intent);
			}
		});
	}
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mainView.setVisibility(show ? View.GONE : View.VISIBLE);
			mainView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mainView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mainView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.landing, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			SharedPreferences sharedPreferences = getSharedPreferences(
					"baladeye", MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean("loggedIn", false);
			editor.apply();
			Intent intent = new Intent(LandingActivity.this, SplashActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
