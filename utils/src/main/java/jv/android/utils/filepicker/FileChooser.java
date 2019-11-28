package jv.android.utils.filepicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jv.android.utils.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.content.Intent;

public class FileChooser extends ListActivity {

	private File currentDir;
	private FileArrayAdapter adapter;
	private Intent intent;
	private String[] ext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ext = null;
		intent = getIntent();
		if (intent != null)
		{
			Bundle params = intent.getExtras();

			if (params != null) {
				ext = params.getStringArray("ext");
			}
		}

		currentDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		fill(currentDir);
	}

	private boolean canAddFile(String name) {
		if (ext == null || ext.length == 0) {
			return true;
		} else {
			int pos = name.lastIndexOf(".");

			if (pos > 0) {		
				String extension = name.substring(pos+1);

				for (int i = 0; i < ext.length; i++) {
					if (extension.equalsIgnoreCase(ext[i])) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private void fill(File f) {
		File[]dirs = f.listFiles();
		this.setTitle(getString(R.string.currentDir) + f.getName());
		List<Option>dir = new ArrayList<Option>();
		List<Option>fls = new ArrayList<Option>();

		try {
			for (File ff: dirs) {
				if (ff.isDirectory())
					dir.add(new Option(ff.getName(), getString(R.string.folder), ff.getAbsolutePath(), true));
				else if (canAddFile(ff.getName()))
					fls.add(new Option(ff.getName(), getString(R.string.fileSize) + " " + ff.length(), ff.getAbsolutePath(), false));
			}
		} catch(Exception e) {

		}

		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);

		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new Option("..", getString(R.string.parentFolder), f.getParent(), true));

		adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view, dir);

		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Option o = adapter.getItem(position);

		if (o.getData().equalsIgnoreCase(getString(R.string.folder)) || o.getData().equalsIgnoreCase(getString(R.string.parentFolder))) {
			currentDir = new File(o.getPath());
			fill(currentDir);
		} else {
			intent.putExtra("name", o.getName());
			intent.putExtra("path", o.getPath());
			setResult(RESULT_OK, intent);
			finish();
		}
	}

}