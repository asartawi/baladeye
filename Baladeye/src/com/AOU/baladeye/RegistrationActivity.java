package com.AOU.baladeye;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************ In order for Google+ sign in
 * to work with your app, you must first go to:
 * https://developers.google.com/+/mobile
 * /android/getting-started#step_1_enable_the_google_api and follow the steps in
 * "Step 1" to create an OAuth 2.0 client for your package.
 */
public class RegistrationActivity extends Activity {

	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserSignupTask mAuthTask = null;

	// UI references.
	private AutoCompleteTextView mEmailView;
	private AutoCompleteTextView mobileView;
	private AutoCompleteTextView nameView;

	private EditText mPasswordView;
	private View mProgressView;
	private View mEmailLoginFormView;
	private View mLoginFormView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		mobileView = (AutoCompleteTextView) findViewById(R.id.mobile);
		nameView = (AutoCompleteTextView) findViewById(R.id.name);

//		populateAutoComplete();

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptSignup();
							return true;
						}
						return false;
					}
				});

		Button mEmailSignInButton = (Button) findViewById(R.id.email_register_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptSignup();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
		mEmailLoginFormView = findViewById(R.id.email_login_form);
	}

//	private void populateAutoComplete() {
//		if (VERSION.SDK_INT >= 14) {
//			// Use ContactsContract.Profile (API 14+)
//			getLoaderManager().initLoader(0, null, this);
//		} else if (VERSION.SDK_INT >= 8) {
//			// Use AccountManager (API 8+)
//			new SetupEmailAutoCompleteTask().execute(null, null);
//		}
//	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptSignup() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserSignupTask(email, password);
			mAuthTask.execute((String) null);
		}
	}

	public static boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		return password != null && !password.equals("")
				&& password.length() > 4;
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

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
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
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

//	@Override
//	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//		return new CursorLoader(this,
//				// Retrieve data rows for the device user's 'profile' contact.
//				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
//				ProfileQuery.PROJECTION,
//
//				// Select only email addresses.
//				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
//				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },
//
//				// Show primary email addresses first. Note that there won't be
//				// a primary email address if the user hasn't specified one.
//				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//	}

//	@Override
//	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//		List<String> emails = new ArrayList<String>();
//		cursor.moveToFirst();
//		while (!cursor.isAfterLast()) {
//			emails.add(cursor.getString(ProfileQuery.ADDRESS));
//			cursor.moveToNext();
//		}
//
//		addEmailsToAutoComplete(emails);
//	}

//	@Override
//	public void onLoaderReset(Loader<Cursor> cursorLoader) {
//
//	}

//	private interface ProfileQuery {
//		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
//				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };
//
//		int ADDRESS = 0;
//		int IS_PRIMARY = 1;
//	}

	/**
	 * Use an AsyncTask to fetch the user's email addresses on a background
	 * thread, and update the email text field with results on the main UI
	 * thread.
	 */
	class SetupEmailAutoCompleteTask extends
			AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... voids) {
			ArrayList<String> emailAddressCollection = new ArrayList<String>();

			// Get all emails from the user's contacts and copy them to a list.
			ContentResolver cr = getContentResolver();
			Cursor emailCur = cr.query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					null, null, null);
			while (emailCur.moveToNext()) {
				String email = emailCur
						.getString(emailCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				emailAddressCollection.add(email);
			}
			emailCur.close();

			return emailAddressCollection;
		}

		@Override
		protected void onPostExecute(List<String> emailAddressCollection) {
			addEmailsToAutoComplete(emailAddressCollection);
		}
	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				RegistrationActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				emailAddressCollection);

		mEmailView.setAdapter(adapter);
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserSignupTask extends AsyncTask<String, Void, Boolean> {

		private final String mEmail;
		private final String mPassword;
		private final String registrationUrl = "http://www.go-social.me/i-salfit/api/reg.php";

		UserSignupTask(String email, String password) {
			mEmail = email;
			mPassword = password;

		}

		@Override
		protected Boolean doInBackground(String... params) {
			String responseString;
			try {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("name", nameView
						.getText().toString()));
				parameters.add(new BasicNameValuePair("mobile", mobileView
						.getText().toString()));
				parameters.add(new BasicNameValuePair("email", mEmailView
						.getText().toString()));
				parameters.add(new BasicNameValuePair("password", mPasswordView
						.getText().toString()));

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(registrationUrl);

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
						SharedPreferences sharedPreferences = getSharedPreferences(
								"baladeye", MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPreferences
								.edit();
						editor.putString("username", mEmail);
						editor.putString("password", mPassword);
						editor.apply();
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
				// Log.e("frontdoor exception", "failed to request: " +
				// uri.toString()
				// + " " + e.toString());
				return false;
			} catch (IOException e) {
				// Log.e("frontdoor exception", "failed to request: " +
				// uri.toString()
				// + " " + e.toString());
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				Intent intent = new Intent(RegistrationActivity.this,
						LandingActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("EXIT", true);
				startActivity(intent);
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
