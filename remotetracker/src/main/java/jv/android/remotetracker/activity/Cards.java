package jv.android.remotetracker.activity;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.utils.Message;
import jv.android.utils.PhoneUtils;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class Cards extends AppCompatActivity {

	private Preferences preferences;
	private EditText etCard1; 
	private EditText etCard2; 
	private EditText etCard3; 
	private EditText etCard4; 
	private EditText etAlias1; 
	private EditText etAlias2; 
	private EditText etAlias3; 
	private EditText etAlias4; 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cards);        

        ImageButton ibCard1 = findViewById(R.id.ibCard1);
        ImageButton ibCard2 = findViewById(R.id.ibCard2);
        ImageButton ibCard3 = findViewById(R.id.ibCard3);
        ImageButton ibCard4 = findViewById(R.id.ibCard4);
        ImageButton ibClear1 = findViewById(R.id.ibClear1);
        ImageButton ibClear2 = findViewById(R.id.ibClear2);
        ImageButton ibClear3 = findViewById(R.id.ibClear3);
        ImageButton ibClear4 = findViewById(R.id.ibClear4);
		etCard1 = findViewById(R.id.etCard1);
		etCard2 = findViewById(R.id.etCard2);
		etCard3 = findViewById(R.id.etCard3);
		etCard4 = findViewById(R.id.etCard4);
		etAlias1 = findViewById(R.id.etAlias1);
		etAlias2 = findViewById(R.id.etAlias2);
		etAlias3 = findViewById(R.id.etAlias3);
		etAlias4 = findViewById(R.id.etAlias4);
		
		etCard1.setEnabled(false);
		etCard2.setEnabled(false);
		etCard3.setEnabled(false);
		etCard4.setEnabled(false);
		
		preferences = new Preferences (getApplicationContext());

		etCard1.setText(preferences.getImsi1());
		etCard2.setText(preferences.getImsi2());
		etCard3.setText(preferences.getImsi3());
		etCard4.setText(preferences.getImsi4());
		etAlias1.setText(preferences.getImsiAlias1());
		etAlias2.setText(preferences.getImsiAlias2());
		etAlias3.setText(preferences.getImsiAlias3());
		etAlias4.setText(preferences.getImsiAlias4());
		
		final PhoneUtils pu = new PhoneUtils(getApplicationContext());
		
        if (ibCard1 != null)
        	ibCard1.setOnClickListener(new ImageButton.OnClickListener() {
        		public void onClick(View v) {
        			etCard1.setText(pu.getIMSI());
        		}
        	});

        if (ibCard2 != null)
        	ibCard2.setOnClickListener(new ImageButton.OnClickListener() {
        		public void onClick(View v) {
        			etCard2.setText(pu.getIMSI());
        		}
        	});

        if (ibCard3 != null)
        	ibCard3.setOnClickListener(new ImageButton.OnClickListener() {
        		public void onClick(View v) {
        			etCard3.setText(pu.getIMSI());
        		}
        	});

        if (ibCard4 != null)
        	ibCard4.setOnClickListener(new ImageButton.OnClickListener() {
        		public void onClick(View v) {
        			etCard4.setText(pu.getIMSI());
        		}
        	});

        if (ibClear1 != null)
        	ibClear1.setOnClickListener(new ImageButton.OnClickListener() {
        		public void onClick(View v) {
        			etCard1.setText("");
        			etAlias1.setText("");
        		}
        	});

        if (ibClear2 != null)
        	ibClear2.setOnClickListener(new ImageButton.OnClickListener() {
        		public void onClick(View v) {
        			etCard2.setText("");
        			etAlias2.setText("");
        		}
        	});

        if (ibClear3 != null)
        	ibClear3.setOnClickListener(new ImageButton.OnClickListener() {
        		public void onClick(View v) {
        			etCard3.setText("");
        			etAlias3.setText("");
        		}
        	});

        if (ibClear4 != null)
        	ibClear4.setOnClickListener(new ImageButton.OnClickListener() {
        		public void onClick(View v) {
        			etCard4.setText("");
        			etAlias4.setText("");
        		}
        	});
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
			if (etCard1.getText().toString().trim().equals("") &&
					etCard2.getText().toString().trim().equals("") &&
					etCard3.getText().toString().trim().equals("") &&
					etCard4.getText().toString().trim().equals("")) {
				Message.showMessage(Cards.this, getString(R.string.msgError), getString(R.string.msgNeedOneSIMCard));
			} else {
				preferences.setImsi1(etCard1.getText().toString().trim());
				preferences.setImsi2(etCard2.getText().toString().trim());
				preferences.setImsi3(etCard3.getText().toString().trim());
				preferences.setImsi4(etCard4.getText().toString().trim());
				preferences.setImsiAlias1(etAlias1.getText().toString().trim());
				preferences.setImsiAlias2(etAlias2.getText().toString().trim());
				preferences.setImsiAlias3(etAlias3.getText().toString().trim());
				preferences.setImsiAlias4(etAlias4.getText().toString().trim());

				SharedPreferences.Editor prefEditor = preferences.getSharedPrefs().edit();
				prefEditor.putString("imsi1", preferences.getImsi1());
				prefEditor.putString("imsi2", preferences.getImsi2());
				prefEditor.putString("imsi3", preferences.getImsi3());
				prefEditor.putString("imsi4", preferences.getImsi4());
				prefEditor.putString("imsiAlias1", preferences.getImsiAlias1());
				prefEditor.putString("imsiAlias2", preferences.getImsiAlias2());
				prefEditor.putString("imsiAlias3", preferences.getImsiAlias3());
				prefEditor.putString("imsiAlias4", preferences.getImsiAlias4());
				prefEditor.apply();

				preferences.exportPreferences();

				finish();
			}
		}

		return super.onOptionsItemSelected(item);
	}
}
