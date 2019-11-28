package jv.android.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by joubertvasc on 14/06/2017.
 */

public class SimpleFileWriter {

    private static String fileName;
    private static File _root;
    private static File file;
    private static FileOutputStream _out;

    public static boolean start(Context context, String fileName) {
        _root = Environment.getExternalStorageDirectory();
        try {
            file = new File(_root, fileName);
            _out = new FileOutputStream(file, true);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean write(String text) {
        try {
            if (file != null && text != null && _out != null) {
                _out.write(text.getBytes());

                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean end() {
        try {
            if (_out != null) {
                _out.flush();
                _out.close();

                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
