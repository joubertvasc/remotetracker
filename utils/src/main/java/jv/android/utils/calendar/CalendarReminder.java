package jv.android.utils.calendar;

import android.provider.CalendarContract;

public class CalendarReminder {

	private int method;
	private int minutes;
	
	public CalendarReminder() {
		method = CalendarContract.Reminders.METHOD_ALARM;
		minutes = 0;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
}
