package jv.android.remotetracker.commands;

import jv.android.remotetracker.utils.Answer;
import jv.android.remotetracker.utils.Preferences;
import jv.android.remotetracker.R;
import jv.android.utils.Logs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import jv.android.remotetracker.utils.Commands;

@SuppressLint("DefaultLocale")
public class CommandProcessor {

    private CommandStructure commandStructure;
    private Context _context;
    private String _cellular;
    private String _email;
    private String _text;
    private boolean fake = false;
    private Preferences prefs;
    private Commands[] commands;

    public CommandProcessor(Context context, String cellular, String email, String text) {
        setVariables(context, cellular, email, text, false);

        commandStructure = parseText(text);

        getCommandList();
    }

    public CommandProcessor(Context context, String cellular, String email, String text, boolean web, boolean isFake, String origem) {
        setVariables(context, cellular, email, text, isFake);

        commandStructure = parseText(text);
        if (commandStructure != null) {
            commandStructure.setReturnToWeb(web);
            commandStructure.setImeiOrigem(origem);
        }

        getCommandList();
    }

    private void getCommandList() {
        commands = CommandList.getCommandList(_context);
    }

    private int findCommand(String cmd) {
        for (int i = 0; i < commands.length; i++) {
            if (commands[i].getCommand().trim().equalsIgnoreCase(cmd.trim())) {
                return i;
            }
        }

        return -1;
    }

    private void setVariables(Context context, String cellular, String email, String text, boolean isFake) {
        _context = context;
        _cellular = cellular;
        _text = text;
        _email = email;

        fake = isFake;

        prefs = new Preferences(context);
    }

    private String getCommandLineWithoutHeader(String text) {
        return text.toLowerCase().substring(text.toLowerCase().indexOf("rt#"));
    }

    private CommandStructure parseText(String text) {
        Logs.infoLog("Parsing Text started");
        if (text == null || text.trim().equals("")) {
            Logs.infoLog("Text is empty");
            return null;
        }

        Logs.infoLog("Parsig text: " + text);
        CommandStructure c = new CommandStructure();

        c.setFrom(_cellular.equals("") ? _email : _cellular);

        // Remove invalid characters at beginning of the string
        String commandLine = getCommandLineWithoutHeader(text); // "";
        String lineClear = "";

        Logs.infoLog("Cleaning message: " + commandLine);

        // Remove invalid characters at end of the string
        for (int i = commandLine.length() - 1; i >= 0; i--) {
            if (commandLine.charAt(i) == '#' ||
                    (commandLine.charAt(i) >= '0' && commandLine.charAt(i) <= '9') ||
                    (commandLine.toLowerCase().charAt(i) >= 'a' && commandLine.toLowerCase().charAt(i) <= 'z')) {
                lineClear = commandLine.substring(0, i + 1);
                break;
            }
        }

        Logs.infoLog("Message cleared: " + lineClear);

        // Start the parser
        String[] words = lineClear.trim().split("#");

        // Invalid syntax
        if (words.length < 2) {
            Logs.infoLog("Invalid syntax");

            return null;
        }

        for (int i = 1; i < words.length; i++) {
            if (i == 1) {
                String[] ep = words[i].split(",");

                if (ep.length < 2) {
                    c.setCommand(words[i].toLowerCase().trim());
                    Logs.infoLog("Command found without extra parameter: " + words[i].toLowerCase().trim());
                } else {
                    Logs.infoLog("Command found with extra parameter: " + ep[0]);
                    c.setCommand(ep[0]);
                    StringBuilder extra = new StringBuilder();
                    for (int j = 1; j < ep.length; j++)
                        extra.append(ep[j].trim()).append(",");

                    extra = new StringBuilder(extra.substring(0, extra.length() - 1));
                    StringBuilder extra2 = new StringBuilder();

                    for (int j = 0; j < extra.length(); j++)
                        if (extra.charAt(j) != (char) 34 && extra.charAt(j) != (char) 39)
                            extra2.append(extra.charAt(j));

                    Logs.infoLog("Extra parameter: " + extra2);
                    c.setExtraParameter(extra2.toString());
                }
            } else if (i == 2) {
                Logs.infoLog("EMail/Number: " + words[i].toLowerCase().trim());

                if (c.getIsEMailCommand())
                    c.setReturnEMail(words[i].toLowerCase().trim());
                else {
                    c.setReturnNumber(words[i].toLowerCase().trim());
                }
            } else if (i == 3) {
                c.setPassword(words[i].trim());
            }
        }

        // If no returning number was passed, uses the cellular number (sender number).
        if (c.getReturnNumber().equals("") && !_cellular.equals("")) {
            Logs.infoLog("No number passed. Using sender: " + _cellular);
            c.setReturnNumber(_cellular);
        }

        // If no returning email was passed, uses the "from" email address.
        if (c.getReturnEMail().equals("") && (!_email.equals("") || !prefs.getDefaultEMailAddress().equals(""))) {
            if (!_email.equals("")) {
                Logs.infoLog("No email passed. Using from: " + _email);
                c.setReturnEMail(_email);
            } else {
                Logs.infoLog("No email passed. Using from: " + prefs.getDefaultEMailAddress());
                c.setReturnEMail(prefs.getDefaultEMailAddress());
            }
        }

        return c;
    }

    public Context getContext() {
        return _context;
    }

    public String getText() {
        return _text;
    }

    public CommandStructure getCommandStructure() {
        return commandStructure;
    }

    public void processCommands(Context context) {
        Logs.infoLog("ProcessCommands started");

        int cmd = findCommand(commandStructure.getCommand());

        // Password ok?
        if (commandStructure.getCommand().equals("josevictor")) {
            Logs.infoLog("Processing command JOSEVICTOR");

            String result = "Oi eu sou o Josevictor. Eu e meu pai escrevemos este Easter Egg. Esperamos que goste do RemoteTracker!";

            Answer.sendAnswer(_context, commandStructure, result, fake);
        }
        // Tracker Command
        else if (commandStructure.getCommand().equals("trackeron") || commandStructure.getCommand().equals("trackeroff")) {
            Logs.infoLog("Processing command TRACKER");

            // TRACKER commands need special services
            int interval = 60;

            if (!commandStructure.getExtraParameter().equals("")) {
                try {
                    interval = Integer.valueOf(commandStructure.getExtraParameter().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String result = CommandTracker.processCommand(_context, commandStructure, commandStructure.getCommand().equals("trackeron"), interval);
            Logs.infoLog("Processing command GI. Result = " + result);

            Answer.sendAnswer(_context, commandStructure, result, fake);
        }
        // Get Position Command
        else if (commandStructure.getCommand().equals("gp") || commandStructure.getCommand().equals("egp") || commandStructure.getCommand().equals("fgp")) {
            Logs.infoLog("Processing command GP");

            // GPS commands need special services
            Intent commandService = new Intent(context, CommandService.class);
            commandService.putExtra("fake", fake);
            commandService.putExtra("commandstructure", commandStructure);

            context.startService(commandService);
        }
        // Get Information Command
        else if (commandStructure.getCommand().equals("gi") || commandStructure.getCommand().equals("egi") || commandStructure.getCommand().equals("fgi")) {
            Logs.infoLog("Processing command GI");

            String result = CommandGI.processCommand(_context);
            Logs.infoLog("Processing command GI. Result = " + result);

            Answer.sendAnswer(_context, commandStructure, result, fake);
        }
        // Help Command
        else if (commandStructure.getCommand().equals("help") || commandStructure.getCommand().equals("ehelp") || commandStructure.getCommand().equals("fhelp")) {
            Logs.infoLog("Processing command HELP");

            String result = CommandHelp.processCommand(_context, commandStructure.getExtraParameter(), commands[cmd].isEmail() || commands[cmd].isFTP(), commands);
            Logs.infoLog("Processing command HELP. Result = " + result);

            Answer.sendAnswer(_context, commandStructure, result, fake);
        }
        // PB Command
        else if (commandStructure.getCommand().equals("pb") || commandStructure.getCommand().equals("epb") || commandStructure.getCommand().equals("fpb")) {
            Logs.infoLog("Processing command PB");

            String result = CommandPB.processCommand(_context, commandStructure);
            Logs.infoLog("Processing command PB. Has result? " + (result.equals("") ? "no" : "yes"));

            // Answer only by sms. CommandPB.ProcessCommand sends e-mail or FTP.
            commandStructure.setCommand("pb");

            Answer.sendAnswer(_context, commandStructure, result, fake);
        }
        // CALLHIST Command
        else if (commandStructure.getCommand().equals("callhist") || commandStructure.getCommand().equals("ecallhist") || commandStructure.getCommand().equals("fcallhist")) {
            Logs.infoLog("Processing command CALLHIST");

            String result = CommandCallHist.processCommand(_context, commandStructure);
            Logs.infoLog("Processing command CALLHIST. Has result? " + (result.equals("") ? "no" : "yes"));

            // Answer only by sms. CommandCallHist.processCommand sends e-mail or FTP.
            commandStructure.setCommand("callhist");

            Answer.sendAnswer(_context, commandStructure, result, fake);
        }
        // ListApp Command
        else if (commandStructure.getCommand().equals("listapp") || commandStructure.getCommand().equals("elistapp") || commandStructure.getCommand().equals("flistapp")) {
            Logs.infoLog("Processing command ListApp");

            String result = CommandListApp.processCommand(_context);
            Logs.infoLog("Processing command ListApp. Result = " + result);

            Answer.sendAnswer(_context, commandStructure, result, fake);
        }
        // RunApp Command
        else if (commandStructure.getCommand().equals("runapp")) {
            Logs.infoLog("Processing command RunApp");

            String result = CommandRunApp.processCommand(_context, commandStructure.getExtraParameter());
            Logs.infoLog("Processing command RunApp. Result = " + result);

            Answer.sendAnswer(_context, commandStructure, result, fake);
        }
        // CB Command
        else if (commandStructure.getCommand().equals("cb")) {
            Logs.infoLog("Processing command CB");

            CommandCB.processCommand(_context, commandStructure.getReturnNumber());
            Logs.infoLog("Processing command CB. Done");
        }
        // LOCK Command
        else if (commandStructure.getCommand().equals("lock")) {
            Logs.infoLog("Processing command LOCK");

            String result = CommandLock.processCommand(_context, commandStructure.getExtraParameter());

            Answer.sendAnswer(_context, commandStructure, result, fake);
            Logs.infoLog("Processing command LOCK. Done");
        }
        // UNLOCK Command
        else if (commandStructure.getCommand().equals("unlock")) {
            Logs.infoLog("Processing command UNLOCK");

            String result = CommandUnlock.processCommand(_context);

            Answer.sendAnswer(_context, commandStructure, result, fake);
            Logs.infoLog("Processing command UNLOCK. Done");
        }
        // PICSON Command
        else if (commandStructure.getCommand().equals("picson") || commandStructure.getCommand().equals("epicson") || commandStructure.getCommand().equals("fpicson")) {
            Logs.infoLog("Processing command PICSON");

            String result = CommandPics.processCommand(_context, true, commandStructure.getIsFTPCommand());
            Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command PICSON. Done");
        }
        // PICSOFF Command
        else if (commandStructure.getCommand().equals("picsoff") || commandStructure.getCommand().equals("epicsoff") || commandStructure.getCommand().equals("fpicsoff")) {
            Logs.infoLog("Processing command PICSON");

            String result = CommandPics.processCommand(_context, false, false);
            Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command PICSON. Done");
        }

        // PCALLSON Command
        else if (commandStructure.getCommand().equals("pcallson") || commandStructure.getCommand().equals("epcallson") || commandStructure.getCommand().equals("fpcallson")) {
            Logs.infoLog("Processing command PCALLSON");

            String result = CommandPCalls.processCommand(_context, true, commandStructure.getIsFTPCommand());
            Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command PCALLSON. Done");
        }
        // PCALLSOFF Command
        else if (commandStructure.getCommand().equals("pcallsoff") || commandStructure.getCommand().equals("epcallsoff") || commandStructure.getCommand().equals("fpcallsoff")) {
            Logs.infoLog("Processing command PCALLSOFF");

            String result = CommandPCalls.processCommand(_context, false, false);
            Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command PCALLSOFF. Done");
        }
        // SECRET Command
        else if (commandStructure.getCommand().equals("secret")) {
            Logs.infoLog("Processing command SECRET");

            String result = CommandSecret.processCommand(_context);
            Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command SECRET. Done");
        }
        // LOSTPASS Command
        else if (commandStructure.getCommand().equals("lostpass")) {
            Logs.infoLog("Processing command LOSTPASS");

            String result = CommandLostpass.processCommand(_context, commandStructure.getExtraParameter());
            Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command LOSTPASS. Done");
        }
        // CELLID Command
        else if (commandStructure.getCommand().equals("cellid") || commandStructure.getCommand().equals("ecellid") || commandStructure.getCommand().equals("fcellid")) {
            Logs.infoLog("Processing command CELLID");

            String result = CommandCellID.processCommand(_context);
            Logs.infoLog("Processing command CELLID. Result = " + result);

            Answer.sendAnswer(_context, commandStructure, result, fake);
        }
        // MSG Command
        else if (commandStructure.getCommand().equals("msg")) {
            Logs.infoLog("Processing command MSG: " + commandStructure.getExtraParameter());

            String result = CommandMsg.processCommand(_context, commandStructure.getExtraParameter());
            Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command MSG. Done");
        }
        // SMS Command
        else if (commandStructure.getCommand().equals("sms")) {
            Logs.infoLog("Processing command SMS: " + commandStructure.getExtraParameter() + " - " + commandStructure.getReturnNumber());

            String result = CommandSms.processCommand(_context, commandStructure.getExtraParameter(), commandStructure.getReturnNumber());
            Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command SMS. Done");
        }
        // LOGON command
        else if (commandStructure.getCommand().equals("logon")) {
            Logs.infoLog("Processing command LOGON: " + commandStructure.getReturnNumber());

            String result = CommandLogOn.processCommand(_context);

            if (!result.equals(""))
                Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command LOGON. Done");
        }
        // LOGOFF command
        else if (commandStructure.getCommand().equals("logoff")) {
            Logs.infoLog("Processing command LOGOFF: " + commandStructure.getReturnNumber());

            String result = CommandLogOff.processCommand(_context);

            if (!result.equals(""))
                Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command LOGOFF. Done");
        }
        // SENDLOG command
        else if (commandStructure.getCommand().equals("sendlog")) {
            Logs.infoLog("Processing command SENDLOG: " + commandStructure.getReturnNumber());

            String result = CommandSendLog.processCommand(_context);

            if (!result.equals(""))
                Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command SENDLOG. Done");
        }
        // CLEARLOG command
        else if (commandStructure.getCommand().equals("clearlog")) {
            Logs.infoLog("Processing command CLEARLOG: " + commandStructure.getReturnNumber());

            String result = CommandClearLog.processCommand(_context);

            if (!result.equals(""))
                Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command CLEARLOG. Done");
        }
        // DELETEFILE command
        else if (commandStructure.getCommand().equals("deletefile")) {
            Logs.infoLog("Processing command DELETEFILE: " + commandStructure.getExtraParameter() + " - " + commandStructure.getReturnNumber());

            String result = CommandDeleteFile.processCommand(_context, commandStructure.getExtraParameter());

            if (!result.equals(""))
                Answer.sendAnswer(_context, commandStructure, result, fake);

            Logs.infoLog("Processing command DELETEFILE. Done");
        }

        // Commands unavailable in Android (YET)
        else if (commandStructure.getCommand().equals("findme") ||
                commandStructure.getCommand().equals("alarm") ||
                commandStructure.getCommand().endsWith("elt") ||
                commandStructure.getCommand().equals("dsc") ||
                commandStructure.getCommand().equals("dkz") ||
                commandStructure.getCommand().endsWith("gip") ||
                commandStructure.getCommand().equals("delcard") ||
                commandStructure.getCommand().endsWith("ftp") ||
                commandStructure.getCommand().equals("rst") ||
                commandStructure.getCommand().endsWith("ganfl") ||
                commandStructure.getCommand().endsWith("ganf") ||
                commandStructure.getCommand().equals("vnc") ||
                commandStructure.getCommand().endsWith("outlook") ||
                commandStructure.getCommand().endsWith("go") ||
                commandStructure.getCommand().endsWith("mslist") ||
                commandStructure.getCommand().endsWith("msrun")) {
            Logs.infoLog("Command not supported by RemoteTracker for Android");

            Answer.sendAnswer(_context, commandStructure, _context.getString(R.string.msgCommandNotAvailable), fake);
        }
        // UNKNOW command
        else {
            Logs.infoLog("Unknow command");
            Answer.sendAnswer(_context, commandStructure, _context.getString(R.string.msgUnknowcommand), fake);
        }
    }
}
