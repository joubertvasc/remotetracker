package jv.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.Environment;
import java.util.Random;

public final class FileUtil {

    private FileUtil() {     
    }       
    
    @SuppressWarnings("resource")
	public static void copyFile(File src, File dst) throws IOException {
		FileChannel inChannel = new FileInputStream(src).getChannel();        
        FileChannel outChannel = new FileOutputStream(dst).getChannel();        
        
        try {           
            inChannel.transferTo(0, inChannel.size(), outChannel);        
        } finally {           
            if (inChannel != null) {              
                inChannel.close();           
            }           
            
            if (outChannel != null) {              
                outChannel.close();           
            }        
        }     
    }
    
    public static String replaceInvalidChars(String fileName) {
    	String x = "";
    	
    	for (int i = 0; i < fileName.length(); i++) {    	
    		if (fileName.substring(i, i+1).equals("\"") || fileName.substring(i, i+1).equals("\'") || fileName.substring(i, i+1).equals("*") || 
    			fileName.substring(i, i+1).equals("|") || fileName.substring(i, i+1).equals("\\") || fileName.substring(i, i+1).equals("/") || 
    			fileName.substring(i, i+1).equals("?") || fileName.substring(i, i+1).equals(":") || fileName.substring(i, i+1).equals("<") || 
    			fileName.substring(i, i+1).equals(">")) {
    			x = x + "_";
    		} else {    		
    			x = x + fileName.substring(i, i+1); 
    		}
    	}
    	
    	return x;
    }

    public static String generateRandomName() {
    	return generateRandomName ("", "");
    }
    
    public static String generateRandomName(String extension) {
    	return generateRandomName ("", extension);
    }
    
    public static String generateRandomName(String header, String extension) {
    	String result = (header == null ? "" : header.trim());
    	
    	Random randomGenerator = new Random();

    	for (int i = 1; i < 10; i++) {
    		int r = randomGenerator.nextInt(25) + 65;
    		
    		result += (char)r;
    	}
    	
    	result += (extension == null || extension.trim().equals("") ? ".tmp" : 
    		        (extension.trim().startsWith(".") ? extension.trim() : "." + extension.trim()));
    	
    	return result;
    }
    
	public static File getTempFile(Context context) {
		return getTempFile (context, "", "", "");	
	}
	
	public static File getTempFile(Context context, String directory, String extension) {
		return getTempFile (context, directory, "", extension);	
	}
	
	public static File getTempFile(Context context, String directory, String header, String extension){
		File path = new File (Environment.getExternalStorageDirectory(), 
				(directory == null || directory.equals("") ? "temp" : directory));
		
		if (!path.exists()) {
			path.mkdir();
		}
		
		return new File(path, generateRandomName(header, extension));
	}

	public static void emptyFolder(File folder) {
		if (folder != null) {		
			if (folder.exists()) {
				File[] files = folder.listFiles();

				if (files != null && files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						if (files[i].exists())
							files[i].delete();
					}
				}
			}
		}
	}	

	public static void deleteFolder(File folder) {
		if (folder != null) {
			emptyFolder(folder);
			
			folder.delete();
		}
	}
	
	public static void deleteCache(Context context) {
	    try {
	        File dir = context.getCacheDir();
	        if (dir != null && dir.isDirectory()) {
	            deleteDir(dir);
	        }
	    } catch (Exception e) {}
	}

	private static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }
	    return dir.delete();
	}
}    
