package jv.android.utils;

import java.io.File;

import android.os.Environment;

public class Debugging {

	public static boolean isDebugging() {
		File file = new File (Environment.getExternalStorageDirectory() + "/jvtest.txt");
		File file2 = new File (Environment.getExternalStorageDirectory() + "/jvlog.txt");
		File file3 = new File (Environment.getExternalStorageDirectory() + "/jv-log.txt");
		File file4 = new File (Environment.getExternalStorageDirectory() + "/jvlogs.txt");

		return file.exists() || file2.exists() || file3.exists() || file4.exists();
	}
}
