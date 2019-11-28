package jv.android.remotetracker.commands;

import android.content.Context;
import android.os.Environment;

import java.text.MessageFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.remotetracker.utils.SendEMailOrFTP;
import jv.android.utils.*;

public class CommandPB {

    public static String processCommand(Context context, CommandStructure cs) {
        Preferences p = new Preferences(context);
        String filename = "";

        if (cs.getIsEMailCommand() || cs.getIsFTPCommand()) {
            Date data = Calendar.getInstance().getTime();

            filename = "CommandPBResult" + jv.android.utils.Format.formatDateTimeOnlyDigits(data) + ".csv";
        }

        return CommandPB.processCommand(context, filename, cs.getIsFTPCommand());
    }

    private static String returnList(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && !list.get(i).trim().equals(""))
                    result.append(result.toString().equals("") ? "" : ",").append(list.get(i).trim());
            }

            return result.toString();
        }
    }

    public static String processCommand(Context context, String filename, boolean ftp) {
        StringBuilder result = new StringBuilder();
        File file = null;
        FileOutputStream out = null;

        if (!filename.equals(""))
            result = new StringBuilder("\"" + context.getString(R.string.pbID) + "\";" +
                    "\"" + context.getString(R.string.pbName) + "\";" +
                    "\"" + context.getString(R.string.pbHomePhone) + "\";" +
                    "\"" + context.getString(R.string.pbMobilePhone) + "\";" +
                    "\"" + context.getString(R.string.pbWorkPhone) + "\";" +
                    "\"" + context.getString(R.string.pbOtherPhone) + "\";" +
                    "\"" + context.getString(R.string.pbCustomPhone) + "\";" +
                    "\"" + context.getString(R.string.pbHomeFax) + "\";" +
                    "\"" + context.getString(R.string.pbWorkFax) + "\";" +
                    "\"" + context.getString(R.string.pbOtherFax) + "\";" +
                    "\"" + context.getString(R.string.pbHomeEmail) + "\";" +
                    "\"" + context.getString(R.string.pbMobileEmail) + "\";" +
                    "\"" + context.getString(R.string.pbWorkEmail) + "\";" +
                    "\"" + context.getString(R.string.pbOtherEmail) + "\";" +
                    "\"" + context.getString(R.string.pbCustomEmail) + "\";\n");
        else
            result = new StringBuilder(context.getString(R.string.pbName) + ";" +
                    context.getString(R.string.pbHomePhone) + ";" +
                    context.getString(R.string.pbMobilePhone) + ";" +
                    context.getString(R.string.pbWorkPhone) + "\n");

        if (!filename.equals("")) {
            file = new File(Environment.getExternalStorageDirectory(), filename);

            if (file.exists())
                file.delete();

            try {
                out = new FileOutputStream(file);
                out.write(result.toString().getBytes());
            } catch (Exception e) {
                Logs.errorLog("CommandPB.processCommand error", e);
            }
        }

        // Read all contacts
        ContactDetail[] cd = Contacts.getContacts(context, "");

        if (cd != null)
            for (ContactDetail contactDetail : cd) {
                String line = "";

                if (!filename.equals(""))
                    if (contactDetail != null)
                        line = contactDetail.getId() + ";" + contactDetail.getName() + ";" +
                                returnList(contactDetail.getHomePhone()) + ";" +
                                returnList(contactDetail.getMobilePhone()) + ";" +
                                returnList(contactDetail.getWorkPhone()) + ";" +
                                returnList(contactDetail.getOtherPhone()) + ";" +
                                returnList(contactDetail.getCustomPhone()) + ";" +
                                returnList(contactDetail.getHomeFax()) + ";" +
                                returnList(contactDetail.getWorkFax()) + ";" +
                                returnList(contactDetail.getOtherFax()) + ";" +
                                returnList(contactDetail.getHomeEmail()) + ";" +
                                returnList(contactDetail.getMobileEmail()) + ";" +
                                returnList(contactDetail.getWorkEmail()) + ";" +
                                returnList(contactDetail.getOtherEmail()) + ";" +
                                returnList(contactDetail.getCustomEmail()) + "\n";

                if (!line.equals("") && !filename.equals(""))
                    try {
                        if (out != null) {
                            out.write(line.getBytes());
                        }
                    } catch (Exception e) {
                        Logs.errorLog("CommandPB.processCommand error", e);
                    }
                else
                    result.append(line);
            }

        if (!filename.equals("")) {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                Logs.errorLog("CommandPB.processCommand error", e);
            }

            if (SendEMailOrFTP.send(context, file, context.getString(R.string.msgSubject), context.getString(R.string.msgPBCommandBody), ftp)) {
                Logs.infoLog("CommandPB.processCommand: email/ftp sent.");

                MessageFormat form = new MessageFormat(context.getString(R.string.msgFileSent));
                Object[] args = {filename};
                result = new StringBuilder(form.format(args));
            } else {
                Logs.infoLog("CommandPB.processCommand: email/ftp not sent.");

                MessageFormat form = new MessageFormat(context.getString(R.string.msgFileNotSent));
                Object[] args = {filename};
                result = new StringBuilder(form.format(args));
            }

            try {
                file.delete();
                Logs.infoLog("CommandPB.processCommand: Temp file was deleted.");
            } catch (Exception e) {
                Logs.errorLog("CommandPB.processCommand: Temp file was not deleted.", e);
            }
        }

        return result.toString();
    }
}
