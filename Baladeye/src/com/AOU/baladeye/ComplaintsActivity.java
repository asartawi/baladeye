package com.AOU.baladeye;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.TextUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.AOU.baladeye.frontdoors.ComplaintsFrontdoor;

public class ComplaintsActivity extends Activity implements
		OnItemSelectedListener {
	private Spinner complaintTypesSpinner;
	private int selectedType;

	private TextView placeTextView;
	private Button sendBtn;
	private AutoCompleteTextView nameView;
	private AutoCompleteTextView contentView;
	private AutoCompleteTextView mobileNumber;

	private int year;
	private int month;
	private int day;
	private final int SELECT_PHOTO = 1;
	private Uri outputFileUri;
	private ImageView imageView;
	static final int DATE_DIALOG_ID = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaints);
		complaintTypesSpinner = (Spinner) findViewById(R.id.complaint_types);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.complaint_types_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		complaintTypesSpinner.setAdapter(adapter);
		complaintTypesSpinner.setOnItemSelectedListener(this);

		placeTextView = (TextView) findViewById(R.id.place_editText);
		nameView = (AutoCompleteTextView) findViewById(R.id.name_editText);
		imageView = (ImageView) findViewById(R.id.imageView);
		contentView = (AutoCompleteTextView) findViewById(R.id.complaint_content_txt);
		mobileNumber = (AutoCompleteTextView) findViewById(R.id.mobile_complaint);

		sendBtn = (Button) findViewById(R.id.send_request_btn);
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				placeTextView.setError(null);
				nameView.setError(null);
				mobileNumber.setError(null);
				contentView.setError(null);
				if (placeTextView.getText() == null
						|| TextUtils
								.isEmpty(placeTextView.getText().toString())
						|| TextUtils
								.isBlank(placeTextView.getText().toString())) {
					placeTextView.setError("هذا المدخل مطلوب");
				} else if (nameView.getText() == null
						|| TextUtils.isEmpty(nameView.getText().toString())
						|| TextUtils.isBlank(nameView.getText().toString())) {
					nameView.setError("هذا المدخل مطلوب");
				} else if (mobileNumber.getText() == null
						|| TextUtils.isEmpty(mobileNumber.getText().toString())
						|| TextUtils.isBlank(mobileNumber.getText().toString())) {
					mobileNumber.setError("هذا المدخل مطلوب");
				} else if (contentView.getText() == null

				|| TextUtils.isEmpty(contentView.getText().toString())
						|| TextUtils.isBlank(contentView.getText().toString())) {
					contentView.setError("هذا المدخل مطلوب");
				} else {
					new ComplaintsFrontdoor(ComplaintsActivity.this,
							contentView.getText().toString(),
							selectedType + "", placeTextView.getText()
									.toString(), nameView.getText().toString(),
							mobileNumber.getText().toString(), imageView
									.getDrawingCache())
							.execute("http://go-social.me/i-salfit/api/complaints.php");
				}
			}
		});
		Button pickImage = (Button) findViewById(R.id.btn_pick);
		pickImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				openImageIntent();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complaints, menu);
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

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		this.selectedType = pos + 1;
	}

	private void openImageIntent() {

		// Determine Uri of camera image to save.
		final File root = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "Baladeye" + File.separator);
		root.mkdirs();
		final String fname = "img_" + System.currentTimeMillis() + ".jpg";
		final File sdImageMainDirectory = new File(root, fname);
		outputFileUri = Uri.fromFile(sdImageMainDirectory);

		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				"اختر مصدر الوثيقة");

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));

		startActivityForResult(chooserIntent, SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PHOTO) {
				final boolean isCamera;
				if (data == null) {
					isCamera = true;
				} else {
					final String action = data.getAction();
					if (action == null) {
						isCamera = false;
					} else {
						isCamera = action
								.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					}
				}

				Uri selectedImageUri;
				if (isCamera) {
					selectedImageUri = outputFileUri;
				} else {
					selectedImageUri = data == null ? null : data.getData();
				}
				try {
					final InputStream imageStream = getContentResolver()
							.openInputStream(selectedImageUri);
					final Bitmap selectedImage = BitmapFactory
							.decodeStream(imageStream);
					imageView.setImageBitmap(CityResidentActivity
							.getResizedBitmap(selectedImage, 200, 200));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
