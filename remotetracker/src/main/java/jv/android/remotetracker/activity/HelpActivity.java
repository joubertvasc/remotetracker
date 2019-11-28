package jv.android.remotetracker.activity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import jv.android.remotetracker.R;
import jv.android.remotetracker.adapter.HelpAdapter;
import jv.android.remotetracker.commands.CommandList;
import jv.android.remotetracker.utils.Commands;

public class HelpActivity extends ListActivity {

	@SuppressLint("SetTextI18n")
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		List<Commands> ct = new ArrayList<>();

		for (Commands command : CommandList.getCommandList(HelpActivity.this))
			if (!command.isHidden())
				ct.add(command);

		ListView list = getListView();

		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.helpheader, list, false);
		list.addHeaderView(header, null, false);		

		HelpAdapter helpAdapter = new HelpAdapter(HelpActivity.this, R.layout.rowhelp, ct);
		list.setAdapter(helpAdapter);

		TextView tvExplanation = findViewById(R.id.tvExplanation);

		tvExplanation.setText(getString(R.string.hlpUsage1) + " " +    
				getString(R.string.app_name) +
				getString(R.string.hlpUsage2));

	}
}
