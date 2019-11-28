package jv.android.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Format {

	private static final String accents = "áéíóúàèìòùâêîôûãñçäëïöüÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÑÇÄËÏÖÜ";
	private static final String normal  = "aeiouaeiouaeiouancaeiouAEIOUAEIOUAEIOUANCAEIOU";

	public static String commaToPoint(String value) {
		return value.replace(',', '.');		
	}

	public static String doubleToString(double value, int decimals) {
		String n = format(value, decimals);

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();

		if (dfs.getDecimalSeparator() != '.') 
			n = n.replace('.', dfs.getDecimalSeparator());

		return n;
	}

	public static String doubleToString(double value) {
		String n = format(value);

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();

		if (dfs.getDecimalSeparator() != '.') 
			n = n.replace('.', dfs.getDecimalSeparator());

		return n;
	}

	public static String floatToString(float value, int decimals) {
		String n = format(value, decimals);

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();

		if (dfs.getDecimalSeparator() != '.') 
			n = n.replace('.', dfs.getDecimalSeparator());

		return n;
	}

	public static String floatToString(float value) {
		String n = format(value);

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();

		if (dfs.getDecimalSeparator() != '.') 
			n = n.replace('.', dfs.getDecimalSeparator());

		return n;
	}

	public static String longToString(long value, int decimals) {
		String n = format(value, decimals);

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();

		if (dfs.getDecimalSeparator() != '.')
			n = n.replace('.', dfs.getDecimalSeparator());

		return n;
	}

	public static String longToString(long value) {
		String n = format(value);

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();

		if (dfs.getDecimalSeparator() != '.')
			n = n.replace('.', dfs.getDecimalSeparator());

		return n;
	}

	public static String zeroFill(String number, int length) {
		String r = number;

		for (int i = r.length(); i < length; i++) {
			r = "0" + r;
		}		

		return r;
	}

	public static String zeroFill(int number, int length) {
		String r = String.valueOf(number);

		for (int i = r.length(); i < length; i++) {
			r = "0" + r;
		}		

		return r;
	}

	public static String format(double x) {   
		return String.format("%.2f", x);   
	}

	public static String format(float x) {   
		return String.format("%.2f", x);   
	}

	public static String format(double x, int decimals) {   
		return String.format("%." + String.valueOf(decimals) + "f", x);   
	}

	public static String format(float x, int decimals) {   
		return String.format("%." + String.valueOf(decimals) + "f", x);   
	}

	public static String formatDate (Date date) {
		if (date == null || date.equals(""))
			return "";
		else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 

			return dateFormat.format(date);
		}
	}

	public static String formatTime (Date date) {
		if (date == null || date.equals(""))
			return "";
		else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); 

			return dateFormat.format(date);
		}
	}

	public static String formatDateTime (Date date) {
		if (date == null || date.equals(""))
			return "";
		else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 

			return dateFormat.format(date);
		}
	}

	public static String formatDateTimeOnlyDigits (Date date) {
		if (date == null || date.equals(""))
			return "";
		else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

			return dateFormat.format(date);
		}
	}

	public static String removeAccents (String phrase) {
		String result = "";

		if (phrase != null && !phrase.equals("")) {
			for (int i=0; i < phrase.length(); i++) {
				int x = accents.indexOf(phrase.substring(i,  i+1));

				if (x == -1)
					result += phrase.substring(i, i+1);
				else
					result += normal.substring(x, x+1);
			}
		}

		return result;
	}

	public static String removeSpace (String phrase) {
		String result = "";

		if (phrase != null && !phrase.equals("")) {
			for (int i=0; i < phrase.length(); i++) {
				if (!phrase.substring(i, i+1).equals(" "))
					result += phrase.substring(i, i+1);
			}
		}

		return result;
	}

	public static String onlyDigits(String number, boolean inclubePlusSignal) {
		String result = "";

		if (number != null && !number.equals("")) {
			for (int i=0; i < number.length(); i++) {
				char[] c = number.substring(i, i+1).toCharArray();

				if (((int)c[0] >= 48 && (int)c[0] <= 71) || (inclubePlusSignal && number.substring(i, i+1).equals("+")))
					result += number.substring(i, i+1);
			}			
		}

		return result;
	}

	public static String onlyDigits(String number) {
		return onlyDigits (number, false);
	}

	public static String onlyLetters(String text) {
		String result = "";

		if (text != null && !text.equals("")) {
			for (int i=0; i < text.length(); i++) {
				char[] c = text.substring(i, i+1).toUpperCase().toCharArray();

				if ((int)c[0] >= 65 && (int)c[0] <= 90)
					result += text.substring(i, i+1);
			}			
		}

		return result;
	}

	public static String secondsToStringTime(int seconds) {

		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;

		return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
	}

	public static String secondsToStringTime(long seconds) {

		long hours = seconds / 3600;
		long minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;

		return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
	}

	public static String twoDigitString(int number) {

		if (number == 0) {
			return "00";
		}

		if (number / 10 == 0) {
			return "0" + number;
		}

		return String.valueOf(number);
	}	

	public static String twoDigitString(long number) {

		if (number == 0) {
			return "00";
		}

		if (number / 10 == 0) {
			return "0" + number;
		}

		return String.valueOf(number);
	}	

	public static String formatString(String text, String parameter) {
		MessageFormat msgF = new MessageFormat(text);
		Object[] args = {parameter};
		return msgF.format(args);
	}

	public static String capitalize(String text) {
		String result = "";
		String[] p = {"para", "do", "da", "de", "e", "das", "dos", "S/A", "of", "the", "a", "an", "to"};

		if (text != null && !text.trim().equals("")) {
			String[] words = text.split(" ");

			for (int i = 0; i < words.length; i++) {
				boolean isPrep = false;

				for (int j = 0; j < p.length; j++) {
					if (words[i].toLowerCase(Locale.getDefault()).equals(p[j])) {
						isPrep = true;
						result = result + p[j] + " ";
						break;
					}
				}

				if (!isPrep) {
					result = result +
							words[i].substring(0, 1).toUpperCase(Locale.getDefault()) +
							words[i].substring(1).toLowerCase(Locale.getDefault()) + " ";
				}
			}
		}

        return result.trim();
	}

	public static boolean isNumeric(String str) {
		try	{
			double d = Double.parseDouble(str);
		} catch(NumberFormatException nfe) {
			return false;
		}

		return true;
	}

	public static String stringTransform(String s, int i) {
		char[] chars = s.toCharArray();
		for(int j = 0; j<chars.length; j++)
			chars[j] = (char)(chars[j] ^ i);
		return String.valueOf(chars);
	}
}
