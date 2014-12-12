package com.AOU.baladeye;

import org.apache.http.util.TextUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.AOU.baladeye.frontdoors.InformationFrontdoor;

public class InformationActivity extends Activity implements Progress {

	private Button informationSendBtn;
	private EditText title;
	private EditText content;
	private View mProgressView;

	// private View mainView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);
		mProgressView = findViewById(R.id.information_data_progress);
		title = (EditText) findViewById(R.id.information_title);
		content = (EditText) findViewById(R.id.information_content);
		informationSendBtn = (Button) findViewById(R.id.information_send_btn);
		informationSendBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				title.setError(null);
				content.setError(null);

				if (TextUtils.isEmpty(title.getText())) {
					title.setError("يجب ملأ الحقل");
				} else if (TextUtils.isEmpty(content.getText())) {
					content.setError("يجب ملأ الحقل");
				} else {
					showProgress(true);
					SharedPreferences sharedPreferences = getSharedPreferences(
							"baladeye", MODE_PRIVATE);
					String userId = sharedPreferences.getString("user_id", "1");
					new InformationFrontdoor(InformationActivity.this, title
							.getText().toString(),
							content.getText().toString(), userId)
							.execute("http://www.go-social.me/i-salfit/api/questions.php");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.information, menu);
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

			// mainView.setVisibility(show ? View.GONE : View.VISIBLE);
			// mainView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
			// .setListener(new AnimatorListenerAdapter() {
			// @Override
			// public void onAnimationEnd(Animator animation) {
			// mainView.setVisibility(show ? View.GONE
			// : View.VISIBLE);
			// }
			// });

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
			// mainView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
