package jv.android.utils.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import jv.android.utils.R;

public class WaitActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.waitactivity);
		
		TextView tvWaitTitle = (TextView)findViewById(R.id.tvWaitTitle);
		TextView tvWaitUpperText = (TextView)findViewById(R.id.tvWaitUpperText);
		TextView tvWaitBottomText = (TextView)findViewById(R.id.tvWaitBottomText);
		
	    Intent intent = getIntent();
	    
	    if (intent != null) {
		    Bundle params = intent.getExtras();
			
			if (params != null) {
				String caption = params.getString ("waitcaption");
				String title = params.getString("waittitle");
				String upper = params.getString("waituppertext");
				String bottom = params.getString("waitbottomtext");
				
				tvWaitTitle.setText(title == null || title.trim().equals("") ? "Please Wait..." : title);
				tvWaitUpperText.setText(upper == null ? "" : upper);
				tvWaitBottomText.setText(bottom == null ? "" : bottom);
				
				this.setTitle(caption == null || caption.trim().equals("") ? "Wait" : caption);
			}			
	    }
	}


}
