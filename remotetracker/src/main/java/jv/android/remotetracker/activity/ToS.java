package jv.android.remotetracker.activity;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.utils.PhoneUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class ToS extends AppCompatActivity {

	Intent intent;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tos);        

		intent = getIntent();
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
			doOk();
		}

		return super.onOptionsItemSelected(item);
	}

	private void doOk() {
        Preferences preferences = new Preferences(getApplicationContext());
        SharedPreferences.Editor prefEditor = preferences.getSharedPrefs().edit();
		prefEditor.putBoolean("tos_accepted", true);

		if (preferences.getImsi1().equals("")) {
			PhoneUtils pu = new PhoneUtils(getApplicationContext());

			preferences.setImsi1(pu.getIMSI());
			preferences.setImsiAlias1(getString(R.string.msgOwnerSIMCard));
			prefEditor.putString("imsi1", preferences.getImsi1());
			prefEditor.putString("imsiAlias1", preferences.getImsiAlias1());
		}

		prefEditor.apply();
		intent.putExtra("result", 0);
		setResult(RESULT_OK, intent);

		finish();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
    	if (keyCode == KeyEvent.KEYCODE_BACK) { 
		    intent.putExtra("result", 1);
			setResult(RESULT_OK, intent);

			finish();
    	}
    	
    	return super.onKeyDown(keyCode, event);
    }
}
