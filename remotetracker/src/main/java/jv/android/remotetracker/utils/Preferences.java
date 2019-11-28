package jv.android.remotetracker.utils;

import android.content.Context;

import jv.android.remotetracker.data.Config;
import jv.android.utils.Crypt;
import jv.android.utils.Logs;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Objects;

public class Preferences {

	// Used to export/import preferences
	private String filename = ".devicelib.db";	
	
	// Preferences
	private SharedPreferences sharedPrefs;
	
	// Generic options
	private boolean tosAccepted = false;
	private boolean usingMetric = false;
	private boolean debug = false;
	private boolean proVersion = false;
	private boolean interceptPics = false;
	private boolean takePhotoOnCalls = false;
	private boolean interceptPicsFTP = false;
	private boolean takePhotoOnCallsFTP = false;
	private String msgLock = "";

	private String ownerName = "";
	private String defaultEMailAddress = "";

	// Password
	private String password = "";
	private String secretQuestion = "";
	private String secretAnswer = "";
	
	// E-Mail options
	private String emailUserName = "";
	private String emailAddress = "";
	private boolean emailSMTPAuth = true;
	private String emailUser = "";
	private String emailPassword = "";
	private String emailServer = "";
	private int emailPort = 25;
	
	// GPS options
	private int timeout = 120;
	
	// SIM Card 
	private String imsi1 = "";
	private String imsi2 = "";
	private String imsi3 = "";
	private String imsi4 = "";
	private String imsiAlias1 = "";	
	private String imsiAlias2 = "";	
	private String imsiAlias3 = "";	
	private String imsiAlias4 = "";	

	// Contacts for emergency
	private String cel1 = "";
	private String cel2 = "";
	private String cel3 = "";
	private String cel4 = "";
	private String celAlias1 = "";
	private String celAlias2 = "";
	private String celAlias3 = "";
	private String celAlias4 = "";
	private String email1 = "";
	private String email2 = "";
	private String email3 = "";
	private String email4 = "";
	private String emailAlias1 = "";
	private String emailAlias2 = "";
	private String emailAlias3 = "";
	private String emailAlias4 = "";
	
	// FTP properties
	private String ftpServer = "";
	private String ftpUserName = "";
	private String ftpPassword = "";
	private String ftpRemotePath = "";

	public Preferences(Context context) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        readPreferences();
	}

	public void readPreferences() {
        tosAccepted = sharedPrefs.getBoolean("tos_accepted", false);
        usingMetric = sharedPrefs.getBoolean("pref_metric", false);
        debug = sharedPrefs.getBoolean("debug_mode", false);
       	proVersion = sharedPrefs.getBoolean("proVersion", false);
       	interceptPics = sharedPrefs.getBoolean("interceptPics", false);
       	takePhotoOnCalls = sharedPrefs.getBoolean("takePhotoOnCalls", false);
       	interceptPicsFTP = sharedPrefs.getBoolean("interceptPicsFTP", false);
       	takePhotoOnCallsFTP = sharedPrefs.getBoolean("takePhotoOnCallsFTP", false);
       	msgLock = sharedPrefs.getString("msgLock", "");

       	ownerName = sharedPrefs.getString("owner_name", "");
       	defaultEMailAddress = sharedPrefs.getString("default_email_address", "");
       	
       	password = sharedPrefs.getString("password", "");
    	secretQuestion = sharedPrefs.getString("secretQuestion", "");
    	secretAnswer = sharedPrefs.getString("secretAnswer", "");

    	emailUserName = sharedPrefs.getString("email_username", "");
    	emailAddress = sharedPrefs.getString("email_address", "");
    	emailSMTPAuth = sharedPrefs.getBoolean("email_smtp_auth", false);
    	emailUser = sharedPrefs.getString("email_user", "");
    	emailPassword = sharedPrefs.getString("email_password", "");
    	emailServer = sharedPrefs.getString("email_server", "");
		try {
			emailPort = Integer.parseInt(Objects.requireNonNull(sharedPrefs.getString("email_port", "25")));
		} catch (Exception e) {
			emailPort = 25;
		}

		try {
			timeout = Integer.parseInt(Objects.requireNonNull(sharedPrefs.getString("gps_timeout", "120")));
		} catch (Exception e) {
			timeout = 120;
		}

    	imsi1 = sharedPrefs.getString("imsi1", "");
    	imsi2 = sharedPrefs.getString("imsi2", "");
    	imsi3 = sharedPrefs.getString("imsi3", "");
    	imsi4 = sharedPrefs.getString("imsi4", "");
    	imsiAlias1 = sharedPrefs.getString("imsiAlias1", "");
    	imsiAlias2 = sharedPrefs.getString("imsiAlias2", "");
    	imsiAlias3 = sharedPrefs.getString("imsiAlias3", "");
    	imsiAlias4 = sharedPrefs.getString("imsiAlias4", "");

    	cel1 = sharedPrefs.getString("cel1", "");
    	cel2 = sharedPrefs.getString("cel2", "");
    	cel3 = sharedPrefs.getString("cel3", "");
    	cel4 = sharedPrefs.getString("cel4", "");
    	celAlias1 = sharedPrefs.getString("celAlias1", "");
    	celAlias2 = sharedPrefs.getString("celAlias2", "");
    	celAlias3 = sharedPrefs.getString("celAlias3", "");
    	celAlias4 = sharedPrefs.getString("celAlias4", "");
    	email1 = sharedPrefs.getString("email1", "");
    	email2 = sharedPrefs.getString("email2", "");
    	email3 = sharedPrefs.getString("email3", "");
    	email4 = sharedPrefs.getString("email4", "");
    	emailAlias1 = sharedPrefs.getString("emailAlias1", "");
    	emailAlias2 = sharedPrefs.getString("emailAlias2", "");
    	emailAlias3 = sharedPrefs.getString("emailAlias3", "");
    	emailAlias4 = sharedPrefs.getString("emailAlias4", "");

    	ftpServer = sharedPrefs.getString("ftp_server", "");
    	ftpUserName = sharedPrefs.getString("ftp_username", "");
    	ftpPassword = sharedPrefs.getString("ftp_password", ""); 
    	ftpRemotePath = sharedPrefs.getString("ftp_remote_path", "");
	}
	
	public void exportPreferences() {
		if (tosAccepted) {
	        try {
	            readPreferences();

	            String memo =
	            	"tosAccepted=" + tosAccepted + "\n" +
	            	"usingMetric=" + usingMetric + "\n" +
	            	"debug=" + debug + "\n" +
	            	"interceptPics=" + interceptPics + "\n" +
	            	"takePhotoOnCalls=" + takePhotoOnCalls + "\n" +
	            	"interceptPicsFTP=" + interceptPicsFTP + "\n" +
	            	"takePhotoOnCallsFTP=" + takePhotoOnCallsFTP + "\n" +
	               	"msgLock=" + msgLock + "\n" +
	            	"ownerName=" + ownerName + "\n" + 
	            	"defaultEMailAddress=" + defaultEMailAddress + "\n" + 
	            	"password=" + password + "\n" + 
	            	"secretQuestion=" + secretQuestion + "\n" + 
	            	"secretAnswer=" + secretAnswer + "\n" + 
	            	"emailUserName=" + emailUserName + "\n" + 
	            	"emailAddress=" + emailAddress + "\n" + 
	            	"emailSMTPAuth=" + emailSMTPAuth + "\n" +
	            	"emailUser=" + emailUser + "\n" + 
	            	"emailPassword=" + emailPassword + "\n" + 
	            	"emailServer=" + emailServer + "\n" + 
	            	"emailPort=" + emailPort + "\n" +
	            	"timeout=" + timeout + "\n" +
	            	"imsi1=" + imsi1 + "\n" + 
	            	"imsi2=" + imsi2 + "\n" + 
	            	"imsi3=" + imsi3 + "\n" + 
	            	"imsi4=" + imsi4 + "\n" + 
	            	"imsiAlias1=" + imsiAlias1 + "\n" + 
	            	"imsiAlias2=" + imsiAlias2 + "\n" + 
	            	"imsiAlias3=" + imsiAlias3 + "\n" + 
	            	"imsiAlias4=" + imsiAlias4 + "\n" + 
	            	"cel1=" + cel1 + "\n" + 
	            	"cel2=" + cel2 + "\n" + 
	            	"cel3=" + cel3 + "\n" + 
	            	"cel4=" + cel4 + "\n" + 
	            	"celAlias1=" + celAlias1 + "\n" + 
	            	"celAlias2=" + celAlias2 + "\n" + 
	            	"celAlias3=" + celAlias3 + "\n" + 
	            	"celAlias4=" + celAlias4 + "\n" + 
	            	"email1=" + email1 + "\n" + 
	            	"email2=" + email2 + "\n" + 
	            	"email3=" + email3 + "\n" + 
	            	"email4=" + email4 + "\n" + 
	            	"emailAlias1=" + emailAlias1 + "\n" + 
	            	"emailAlias2=" + emailAlias2 + "\n" + 
	            	"emailAlias3=" + emailAlias3 + "\n" + 
	            	"emailAlias4=" + emailAlias4 + "\n" + 
	            	"ftpServer=" + ftpServer + "\n" + 
	            	"ftpUserName=" + ftpUserName + "\n" + 
	            	"ftpPassword=" + ftpPassword + "\n" + 
	            	"ftpRemotePath=" + ftpRemotePath;
	            
	        	// Create encrypter/decrypter class
	            Crypt encrypter = new Crypt(CustomPhrase.getCustomPhrase());
	            
	            // Encrypt
	            String encrypted = encrypter.encrypt(memo);
	            
	       	    File file = new File (Environment.getExternalStorageDirectory(), filename);
	       	    
	       	    if (file.exists()) 
	       	    	file.delete();
	       	    
	            try {
	            	FileOutputStream out = new FileOutputStream(file);
	            	out.write(encrypted.getBytes());
	            	out.flush();
	            	out.close();
	            } catch (Exception e) {
	            	Logs.errorLog("exportPreferences error", e);
	            }
	        } catch (Exception e) {
            	Logs.errorLog("exportPreferences error", e);
	        }
		}
	}
	
	public boolean backupExists() {
   	    File file = new File (Environment.getExternalStorageDirectory(), filename);
   	    
   	    return file.exists(); 
	}
	
	public void importPreferences () {
   	    File file = new File (Environment.getExternalStorageDirectory(), filename);

        if (file.exists()) {
   	        try {
   	        	FileInputStream in = new FileInputStream(file);
   	        	byte[] encrypted = new byte [ (int) file.length()];
   	        	
   	        	int r = in.read(encrypted);
   	        	
   	        	if (r > -1) {
   	        		Crypt c = new Crypt(CustomPhrase.getCustomPhrase());
   	        		String decrypted = c.decrypt(new String(encrypted));
   	        		
   	        		String[] lines = decrypted.split("\n");
                    SharedPreferences.Editor prefEditor = getSharedPrefs().edit();

					for (String line : lines) {
						String[] words = line.split("=");

						if (words.length == 2) {
							switch (words[0]) {
								case "tosAccepted":
									tosAccepted = (words[1].equals("true"));
									prefEditor.putBoolean("tos_accepted", tosAccepted);
									break;
								case "usingMetric":
									usingMetric = (words[1].equals("true"));
									prefEditor.putBoolean("pref_metric", usingMetric);
									break;
								case "debug":
									debug = (words[1].equals("true"));
									prefEditor.putBoolean("debug_mode", debug);
									break;
								case "interceptPics":
									interceptPics = (words[1].equals("true"));
									prefEditor.putBoolean("interceptPics", interceptPics);
									break;
								case "takePhotoOnCalls":
									takePhotoOnCalls = (words[1].equals("true"));
									prefEditor.putBoolean("takePhotoOnCalls", takePhotoOnCalls);
									break;
								case "interceptPicsFTP":
									interceptPicsFTP = (words[1].equals("true"));
									prefEditor.putBoolean("interceptPicsFTP", interceptPicsFTP);
									break;
								case "takePhotoOnCallsFTP":
									takePhotoOnCallsFTP = (words[1].equals("true"));
									prefEditor.putBoolean("takePhotoOnCallsFTP", takePhotoOnCallsFTP);
									break;
								case "msgLock":
									msgLock = words[1];
									prefEditor.putString("msgLock", msgLock);
									break;
								case "ownerName":
									ownerName = words[1];
									prefEditor.putString("owner_name", ownerName);
									break;
								case "defaultEMailAddress":
									defaultEMailAddress = words[1];
									prefEditor.putString("default_email_address", defaultEMailAddress);
									break;
								case "password":
									password = words[1];
									prefEditor.putString("password", password);
									break;
								case "secretQuestion":
									secretQuestion = words[1];
									prefEditor.putString("secretQuestion", secretQuestion);
									break;
								case "secretAnswer":
									secretAnswer = words[1];
									prefEditor.putString("secretAnswer", secretAnswer);
									break;
								case "emailUserName":
									emailUserName = words[1];
									prefEditor.putString("email_username", emailUserName);
									break;
								case "emailAddress":
									emailAddress = words[1];
									prefEditor.putString("email_address", emailAddress);
									break;
								case "emailUser":
									emailUser = words[1];
									prefEditor.putString("email_user", emailUser);
									break;
								case "emailPassword":
									emailPassword = words[1];
									prefEditor.putString("email_password", emailPassword);
									break;
								case "emailServer":
									emailServer = words[1];
									prefEditor.putString("email_server", emailServer);
									break;
								case "emailSMTPAuth":
									emailSMTPAuth = (words[1].equals("true"));
									prefEditor.putBoolean("email_smtp_auth", emailSMTPAuth);
									break;
								case "emailPort":
									emailPort = Integer.valueOf(words[1]);
									prefEditor.putString("email_port", words[1]);
									break;
								case "timeout":
									timeout = Integer.valueOf(words[1]);
									prefEditor.putString("gps_timeout", words[1]);
									break;
								case "imsi1":
									imsi1 = words[1];
									prefEditor.putString("imsi1", imsi1);
									break;
								case "imsi2":
									imsi2 = words[1];
									prefEditor.putString("imsi2", imsi2);
									break;
								case "imsi3":
									imsi3 = words[1];
									prefEditor.putString("imsi3", imsi3);
									break;
								case "imsi4":
									imsi4 = words[1];
									prefEditor.putString("imsi4", imsi4);
									break;
								case "imsiAlias1":
									imsiAlias1 = words[1];
									prefEditor.putString("imsiAlias1", imsiAlias1);
									break;
								case "imsiAlias2":
									imsiAlias2 = words[1];
									prefEditor.putString("imsiAlias2", imsiAlias2);
									break;
								case "imsiAlias3":
									imsiAlias3 = words[1];
									prefEditor.putString("imsiAlias3", imsiAlias3);
									break;
								case "imsiAlias4":
									imsiAlias4 = words[1];
									prefEditor.putString("imsiAlias4", imsiAlias4);
									break;
								case "cel1":
									cel1 = words[1];
									prefEditor.putString("cel1", cel1);
									break;
								case "cel2":
									cel2 = words[1];
									prefEditor.putString("cel2", cel2);
									break;
								case "cel3":
									cel3 = words[1];
									prefEditor.putString("cel3", cel3);
									break;
								case "cel4":
									cel4 = words[1];
									prefEditor.putString("cel4", cel4);
									break;
								case "celAlias1":
									celAlias1 = words[1];
									prefEditor.putString("celAlias1", celAlias1);
									break;
								case "celAlias2":
									celAlias2 = words[1];
									prefEditor.putString("celAlias2", celAlias2);
									break;
								case "celAlias3":
									celAlias3 = words[1];
									prefEditor.putString("celAlias3", celAlias3);
									break;
								case "celAlias4":
									celAlias4 = words[1];
									prefEditor.putString("celAlias4", celAlias4);
									break;
								case "email1":
									email1 = words[1];
									prefEditor.putString("email1", email1);
									break;
								case "email2":
									email2 = words[1];
									prefEditor.putString("email2", email2);
									break;
								case "email3":
									email3 = words[1];
									prefEditor.putString("email3", email3);
									break;
								case "email4":
									email4 = words[1];
									prefEditor.putString("email4", email4);
									break;
								case "emailAlias1":
									emailAlias1 = words[1];
									prefEditor.putString("emailAlias1", emailAlias1);
									break;
								case "emailAlias2":
									emailAlias2 = words[1];
									prefEditor.putString("emailAlias2", emailAlias2);
									break;
								case "emailAlias3":
									emailAlias3 = words[1];
									prefEditor.putString("emailAlias3", emailAlias3);
									break;
								case "emailAlias4":
									emailAlias4 = words[1];
									prefEditor.putString("emailAlias4", emailAlias4);
									break;
								case "ftpServer":
									ftpServer = words[1];
									prefEditor.putString("ftp_server", ftpServer);
									break;
								case "ftpUserName":
									ftpUserName = words[1];
									prefEditor.putString("ftp_username", ftpUserName);
									break;
								case "ftpPassword":
									ftpPassword = words[1];
									prefEditor.putString("ftp_password", ftpPassword);
									break;
								case "ftpRemotePath":
									ftpRemotePath = words[1];
									prefEditor.putString("ftp_remote_path", ftpRemotePath);
									break;
							}
						}
					}

                    prefEditor.apply();
                }
   	        	
   	        	in.close();
   	        } catch (Exception e) {
   	        	Logs.errorLog("exportPreferences error", e);
   	        }
   	    }
}

	public void setParamsByConfig(Config config) {
        if (config != null) {
            SharedPreferences.Editor prefEditor = getSharedPrefs().edit();

            prefEditor.putBoolean("tos_accepted", config.getTosAccepted().equals("S"));
            prefEditor.putBoolean("pref_metric", config.getUsemetric().equals("S"));
            prefEditor.putBoolean("debug_mode", config.getDebug().equals("S"));
            prefEditor.putBoolean("interceptPics", config.getInterceptPics().equals("S"));
            prefEditor.putBoolean("takePhotoOnCalls", config.getTakePhotoOnCalls().equals("S"));
            prefEditor.putBoolean("interceptPicsFTP", config.getInterceptPicsFTP().equals("S"));
            prefEditor.putBoolean("takePhotoOnCallsFTP", config.getTakePhotoOnCallsFTP().equals("S"));
            prefEditor.putString("default_email_address", config.getDefaultEMailAddress());
            prefEditor.putString("msgLock", config.getMsgLock());
            prefEditor.putString("owner_name", config.getOwner());
            prefEditor.putString("password", config.getPassword());
            prefEditor.putString("secretQuestion", config.getSecretquestion());
            prefEditor.putString("secretAnswer", config.getSecretanswer());
            prefEditor.putString("email_username", config.getEmailname());
            prefEditor.putString("email_address", config.getEmailaddress());
            prefEditor.putString("email_user", config.getEmaillogin());
            prefEditor.putString("email_password", config.getEmailpassword());
            prefEditor.putString("email_server", config.getEmailsmtp());
            prefEditor.putBoolean("email_smtp_auth", !config.getEmaillogin().equals(""));
            prefEditor.putString("email_port", config.getEmailport());
            prefEditor.putString("gps_timeout", config.getTimeout());
            prefEditor.putString("imsi1", config.getImsi1());
            prefEditor.putString("imsi2", config.getImsi2());
            prefEditor.putString("imsi3", config.getImsi3());
            prefEditor.putString("imsi4", config.getImsi4());
            prefEditor.putString("imsiAlias1", config.getImsialias1());
            prefEditor.putString("imsiAlias2", config.getImeialias2());
            prefEditor.putString("imsiAlias3", config.getImeialias3());
            prefEditor.putString("imsiAlias4", config.getImsialias4());
            prefEditor.putString("cel1", config.getTel1());
            prefEditor.putString("cel2", config.getTel2());
            prefEditor.putString("cel3", config.getTel3());
            prefEditor.putString("cel4", config.getTel4());
            prefEditor.putString("celAlias1", config.getTelalias1());
            prefEditor.putString("celAlias2", config.getTelalias2());
            prefEditor.putString("celAlias3", config.getTelalias3());
            prefEditor.putString("celAlias4", config.getTelalias4());
            prefEditor.putString("email1", config.getEmail1());
            prefEditor.putString("email2", config.getEmail2());
            prefEditor.putString("email3", config.getEmail3());
            prefEditor.putString("email4", config.getEmail4());
            prefEditor.putString("emailAlias1", config.getEmailalias1());
            prefEditor.putString("emailAlias2", config.getEmailalias2());
            prefEditor.putString("emailAlias3", config.getEmailalias3());
            prefEditor.putString("emailAlias4", config.getEmailalias4());
            prefEditor.putString("ftp_server", config.getFtpaddress());
            prefEditor.putString("ftp_username", config.getFtplogin());
            prefEditor.putString("ftp_password", config.getFtppassword());
            prefEditor.putString("ftp_remote_path", config.getFtpdirectory());

            prefEditor.apply();
            readPreferences();
        }
    }
	
	public boolean getTosAccepted() {
		return tosAccepted;	
	}
	
	public void setTosAccepted(boolean tosAccepted) {
		this.tosAccepted = tosAccepted;	

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putBoolean("tos_accepted", tosAccepted);
		prefEditor.apply();
	}
	
	public boolean getDebug() {
		return debug;	
	}

	public String getPassword() {
		return password;		
	}
	
	public String getDefaultEMailAddress() {
		return defaultEMailAddress;
	}
	
	String getEmailUserName() {
		return emailUserName;
	}

	String getEmailAddress() {
		return emailAddress;
	}

	boolean isEmailSMTPAuth() {
		return emailSMTPAuth;
	}

	String getEmailUser() {
		return emailUser;
	}

	String getEmailPassword() {
		return emailPassword;
	}

	String getEmailServer() {
		return emailServer;
	}

	int getEmailPort() {
		return emailPort;
	}

	public SharedPreferences getSharedPrefs() {
		return sharedPrefs;		
	}

	public String getImsi1() {
		return imsi1;
	}

	public void setImsi1(String imsi1) {
		this.imsi1 = imsi1;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("imsi1", imsi1);
		prefEditor.apply();
	}

	public String getImsi2() {
		return imsi2;
	}

	public void setImsi2(String imsi2) {
		this.imsi2 = imsi2;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("imsi2", imsi2);
		prefEditor.apply();
	}

	public String getImsi3() {
		return imsi3;
	}

	public void setImsi3(String imsi3) {
		this.imsi3 = imsi3;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("imsi3", imsi3);
		prefEditor.apply();
	}

	public String getImsi4() {
		return imsi4;
	}

	public void setImsi4(String imsi4) {
		this.imsi4 = imsi4;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("imsi4", imsi4);
		prefEditor.apply();
	}

	public String getImsiAlias1() {
		return imsiAlias1;
	}

	public void setImsiAlias1(String imsiAlias1) {
		this.imsiAlias1 = imsiAlias1;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("imsiAlias1", imsiAlias1);
		prefEditor.apply();
	}

	public String getImsiAlias2() {
		return imsiAlias2;
	}

	public void setImsiAlias2(String imsiAlias2) {
		this.imsiAlias2 = imsiAlias2;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("imsiAlias2", imsiAlias2);
		prefEditor.apply();
	}

	public String getImsiAlias3() {
		return imsiAlias3;
	}

	public void setImsiAlias3(String imsiAlias3) {
		this.imsiAlias3 = imsiAlias3;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("imsiAlias3", imsiAlias3);
		prefEditor.apply();
	}

	public String getImsiAlias4() {
		return imsiAlias4;
	}

	public void setImsiAlias4(String imsiAlias4) {
		this.imsiAlias4 = imsiAlias4;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("imsiAlias4", imsiAlias4);
		prefEditor.apply();
	}

	public String getCel1() {
		return cel1;
	}

	public void setCel1(String cel1) {
		this.cel1 = cel1;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("cel1", cel1);
		prefEditor.apply();
	}

	public String getCel2() {
		return cel2;
	}

	public void setCel2(String cel2) {
		this.cel2 = cel2;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("cel2", cel2);
		prefEditor.apply();
	}

	public String getCel3() {
		return cel3;
	}

	public void setCel3(String cel3) {
		this.cel3 = cel3;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("cel3", cel3);
		prefEditor.apply();
	}

	public String getCel4() {
		return cel4;
	}

	public void setCel4(String cel4) {
		this.cel4 = cel4;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("cel4", cel4);
		prefEditor.apply();
	}

	public String getCelAlias1() {
		return celAlias1;
	}

	public void setCelAlias1(String celAlias1) {
		this.celAlias1 = celAlias1;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("celAlias1", celAlias1);
		prefEditor.apply();
	}

	public String getCelAlias2() {
		return celAlias2;
	}

	public void setCelAlias2(String celAlias2) {
		this.celAlias2 = celAlias2;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("celAlias2", celAlias2);
		prefEditor.apply();
	}

	public String getCelAlias3() {
		return celAlias3;
	}

	public void setCelAlias3(String celAlias3) {
		this.celAlias3 = celAlias3;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("celAlias3", celAlias3);
		prefEditor.apply();
	}

	public String getCelAlias4() {
		return celAlias4;
	}

	public void setCelAlias4(String celAlias4) {
		this.celAlias4 = celAlias4;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("celAlias4", celAlias4);
		prefEditor.apply();
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("email1", email1);
		prefEditor.apply();
	}

	public String getEmail2() {
		return email2;
	}
	
	public void setEmail2(String email2) {
		this.email2 = email2;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("email2", email2);
		prefEditor.apply();
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("email3", email3);
		prefEditor.apply();
	}

	public String getEmail4() {
		return email4;
	}

	public void setEmail4(String email4) {
		this.email4 = email4;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("email4", email4);
		prefEditor.apply();
	}

	public String getEmailAlias1() {
		return emailAlias1;
	}

	public void setEmailAlias1(String emailAlias1) {
		this.emailAlias1 = emailAlias1;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("emailAlias1", emailAlias1);
		prefEditor.apply();
	} 

	public String getEmailAlias2() {
		return emailAlias2;
	}

	public void setEmailAlias2(String emailAlias2) {
		this.emailAlias2 = emailAlias2;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("emailAlias2", emailAlias2);
		prefEditor.apply();
	}

	public String getEmailAlias3() {
		return emailAlias3;
	}

	public void setEmailAlias3(String emailAlias3) {
		this.emailAlias3 = emailAlias3;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("emailAlias3", emailAlias3);
		prefEditor.apply();
	}

	public String getEmailAlias4() {
		return emailAlias4;
	}

	public void setEmailAlias4(String emailAlias4) {
		this.emailAlias4 = emailAlias4;

		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("emailAlias4", emailAlias4);
		prefEditor.apply();
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public String getFtpServer() {
		return ftpServer;
	}

	public String getSecretQuestion() {
		return secretQuestion;
	}

	public String getSecretAnswer() {
		return secretAnswer;
	}

	public String getFtpUserName() {
		return ftpUserName;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public String getFtpRemotePath() {
		return ftpRemotePath;
	}

	public boolean isProVersion() {
		return proVersion;
	}

	public void setProVersion(boolean proVersion) {
		this.proVersion = proVersion;
		
    	SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
    	prefEditor.putBoolean("proVersion", proVersion);
    	prefEditor.apply();	
	}

	public boolean isInterceptPics() {
		return interceptPics;
	}

	public boolean isInterceptPicsFTP() {
		return interceptPicsFTP;
	}

	public String getMsgLock() {
		return msgLock;
	}

	public void setMsgLock(String msgLock) {
		this.msgLock = msgLock;
		
        SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("msgLock", msgLock);
		prefEditor.apply();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setPassword(String password) {
		this.password = password;
		
        SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("password", password);
		prefEditor.apply();
	}

	public void setDeviceAdmin(boolean isDeviceAdmin) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putBoolean("isDeviceAdmin", isDeviceAdmin);
		prefEditor.apply();
	}

	public void setRegId(String regId) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("regid", regId);
		prefEditor.apply();
	}

	public int getTrackerInterval() {
		return sharedPrefs.getInt("trackerinterval", 0);
	}

	public int getGPSTimeout() {
		return timeout;
	}

	public boolean isTakePhotoOnCalls() {
		return takePhotoOnCalls;
	}

	public boolean isTakePhotoOnCallsFTP() {
		return takePhotoOnCallsFTP;
	}

	public void setTrackerInterval(int interval) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putInt("trackerinterval", interval);
		prefEditor.apply();
	}

	public boolean getTrackerGPSProcessed() {
		return sharedPrefs.getBoolean("trackerprocessed", false);
	}

	public float getTrackerGPSLatitude() {
		return sharedPrefs.getFloat("trackerlatitude", 0f);
	}

	public float getTrackerGPSLongitude() {
		return sharedPrefs.getFloat("trackerlongitude", 0f);
	}

	public float getTrackerGPSAltitude() {
		return sharedPrefs.getFloat("trackeraltitude", 0f);
	}

	public float getTrackerGPSSpeed() {
		return sharedPrefs.getFloat("trackerspeed", 0f);
	}

	public String getTrackerGPSType() {
		return sharedPrefs.getString("trackertype", "N");
	}

	public String getTrackerGPSMessage() {
		return sharedPrefs.getString("trackermessage", "");
	}

	public void setTrackerGPSProcessed(boolean processed) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putBoolean("trackerprocessed", processed);
		prefEditor.apply();
	}

	public void setTrackerGPSLatitude(float latitude) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putFloat("trackerlatitude", latitude);
		prefEditor.apply();
	}

	public void setTrackerGPSLongitude(float longitude) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putFloat("trackerlongitude", longitude);
		prefEditor.apply();
	}

	public void setTrackerGPSAltitude(float altitude) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putFloat("trackeraltitude", altitude);
		prefEditor.apply();
	}

	public void setTrackerGPSSpeed(float speed) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putFloat("trackerspeed", speed);
		prefEditor.apply();
	}

	public void setTrackerGPSType(String type) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("trackertype", type);
		prefEditor.apply();
	}

	public void setTrackerGPSMessage(String message) {
		SharedPreferences.Editor prefEditor = getSharedPrefs().edit();
		prefEditor.putString("trackermessage", message);
		prefEditor.apply();
	}


}
