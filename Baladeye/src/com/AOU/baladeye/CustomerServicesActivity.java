package com.AOU.baladeye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CustomerServicesActivity extends Activity {
private Button citizencerteficateBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_services);
		citizencerteficateBtn = (Button)findViewById(R.id.city_citizen_certificate_btn);
				citizencerteficateBtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(CustomerServicesActivity.this,
								CityResidentActivity.class);
						startActivity(intent);
//						showProgress(true);
//						new CerteficatesFrontdoor(intent, AccountActivity.this)
//								.execute("http://go-social.me/i-salfit/api/certificate_city_resident.php");
						
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer_services, menu);
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
