package jv.android.remotetracker.commands;

import jv.android.remotetracker.R;
import android.content.Context;
import jv.android.remotetracker.utils.Commands;

public class CommandHelp {

	public static String processCommand(Context context, String cmd, boolean complete, Commands[] commands) {
		String result = context.getString(R.string.hlpUsage1) + " " +    
				        context.getString(R.string.app_name) +
                        context.getString(R.string.hlpUsage2) + "\n\n";
		
		if (cmd == null || cmd.trim().equals("")) {
			result += context.getString(R.string.hlpCommandList) + "\n";
			
			for (int i = 0; i < commands.length; i++) {
				if (!commands[i].isHidden()) {
					if (complete) {
						result += commands[i].getCommand().toUpperCase() + ": " +
								commands[i].getDescription() + " " + context.getString(R.string.msgExample) + ": " + 
								commands[i].getExample() + "\n";
					            
					}
					else {
						result += commands[i].getCommand() + "\n"; 
					}
				}
			}
		} else {
			for (int i = 0; i < commands.length; i++) {
				if (!commands[i].isHidden() && commands[i].getCommand().toLowerCase().trim().equals(cmd.toLowerCase().trim())) {
					result += commands[i].getCommand().toUpperCase() + ": " +
							commands[i].getDescription() + " " + context.getString(R.string.msgExample) + ": " + 
							commands[i].getExample() + "\n";
				}
			}
		}
		
		return result;
	}
}
