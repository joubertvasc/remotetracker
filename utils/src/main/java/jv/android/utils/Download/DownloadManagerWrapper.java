package jv.android.utils.Download;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by joubert on 27/02/18.
 */

public class DownloadManagerWrapper {

    private DownloadManager downloadManager;
    private long refid;

    public DownloadManagerWrapper(Context context) {
        downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public long DownloadFile(String title, String description, String remoteFileName, String localFileName) {
        Uri Download_Uri = Uri.parse(remoteFileName);

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(title);
        request.setDescription(description);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, localFileName);

        refid = downloadManager.enqueue(request);

        return refid;
    }
}
