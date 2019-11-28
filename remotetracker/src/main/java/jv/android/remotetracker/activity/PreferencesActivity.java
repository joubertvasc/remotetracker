package jv.android.remotetracker.activity;

import jv.android.remotetracker.utils.BuyProVersion;
import jv.android.remotetracker.receiver.ListeningAdminReceiver;
import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.KeyEvent;

public class PreferencesActivity extends PreferenceActivity {

	private static final int RESULT_DEVICE_ADMIN = 1;
	private SharedPreferences.Editor e;
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdmin;

    private Intent intent;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

        intent = getIntent();

		mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdmin = new ComponentName(PreferencesActivity.this.getApplicationContext(), ListeningAdminReceiver.class);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		e =  sp.edit();
		e.putBoolean("device_admin", mDPM.isAdminActive(mDeviceAdmin));
		e.apply();

		final Preference device = findPreference("device_admin");
		final Preferences p = new Preferences(PreferencesActivity.this.getApplicationContext());

		if (!p.isProVersion()) {
			((CheckBoxPreference)device).setChecked(false);							
		} else {
			((CheckBoxPreference)device).setChecked(mDPM.isAdminActive(mDeviceAdmin));							
		}

		Preference deviceAdmin = findPreference("device_admin");
		deviceAdmin.setOnPreferenceClickListener(new OnPreferenceClickListener() { //this line forcecloses every time i run the activity.
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if (!p.isProVersion()) {
					e.putBoolean("device_admin", false);
					e.apply();
					((CheckBoxPreference)device).setChecked(false);							

					BuyProVersion.askBuyProVersion(PreferencesActivity.this);
				} else {
					if (mDPM.isAdminActive(mDeviceAdmin)) {
						AlertDialog.Builder alert = new AlertDialog.Builder(PreferencesActivity.this);            
						alert.setIcon(R.drawable.exclamation);
						alert.setTitle(getString(R.string.msgWarning));            
						alert.setMessage(getString(R.string.msgSureTurnDevAdminOff));            
						alert.setNegativeButton(getString(R.string.btNo), new DialogInterface.OnClickListener() {                
							public void onClick(DialogInterface dialog, int whichButton) {                    
								e.putBoolean("device_admin", true);
								e.apply();
								((CheckBoxPreference)device).setChecked(true);							

								dialog.dismiss();                
							}            
						});            

						alert.setPositiveButton(getString(R.string.btYes), new DialogInterface.OnClickListener() {                
							public void onClick(DialogInterface dialog, int whichButton) {
								mDPM.removeActiveAdmin(mDeviceAdmin);
								e.putBoolean("device_admin", false);
								e.apply();
								((CheckBoxPreference)device).setChecked(false);							

								dialog.dismiss();                
							}            
						});            

						alert.create();            
						alert.show(); 	
					} else {
						Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
						intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
						intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.msgDeviceAdmin));
						startActivityForResult(intent, RESULT_DEVICE_ADMIN);
					}
				}

				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == RESULT_OK) {    		
			if (requestCode == RESULT_DEVICE_ADMIN) {
				e.putBoolean("device_admin", true);
				e.apply();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Preferences preferences = new Preferences(this);
			preferences.exportPreferences();

            setResult(RESULT_OK, intent);
		}

		return super.onKeyDown(keyCode, event);
	}

}
