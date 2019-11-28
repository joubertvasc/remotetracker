package jv.android.utils.http;

import java.net.HttpURLConnection;
import java.net.URLConnection;

import jv.android.utils.ThreadUtils;
import jv.android.utils.async.CustomAsyncTask;

/**
 * Created by joubert on 08/11/2016.
 */

public class InterruptThread implements Runnable {

    private URLConnection con;
    private int timeout;
    private boolean dontStop;

    public InterruptThread(URLConnection con, int timeout) {
        this.con = con;
        this.timeout = timeout;
        this.dontStop = false;
    }

    public void setOk(boolean dontStop) {
        this.dontStop = dontStop;
    }

    public void run() {
        InterruptClassAsync c = new InterruptClassAsync();
        c.exec();
    }

    private class InterruptClassAsync extends CustomAsyncTask {

        @Override
        protected Void doInBackground(Void... arg0) {
            ThreadUtils.waitms(timeout);

            if (!dontStop) {
                ((HttpURLConnection) con).disconnect();
            }

            return null;
        }
    }
}
