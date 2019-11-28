package jv.android.remotetracker.activity;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.utils.Logs;
import android.app.Activity;
import android.content.Intent;
import java.util.Calendar;
import java.util.Objects;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Context;
import android.view.KeyEvent;
import 	android.view.WindowManager.LayoutParams;

public class LockActivity extends Activity {
	
	boolean isLock = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock);
        
        TextView tvMsg = findViewById(R.id.tvMsg);
        
	    Intent intent = getIntent();	    
	    
	    if (intent != null) {
		    Bundle params = intent.getExtras();
	    	
	    	if (params != null) {
	    		isLock = Objects.equals(params.get("islock"), "true");
	    		boolean fullScreen = Objects.equals(params.get("fullscreen"), "true");
	    		String msg = (String)params.get("msg");
	    		
	    		if (msg == null || msg.equals("")) 
	    			tvMsg.setText(getString(R.string.msgLock));
	    		else
	    			tvMsg.setText(msg);
	    		
	    		if (isLock) {
	    			Preferences p = new Preferences(getApplicationContext());
	    			p.setMsgLock(tvMsg.getText().toString());
	    		}

	    		// Remove RemoteTracker from title.
				this.setTitle(getString(R.string.msgMessage));

				// Set to full screen
    			if (fullScreen)
    				getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
	    	}
	    }
    }

    @Override	
    protected void onDestroy() {
    	if (isLock) {
    		Logs.infoLog("OnDestroy: scheduling");
    		sched (getApplicationContext());
    	}   	
    	
		super.onDestroy();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (isLock)
    		return false;
    	else
    		return super.onKeyDown(keyCode, event);
    }

    private void sched(Context contexto) {
		// Intent to send broadcast
		Intent it = new Intent("RT_LOCK");
		PendingIntent p = PendingIntent.getBroadcast(contexto, 0, it, 0);
	
		// Para executar o alarme depois de x segundos a partir de agora
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.add(Calendar.SECOND, 2);
	
		// Agenda o alarme
		AlarmManager alarme = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
		long time = c.getTimeInMillis();
		alarme.set(AlarmManager.RTC_WAKEUP, time, p);
//		alarme.setRepeating(AlarmManager.RTC_WAKEUP, time, tempoRepetir, p);		
	}
}
