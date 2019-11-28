package jv.android.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import jv.android.utils.interfaces.IFTP;
import jv.android.utils.network.Network;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamAdapter;

import android.os.Environment;
import android.content.Context;
import android.content.Intent;

public class FTP {

	public static final String BROADCAST_FTP = "jv.antroid.utils.ftp";
	
	private static String lastError = "";

	public static String getLastError() {
		return lastError;
	}

	public static boolean sendTxtFile (Context context, String server, String userName, String password, String remotePath, String localFile) {
		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.ASCII_FILE_TYPE, 30000, null);
	}

	public static boolean sendFile (Context context, String server, String userName, String password, String remotePath, String localFile) {
		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE, 30000, null);
	}

	public static boolean sendFile (Context context, String server, String userName, String password, String remotePath, File file) {
		String localFile = file.getAbsolutePath().replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "");

		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE, 30, null);
	}

	public static boolean sendFile (Context context, String server, String userName, String password, String remotePath, File file, int timeout) {
		String localFile = file.getAbsolutePath().replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "");

		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE, timeout, null);
	}

	public static boolean sendTxtFile (Context context, String server, String userName, String password, String remotePath, String localFile, int timeout) {
		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.ASCII_FILE_TYPE, timeout, null);
	}

	public static boolean sendFile (Context context, String server, String userName, String password, String remotePath, String localFile, int timeout) {
		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE, timeout, null);
	}

	public static boolean sendTxtFile (Context context, String server, String userName, String password, String remotePath, String localFile, IFTP iftp) {
		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.ASCII_FILE_TYPE, 30, iftp);
	}

	public static boolean sendFile (Context context, String server, String userName, String password, String remotePath, String localFile, IFTP iftp) {
		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE, 30, iftp);
	}

	public static boolean sendFile (Context context, String server, String userName, String password, String remotePath, File file, IFTP iftp) {
		String localFile = file.getAbsolutePath().replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "");

		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE, 30, iftp);
	}

	public static boolean sendFile (Context context, String server, String userName, String password, String remotePath, File file, int timeout, IFTP iftp) {
		String localFile = file.getAbsolutePath().replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "");

		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE, timeout, iftp);
	}

	public static boolean sendTxtFile (Context context, String server, String userName, String password, String remotePath, String localFile, int timeout, IFTP iftp) {
		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.ASCII_FILE_TYPE, timeout, iftp);
	}

	public static boolean sendFile (Context context, String server, String userName, String password, String remotePath, String localFile, int timeout, IFTP iftp) {
		return sendInternalFile (context, server, userName, password, remotePath, localFile, org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE, timeout, iftp);
	}

	private static boolean sendInternalFile (Context context, String server, String userName, String password, String remotePath, String localFile, int fileType, int timeout, final IFTP iFTP) {
		Logs.infoLog("FTP.sendFile. Starting");
		FTPClient ftpClient = new FTPClient();
		boolean result = false;

		File root = Environment.getExternalStorageDirectory();
		File file = new File (root, localFile);

		if (localFile.contains("/")) {
			localFile = localFile.substring(localFile.lastIndexOf("/") + 1);
		}

		final int fileLength = localFile.length();

		if (server.equals("") || userName.equals("") || password.equals("") || localFile.equals("")) {
			lastError = "missing FTP informations.";
			Logs.warningLog("FTP.sendFile. server = " + server + 
					"\nUsername=" + userName +
					"\nPassword empty? " + (password.equals("") ? "yes" : "no") +
					"\nLocalFile: " + localFile + 
					". Something is empty and nothing to do");
		}
		else
		{
			if (!file.exists()) {
				lastError = "file does not exists";
				Logs.warningLog("FTP.sendFile. file " + localFile + " does not exists. Nothing to do");
			}
			else
			{
				if (!Network.isNetworkAvailable(context)) {
					lastError = "Network unavailable";
					Logs.warningLog("FTP.sendFile. No network connection. Nothing to do.");
				}
				else
				{
					try {
						Logs.infoLog("FTP.sendFile. Connecting to " + server);
						ftpClient.setConnectTimeout(timeout);
						ftpClient.connect(InetAddress.getByName(server));

						BufferedInputStream buffIn = null;
						try {
							Logs.infoLog("FTP.sendFile. Login: " + userName);
							ftpClient.login(userName, password);

							if (remotePath != null && !remotePath.equals("")) {
								Logs.infoLog("FTP.sendFile. Changing remote path to " + remotePath);
								ftpClient.changeWorkingDirectory(remotePath);
							}

							Logs.infoLog("FTP.sendFile. Getting Reply String");
							if (ftpClient.getReplyString().contains("250")) {
								try {
									ftpClient.deleteFile(localFile);
								} catch (Exception e) {
									lastError = "Failed delete remote file";
									Logs.infoLog("FTP.sendFile. Failed delete remote file");
								}

								Logs.infoLog("FTP.sendFile. Changing type to binary/ascii");
								ftpClient.setFileType(fileType);

								buffIn = new BufferedInputStream(new FileInputStream(file.getPath()));

								Logs.infoLog("FTP.sendFile. Changing to Passive Mode");
								ftpClient.enterLocalPassiveMode();

								if (iFTP != null) {
									CopyStreamAdapter streamListener = new CopyStreamAdapter() {
										@Override
										public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
											int percent = (int)(totalBytesTransferred * 100 / fileLength);
											iFTP.onFTPProgress(percent);
										}
									};
									ftpClient.setCopyStreamListener(streamListener);
								}

								Logs.infoLog("FTP.sendFile. Sending file " + localFile);
								result = ftpClient.storeFile(localFile, buffIn);
							} else {
								lastError = "Failed to get reply string";
								Logs.infoLog("FTP.sendFile. Failed to get reply string");
							}	
						} finally {
							Logs.infoLog("FTP.sendFile. Closing connection");

							if (buffIn != null)
								buffIn.close();

							if (ftpClient != null) {
								ftpClient.logout();
								ftpClient.disconnect();
							}
						}
					} catch (SocketException e) {
						lastError = "SocketException " + e.getLocalizedMessage();
						Logs.errorLog("FTP.sendFile. SocketException", e);
					} catch (UnknownHostException e) {
						lastError = "UnknownHostException " + e.getLocalizedMessage();
						Logs.errorLog("FTP.sendFile. UnknownHostException", e);
					} catch (IOException e) {
						lastError = "IOException " + e.getLocalizedMessage();
						Logs.errorLog("FTP.sendFile. IOException", e);
					}
				}
			}
		}

		Logs.infoLog("FTP.sendFile. Ending");
		return result;
	}

	public static boolean getFile (Context context, String server, String userName, String password, String remoteFile, 
			String localFile, int fileType, int timeout) {
		Logs.infoLog("FTP.getFile. Starting");
		FTPClient ftpClient = new FTPClient();
		boolean result = false;

		if (server.equals("") || userName.equals("") || password.equals("") || localFile.equals("")) {
			lastError = "missing FTP informations.";
			Logs.warningLog("FTP.getFile. server = " + server + 
					"\nUsername=" + userName +
					"\nPassword empty? " + (password.equals("") ? "yes" : "no") +
					"\nLocalFile: " + localFile + 
					". Something is empty and nothing to do");
		} else {
			if (!Network.isNetworkAvailable(context)) {
				lastError = "Network unavailable";
				Logs.warningLog("FTP.getFile. No network connection. Nothing to do.");
			} else {
				try {
					Logs.infoLog("FTP.getFile. Connecting to " + server);
					ftpClient.setControlEncoding("UTF-8");
					ftpClient.setConnectTimeout(timeout);
					ftpClient.connect(InetAddress.getByName(server));

					Logs.infoLog("FTP.getFile. Login: " + userName);
					ftpClient.login(userName, password);

					Logs.infoLog("FTP.getFile. Changing type to binary/ascii");
					ftpClient.setFileType(fileType);

					Logs.infoLog("FTP.getFile. Changing to Passive Mode");
					ftpClient.enterLocalPassiveMode();

					if (remoteFile.contains("/")) {
						Logs.infoLog("FTP.getFile. Changing remote path to " + remoteFile.substring(0, remoteFile.lastIndexOf("/")));
						ftpClient.changeWorkingDirectory(remoteFile.substring(0, remoteFile.lastIndexOf("/")));
					}

					Logs.infoLog("FTP.getFile. Getting file " + localFile);

					long fileSize = 0;
				    FTPFile[] files = ftpClient.listFiles(remoteFile);
				    if (files.length == 1 && files[0].isFile()) {
				        fileSize = files[0].getSize();
				    }
				    
					FileOutputStream fos = new FileOutputStream(localFile);
					InputStream is = ftpClient.retrieveFileStream(remoteFile);
					
					if (is == null) {
						lastError = "Retrive null " + String.valueOf(ftpClient.getReplyCode()) + " - " + ftpClient.getReplyString();
						Logs.errorLog("FTP.getFile. Retrive null " + String.valueOf(ftpClient.getReplyCode()) + " - " + ftpClient.getReplyString());
					} else {
						int bytesRead = -1;
						long downloaded = 0;
						byte[] buf = new byte[8096];

						while ((bytesRead = is.read(buf)) != -1) {
							fos.write(buf, 0, bytesRead);
							
							downloaded = downloaded + bytesRead;
							
							Intent intent = new Intent(BROADCAST_FTP);
							intent.putExtra("file", (remoteFile.contains("/") ? remoteFile.substring(remoteFile.lastIndexOf("/") + 1) : remoteFile));
							intent.putExtra("size", fileSize);
							intent.putExtra("downloaded", downloaded);
							intent.putExtra("error", false);
							intent.putExtra("errormessage", "");
							context.sendBroadcast(intent);	
						}

						if (is != null)
							is.close();

						if (fos != null) { 
							fos.flush(); 
							fos.close(); 
						}							
					}
					ftpClient.disconnect();

					return true;
				} catch (Exception e) {
					lastError = "IOException " + e.getLocalizedMessage();
					Logs.errorLog("FTP.getFile. IOException", e);

					Intent intent = new Intent(BROADCAST_FTP);
					intent.putExtra("file", (remoteFile.contains("/") ? remoteFile.substring(remoteFile.lastIndexOf("/") + 1) : remoteFile));
					intent.putExtra("size", 0);
					intent.putExtra("downloaded", 0);
					intent.putExtra("error", true);
					intent.putExtra("errormessage", e.getLocalizedMessage());
					context.sendBroadcast(intent);	
				}
			}
		}

		Logs.infoLog("FTP.getFile. Ending");
		return result;
	}
}
