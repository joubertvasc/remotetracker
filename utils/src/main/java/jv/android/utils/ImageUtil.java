package jv.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;

public class ImageUtil {

	public static Bitmap decodeSampledBitmap(String arquivo, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(arquivo, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(arquivo, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static boolean copyFileReducingSize (File original, File to, int newWidth, int newHeight) {
		return copyFileReducingSize (original, to, newWidth, newHeight, Bitmap.CompressFormat.JPEG);
	}

	public static boolean copyFileReducingSize (File original, File to, int newWidth, int newHeight, Bitmap.CompressFormat format) {
		try {
			Bitmap b = ImageUtil.decodeSampledBitmap (original.getAbsolutePath(), newWidth, newHeight);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			b.compress(format, 40, bytes);

			to.createNewFile();

			FileOutputStream fo = new FileOutputStream(to);
			fo.write(bytes.toByteArray());

			// remember close de FileOutput
			fo.close();
			b.recycle();
			b = null;

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean setGetTag(String file, double latitude, double longitude) {
		try {
			ExifInterface exif = new ExifInterface(file);

			//String latitudeStr = "90/1,12/1,30/1";
			double lat = latitude;
			double alat = Math.abs(lat);
			String dms = Location.convert(alat, Location.FORMAT_SECONDS);
			String[] splits = dms.split(":");
			String[] secnds = (splits[2]).split("\\.");
			String seconds;

			if (secnds.length == 0) {
				seconds = splits[2];
			} else {
				seconds = secnds[0];
			}

			String latitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
			exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitudeStr);

			exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, lat > 0 ? "N" : "S");

			double lon = longitude;
			double alon = Math.abs(lon);

			dms = Location.convert(alon, Location.FORMAT_SECONDS);
			splits = dms.split(":");
			secnds = (splits[2]).split("\\.");

			if (secnds.length == 0)	{
				seconds = splits[2];
			} else {
				seconds = secnds[0];
			}

			String longitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";

			exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitudeStr);
			exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon> 0 ? "E" : "W");

			exif.saveAttributes();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Bitmap drawableToBitmap (Drawable drawable) {
		if (drawable != null) {
			if (drawable instanceof BitmapDrawable) {
				return ((BitmapDrawable)drawable).getBitmap();
			}

			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap); 
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);

			return bitmap;
		} else {
			return null;
		}
	}

	public static byte[] bitmapToByteArray(Bitmap.CompressFormat format, Bitmap bitmap) {
		if (bitmap != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bitmap.compress(format, 100, baos);

			return baos.toByteArray();
		} else {
			return null;
		}
	}

	public static byte[] drawableToByteArray(Drawable d) {
		return bitmapToByteArray(Bitmap.CompressFormat.JPEG, drawableToBitmap(d));
	}

	public static byte[] drawableToByteArray(Bitmap.CompressFormat format, Drawable d) {
		return bitmapToByteArray(format, drawableToBitmap(d));
	}

	public static Bitmap byteArrayToBitmap(byte[] bytes) {
		if (bytes != null && bytes.length > 0) {
			try {
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable (Bitmap image) {
		if (image == null) {
			return null;			
		} else {
			try {
				return new BitmapDrawable(image);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static Drawable byteArrayToDrawable(byte[] bytes) {
		return bitmapToDrawable(byteArrayToBitmap(bytes));
	}

	public static boolean saveBitmapToFile(String filename, Bitmap bmp) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(filename);
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}			
	}
	
	public static boolean saveDrawableToFile(String filename, Drawable drawable) {
		return saveBitmapToFile(filename, drawableToBitmap(drawable));
	}
}
