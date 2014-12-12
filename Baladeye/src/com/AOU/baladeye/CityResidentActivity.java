package com.AOU.baladeye;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.util.TextUtils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.AOU.baladeye.frontdoors.CerteficatesFrontdoor;

public class CityResidentActivity extends Activity {

	private TextView tvDisplayDate;
	private Button btnChangeDate;
	private Button sendBtn;
	private AutoCompleteTextView emailView;
	private Switch genderSwitch;
	private AutoCompleteTextView name;
	private AutoCompleteTextView mobileNumber;

	private int year;
	private int month;
	private int day;
	private Uri outputFileUri;

	private final int EJAR_SELECT_PHOTO = 1;
	private final int BITAKA_SELECT_PHOTO = 2;
	private final int BARAA_SELECT_PHOTO = 3;
	private ImageView baraaImageView;
	private ImageView ejarImageView;
	private ImageView bitakaImageView;
	private Button ejarPickImage;
	private Button baraaPickImage;
	private Button bitakaPickImage;
	private boolean isBaraa = false;
	private boolean isBitaka = false;
	private boolean isEjar = false;

	static final int DATE_DIALOG_ID = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_resident);

		tvDisplayDate = (TextView) findViewById(R.id.tvDate);
		emailView = (AutoCompleteTextView) findViewById(R.id.email_resident_editText);
		genderSwitch = (Switch) findViewById(R.id.gender_switch);
		baraaImageView = (ImageView) findViewById(R.id.baraa_imageView);
		ejarImageView = (ImageView) findViewById(R.id.ejar_imageView);
		bitakaImageView = (ImageView) findViewById(R.id.bitaka_imageView);

		name = (AutoCompleteTextView) findViewById(R.id.resident_name_txt);
		mobileNumber = (AutoCompleteTextView) findViewById(R.id.mobile_resident);

		sendBtn = (Button) findViewById(R.id.send_request_btn);
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				tvDisplayDate.setError(null);
				name.setError(null);
				emailView.setError(null);
				mobileNumber.setError(null);
				baraaPickImage.setError(null);
				bitakaPickImage.setError(null);
				ejarPickImage.setError(null);
				if (tvDisplayDate.getText() == null
						|| TextUtils
								.isEmpty(tvDisplayDate.getText().toString())
						|| TextUtils
								.isBlank(tvDisplayDate.getText().toString())) {
					tvDisplayDate.setError("هذا المدخل مطلوب");
				} else if (emailView.getText() == null
						|| TextUtils.isEmpty(emailView.getText().toString())
						|| TextUtils.isBlank(emailView.getText().toString())) {
					emailView.setError("هذا المدخل مطلوب");
				} else if (!RegistrationActivity.isEmailValid(emailView
						.getText().toString())) {
					emailView.setError("البريد الإلكتروني غير صالح");

				} else if (name.getText() == null

				|| TextUtils.isEmpty(name.getText().toString())
						|| TextUtils.isBlank(name.getText().toString())) {
					name.setError("هذا المدخل مطلوب");
				} else if (mobileNumber.getText() == null
						|| TextUtils.isEmpty(mobileNumber.getText().toString())
						|| TextUtils.isBlank(mobileNumber.getText().toString())) {
					mobileNumber.setError("هذا المدخل مطلوب");
				} else if (!isBaraa) {
					baraaPickImage.setError("يجب إرفاق براءة ذمة");
					;
				} else if (!isBitaka) {
					bitakaPickImage.setError("يجب إرفاق بطاقة الهوية");
					;
				} else if (!isEjar) {
					ejarPickImage.setError("يجب إرفاق وثيقة أجار");
					;
				} else {
					new CerteficatesFrontdoor(CityResidentActivity.this,
							tvDisplayDate.getText().toString(), emailView
									.getText().toString(), genderSwitch
									.getTextOn().toString(), name.getText()
									.toString(), mobileNumber.getText()
									.toString(), "")
							.execute("http://go-social.me/i-salfit/api/certificate_city_resident.php");
				}
			}
		});
		ejarPickImage = (Button) findViewById(R.id.ejar_btn_pick);
		ejarPickImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				openImageIntent(EJAR_SELECT_PHOTO);
			}
		});
		baraaPickImage = (Button) findViewById(R.id.baraa_btn_pick);
		baraaPickImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				openImageIntent(BARAA_SELECT_PHOTO);
			}
		});
		bitakaPickImage = (Button) findViewById(R.id.bitaka_btn_pick);
		bitakaPickImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				openImageIntent(BITAKA_SELECT_PHOTO);
			}
		});
		setCurrentDateOnView();
		addListenerOnButton();
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri) {
		if (uri != null) {
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
		return "";
	}

	private void openImageIntent(int key) {

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

		startActivityForResult(chooserIntent, key);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == EJAR_SELECT_PHOTO
					|| requestCode == BARAA_SELECT_PHOTO
					|| requestCode == BITAKA_SELECT_PHOTO) {
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
					ImageView imageView = null;

					if (requestCode == BARAA_SELECT_PHOTO) {
						isBaraa = true;
						imageView = baraaImageView;
					} else if (requestCode == EJAR_SELECT_PHOTO) {
						isEjar = true;
						imageView = ejarImageView;
					} else if (requestCode == BITAKA_SELECT_PHOTO) {
						isBitaka = true;
						imageView = bitakaImageView;
					}
					imageView.setImageBitmap(getResizedBitmap(selectedImage,
							200, 200));

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.city_resident, menu);
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

	// display current date
	public void setCurrentDateOnView() {

		// dpResult = (DatePicker) findViewById(R.id.dpResult);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		tvDisplayDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(month + 1).append("-").append(day).append("-")
				.append(year).append(" "));
	}

	public void addListenerOnButton() {
		btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
		btnChangeDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			tvDisplayDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));
		}
	};
}
