package jv.android.remotetracker.activity;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.utils.Contacts;
import jv.android.utils.Logs;
import jv.android.utils.Message;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class Emergency extends AppCompatActivity {

	private EditText etPhone1;
	private EditText etPhone2;
	private EditText etPhone3;
	private EditText etPhone4;
	private EditText etPhoneAlias1;
	private EditText etPhoneAlias2;
	private EditText etPhoneAlias3;
	private EditText etPhoneAlias4;
	private EditText etEmail1;
	private EditText etEmail2;
	private EditText etEmail3;
	private EditText etEmail4;
	private EditText etEmailAlias1;
	private EditText etEmailAlias2;
	private EditText etEmailAlias3;
	private EditText etEmailAlias4;

	private Preferences preferences;

	private static final int PHONE1 = 1;
	private static final int PHONE2 = 2;
	private static final int PHONE3 = 3;
	private static final int PHONE4 = 4;
	private static final int EMAIL1 = 5;
	private static final int EMAIL2 = 6;
	private static final int EMAIL3 = 7;
	private static final int EMAIL4 = 8;

	//	private static final int CONTACTID = 0;
	private static final int CONTACTNAME = 1;
	private static final int CONTACTPHONENUMBER = 2;
	private static final int CONTACTEMAILADDRESS = 3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency);        

		ImageButton ibPhone1 = findViewById(R.id.ibPhone1);
		ImageButton ibPhone2 = findViewById(R.id.ibPhone2);
		ImageButton ibPhone3 = findViewById(R.id.ibPhone3);
		ImageButton ibPhone4 = findViewById(R.id.ibPhone4);
		ImageButton ibPhone1C = findViewById(R.id.ibPhone1C);
		ImageButton ibPhone2C = findViewById(R.id.ibPhone2C);
		ImageButton ibPhone3C = findViewById(R.id.ibPhone3C);
		ImageButton ibPhone4C = findViewById(R.id.ibPhone4C);
		ImageButton ibEmail1 = findViewById(R.id.ibEMail1);
		ImageButton ibEmail2 = findViewById(R.id.ibEMail2);
		ImageButton ibEmail3 = findViewById(R.id.ibEMail3);
		ImageButton ibEmail4 = findViewById(R.id.ibEMail4);
		ImageButton ibEmail1C = findViewById(R.id.ibEMail1C);
		ImageButton ibEmail2C = findViewById(R.id.ibEMail2C);
		ImageButton ibEmail3C = findViewById(R.id.ibEMail3C);
		ImageButton ibEmail4C = findViewById(R.id.ibEMail4C);
		etPhone1 = findViewById(R.id.etPhone1);
		etPhone2 = findViewById(R.id.etPhone2);
		etPhone3 = findViewById(R.id.etPhone3);
		etPhone4 = findViewById(R.id.etPhone4);
		etPhoneAlias1 = findViewById(R.id.etPhoneAlias1);
		etPhoneAlias2 = findViewById(R.id.etPhoneAlias2);
		etPhoneAlias3 = findViewById(R.id.etPhoneAlias3);
		etPhoneAlias4 = findViewById(R.id.etPhoneAlias4);
		etEmail1 = findViewById(R.id.etEMail1);
		etEmail2 = findViewById(R.id.etEMail2);
		etEmail3 = findViewById(R.id.etEMail3);
		etEmail4 = findViewById(R.id.etEMail4);
		etEmailAlias1 = findViewById(R.id.etEMailAlias1);
		etEmailAlias2 = findViewById(R.id.etEMailAlias2);
		etEmailAlias3 = findViewById(R.id.etEMailAlias3);
		etEmailAlias4 = findViewById(R.id.etEMailAlias4);

		preferences = new Preferences (getApplicationContext());

		etPhone1.setText(preferences.getCel1());
		etPhone2.setText(preferences.getCel2());
		etPhone3.setText(preferences.getCel3());
		etPhone4.setText(preferences.getCel4());
		etPhoneAlias1.setText(preferences.getCelAlias1());
		etPhoneAlias2.setText(preferences.getCelAlias2());
		etPhoneAlias3.setText(preferences.getCelAlias3());
		etPhoneAlias4.setText(preferences.getCelAlias4());
		etEmail1.setText(preferences.getEmail1());
		etEmail2.setText(preferences.getEmail2());
		etEmail3.setText(preferences.getEmail3());
		etEmail4.setText(preferences.getEmail4());
		etEmailAlias1.setText(preferences.getEmailAlias1());
		etEmailAlias2.setText(preferences.getEmailAlias2());
		etEmailAlias3.setText(preferences.getEmailAlias3());
		etEmailAlias4.setText(preferences.getEmailAlias4());

		if (ibPhone1 != null)
			ibPhone1.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
					startActivityForResult(contactPickerIntent, PHONE1);  
				}
			});
		if (ibPhone2 != null)
			ibPhone2.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
					startActivityForResult(contactPickerIntent, PHONE2);  
				}
			});
		if (ibPhone3 != null)
			ibPhone3.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
					startActivityForResult(contactPickerIntent, PHONE3);  
				}
			});
		if (ibPhone4 != null)
			ibPhone4.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
					startActivityForResult(contactPickerIntent, PHONE4);  
				}
			});

		if (ibEmail1 != null)
			ibEmail1.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
					startActivityForResult(contactPickerIntent, EMAIL1);  
				}
			});
		if (ibEmail2 != null)
			ibEmail2.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
					startActivityForResult(contactPickerIntent, EMAIL2);  
				}
			});
		if (ibEmail3 != null)
			ibEmail3.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
					startActivityForResult(contactPickerIntent, EMAIL3);  
				}
			});
		if (ibEmail4 != null)
			ibEmail4.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
					startActivityForResult(contactPickerIntent, EMAIL4);  
				}
			});

		if (ibPhone1C != null)
			ibPhone1C.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					etPhone1.setText("");
					etPhoneAlias1.setText("");
				}
			});
		if (ibPhone2C != null)
			ibPhone2C.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					etPhone2.setText("");
					etPhoneAlias2.setText("");
				}
			});
		if (ibPhone3C != null)
			ibPhone3C.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					etPhone3.setText("");
					etPhoneAlias3.setText("");
				}
			});
		if (ibPhone4C != null)
			ibPhone4C.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					etPhone4.setText("");
					etPhoneAlias4.setText("");
				}
			});
		if (ibEmail1C != null)
			ibEmail1C.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					etEmail1.setText("");
					etEmailAlias1.setText("");
				}
			});
		if (ibEmail2C != null)
			ibEmail2C.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					etEmail2.setText("");
					etEmailAlias2.setText("");
				}
			});
		if (ibEmail3C != null)
			ibEmail3C.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					etEmail3.setText("");
					etEmailAlias3.setText("");
				}
			});
		if (ibEmail4C != null)
			ibEmail4C.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					etEmail4.setText("");
					etEmailAlias4.setText("");
				}
			});

		etPhone1.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ok, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_ok) {
			preferences.setCel1(etPhone1.getText().toString().trim());
			preferences.setCel2(etPhone2.getText().toString().trim());
			preferences.setCel3(etPhone3.getText().toString().trim());
			preferences.setCel4(etPhone4.getText().toString().trim());
			preferences.setCelAlias1(etPhoneAlias1.getText().toString().trim());
			preferences.setCelAlias2(etPhoneAlias2.getText().toString().trim());
			preferences.setCelAlias3(etPhoneAlias3.getText().toString().trim());
			preferences.setCelAlias4(etPhoneAlias4.getText().toString().trim());
			preferences.setEmail1(etEmail1.getText().toString().trim());
			preferences.setEmail2(etEmail2.getText().toString().trim());
			preferences.setEmail3(etEmail3.getText().toString().trim());
			preferences.setEmail4(etEmail4.getText().toString().trim());
			preferences.setEmailAlias1(etEmailAlias1.getText().toString().trim());
			preferences.setEmailAlias2(etEmailAlias2.getText().toString().trim());
			preferences.setEmailAlias3(etEmailAlias3.getText().toString().trim());
			preferences.setEmailAlias4(etEmailAlias4.getText().toString().trim());

			SharedPreferences.Editor prefEditor = preferences.getSharedPrefs().edit();
			prefEditor.putString("cel1", preferences.getCel1());
			prefEditor.putString("cel2", preferences.getCel2());
			prefEditor.putString("cel3", preferences.getCel3());
			prefEditor.putString("cel4", preferences.getCel4());
			prefEditor.putString("celAlias1", preferences.getCelAlias1());
			prefEditor.putString("celAlias2", preferences.getCelAlias2());
			prefEditor.putString("celAlias3", preferences.getCelAlias3());
			prefEditor.putString("celAlias4", preferences.getCelAlias4());
			prefEditor.putString("email1", preferences.getEmail1());
			prefEditor.putString("email2", preferences.getEmail2());
			prefEditor.putString("email3", preferences.getEmail3());
			prefEditor.putString("email4", preferences.getEmail4());
			prefEditor.putString("emailAlias1", preferences.getEmailAlias1());
			prefEditor.putString("emailAlias2", preferences.getEmailAlias2());
			prefEditor.putString("emailAlias3", preferences.getEmailAlias3());
			prefEditor.putString("emailAlias4", preferences.getEmailAlias4());
			prefEditor.apply();

			preferences.exportPreferences();
			finish();
		}

        return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {  
			Contacts.setPbCustomEmail(getString(R.string.pbCustomEmail));
			Contacts.setPbCustomPhone(getString(R.string.pbCustomPhone));
			Contacts.setPbHomeEmail(getString(R.string.pbHomeEmail));
			Contacts.setPbHomeFax(getString(R.string.pbHomeFax));
			Contacts.setPbHomePhone(getString(R.string.pbHomePhone));
			Contacts.setPbMobileEmail(getString(R.string.pbMobileEmail));
			Contacts.setPbMobilePhone(getString(R.string.pbMobilePhone));
			Contacts.setPbOtherEmail(getString(R.string.pbOtherEmail));
			Contacts.setPbOtherFax(getString(R.string.pbOtherFax));
			Contacts.setPbOtherPhone(getString(R.string.pbOtherPhone));
			Contacts.setPbWorkEmail(getString(R.string.pbWorkEmail));
			Contacts.setPbWorkFax(getString(R.string.pbWorkFax));
			Contacts.setPbWorkPhone(getString(R.string.pbWorkPhone));

			final String[] contact = Contacts.pickContact(Emergency.this, data);
			final CharSequence[] num = Contacts.numbers(Emergency.this, contact[CONTACTNAME]);
			final CharSequence[] em = Contacts.emails(Emergency.this, contact[CONTACTNAME]);

			switch (requestCode) {  
			case PHONE1:
				if (num == null || num.length == 0) {
					Message.showMessage(this, getString(R.string.msgWarning), getString(R.string.msgContactWithoutNumber));					
				} else if (num.length == 1) {
					etPhone1.setText(contact[CONTACTPHONENUMBER]);
					etPhoneAlias1.setText(contact[CONTACTNAME]);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.msgPleaseSelect)
							.setItems(num, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									CharSequence[] n = String.valueOf(num[which]).split("=");
									etPhone1.setText(n[1]);
									etPhoneAlias1.setText(contact[CONTACTNAME]);
								}
							});

					AlertDialog alert = builder.create();
					alert.show();					
				}

				break;  
			case PHONE2:  
				if (num == null || num.length == 0) {
					Message.showMessage(this, getString(R.string.msgWarning), getString(R.string.msgContactWithoutNumber));					
				} else if (num.length == 1) {
					etPhone2.setText(contact[CONTACTPHONENUMBER]);
					etPhoneAlias2.setText(contact[CONTACTNAME]);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.msgPleaseSelect)
							.setItems(num, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									CharSequence[] n = String.valueOf(num[which]).split("=");
									etPhone2.setText(n[1]);
									etPhoneAlias2.setText(contact[CONTACTNAME]);
								}
							});

					AlertDialog alert = builder.create();
					alert.show();					
				}
				break;  
			case PHONE3:  
				if (num == null || num.length == 0) {
					Message.showMessage(this, getString(R.string.msgWarning), getString(R.string.msgContactWithoutNumber));					
				} else if (num.length == 1) {
					etPhone3.setText(contact[CONTACTPHONENUMBER]);
					etPhoneAlias3.setText(contact[CONTACTNAME]);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.msgPleaseSelect)
							.setItems(num, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									CharSequence[] n = String.valueOf(num[which]).split("=");
									etPhone3.setText(n[1]);
									etPhoneAlias3.setText(contact[CONTACTNAME]);
								}
							});

					AlertDialog alert = builder.create();
					alert.show();					
				}
				break;  
			case PHONE4:  
				if (num == null || num.length == 0) {
					Message.showMessage(this, getString(R.string.msgWarning), getString(R.string.msgContactWithoutNumber));					
				} else if (num.length == 1) {
					etPhone4.setText(contact[CONTACTPHONENUMBER]);
					etPhoneAlias4.setText(contact[CONTACTNAME]);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.msgPleaseSelect)
							.setItems(num, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									CharSequence[] n = String.valueOf(num[which]).split("=");
									etPhone4.setText(n[1]);
									etPhoneAlias4.setText(contact[CONTACTNAME]);
								}
							});

					AlertDialog alert = builder.create();
					alert.show();					
				}
				break;  
			case EMAIL1:  
				if (em == null || em.length == 0) {
					Message.showMessage(this, getString(R.string.msgWarning), getString(R.string.msgContactWithoutEMail));					
				} else if (em.length == 1) {
					etEmail1.setText(contact[CONTACTEMAILADDRESS]);
					etEmailAlias1.setText(contact[CONTACTNAME]);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.msgPleaseSelect)
							.setItems(em, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									CharSequence[] e = String.valueOf(em[which]).split("=");
									etEmail1.setText(e[1]);
									etEmailAlias1.setText(contact[CONTACTNAME]);
								}
							});

					AlertDialog alert = builder.create();
					alert.show();					
				}
				break;  
			case EMAIL2:  
				if (em == null || em.length == 0) {
					Message.showMessage(this, getString(R.string.msgWarning), getString(R.string.msgContactWithoutEMail));					
				} else if (em.length == 1) {
					etEmail2.setText(contact[CONTACTEMAILADDRESS]);
					etEmailAlias2.setText(contact[CONTACTNAME]);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.msgPleaseSelect)
							.setItems(em, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									CharSequence[] e = String.valueOf(em[which]).split("=");
									etEmail2.setText(e[1]);
									etEmailAlias2.setText(contact[CONTACTNAME]);
								}
							});

					AlertDialog alert = builder.create();
					alert.show();					
				}
				break;  
			case EMAIL3:  
				if (em == null || em.length == 0) {
					Message.showMessage(this, getString(R.string.msgWarning), getString(R.string.msgContactWithoutEMail));					
				} else if (em.length == 1) {
					etEmail3.setText(contact[CONTACTEMAILADDRESS]);
					etEmailAlias3.setText(contact[CONTACTNAME]);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.msgPleaseSelect)
							.setItems(em, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									CharSequence[] e = String.valueOf(em[which]).split("=");
									etEmail3.setText(e[1]);
									etEmailAlias3.setText(contact[CONTACTNAME]);
								}
							});

					AlertDialog alert = builder.create();
					alert.show();					
				}
				break;  
			case EMAIL4:  
				if (em == null || em.length == 0) {
					Message.showMessage(this, getString(R.string.msgWarning), getString(R.string.msgContactWithoutEMail));					
				} else if (em.length == 1) {
					etEmail4.setText(contact[CONTACTEMAILADDRESS]);
					etEmailAlias4.setText(contact[CONTACTNAME]);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.msgPleaseSelect)
							.setItems(em, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									CharSequence[] e = String.valueOf(em[which]).split("=");
									etEmail4.setText(e[1]);
									etEmailAlias4.setText(contact[CONTACTNAME]);
								}
							});

					AlertDialog alert = builder.create();
					alert.show();					
				}
				break;  
			}  

		} else {  
			// gracefully handle failure  
			Logs.warningLog("Warning: activity result not ok");  
		}  
	}
}
