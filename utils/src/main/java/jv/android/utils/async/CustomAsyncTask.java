package jv.android.utils.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.os.Build;

public class CustomAsyncTask extends AsyncTask<Void, Void, Void> {

	public void exec() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			int corePoolSize = 60;
			int maximumPoolSize = 80;
			int keepAliveTime = 10;

			BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
			Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
			executeOnExecutor(threadPoolExecutor);
		} else {
			execute();
		}	
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		return null;
	}

	@Override
	protected void onPostExecute( Void result ) {
		super.onPostExecute (result);
	}
}
