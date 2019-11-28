package jv.android.remotetracker.activity;

import jv.android.remotetracker.R;
import jv.android.utils.Message;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Objects;

public class Password extends AppCompatActivity {

	private Intent intent;
    private String password;
    private EditText etPassword;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);        

		intent = getIntent();
	    Bundle params = intent.getExtras();
	    password = Objects.requireNonNull(params).getString("password");
		etPassword = findViewById(R.id.etPassword);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ok, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_ok) {
            if (!etPassword.getText().toString().toLowerCase().trim().equals(password.toLowerCase().trim())) {
                Message.showMessage(Password.this, getString(R.string.msgError), getString(R.string.msgInvalidPassword), R.drawable.exclamation);
            }
            else {
                intent.putExtra("result", 0);
                setResult(RESULT_OK, intent);

                finish();
            }
		}

        return super.onOptionsItemSelected(item);
	}
}
