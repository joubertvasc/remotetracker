package jv.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import android.content.SharedPreferences;

public class PreferencesUtil {

	public static boolean backup (File file, SharedPreferences sp) {
		if (file == null || sp == null)
			return false;
		else {		
			if (file.exists())
				file.delete();

			Map <String, ?> prefs = sp.getAll();
			try {
				FileOutputStream _out = new FileOutputStream(file);

				for(Map.Entry<String,?> entry : prefs.entrySet())
				{
					if (entry != null && entry.getKey() != null && entry.getValue() != null) {
						String linha = entry.getKey() + "=" + entry.getValue().toString() + "\n";
						_out.write(linha.getBytes());
					}
				}				

				_out.flush();
				_out.close();	

				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

	public static boolean backup (String fileName, SharedPreferences sp) {
		if (fileName == null || sp == null || fileName.trim().equals(""))
			return false;
		else {		
			File file = new File (fileName);

			return backup (file, sp);
		}
	}

	public static boolean restore (File file, SharedPreferences sp) {
		if (file == null || sp == null)
			return false;
		else {
			if (!file.exists()) 
				return false;
			else {
				try {
					FileInputStream in = new FileInputStream(file);
					byte[] leitura = new byte [ (int) file.length()];

					int r = in.read(leitura);

					if (r > -1) {
						String data = new String (leitura);
						String[] lines = data.split("\n");

						SharedPreferences.Editor prefEditor = sp.edit();
						for (int i = 0; i < lines.length; i++) {
							if (lines[i] != null && !lines[i].trim().equals("")) {
								String[] words = lines[i].trim().split("=");

								if (words[1] != null) {
									if (words[1].toLowerCase().equals("true")) 
										prefEditor.putBoolean(words[0],  true);
									else if (words[1].toLowerCase().equals("false"))
										prefEditor.putBoolean(words[0], false);
									else
										prefEditor.putString(words[0], words[1]);
								}
							}
						}	   	        		

						prefEditor.apply();
						in.close();

						return true;
					} else { 
						in.close();
						return false;
					}
				} catch (Exception e) {
					return false;
				}
			}
		}

	}

	public static boolean restore (String fileName, SharedPreferences sp) {
		if (fileName == null || fileName.trim().equals("") || sp == null)
			return false;
		else {
			File file = new File (fileName);

			return restore (file, sp);  
		}

	}
}
