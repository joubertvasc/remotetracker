package jv.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.InputFilter;
import android.text.Spanned;

public class DateTime {

	public static long getLongDiffTime(Date minorTime, Date greaterTime) {
		long diff = greaterTime.getTime() - minorTime.getTime();
		long timeInSeconds = diff / 1000;
		long hours = timeInSeconds / 3600;
		timeInSeconds = timeInSeconds - (hours * 3600);
		long minutes = timeInSeconds / 60;
		timeInSeconds = timeInSeconds - (minutes * 60);

		return timeInSeconds;		
	}

	public static long getLongDiffTime(long minorTime, long greaterTime) {
		long diff = greaterTime - minorTime;
		long timeInSeconds = diff / 1000;
		long hours = timeInSeconds / 3600;
		timeInSeconds = timeInSeconds - (hours * 3600);
		long minutes = timeInSeconds / 60;
		timeInSeconds = timeInSeconds - (minutes * 60);

		return timeInSeconds;		
	}

	public static String getStringDiffTime(Date minorTime, Date greaterTime) {
		return Format.secondsToStringTime(getLongDiffTime(minorTime, greaterTime));
	}

	public static String formatDateTime (Date dateTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()); 

		return dateFormat.format(dateTime);
	}

	public static String formatDateTimeNoMask (Date dateTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()); 

		return dateFormat.format(dateTime);
	}

	public static String formatDate (Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); 

		return dateFormat.format(date);
	}

	public static String formatTime (int time) {
		String h = "0000" + String.valueOf(time);
		h = h.substring(h.length()-4, h.length());
		
		int hora = Integer.valueOf(h.substring(0, 2));
		int min = Integer.valueOf(h.substring(2, 4));
		
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.set(Calendar.HOUR_OF_DAY, hora);			
		calendar.set(Calendar.MINUTE, min);			
		calendar.set(Calendar.SECOND, 0);
		
		return formatTime(calendar.getTime());
	}
	
	public static String formatTime (Date time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()); 

		return dateFormat.format(time);
	}

	public static String currentDateTime () {
		Date data = Calendar.getInstance().getTime();

		return formatDateTime(data);
	}

	public static String currentDate () {
		Date data = Calendar.getInstance().getTime();

		return formatDate(data);
	}

	public static String currentTime () {
		Date data = Calendar.getInstance().getTime();

		return formatTime(data);
	}

	public static boolean DateIsValid(String date) {
		if (date == null || date.length() != 10)
			return false;

		int mes = 0;
		int ano = 0;
		int dia = 0;

		try {
			dia = Integer.valueOf(date.substring(0, 2));
		} catch (Exception e) {
			return false;
		}

		try {
			mes = Integer.valueOf(date.substring(3, 5));
		} catch (Exception e) {
			return false;
		}

		try {
			ano = Integer.valueOf(date.substring(6, 10));
		} catch (Exception e) {
			return false;
		}

		if (dia < 1 || dia > 31)
			return false;

		if (mes < 1 || mes > 12)
			return false;

		if (mes == 2) {
			if (ano % 4 == 0 && ano % 100 != 0 && ano % 400 != 0) {
				if (dia > 29) 
					return false;
			} else if (dia > 28)
				return false;
		} else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
			if (dia > 30)
				return false;
		} else {
			if (dia > 31)
				return false;
		}

		return true;
	}

	public static InputFilter[] timeInputFilter() {
		InputFilter[] timeFilter = new InputFilter[1];
		timeFilter[0]   = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (source.length() == 0) {
					return null;// deleting, keep original editing
				}

				String result = "";
				result += dest.toString().substring(0, dstart);
				result += source.toString().substring(start, end);
				result += dest.toString().substring(dend, dest.length());

				if (result.length() > 5) {
					return "";// do not allow this edit
				}

				boolean allowEdit = true;
				char c;
				if (result.length() > 0) {
					c = result.charAt(0);
					allowEdit &= (c >= '0' && c <= '2' && !(Character.isLetter(c)));
				}
				if (result.length() > 1) {
					c = result.charAt(1);
					allowEdit &= (c >= '0' && c <= '9' && !(Character.isLetter(c)));
				}
				if (result.length() == 2 && Integer.valueOf(result) > 23) {
					allowEdit = false;
				}
				if (result.length() > 2) {
					c = result.charAt(2);
					allowEdit &= (c == ':'&&!(Character.isLetter(c)));
				}
				if (result.length() > 3) {
					c = result.charAt(3);
					allowEdit &= (c >= '0' && c <= '5' && !(Character.isLetter(c)));
				}
				if (result.length() > 4) {
					c = result.charAt(4);
					allowEdit &= (c >= '0' && c <= '9'&& !(Character.isLetter(c)));
				}
				return allowEdit ? null : "";
			}
		};

		return timeFilter;
	}

	public static InputFilter[] dateInputFilter() {
		InputFilter[] dateFilter = new InputFilter[1];
		dateFilter[0]   = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (source.length() == 0) {
					return null; // deleting, keep original editing
				}

				String result = "";
				result += dest.toString().substring(0, dstart);
				result += source.toString().substring(start, end);
				result += dest.toString().substring(dend, dest.length());

				if (result.length() > 10) {
					return ""; // do not allow this edit
				}

				boolean allowEdit = true;
				char c;
				if (result.length() > 0) {
					c = result.charAt(0);
					allowEdit &= (c >= '0' && c <= '3' && !(Character.isLetter(c)));
				}
				if (result.length() > 1) {
					c = result.charAt(1);
					allowEdit &= (c >= '0' && c <= '9' && !(Character.isLetter(c)));
				}
				if (result.length() == 2 && Integer.valueOf(result) > 31) {
					allowEdit = false;
				}
				if (result.length() > 2) {
					c = result.charAt(2);
					allowEdit &= (c == '/' && !(Character.isLetter(c)));
				}
				if (result.length() > 3) {
					c = result.charAt(3);
					allowEdit &= (c >= '0' && c <= '1' && !(Character.isLetter(c)));
				}
				if (result.length() > 4) {
					c = result.charAt(4);
					allowEdit &= (c >= '0' && c <= '9'&& !(Character.isLetter(c)));
				}
				if (result.length() == 5 && Integer.valueOf(result.substring(3)) > 12) {
					allowEdit = false;
				}
				if (result.length() > 5) {
					c = result.charAt(5);
					allowEdit &= (c == '/' && !(Character.isLetter(c)));
				}
				if (result.length() > 6) {
					c = result.charAt(6);
					allowEdit &= (c >= '2' && c <= '2' && !(Character.isLetter(c)));
				}
				if (result.length() > 7) {
					c = result.charAt(7);
					allowEdit &= (c >= '0' && c <= '9'&& !(Character.isLetter(c)));
				}
				if (result.length() > 8) {
					c = result.charAt(8);
					allowEdit &= (c >= '0' && c <= '9' && !(Character.isLetter(c)));
				}
				if (result.length() > 9) {
					c = result.charAt(9);
					allowEdit &= (c >= '0' && c <= '9'&& !(Character.isLetter(c)));
				}
				return allowEdit ? null : "";
			}
		};

		return dateFilter;
	}

	public static Date addDays(Date date, int days)	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); //minus number would decrement the days
		return cal.getTime();
	}

	public static Date addHours(Date time, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.HOUR, hours); //minus number would decrement the days
		return cal.getTime();
	}

	public static Date formatString(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
