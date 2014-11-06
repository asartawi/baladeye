package com.AOU.baladeye;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SplashActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (getIntent().getBooleanExtra("EXIT", false)) {
		    finish();  
		}
		super.onCreate(savedInstanceState);
		final SharedPreferences sharedPreferences = getSharedPreferences("baladeye", MODE_PRIVATE);
		if(sharedPreferences.contains("loggedIn") && sharedPreferences.getBoolean("loggedIn", false) != false){
			Intent intent = new Intent(this, LandingActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.activity_splash);
		final Button loginButton = (Button) findViewById(R.id.sign_in);
		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!sharedPreferences.contains("username")) {
					Toast.makeText(SplashActivity.this,
							getString(R.string.tostSignupFirst),
							Toast.LENGTH_LONG).show();
					loginButton.setError(getString(R.string.tostSignupFirst));
				} else {
					Intent intent = new Intent(SplashActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
			}
		});

		final Button signupButton = (Button) findViewById(R.id.sign_up);
		signupButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(SplashActivity.this,
						RegistrationActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
