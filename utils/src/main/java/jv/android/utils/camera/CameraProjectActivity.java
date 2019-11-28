package jv.android.utils.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import jv.android.utils.R;

public class CameraProjectActivity extends Activity {
	public static ImageView image; 
	private Button btn_camera;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameraprojectactivity);

		image = (ImageView) findViewById(R.id.image);

		btn_camera = (Button) findViewById(R.id.btn_camera);
		btn_camera.setOnClickListener(new View.OnClickListener() 
		{ 

			@Override
			public void onClick(View v)
			{ 
				Intent i = new Intent(CameraProjectActivity.this,CameraView.class);
				startActivityForResult(i, 999);
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{
		if(requestCode==999)
		{
			if(resultCode==585)
			{
				//Intent Works
			}
			else
			{
				alert("Picture not Captured!");
			}
		}
	}

	private void alert(String string) 
	{ 
		AlertDialog.Builder alert=new AlertDialog.Builder(CameraProjectActivity.this);
		alert.setMessage(string);
		alert.setTitle("Alert");
		alert.setNeutralButton("Ok",null);
		alert.show();
	}
}
