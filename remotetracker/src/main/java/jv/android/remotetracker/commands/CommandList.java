package jv.android.remotetracker.commands;

import jv.android.remotetracker.R;
import android.content.Context;
import jv.android.remotetracker.utils.Commands;

public class CommandList {

	public static final int total = 41;

	public static jv.android.remotetracker.utils.Commands[] getCommandList(Context context) {
		jv.android.remotetracker.utils.Commands[] result = new Commands[total];

		if (context != null) {
			result[0]  = CommandList.setCommand(1, true, "help", context.getString(R.string.hlpHELP), context.getString(R.string.sdHELP), "rt#help##1234; rt#help,gp##1234", false, false, false, true, true, false);
			result[1]  = CommandList.setCommand(2, true, "ehelp", context.getString(R.string.hlpEHELP), context.getString(R.string.sdEHELP), "rt#ehelp##1234; rt#ehelp,gp##1234", false, true, false, true, true, false);
			result[2]  = CommandList.setCommand(3, true, "fhelp", context.getString(R.string.hlpFHELP), context.getString(R.string.sdFHELP), "rt#fhelp##1234; rt#fhelp,gp##1234", false, false, true, true, true, false);
			result[3]  = CommandList.setCommand(4, false, "secret", context.getString(R.string.hlpSecret), context.getString(R.string.sdSecret), "rt#secret", false, false, false, false, true, false);
			result[4]  = CommandList.setCommand(5, false, "lostpass", context.getString(R.string.hlpLostpass), context.getString(R.string.sdLostpass), "rt#lostpass," + context.getString(R.string.configAnswer), true, false, false, false, true, false);
			result[5]  = CommandList.setCommand(6, false, "cb", context.getString(R.string.hlpCB), context.getString(R.string.sdCB), "rt#cb##1234", false, false, false, true, true, false);
			result[6]  = CommandList.setCommand(7, false, "gp", context.getString(R.string.hlpGP), context.getString(R.string.sdGP), "rt#gp##1234", false, false, false, true, true, false);
			result[7]  = CommandList.setCommand(8, true, "egp", context.getString(R.string.hlpEGP), context.getString(R.string.sdEGP), "rt#egp##1234", false, true, false, true, true, false);
			result[8]  = CommandList.setCommand(9, true, "fgp", context.getString(R.string.hlpFGP), context.getString(R.string.sdFGP), "rt#fgp##1234", false, false, true, true, true, false);
			result[9] = CommandList.setCommand(10, false, "gi", context.getString(R.string.hlpGI), context.getString(R.string.sdGI), "rt#gi##1234", false, false, false, true, true, false);
			result[10] = CommandList.setCommand(11, true, "egi", context.getString(R.string.hlpEGI), context.getString(R.string.sdEGI), "rt#egi##1234", false, true, false, true, true, false);
			result[11] = CommandList.setCommand(12, true, "fgi", context.getString(R.string.hlpFGI), context.getString(R.string.sdFGI), "rt#fgi##1234", false, false, true, true, true, false);
			result[12] = CommandList.setCommand(13, true, "cellid", context.getString(R.string.hlpCellID), context.getString(R.string.sdCellID), "rt#cellid##1234", false, false, false, true, true, false);
			result[13] = CommandList.setCommand(14, true, "ecellid", context.getString(R.string.hlpECellID), context.getString(R.string.sdECellID), "rt#ecellid##1234", false, true, false, true, true, false);
			result[14] = CommandList.setCommand(15, true, "fcellid", context.getString(R.string.hlpFCellID), context.getString(R.string.sdFCellID), "rt#fcellid##1234", false, false, true, true, true, false);
			result[15] = CommandList.setCommand(16, true, "pb", context.getString(R.string.hlpPB), context.getString(R.string.sdPB), "rt#pb##1234", false, false, false, true, true, false);
			result[16] = CommandList.setCommand(17, true, "epb", context.getString(R.string.hlpEPB), context.getString(R.string.sdEPB), "rt#epb##1234", false, true, false, true, true, false);
			result[17] = CommandList.setCommand(18, true, "fpb", context.getString(R.string.hlpFPB), context.getString(R.string.sdFPB), "rt#fpb##1234", false, false, true, true, true, false);
			result[18] = CommandList.setCommand(19, true, "listapp", context.getString(R.string.hlpListApp), context.getString(R.string.sdListApp), "rt#listapp##1234", false, false, false, true, true, false);
			result[19] = CommandList.setCommand(20, true, "elistapp", context.getString(R.string.hlpeListApp), context.getString(R.string.sdeListApp), "rt#elistapp##1234", false, true, false, true, true, false);
			result[20] = CommandList.setCommand(21, true, "flistapp", context.getString(R.string.hlpfListApp), context.getString(R.string.sdfListApp), "rt#flistapp##1234", false, false, true, true, true, false);
			result[21] = CommandList.setCommand(22, true, "runapp", context.getString(R.string.hlpRunApp), context.getString(R.string.sdRunApp), "rt#runapp,calc##1234", true, false, false, true, true, false);
			result[22] = CommandList.setCommand(23, true, "lock", context.getString(R.string.hlpLock), context.getString(R.string.sdLock), "rt#lock," + context.getString(R.string.msgLock)+ "##1234", true, false, false, true, true, true);
			result[23] = CommandList.setCommand(24, true, "unlock", context.getString(R.string.hlpUnlock), context.getString(R.string.sdUnlock), "rt#unlock##1234", false, false, false, true, true, true);
			result[24] = CommandList.setCommand(25, true, "msg", context.getString(R.string.hlpMsg), context.getString(R.string.sdMsg), "rt#msg,your message##1234", true, false, false, true, true, false);
			result[25] = CommandList.setCommand(26, true, "sms", context.getString(R.string.hlpSms), context.getString(R.string.sdSms), "rt#sms,your message#5551234#1234", true, false, false, true, true, false);

			result[26] = CommandList.setCommand(27, true, "picson", context.getString(R.string.hlpPicsOn), context.getString(R.string.sdPicsOn), "rt#picson##1234", false, false, false, true, false, false);
			result[27] = CommandList.setCommand(28, true, "picsoff", context.getString(R.string.hlpPicsOff), context.getString(R.string.sdPicsOff), "rt#picsoff##1234", false, false, false, true, false, false);
			result[28] = CommandList.setCommand(29, true, "epicson", context.getString(R.string.hlpePicsOn), context.getString(R.string.sdePicsOn), "rt#epicson##1234", false, true, false, true, false, false);
			result[29] = CommandList.setCommand(30, true, "epicsoff", context.getString(R.string.hlpePicsOff), context.getString(R.string.sdePicsOff), "rt#epicsoff##1234", false, true, false, true, false, false);
			result[30] = CommandList.setCommand(31, true, "fpicson", context.getString(R.string.hlpfPicsOn), context.getString(R.string.sdfPicsOn), "rt#fpicson##1234", false, false, true, true, false, false);
			result[31] = CommandList.setCommand(32, true, "fpicsoff", context.getString(R.string.hlpfPicsOff), context.getString(R.string.sdfPicsOff), "rt#fpicsoff##1234", false, false, true, true, false, false);
			result[32] = CommandList.setCommand(33, true, "pcallson", context.getString(R.string.hlpTakePhotoOnCallsOn), context.getString(R.string.sdTakePhotoOnCallsOn), "rt#pcallson##1234", false, false, false, true, false, true);
			result[33] = CommandList.setCommand(34, true, "pcallsoff", context.getString(R.string.hlpTakePhotoOnCallsOff), context.getString(R.string.sdTakePhotoOnCallsOff), "rt#pcallsoff##1234", false, false, false, true, false, true);
			result[34] = CommandList.setCommand(35, true, "epcallson", context.getString(R.string.hlpeTakePhotoOnCallsOn), context.getString(R.string.sdeTakePhotoOnCallsOn), "rt#epcallson##1234", false, true, false, true, false, true);
			result[35] = CommandList.setCommand(36, true, "epcallsoff", context.getString(R.string.hlpeTakePhotoOnCallsOff), context.getString(R.string.sdeTakePhotoOnCallsOff), "rt#epcallsoff##1234", false, true, false, true, false, true);
			result[36] = CommandList.setCommand(37, true, "fpcallson", context.getString(R.string.hlpfTakePhotoOnCallsOn), context.getString(R.string.sdfTakePhotoOnCallsOn), "rt#fpcallson##1234", false, false, true, true, false, true);
			result[37] = CommandList.setCommand(38, true, "fpcallsoff", context.getString(R.string.hlpfTakePhotoOnCallsOff), context.getString(R.string.sdfTakePhotoOnCallsOff), "rt#fpcallsoff##1234", false, false, true, true, false, true);
			result[38] = CommandList.setCommand(38, true, "deletefile", context.getString(R.string.hlpDeleteFile), context.getString(R.string.sdDeleteFile), "rt#deletefile,<filename>##1234", true, false, false, true, false, true);
			result[39] = CommandList.setCommand(39, false, "trackeron", context.getString(R.string.hlpTrackerOn), context.getString(R.string.sdTrackerOn), "rt#trackeron,<time>##1234", true, false, false, true, true, false);
			result[40] = CommandList.setCommand(40, false, "trackeroff", context.getString(R.string.hlpTrackerOff), context.getString(R.string.sdTrackerOff), "rt#trackeroff##1234", false, false, false, true, true, false);
		}

		return result;
	}

	private static jv.android.remotetracker.utils.Commands setCommand(int id, boolean lite, String name, String description, String shortDescription, String example, boolean needExtraParameter, boolean isEmail, boolean isFTP, boolean passwordRequired, boolean isFree, boolean isHidden) {
		Commands c = new Commands();
		c.setId(id);
		c.setLite(lite);
		c.setCommand(name);
		c.setDescription(description);
		c.setShortDescription(shortDescription);
		c.setExample(example);
		c.setEmail(isEmail);
		c.setFTP(isFTP);
		c.setPasswordRequired(passwordRequired);
		c.setFree(isFree);
		c.setHidden(isHidden);
		c.setNeedExtraParameter(needExtraParameter);

		return c;
	}

	public static int count() {
		return total;
	}
}
