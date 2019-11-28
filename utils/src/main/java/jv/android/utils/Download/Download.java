package jv.android.utils.Download;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import android.os.Environment;

import jv.android.utils.Logs;

public class Download {

	public static String download(String address, String fileName) {
		Logs.infoLog("Download.download address=" + address + ", fileName=" + fileName);

		if (address == null || address.trim().equals("") || fileName == null || fileName.trim().equals("")) {
			Logs.warningLog("Download.download dados invalidos.");
			return null;
		} else {		
			try {
				URL url = new URL(address.trim());
				URLConnection connection = url.openConnection();
				connection.setReadTimeout(300000);
				connection.connect();
				// this will be useful so that you can show a typical 0-100% progress bar

				// download the file
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(
						fileName.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath()) ? 
								fileName.trim() : 
									Environment.getExternalStorageDirectory() + "//" + fileName.trim());

				byte data[] = new byte[1024];
				int count;

				while ((count = input.read(data)) != -1) {
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();

				return fileName.trim();
			} catch (Exception e) {
				Logs.errorLog("Download.download error", e);

				return null;
			}
		}
	}
}
