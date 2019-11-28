package jv.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

public class Google {

	public final static String YELLOW_PUSHPIN = "#msn_ylw-pushpin";
	public final static String RED_PUSHPIN = "#msn_red-pushpin";
	public final static String GREEN_PUSHPIN = "#msn_grn-pushpin";
	public final static String CAMERA_PUSHPIN = "#m_prp-camera";

	private File _root;
	private FileOutputStream _out;
	private String _name;

	private void write(FileOutputStream out, String linha) {
		String l = linha + "\n";

		try {
			out.write(l.getBytes());
		} catch (Exception e) {
			;
		}
	}

	private void writeHeader(FileOutputStream out, String name) {
		write(out, "<?xml version=" + (char)34 + "1.0" + (char)34 + " encoding=" + (char)34 + "UTF-8" + (char)34 + "?>");
		write(out, "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">");
		//		write(out, "<kml xmlns=\"http://earth.google.com/kml/2.2\">");
		write(out, "  <Document>");
		write(out, "    <name>" + name + "</name>");
		write(out, "    <description>Grupo de coordenadas " + name + "</description>");

		write(out, "	<StyleMap id=" + (char)34 + "m_prp-camera" + (char)34 + ">");
		write(out, "		<Pair>");
		write(out, "			<key>normal</key>");
		write(out, "			<styleUrl>#s_prp-camera</styleUrl>");
		write(out, "		</Pair>");
		write(out, "		<Pair>");
		write(out, "			<key>highlight</key>");
		write(out, "			<styleUrl>#s_prp-camera_hl</styleUrl>");
		write(out, "		</Pair>");
		write(out, "	</StyleMap>");

		write(out, "		<StyleMap id=" + (char)34 + "msn_grn-pushpin" + (char)34 + ">");
		write(out, "		<Pair>");
		write(out, "			<key>normal</key>");
		write(out, "			<styleUrl>#sn_grn-pushpin</styleUrl>");
		write(out, "		</Pair>");
		write(out, "		<Pair>");
		write(out, "			<key>highlight</key>");
		write(out, "			<styleUrl>#sh_grn-pushpin</styleUrl>");
		write(out, "		</Pair>");
		write(out, "	</StyleMap>");

		write(out, "	<StyleMap id=" + (char)34 + "msn_red-pushpin" + (char)34 + ">");
		write(out, "		<Pair>");
		write(out, "			<key>normal</key>");
		write(out, "			<styleUrl>#sn_red-pushpin</styleUrl>");
		write(out, "		</Pair>");
		write(out, "		<Pair>");
		write(out, "			<key>highlight</key>");
		write(out, "			<styleUrl>#sh_red-pushpin</styleUrl>");
		write(out, "		</Pair>");
		write(out, "	</StyleMap>");

		write(out, "	<Style id=" + (char)34 + "sh_grn-pushpin" + (char)34 + ">");
		write(out, "		<IconStyle>");
		write(out, "			<scale>1.3</scale>");
		write(out, "			<Icon>");
		write(out, "				<href>http://maps.google.com/mapfiles/kml/pushpin/grn-pushpin.png</href>");
		write(out, "			</Icon>");
		write(out, "			<hotSpot x=" + (char)34 + "20" + (char)34 + " y=" + (char)34 + "2" + (char)34 + " xunits=" + (char)34 + "pixels" + (char)34 + " yunits=" + (char)34 + "pixels" + (char)34 + "/>");
		write(out, "		</IconStyle>");
		write(out, "	</Style>");

		write(out, "	<Style id=" + (char)34 + "sn_red-pushpin" + (char)34 + ">");
		write(out, "		<IconStyle>");
		write(out, "			<scale>1.1</scale>");
		write(out, "			<Icon>");
		write(out, "				<href>http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png</href>");
		write(out, "			</Icon>");
		write(out, "			<hotSpot x=" + (char)34 + "20" + (char)34 + " y=" + (char)34 + "2" + (char)34 + " xunits=" + (char)34 + "pixels" + (char)34 + " yunits=" + (char)34 + "pixels" + (char)34 + "/>");
		write(out, "		</IconStyle>");
		write(out, "	</Style>");

		write(out, "	<Style id=" + (char)34 + "yellowLineGreenPoly" + (char)34 + ">");
		write(out, "		<LineStyle>");
		write(out, "			<color>7f00ffff</color>");
		write(out, "			<width>4</width>");
		write(out, "		</LineStyle>");
		write(out, "		<PolyStyle>");
		write(out, "			<color>7f00ff00</color>");
		write(out, "		</PolyStyle>");
		write(out, "	</Style>");

		write(out, "	<Style id=" + (char)34 + "sh_red-pushpin" + (char)34 + ">");
		write(out, "		<IconStyle>");
		write(out, "			<scale>1.3</scale>");
		write(out, "			<Icon>");
		write(out, "				<href>http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png</href>");
		write(out, "			</Icon>");
		write(out, "			<hotSpot x=" + (char)34 + "20" + (char)34 + " y=" + (char)34 + "2" + (char)34 + " xunits=" + (char)34 + "pixels" + (char)34 + " yunits=" + (char)34 + "pixels" + (char)34 + "/>");
		write(out, "		</IconStyle>");
		write(out, "	</Style>");

		write(out, "	<Style id=" + (char)34 + "sn_grn-pushpin" + (char)34 + ">");
		write(out, "		<IconStyle>");
		write(out, "			<scale>1.1</scale>");
		write(out, "			<Icon>");
		write(out, "				<href>http://maps.google.com/mapfiles/kml/pushpin/grn-pushpin.png</href>");
		write(out, "			</Icon>");
		write(out, "			<hotSpot x=" + (char)34 + "20" + (char)34 + " y=" + (char)34 + "2" + (char)34 + " xunits=" + (char)34 + "pixels" + (char)34 + " yunits=" + (char)34 + "pixels" + (char)34 + "/>");
		write(out, "		</IconStyle>");
		write(out, "	</Style>");

		write(out, "	<Style id=" + (char)34 + "s_prp-camera" + (char)34 + ">");
		write(out, "		<IconStyle>");
		write(out, "			<scale>1.2</scale>");
		write(out, "			<Icon>");
		write(out, "				<href>http://maps.google.com/mapfiles/kml/shapes/camera.png</href>");
		write(out, "			</Icon>");
		write(out, "			<hotSpot x=" + (char)34 + "0.5" + (char)34 + " y=" + (char)34 + "0" + (char)34 + " xunits=" + (char)34 + "fraction" + (char)34 + " yunits=" + (char)34 + "fraction" + (char)34 + "/>");
		write(out, "		</IconStyle>");
		write(out, "	</Style>");

		write(out, "	<Style id=" + (char)34 + "s_prp-camera_hl" + (char)34 + ">");
		write(out, "		<IconStyle>");
		write(out, "			<scale>1.4</scale>");
		write(out, "			<Icon>");
		write(out, "				<href>http://maps.google.com/mapfiles/kml/shapes/camera.png</href>");
		write(out, "			</Icon>");
		write(out, "			<hotSpot x=" + (char)34 + "0.5" + (char)34 + " y=" + (char)34 + "0" + (char)34 + " xunits=" + (char)34 + "fraction" + (char)34 + " yunits=" + (char)34 + "fraction" + (char)34 + "/>");
		write(out, "		</IconStyle>");
		write(out, "	</Style>");
	}

	private void writeHeaderLine(FileOutputStream out, String name) {
		write(out, "    <Placemark>");
		write(out, "      <name>" + name + "</name>");
		write(out, "      <description>" + name + "</description>");
		write(out, "      <styleUrl>#yellowLineGreenPoly</styleUrl>");
		write(out, "      <LineString>");
		write(out, "        <extrude>1</extrude>");
		write(out, "        <gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>");
		write(out, "        <coordinates>");
	}

	private void writeFooterLine(FileOutputStream out) {
		write(out, "        </coordinates>");
		write(out, "      </LineString>");
		write(out, "    </Placemark>");
	}

	private void writeFooter(FileOutputStream out) {
		write(out, "  </Document>");
		write(out, "</kml>");
	}

	private void writePoint(FileOutputStream out, Coordinates c, String pushpinName) {
		write(out, "<Placemark>");
		write(out, "  <name>" + c.getName() + "</name>");

		if (!c.getPicture().trim().equals("") || !c.getExtraDescription().trim().equals("")) 
			write(out, "  <description>");			

		if (!c.getPicture().trim().equals("")) {
			write(out, "  <![CDATA[<img src=" + (char)34 + c.getPicture().toLowerCase(Locale.getDefault()) + (char)34 + "/>]]>");

			if (!c.getExtraDescription().trim().equals("")) {
				write (out, "<br />");			
				write (out, "<br />");			
			}
		}		

		if (!c.getExtraDescription().trim().equals("")) {
			String[] lines = c.getExtraDescription().trim().split("\n");

			for (int i = 0; i < lines.length; i++) 
				write (out, lines[i].trim() + "<br />");
		}

		if (!c.getPicture().trim().equals("") || !c.getExtraDescription().trim().equals(""))
			write(out, "  </description>");

		write(out, "  <LookAt>");
		write(out, "    <longitude>" + Double.toString(c.getLongitude())+ "</longitude>");
		write(out, "    <latitude>" + Double.toString(c.getLatitude()) + "</latitude>");
		write(out, "    <altitude>" + Double.toString(c.getAltitude())+ "</altitude>");
		write(out, "    <range>100</range>");
		write(out, "    <tilt>0</tilt>");
		write(out, "    <heading>0</heading>");
		write(out, "    <altitudeMode>relativeToGround</altitudeMode>");
		write(out, "    <gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>");

		write(out, "  </LookAt>");
		write(out, "  <styleUrl>" + pushpinName + "</styleUrl>");
		write(out, "  <Point>");
		write(out, "    <coordinates>" + Double.toString(c.getLongitude()) + "," + 
				Double.toString(c.getLatitude()) + "," +
				Double.toString(c.getAltitude()) + "</coordinates>");
		write(out, "  </Point>");
		write(out, "</Placemark>");
	}

	private void writePath(FileOutputStream out, Coordinates c) {
		write(out, Double.toString(c.getLongitude()) + "," + 
				Double.toString(c.getLatitude()) + "," +
				Double.toString(c.getAltitude()) + "\n");
	}

	public boolean simpleWritePointsToKML (File file, Coordinates[] coordinates) {
		if (file != null) {
			try {
				if (file.exists())
					file.delete();

				FileOutputStream out = new FileOutputStream(file);

				writeHeader(out, file.getName());

				for (int i = 0; i < coordinates.length; i++) {
					if (coordinates[i] != null) 
						writePoint(out, coordinates[i], 
								(coordinates[i].getPicture().trim().equals("") ? (coordinates[i].getPin().equals("") ? YELLOW_PUSHPIN : coordinates[i].getPin()) : CAMERA_PUSHPIN));
				}

				writeFooter(out);

				out.flush();
				out.close();	
				return true;

			} catch (Exception e) {
				return false;
			}	
		} else {
			return false;
		}
	}

	public boolean simpleWritePointsToKML(String name, Coordinates[] coordinates) {
		try {
			// Abre o arquivo para escrita ou cria se não existir
			File root = Environment.getExternalStorageDirectory();
			File file = new File (root, name);

			return simpleWritePointsToKML (file, coordinates);
		} catch (Exception e) {
			return false;
		}	
	}

	public boolean simpleWritePointsToKML(File file, Coordinates[] points, Coordinates[] path) {
		if (file != null) {
			try {
				if (file.exists())
					file.delete();

				FileOutputStream out = new FileOutputStream(file);

				writeHeader(out, file.getName());

				for (int i = 0; i < points.length; i++) {
					if (points[i] != null) 
						writePoint(out, points[i], YELLOW_PUSHPIN);
				}

				writeHeaderLine(out, file.getName());

				for (int i = 0; i < path.length; i++) {
					if (path[i] != null) 
						writePath(out, path[i]);
				}

				writeFooterLine(out);
				writeFooter(out);

				out.flush();
				out.close();	
				return true;

			} catch (Exception e) {
				return false;
			}	
		} else {
			return false;
		}
	}

	public boolean simpleWritePointsToKML(String name, Coordinates[] points, Coordinates[] path) {
		try {
			// Abre o arquivo para escrita ou cria se não existir
			File root = Environment.getExternalStorageDirectory();
			File file = new File (root, name);

			return simpleWritePointsToKML(file, points, path);
		} catch (Exception e) {
			return false;
		}	
	}

	public boolean simpleWritePathToKML(File file, Coordinates[] coordinates) {
		if (file != null) {
			try {
				// Abre o arquivo para escrita ou cria se não existir
				if (file.exists())
					file.delete();

				FileOutputStream out = new FileOutputStream(file);

				writeHeader(out, file.getName());
				writeHeaderLine(out, file.getName());

				for (int i = 0; i < coordinates.length; i++) {
					if (coordinates[i] != null)
						writePath(out, coordinates[i]);
				}

				writeFooterLine(out);
				writeFooter(out);

				out.flush();
				out.close();	

				return true;
			} catch (Exception e) {
				return false;
			}	
		} else {
			return false;			
		}
	}

	public boolean simpleWritePathToKML(String name, Coordinates[] coordinates) {
		try {
			// Abre o arquivo para escrita ou cria se não existir
			File root = Environment.getExternalStorageDirectory();
			File file = new File (root, name);

			return simpleWritePathToKML(file, coordinates);
		} catch (Exception e) {
			return false;
		}	
	}

	public String googleMapsURL(double latitude, double longitude) {
		return "http://www.google.com/maps?q=" + latitude + "," + longitude;
	}

	public boolean startNewKML(File file) {
		if (file == null) 
			return false;

		try {
			if (file.exists())
				file.delete();

			_out = new FileOutputStream(file);

			writeHeader(_out, file.getName());

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean startNewKML(String name) {
		try {
			_name = name;

			if (_name.trim().equals(""))
				return false;
			else
			{			
				// Abre o arquivo para escrita ou cria se não existir
				_root = Environment.getExternalStorageDirectory();
				File file = new File (_root, _name);

				return startNewKML(file);
			}
		} catch (Exception e) {
			return false;
		}	
	}

	public boolean addPoints(Coordinates[] coordinates) {
		return addPoints(coordinates, YELLOW_PUSHPIN);
	}

	public boolean addPoints(Coordinates[] coordinates, String pushPin) {
		if (_out == null || coordinates == null)
			return false;
		else
		{
			for (int i = 0; i < coordinates.length; i++) {
				if (coordinates[i] != null)
					writePoint(_out, coordinates[i], (coordinates[i].getPicture().trim().equals("") ? pushPin : CAMERA_PUSHPIN));
			}
		}

		return true;
	}

	public boolean addPath(String name, Coordinates[] coordinates) {
		return addPath (name, coordinates, false, false);
	}

	public boolean addPath(String name, Coordinates[] coordinates, boolean startIcon, boolean endIcon) {
		return addPath(name, coordinates, startIcon, endIcon, GREEN_PUSHPIN, RED_PUSHPIN);
	}

	public boolean addPath(String name, Coordinates[] coordinates, boolean startIcon, boolean endIcon, String startPushPin, String endPushPin) {
		if (_out == null || name.equals("") || coordinates == null)
			return false;
		else
		{
			if (startIcon && coordinates != null && coordinates.length > 0)
				writePoint(_out, coordinates[0], startPushPin); //

			writeHeaderLine(_out, name);

			for (int i = 0; i < coordinates.length; i++) {
				if (coordinates[i] != null)
					writePath(_out, coordinates[i]);
			}

			writeFooterLine(_out);

			if (endIcon && coordinates != null && coordinates.length > 0)
				writePoint(_out, coordinates[coordinates.length - 1], endPushPin);   //
		}

		return true;
	}

	public boolean finishKML() {
		if (_out == null)
			return false;
		else
		{    	
			try {
				writeFooter(_out);

				_out.flush();
				_out.close();	
				return true;

			} catch (Exception e) {
				return false;
			}
		}
	}

	public static boolean isGoogleEarthInstalled(Context context) {
		File file = new File("xxx.kmz");

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.google-earth.kmz");

		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

		return apps.size() > 0;
	}

	public static void openGoogleEarthFile(Context context, String fileName) {
		if (fileName != null && !fileName.trim().equals("")) {
			File file = new File(fileName);

			if (file.exists()) {			
				Intent intent = new Intent(Intent.ACTION_VIEW);

				int pos = file.getName().lastIndexOf(".");
				if (pos > 0) {		
					String extension = file.getName().substring(pos+1);

					if (extension.equalsIgnoreCase("kmz")) {
						intent.setDataAndType(Uri.fromFile(file), "application/vnd.google-earth.kmz");
					} else if (extension.equalsIgnoreCase("kml")) {
						intent.setDataAndType(Uri.fromFile(file), "application/vnd.google-earth.kml+xml");
					} else {
						Message.showMessage(context, context.getString(R.string.aviso), context.getString(R.string.avArquivoNaoKMZ));
						return;
					}

					PackageManager pm = context.getPackageManager();
					List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

					if (apps.size() > 0) {
						try {
							context.startActivity(intent);
						} catch (Exception e) {
							Message.showMessage(context, context.getString(R.string.aviso), context.getString(R.string.avErroAbrindoKMZ));
							Logs.errorLog("Erro abrindo arquivo KMZ/KML", e);
						}
					} else { 
						Message.showMessage(context, context.getString(R.string.aviso), context.getString(R.string.avGoogleEarthNaoInstalado));
					}
				} else {
					Message.showMessage(context, context.getString(R.string.aviso), context.getString(R.string.avArquivoNaoKMZ));
				}
			} else {
				Message.showMessage(context, context.getString(R.string.aviso), context.getString(R.string.avArquivoNaoExiste));
			}
		} else {
			Message.showMessage(context, context.getString(R.string.aviso), context.getString(R.string.avArquivoNaoKMZ));
		}
	} /**/

	public static boolean isStoreVersion(Context context) {
		boolean result = false;

		try {
			String installer = context.getPackageManager()
					.getInstallerPackageName(context.getPackageName());
			result = !TextUtils.isEmpty(installer);
		} catch (Throwable e) {          
		}

		return result;
	}

	public static boolean isDebugDevice(Context context) {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/jvsoftware.txt");
		return file.exists() || DeviceUtils.isEmmulator();
	}
}

