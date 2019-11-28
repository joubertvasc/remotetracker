package jv.android.remotetracker.commands;

import android.content.Context;
import jv.android.utils.PhoneUtils;
import jv.android.utils.CellID;

public class CommandCellID {

	public static String processCommand (Context context) {
		String result = "";
		
		PhoneUtils p = new PhoneUtils(context);

		CellID c = p.getCurrentCellID();

		if (c != null) {
			result = "CELLID: " + c.getCellID() + " LAC: " + c.getLac() + " MCC: " + c.getMcc() + " MNC: " + c.getMnc();
		}

		return result;
	}
}
