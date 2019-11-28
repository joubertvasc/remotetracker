package jv.android.utils.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import jv.android.utils.Logs;

/**
 * Created by joubert on 11/02/18.
 */

public class LogDataHelper {

    private static final String DATABASE_NAME = "logs.db";
    private static final int DATABASE_VERSION = 1;

    public static final String LOGS = "logs";

    private Context context;
    private SQLiteDatabase db;

    public LogDataHelper(Context context) {
        this.context = context;
    }

    public boolean open() {
        try {
            OpenHelper openHelper = new OpenHelper(this.context);

            db = openHelper.getWritableDatabase();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public SQLiteDatabase getDB() {
        return db;
    }

    public void close() {
        db.close();
    }

    public String getDBFile() {
        return context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    private static void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LOGS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, sent text, logkey TEXT, extrakey text, date text, type TEXT, text TEXT, exception TEXT)");
    }

    public int insertLog(Log log) {
        int result = -1;

        if (getDB() != null) {
            if (!getDB().isOpen()) {
                open();
            }

            ContentValues values = new ContentValues();
            values.put("date", log.getDate());
            values.put("sent", log.isSent() ? "Y" : "N");
            values.put("logkey", log.getKey());
            values.put("extrakey", log.getExtrakey());
            values.put("type", log.getType());
            values.put("text", log.getText());
            values.put("exception", log.getException());

            try {
                Long l = getDB().insert(LOGS, null, values);

                result = l.intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

            close();
        }

        return result;
    }

    private boolean setAllLogsToSent() {
        boolean result = false;

        if (getDB() != null) {
            if (!getDB().isOpen())
                open();

            ContentValues values = new ContentValues();
            values.put("sent", "Y");

            try {
                getDB().update(LOGS, values, null, null);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            close();
        }

        return result;
    }

    public boolean clearLogs() {
        boolean result = false;

        if (getDB() != null) {
            if (!getDB().isOpen())
                open();

            try {
                getDB().delete(LOGS, null, null);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            close();
        }

        return result;
    }

    public List<Log> getAllLogs(boolean onlyNotSent) {
        List<Log> result = new ArrayList<Log>();

        if (getDB() != null) {
            if (!getDB().isOpen())
                open();

            try {
                String sql = "select id, sent, logkey, extrakey, date, type, text, exception" +
                        "  from " + LOGS + (onlyNotSent ? " where sent = 'N'" : "") +
                        " order by id";

                Cursor cursor = getDB().rawQuery(sql, null);

                if (cursor.moveToFirst()) {
                    Logs.infoLog("LogDataHelper.getAllHistorico found.");
                    do {
                        Log log = new Log();
                        log.setId(cursor.getInt(0));
                        log.setSent(cursor.getString(1).equalsIgnoreCase("Y"));
                        log.setKey(cursor.getString(2));
                        log.setExtrakey(cursor.getString(3));
                        log.setDate(cursor.getString(4));
                        log.setType(cursor.getString(5));
                        log.setText(cursor.getString(6));
                        log.setException(cursor.getString(7));

                        result.add(log);
                    } while (cursor.moveToNext());
                }

                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            close();
        }

        return result;
    }

    private static class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

}
