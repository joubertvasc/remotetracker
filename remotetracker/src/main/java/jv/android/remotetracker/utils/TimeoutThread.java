// Based on FindMyPhone sourcecode: http://sourceforge.net/projects/findmyphone/
package jv.android.remotetracker.utils;

import jv.android.remotetracker.commands.CommandGPListener;
import jv.android.utils.Logs;

public class TimeoutThread extends Thread implements Runnable {

	private CommandGPListener commandGPListener;
	private int gpsTimeout = 0;
	private int networkTimeout = 0;

	public TimeoutThread(CommandGPListener commandGPListener) {
		this.commandGPListener = commandGPListener;
		this.gpsTimeout = 0;
		this.networkTimeout = 0;
	}

	public void timeoutGps(int timeout) {
		Logs.infoLog("TimeoutGps");
		this.gpsTimeout = timeout;
		this.start();
	}

	public void timeoutNetwork(int timeout) {
		Logs.infoLog("TimeoutNetwork");
		this.networkTimeout = timeout;
		this.start();
	}

	@Override
	public void run() {
		Logs.infoLog("TimeoutThread run()");
		super.run();
		if(gpsTimeout > 0) {
			try {
				Logs.infoLog("GPSTimeout sleeping " + gpsTimeout);
				Thread.sleep(gpsTimeout * 1000);
				Logs.infoLog("GPSTimeout done sleeping");
			} catch (InterruptedException e) {
				Logs.errorLog("GPSTimeout caught interrupted exception");
				e.printStackTrace();
			}
			commandGPListener.abortGpsSearch();
		}
		if(networkTimeout > 0) {
			try {
				Logs.infoLog("NetworkTimeout sleeping " + networkTimeout);
				Thread.sleep(networkTimeout * 1000);
				Logs.infoLog("NetworkTimeout done sleeping ");
			} catch (InterruptedException e) {
				Logs.errorLog("NetworkTimeout caught interrupted exception");
				e.printStackTrace();
			}
			commandGPListener.abortNetworkSearch();
		}
		Logs.infoLog("TimeoutThread run() done");
	}

}
