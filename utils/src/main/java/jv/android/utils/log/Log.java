package jv.android.utils.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jv.android.utils.DateTime;

/**
 * Created by joubert on 11/02/18.
 */

public class Log {

    public static final String INFO_LOG = "I";
    public static final String WARNING_LOG = "W";
    public static final String ERROR_LOG = "E";

    private int id;
    private boolean sent;
    private String key;
    private String extrakey;
    private String type;
    private String date;
    private String text;
    private String exception;

    public Log() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        id = -1;
        sent = false;
        key = "";
        extrakey = "";
        type = INFO_LOG;
        date = dateFormat.format(Calendar.getInstance().getTime());
        text = "";
        exception = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExtrakey() {
        return extrakey;
    }

    public void setExtrakey(String extrakey) {
        this.extrakey = extrakey;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
