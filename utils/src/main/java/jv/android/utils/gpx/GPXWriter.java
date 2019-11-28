package jv.android.utils.gpx;

import java.io.File;
import java.io.FileOutputStream;

import jv.android.utils.Coordinates;

public class GPXWriter {

	private String fileName;
	private File file;
	private FileOutputStream fileOutputStream;
	private int trackNumber;

	private void write(String linha) {
		String l = linha + "\n";

		try
		{
			fileOutputStream.write(l.getBytes());
		} catch (Exception e) {

		}
	}

	private void writeHeader(String name, String description) {
		write ("<?xml version=\"1.0\" standalone=\"yes\"?>");
		write ("<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"KML2GPX.COM\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");
		write ("  <metadata>");
		write ("          <name><![CDATA[" + name + "]]></name>");
		write ("          <desc><![CDATA[" + description + "]]></desc>");
		write ("  </metadata>");
	}

	private void writeFooter() {
		write ("</gpx>");
	}
	
	private void writePoint(String name, double lat, double lon) {
		write ("    <wpt lat=\"" + Double.toString(lat) + "\" lon=\"" + Double.toString(lon) + "\">");
		write ("      <ele>0</ele>");
		write ("      <name><![CDATA[" + name + "]]></name>");
		write ("    </wpt>");
	}
	
	private void writePoint(Coordinates c) {
		writePoint (c.getName(), c.getLatitude(), c.getLongitude());
	}
	
	private void writePath(String name, String description, Coordinates[] coordinates) {
		if (coordinates != null) {		
			write ("    <trk>");
			
			if (name != null && !name.trim().equals("")) {
				write ("      <name><![CDATA[" + name + "]]></name>");
			}
			
			if (description != null && !description.trim().equals("")) {
				write ("      <desc><![CDATA[" + description + "]]></desc>");
			}
			
			write ("      <number>" + String.valueOf(trackNumber) + "</number>");
			write ("      <trkseg>");

			for (int i = 0; i < coordinates.length; i++) {
                write ("        <trkpt lat=\"" + Double.toString(coordinates[i].getLatitude()) + "\" lon=\"" + Double.toString(coordinates[i].getLongitude()) + "\">");
                write ("          <ele>" + String.valueOf(i) + "</ele>");
                write ("          <name>1</name>");
                write ("        </trkpt>");
			}

			write ("      </trkseg>");
			write ("    </trk>");
		}
	}

	public boolean start(String fileName, String gpxName, String gpxDescription) {
		this.fileName = fileName;

		trackNumber = 1;
		
		try {
			if (this.fileName.trim().equals(""))
				return false;
			else
			{	
				file = new File (this.fileName);

				if (file.exists())
					file.delete();

				fileOutputStream = new FileOutputStream(file);

				writeHeader(gpxName, gpxDescription);

				return true;
			}
		} catch (Exception e) {
			return false;
		}		
	}

	public boolean finish() {
		if (fileOutputStream == null)
			return false;
		else
		{    	
			try {
				writeFooter();

				fileOutputStream.flush();
				fileOutputStream.close();	
				return true;

			} catch (Exception e) {
				return false;
			}
		}	
	}

	public boolean addPoints (Coordinates[] coordinates) {
		if (coordinates != null) {
			for (int i = 0; i < coordinates.length; i++) {
				writePoint(coordinates[i]);
			}
			
			return true;
		} else {
			return false;
		}
	}

    public boolean addPaths (String name, Coordinates[] coordinates) {
    	try {
    		writePath(name, "", coordinates);
    		return true;
    	} catch (Exception e) {
    		return false;
    	}
    }

    public boolean addPaths (String name, Coordinates[] coordinates, boolean startIcon, boolean endIcon) {
    	try {
    		if (coordinates == null || coordinates.length == 0) {
    			return false;
    		} else {
    			if (startIcon)
    				writePoint (coordinates[0]);

    			if (addPaths(name, coordinates)) {
    				if (endIcon) {
    					writePoint(coordinates[coordinates.length - 1]);	
    				}    				
    				
        			return true;
    			} else {
    				return false;
    			}
    			
    		}
    	} catch (Exception e) {
    		return false;
    	}
    }
}
