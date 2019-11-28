package jv.android.remotetracker.utils;

public class Commands {

	private int id;
	private boolean lite;
	private String command;
	private String description;
	private String shortDescription;
	private String example;
	private boolean isEmail;
	private boolean isFTP;
	private boolean passwordRequired;
	private boolean isFree;
	private boolean isHidden;
	private boolean needExtraParameter;

	public Commands() {
		id = -1;
		lite = false;
		command = "";
		description = "";
		shortDescription = "";
		example = "";
		isEmail = false;
		isFTP = false;
		passwordRequired = true;
		isFree = true;
		isHidden = false;
	}

	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	public boolean isEmail() {
		return isEmail;
	}
	public void setEmail(boolean isEmail) {
		this.isEmail = isEmail;
	}
	public boolean isFTP() {
		return isFTP;
	}
	public void setFTP(boolean isFTP) {
		this.isFTP = isFTP;
	}
	public boolean isPasswordRequired() {
		return passwordRequired;
	}
	public void setPasswordRequired(boolean passwordRequired) {
		this.passwordRequired = passwordRequired;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public boolean needExtraParameter() {
		return needExtraParameter;
	}

	public void setNeedExtraParameter(boolean needExtraParameter) {
		this.needExtraParameter = needExtraParameter;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public boolean isLite() {
		return lite;
	}

	public void setLite(boolean lite) {
		this.lite = lite;
	}
}
