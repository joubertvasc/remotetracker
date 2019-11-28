package jv.android.remotetracker.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import jv.android.remotetracker.adapter.HistoryAdapter;
import jv.android.remotetracker.R;
import jv.android.remotetracker.utils.RemoteTrackerDataHelper;
import jv.android.remotetracker.commands.CommandTable;

public class ListHistory extends ListActivity {

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		RemoteTrackerDataHelper db = new RemoteTrackerDataHelper(ListHistory.this);
    	List<CommandTable> ct = db.selectAllCommands();
		
		ListView list = getListView();
		
    	if (ct != null && ct.size() > 0) {
        	LayoutInflater inflater = getLayoutInflater();
    		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.historyheader, list, false);
    		list.addHeaderView(header, null, false);		

    		HistoryAdapter historyAdapter = new HistoryAdapter(ListHistory.this, R.layout.rowhistory, ct);
        	list.setAdapter(historyAdapter);
    	} else {
    		AlertDialog.Builder alert = new AlertDialog.Builder(ListHistory.this);            

   			alert.setIcon(R.drawable.exclamation);
    		
    		alert.setTitle(getString(R.string.msgHistoryList));            
    		alert.setMessage(getString(R.string.msgHistoryListEmpty));            
    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {                
    			public void onClick(DialogInterface dialog, int whichButton) {
    				finish();
    			}            
    		});            

    		alert.create();            
    		alert.show();
    	}
	}
}
