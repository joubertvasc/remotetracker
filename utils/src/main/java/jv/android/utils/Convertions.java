package jv.android.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Scanner;

public class Convertions {

	public static double mpmToKmH (double metersPerMinute) {
		return metersPerMinute * 3.6d;		
	}
	
	public static double kmHToMpm (double kmh) {
		return kmh / 3.6d;		
	}
	
	public static double mpmToMph (double metersPerMinute) {
		return metersPerMinute * 2.23693629d;		
	}
	
	public static double mphToMpm (double mph) {
		return mph / 2.23693629d;		
	}

	public static double meterToFeet (double meters) {
		return meters * 3.2808399d;
	}

	public static double feetToMeter (double feets) {
		return feets / 3.2808399d;
	}
	
	public static String doubleToString (double number) {
		String n = String.valueOf(number);
				
		DecimalFormat df = new DecimalFormat();
	    DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
	    
	    if (dfs.getDecimalSeparator() != '.') 
	    	n = n.replace('.', dfs.getDecimalSeparator());

	    return n;
	}
	
	public static double stringToDouble(String value) {
		value = value.replaceAll(",", ".");
		
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}

	public static String floatToString (float number) {
		String n = String.valueOf(number);

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();

		if (dfs.getDecimalSeparator() != '.')
			n = n.replace('.', dfs.getDecimalSeparator());

		return n;
	}

	public static float stringToFloat(String value) {
		value = value.replaceAll(",", ".");

		try {
			return Float.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
	public static double celicusToFahrenheit(double temp) {
		return (temp * 9 / 5.0) + 32;
	}

	public static double fahrenheitToCelcius(double temp) {
		return (temp - 32) * (5 / 9.0);
	}
}
