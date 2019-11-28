package jv.android.utils;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class ImportSQLite {
	
	public static boolean execute(String backupFile, String databaseFile) {
		File dbBackupFile = null;
		
		if (backupFile.contains("/"))
			dbBackupFile = new File(backupFile);
		else
			dbBackupFile = new File(Environment.getExternalStorageDirectory() + "/" + backupFile);           
		
		if (!dbBackupFile.exists()) {              
			return false;           
		} else if (!dbBackupFile.canRead()) {              
			return false;           
		}             
		
//		File dbFile = new File(Environment.getDataDirectory() + databaseFile /*"/data/com.totsp.database/databases/sample.db" */);           
		File dbFile = new File(databaseFile);           
		
		if (dbFile.exists()) {              
			dbFile.delete();           
		}             
		
		try {              
			dbFile.createNewFile();              
			FileUtil.copyFile(dbBackupFile, dbFile);              
//			ManageData.this.application.getDataHelper().resetDbConnection();              
			return true;           
		} catch (IOException e) {              
//			Log.e(Main.LOG_TAG, e.getMessage(), e);              
			return false;           
		}  		
	}
}