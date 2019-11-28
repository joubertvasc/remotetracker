package jv.android.remotetracker.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.remotetracker.utils.SendEMailOrFTP;
import jv.android.utils.Logs;
import jv.android.utils.PhoneUtils;
import jv.android.utils.Calls;

import android.content.Context;
import android.os.Environment;

public class CommandCallHist {

    public static String processCommand(Context context, CommandStructure cs) {
        String filename = "";

        Preferences p = new Preferences(context);
        if (cs.getIsEMailCommand() || cs.getIsFTPCommand()) {
            Date data = new Date();
            data = Calendar.getInstance().getTime();

            filename = "CommandCallHistResult" + jv.android.utils.Format.formatDateTimeOnlyDigits(data) + ".csv";
        }

        return CommandCallHist.processCommand(context, filename, cs.getIsFTPCommand());
    }

    public static String processCommand(Context context, String filename, boolean ftp) {
        String result = "";
        File file = null;
        FileOutputStream out = null;

        if (!filename.equals(""))
            result = "\"" + context.getString(R.string.msgDate) + "\";" +
                    "\"" + context.getString(R.string.chNumber) + "\";" +
                    "\"" + context.getString(R.string.chDuration) + "\";" +
                    "\"" + context.getString(R.string.chType) + "\";\n";
        else
            result = context.getString(R.string.msgDate) + ";" +
                    context.getString(R.string.chNumber) + ";" +
                    context.getString(R.string.chDuration) + ";" +
                    context.getString(R.string.chType) + ";\n";

        if (!filename.equals("")) {
            file = new File(Environment.getExternalStorageDirectory(), filename);

            if (file.exists())
                file.delete();

            try {
                out = new FileOutputStream(file);
                out.write(result.getBytes());
            } catch (Exception e) {
                Logs.errorLog("CommandCallHist.processCommand error", e);
            }
        }

        // Read all calls
        Calls[] c = PhoneUtils.getCallLog(context, context.getString(R.string.msgIncoming),
                context.getString(R.string.msgOutgoing), context.getString(R.string.msgMissed));

        if (c != null) {
            for (int i = 0; i < c.length; i++) {
                if (c[i] != null) {
                    String line = c[i].getDate() + ";" + c[i].getNumber() + ";" +
                            c[i].getDuration() + ";" + c[i].getType() + ";\n";

                    if (!filename.equals(""))
                        try {
                            out.write(line.getBytes());
                        } catch (Exception e) {
                            Logs.errorLog("CommandCallHist.processCommand error", e);
                        }
                    else
                        result += line;
                }
            }
        }

        if (!filename.equals("")) {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                Logs.errorLog("CommandCallHist.processCommand error", e);
            }

            if (SendEMailOrFTP.send(context, file, context.getString(R.string.msgSubject), context.getString(R.string.msgCallHistCommandBody), ftp)) {
                Logs.infoLog("CommandCallHist.processCommand: email/ftp sent.");

                MessageFormat form = new MessageFormat(context.getString(R.string.msgFileSent));
                Object[] args = {filename};
                result = form.format(args);
            } else {
                Logs.infoLog("CommandCallHist.processCommand: email/ftp not sent.");

                MessageFormat form = new MessageFormat(context.getString(R.string.msgFileNotSent));
                Object[] args = {filename};
                result = form.format(args);
            }

            try {
                file.delete();
                Logs.infoLog("CommandCallHist.processCommand: Temp file was deleted.");
            } catch (Exception e) {
                Logs.errorLog("CommandCallHist.processCommand: Temp file was not deleted.", e);
            }
        }

        return result;
    }
}
