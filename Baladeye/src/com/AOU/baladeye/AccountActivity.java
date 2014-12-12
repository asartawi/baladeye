package com.AOU.baladeye;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.AOU.baladeye.frontdoors.InvoicesFrontdoor;

public class AccountActivity extends Activity implements Progress {

	private Button invoicestBtn;
	private Button certeficatesBtn;
	private Button informationButton;
	private View mProgressView;
	private View mainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		mProgressView = findViewById(R.id.account_data_progress);
		mainView = findViewById(R.id.account_form);

		invoicestBtn = (Button) findViewById(R.id.invoices_btn);
		invoicestBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AccountActivity.this,
						InvoicesActivity.class);
				showProgress(true);
				new InvoicesFrontdoor(intent, AccountActivity.this)
						.execute("http://www.go-social.me/i-salfit/api/bills.php");
			}
		});

		certeficatesBtn = (Button) findViewById(R.id.customer_services_btn);
		certeficatesBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AccountActivity.this,
						CustomerServicesActivity.class);
				startActivity(intent);
//				showProgress(true);
//				new CerteficatesFrontdoor(intent, AccountActivity.this)
//						.execute("http://go-social.me/i-salfit/api/certificate_city_resident.php");
			}
		});
		informationButton = (Button) findViewById(R.id.information_btn);
		informationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AccountActivity.this,
						InformationActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
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

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mainView.setVisibility(show ? View.GONE : View.VISIBLE);
			mainView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
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

}
