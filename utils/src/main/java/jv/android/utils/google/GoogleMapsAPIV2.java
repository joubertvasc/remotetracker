package jv.android.utils.google;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;

import jv.android.utils.FileUtil;
import jv.android.utils.Message;
import jv.android.utils.R;
import jv.android.utils.network.Network;

public class GoogleMapsAPIV2 extends AppCompatActivity implements OnMapLongClickListener {

	private GoogleMap map;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.googlemapsapi2);

		((SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.map))).getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap googleMap) {
				map = googleMap;
				map.setOnMapLongClickListener(GoogleMapsAPIV2.this);
			}
		});

		intent = getIntent();

		if (intent != null)	{
			Bundle params = intent.getExtras();

			if (params != null) {
				readParams(intent, params);
			}
		}

		Fragment f = (Fragment)getSupportFragmentManager().findFragmentById(R.id.map);
		f.setHasOptionsMenu(true);

		if (!Network.isNetworkAvailable(GoogleMapsAPIV2.this)) {
			Toast.makeText(this, getString(R.string.avOfflineUsingCache), Toast.LENGTH_LONG).show();
		}
	}

	public void readParams(Intent intent, Bundle params) {
		// To be overrided
	}
	
	public void longClickPressed(LatLng arg0) {
		// To be overrided
	}    

	@Override
	public boolean onCreateOptionsMenu(Menu  menu) {     
		getMenuInflater().inflate(R.menu.googlemapsapiv2, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void mapType(int i) {
		if (i == 0) 
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		else if (i == 1) 
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		else if (i == 2)
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		else if (i == 3)
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	}

	public void screenCapture() {
		SnapshotReadyCallback callback = new SnapshotReadyCallback() {
			Bitmap bitmap;

			@Override
			public void onSnapshotReady(final Bitmap snapshot) {
				AlertDialog.Builder alert = new AlertDialog.Builder(GoogleMapsAPIV2.this);            
				alert.setIcon(R.drawable.balao);
				alert.setTitle(getString(R.string.mnScreenCapture));            
				alert.setMessage(getString(R.string.avTypeFileName));            
				final TextView tx = new TextView(GoogleMapsAPIV2.this);            
				tx.setText("");            
				alert.setView(tx);            

				// Set an EditText view to get user input                 
				final EditText input = new EditText(GoogleMapsAPIV2.this);

				input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);		
				alert.setView(input);            

				alert.setPositiveButton(getString(R.string.btOk), new DialogInterface.OnClickListener() {                
					public void onClick(DialogInterface dialog, int whichButton) {
						if (input.getText().toString().trim().equals("")) {
							Message.showMessage(GoogleMapsAPIV2.this, getString(R.string.avWarning), getString(R.string.avInvalidEmptyName));
						}
						else
						{
							bitmap = snapshot;
							try {
								String arquivo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FileUtil.replaceInvalidChars(input.getText().toString().trim()) + ".png";
								File file = new File(arquivo);

								if (file.exists()) {
									file.delete();
								}

								FileOutputStream out = new FileOutputStream(arquivo);
								bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
								Toast.makeText(GoogleMapsAPIV2.this, getString(R.string.avPictureTaken) + ": " + arquivo, Toast.LENGTH_LONG).show();
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(GoogleMapsAPIV2.this, getString(R.string.avErrorTakingPicture), Toast.LENGTH_LONG).show();
							}
						}
					}            
				});            

				alert.setNegativeButton(getString(R.string.btCancel), new DialogInterface.OnClickListener() {                
					public void onClick(DialogInterface dialog, int whichButton) {                    
						dialog.dismiss();                
					}            
				});            

				alert.create();            
				alert.show();
			}
		};

		map.snapshot(callback);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mnMap) {
			mapType(0);
		} else if (item.getItemId() == R.id.mnSatelite) {
			mapType(1);
		} else if (item.getItemId() == R.id.mnHybrid) {
			mapType(2);
		} else if (item.getItemId() == R.id.mnTerrain) {
			mapType(3);
		} else if (item.getItemId() == R.id.mnMyLocation) {
			map.setMyLocationEnabled(!map.isMyLocationEnabled());
		} else if (item.getItemId() == R.id.mnScreenCapture) {
			screenCapture();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapLongClick(final LatLng arg0) {
		longClickPressed (arg0);
	}    
}
