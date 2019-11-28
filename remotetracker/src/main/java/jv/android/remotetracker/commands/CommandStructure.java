package jv.android.remotetracker.commands;

import java.io.Serializable;
import java.util.Locale;

public class CommandStructure implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String from;
	private String command;
	private String extraParameter;
	private String password;
	private String returnNumber;
	private String returnEMail;
	private String imeiOrigem;
	private boolean isEMailCommand; 
	private boolean isFTPCommand;
	private boolean returnToWeb;
	
	public CommandStructure() {
		clear();
	}

    public void clear() {
		command = "";
		extraParameter = "";
		password = "";
		returnNumber = "";
		returnEMail = "";
		imeiOrigem = "";

		isEMailCommand = false;
		isFTPCommand = false;
		returnToWeb = false;
	}
	
	private boolean commandsReturnedByEMail(String cmd) {
		return (cmd.toLowerCase(Locale.getDefault()).trim().startsWith("e"));
	}
	
	private boolean commandsReturnedByFTP(String cmd) {
		return (cmd.toLowerCase(Locale.getDefault()).trim().startsWith("f"));
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom (String from) {
		this.from = from;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
		
		isEMailCommand = commandsReturnedByEMail(command);
		isFTPCommand = commandsReturnedByFTP(command);
	}
	public String getExtraParameter() {
		return extraParameter;
	}
	public void setExtraParameter(String extraParameter) {
		this.extraParameter = extraParameter;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getReturnNumber() {
		return returnNumber;
	}
	public void setReturnNumber(String returnNumber) {
		this.returnNumber = returnNumber;
	}
	public String getReturnEMail() {
		return returnEMail;
	}
	public void setReturnEMail(String returnEMail) {
		this.returnEMail = returnEMail;
	}
	public boolean getIsEMailCommand() {
		return isEMailCommand;
	}
	public boolean getIsFTPCommand() {
		return isFTPCommand;
	}
	public boolean isReturnToWeb() {
		return returnToWeb;
	}
	public void setReturnToWeb(boolean returnToWeb) {
		this.returnToWeb = returnToWeb;
	}
    public String getImeiOrigem() {
        return imeiOrigem;
    }
    public void setImeiOrigem(String imeiOrigem) {
        this.imeiOrigem = imeiOrigem;
    }

}
