package jv.android.utils.network;

import java.io.IOException;

public class Ping {

    public static boolean isAddressOnline(String address) {
        if (address != null && !address.trim().equals("")) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 " + address);
                int exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean isOnline() {
        return isAddressOnline("8.8.8.8");
    }
}
