package jv.android.utils.textfile;

import android.os.Environment;
import java.io.FileOutputStream;

/**
 * Created by joubert on 26/03/2017.
 */

public class SimpleTextFile {

    public static boolean writeToFile(String fileName, String textToWrite) {
        boolean result = true;

        FileOutputStream _out;
        try	{
            if (!fileName.contains("/")) {
                fileName = Environment.getExternalStorageDirectory() + "/" + fileName;
            }

            _out = new FileOutputStream(fileName);
            try	{
                _out.write (textToWrite.getBytes());
            } finally {
                _out.flush();
                _out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }

        return result;
    }
}
