package jv.android.remotetracker.adapter;

import android.widget.ArrayAdapter;
import java.util.List;

import jv.android.remotetracker.R;
import jv.android.remotetracker.commands.CommandTable;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

public class HistoryAdapter extends ArrayAdapter<CommandTable> {

    private List<CommandTable> items;
    Context context;

    public HistoryAdapter(Context context, int textViewResourceId, List<CommandTable> items) {
             super(context, textViewResourceId, items);
             this.items = items;
             this.context = context;
     }

    @Override
     public View getView(int position, View convertView, ViewGroup parent) {
             View v = convertView;
             if (v == null) {
                 LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 v = vi.inflate(R.layout.rowhistory, null);
             }
             CommandTable o = items.get(position);
             if (o != null) {
                 TextView l1 = (TextView) v.findViewById(R.id.line1);
                 TextView l2 = (TextView) v.findViewById(R.id.line2);
                 TextView l3 = (TextView) v.findViewById(R.id.line3);
                 TextView l4 = (TextView) v.findViewById(R.id.line4);
                 
                 if (l1 != null)
                	 l1.setText(o.getCommand() + " - " + 
                      		    context.getString(R.string.msgFrom) + ": " + o.getFrom());
                     
                 if (l3 != null)
                	 l3.setText(context.getString(R.string.msgDate) + ": " + o.getData());

                 if (l2 != null)
                	 l2.setText(context.getString(R.string.msgTo) + ": " + o.getTo());
                     
                 if(l4 != null)
                	 l4.setText(context.getString(R.string.msgMessage) + ": " + o.getResult());
             }
             return v;
     }
}
