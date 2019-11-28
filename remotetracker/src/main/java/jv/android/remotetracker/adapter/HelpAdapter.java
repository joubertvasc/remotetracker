package jv.android.remotetracker.adapter;

import android.widget.ArrayAdapter;
import java.util.List;
import jv.android.remotetracker.utils.Commands;
import jv.android.remotetracker.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

public class HelpAdapter extends ArrayAdapter<Commands> {

    private List<Commands> items;
    Context context;

    public HelpAdapter(Context context, int textViewResourceId, List<Commands> items) {
             super(context, textViewResourceId, items);
             this.items = items;
             this.context = context;
     }

    @Override
     public View getView(int position, View convertView, ViewGroup parent) {
             View v = convertView;
             
             if (v == null) {
                 LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 v = vi.inflate(R.layout.rowhelp, null);
             }
             
             Commands o = items.get(position);
             if (o != null) {
                 TextView l1 = (TextView) v.findViewById(R.id.line1);
                 TextView l2 = (TextView) v.findViewById(R.id.line2);
                 TextView l3 = (TextView) v.findViewById(R.id.line3);
                 
                 if (l1 != null)
                	 l1.setText(o.getCommand().toUpperCase() + " - " + context.getString(R.string.msgReturnType).toString() + ": " + (o.isEmail() ? "E-mail" : (o.isFTP() ? "FTP" : "SMS")));
                     
                 if (l2 != null)
                	 l2.setText(o.getDescription());

                 if (l3 != null)
                	 l3.setText(context.getString(R.string.msgExample).toString() + ": " + o.getExample());
             }

             return v;
     }
}
