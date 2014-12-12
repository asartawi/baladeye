package com.AOU.baladeye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class FacilitiesListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities_list);
		Button jamahereBtn = (Button) findViewById(R.id.jamahere_btn);
		jamahereBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FacilitiesListActivity.this,
						FacilityJamahereActivity.class);
				startActivity(intent);
			}
		});

		Button masla5Btn = (Button) findViewById(R.id.masla5_btn);
		masla5Btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FacilitiesListActivity.this,
						FacilityMasla5Activity.class);
				startActivity(intent);
			}
		});

		Button diwanBtn = (Button) findViewById(R.id.diwan_btn);
		diwanBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FacilitiesListActivity.this,
						FacilityDiwanActivity.class);
				startActivity(intent);
			}
		});

		Button montazahBtn = (Button) findViewById(R.id.montazah_btn);
		montazahBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FacilitiesListActivity.this,
						FacilityMontazahActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facilities_list, menu);
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
