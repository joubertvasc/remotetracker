package jv.android.remotetracker.activity;

import jv.android.remotetracker.R;
import jv.android.remotetracker.utils.Links;
import jv.android.utils.Email.Email;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        TextView tvVersionNumber = findViewById(R.id.tvVersionNumber);

        try {
            if (tvVersionNumber != null) {
            	tvVersionNumber.setTextColor(Color.GREEN);
            	tvVersionNumber.setText(this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
            }
        } catch (Exception e) {
        	tvVersionNumber.setText("");
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_contact) {
			doContato();
		} else if (id == R.id.action_help) {
			Intent help = new Intent(AboutActivity.this, HelpActivity.class);
			startActivity(help);
		}

		return super.onOptionsItemSelected(item);
	}

	private void doContato() {
    	Email.sendSimpleMessage(AboutActivity.this, getString(R.string.msgSending), Links.getEmail(), getString(R.string.msgSubject), null);
    }
}