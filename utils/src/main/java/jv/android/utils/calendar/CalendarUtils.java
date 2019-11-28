package jv.android.utils.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jv.android.utils.R;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;

public class CalendarUtils {

	public static String dayOfWeek(Context context) {
		Calendar calendar = Calendar.getInstance();
		
		return dayOfWeek(context, calendar);
	}
	
	public static String dayOfWeek(Context context, Calendar calendar) {
		int day = calendar.get(Calendar.DAY_OF_WEEK); 

		switch (day) {
		    case Calendar.SUNDAY:
		    	return context.getString(R.string.sunday);
		    case Calendar.MONDAY:
		    	return context.getString(R.string.monday);
		    case Calendar.TUESDAY:
		    	return context.getString(R.string.tuesday);
		    case Calendar.WEDNESDAY:
		    	return context.getString(R.string.wednesday);
		    case Calendar.THURSDAY:
		    	return context.getString(R.string.thursday);
		    case Calendar.FRIDAY:
		    	return context.getString(R.string.friday);
		    case Calendar.SATURDAY:
		    	return context.getString(R.string.saturday);
		}
		
		return "";
	}
	
	public static List<CalendarName> getAllCalendars(Context context) {
		List<CalendarName> calendars = new ArrayList<CalendarName>();

		Cursor cursor =
				context.getContentResolver().
				query(Calendars.CONTENT_URI, 
						new String[] {Calendars._ID, Calendars.NAME, Calendars.ACCOUNT_NAME},
						Calendars.VISIBLE + " = 1",
						null,
						Calendars._ID + " ASC");

		if (cursor != null) {
			cursor.moveToFirst();

			while (cursor.moveToNext()) {
				CalendarName c = new CalendarName();

				c.setId(cursor.getLong(0));
				c.setName(cursor.getString(1));
				c.setAccount_name(cursor.getString(2));

				calendars.add(c);
			}
		}

		return calendars;
	}

	public static int findCalendarById(List<CalendarName> calendars, long id) {
		if (calendars == null || calendars.size() == 0 || id < 0)
			return -1;

		for (int i = 0; i < calendars.size(); i++) {
			if (calendars.get(i).getId() == id)
				return i;
		}

		return -1;
	}

	public static int findCalendarByName(List<CalendarName> calendars, String name) {
		if (calendars == null || calendars.size() == 0 || name == null || name.trim().equals(""))
			return -1;

		for (int i = 0; i < calendars.size(); i++) {
			if (calendars.get(i).getName().trim().equalsIgnoreCase(name.trim()))
				return i;
		}

		return -1;
	}

	public static long insertEvent(Context context, CalendarEvent calendarEvent) {
		try {
			if (calendarEvent != null && calendarEvent.getCalendarId() != -1 && calendarEvent.getStart() != null && calendarEvent.getEnd() != null) {
				ContentResolver cr = context.getContentResolver();
				ContentValues values = new ContentValues();
				values.put(CalendarContract.Events.CALENDAR_ID, calendarEvent.getCalendarId());

				values.put(CalendarContract.Events.DTSTART, calendarEvent.getStart().getTime());
				values.put(CalendarContract.Events.DTEND, calendarEvent.getEnd().getTime());
				values.put(CalendarContract.Events.ALL_DAY, calendarEvent.isAllDay() ? 1 : 0);
				values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, calendarEvent.isGuestCanInvite() ? 1 : 0);
				values.put(CalendarContract.Events.GUESTS_CAN_MODIFY, calendarEvent.isGuestCanModify() ? 1 : 0);
				values.put(CalendarContract.Events.AVAILABILITY, calendarEvent.getAvailability());

				if (calendarEvent.getTitle() != null && !calendarEvent.getTitle().trim().equals(""))
					values.put(CalendarContract.Events.TITLE, calendarEvent.getTitle());

				if (calendarEvent.getDescription() != null && !calendarEvent.getDescription().trim().equals(""))
					values.put(CalendarContract.Events.DESCRIPTION, calendarEvent.getDescription());

				if (calendarEvent.getColorId() != 0) {
					values.put(CalendarContract.Events.EVENT_COLOR, calendarEvent.getColorId());
					values.put(CalendarContract.Events.EVENT_COLOR_KEY, calendarEvent.getColorKey());					
				}

				if (calendarEvent.getEventZone() != null && !calendarEvent.getEventZone().trim().equals(""))
					values.put(CalendarContract.Events.EVENT_TIMEZONE, calendarEvent.getEventZone());

				if (calendarEvent.getRrule() != null && !calendarEvent.getRrule().trim().equals(""))
					values.put(CalendarContract.Events.RRULE, calendarEvent.getRrule().trim());

				Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
				long eventId = Long.valueOf(uri.getLastPathSegment()); //new Long(uri.getLastPathSegment());

				if (calendarEvent.getReminders().size() > 0) {
					for (int i = 0; i < calendarEvent.getReminders().size(); i++) {
						values.clear();
						values.put(CalendarContract.Reminders.EVENT_ID, eventId);
						values.put(CalendarContract.Reminders.METHOD, calendarEvent.getReminders().get(i).getMethod());
						values.put(CalendarContract.Reminders.MINUTES, calendarEvent.getReminders().get(i).getMinutes());
						context.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, values);
					}
				}
				
				return eventId;
			} else {
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}

	public static long updateEvent(Context context, CalendarEvent calendarEvent) {
		if (deleteEvent(context, calendarEvent.getEventId()) > 0) {
			return insertEvent(context, calendarEvent);			
		}
		
		return -1;
	}

	public static List<CalendarEvent> readAllEvents(Context context, long calendarId) {
		Calendar calendar = Calendar.getInstance();
		
		List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
		
		String[] proj =
				new String[] {
						CalendarContract.Events.CALENDAR_ID,
						CalendarContract.Events._ID,
						CalendarContract.Events.DTSTART,
						CalendarContract.Events.DTEND,
						CalendarContract.Events.ALL_DAY,
						CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS,
						CalendarContract.Events.GUESTS_CAN_MODIFY,
						CalendarContract.Events.AVAILABILITY,
						CalendarContract.Events.TITLE,
						CalendarContract.Events.DESCRIPTION,
						CalendarContract.Events.EVENT_COLOR,
						CalendarContract.Events.EVENT_COLOR_KEY,
						CalendarContract.Events.EVENT_TIMEZONE,
						CalendarContract.Events.RRULE};
		Cursor cursor =	context.getContentResolver().query(CalendarContract.Events.CONTENT_URI,	proj,
				CalendarContract.Events.CALENDAR_ID + " = ? and (deleted != 1) ", new String[] { Long.toString(calendarId) },	null);
		
		while (cursor.moveToNext()) {
			CalendarEvent calendarEvent = new CalendarEvent();
			
			calendarEvent.setCalendarId(cursor.getLong(0));
			calendarEvent.setEventId(cursor.getLong(1));

			calendar.setTimeInMillis(cursor.getLong(2));
			calendarEvent.setStart(calendar.getTime());

			calendar.setTimeInMillis(cursor.getLong(3));
			calendarEvent.setEnd(calendar.getTime());
			
			calendarEvent.setAllDay(cursor.getInt(4) == 1);
			calendarEvent.setGuestCanInvite(cursor.getInt(5) == 1);
			calendarEvent.setGuestCanModify(cursor.getInt(6) == 1);
			calendarEvent.setAvailability(cursor.getInt(7));
			calendarEvent.setTitle(cursor.getString(8));
			calendarEvent.setDescription(cursor.getString(9));
			calendarEvent.setColorId(cursor.getInt(10));
			calendarEvent.setColorKey(cursor.getInt(11));
			calendarEvent.setEventZone(cursor.getString(12));
			calendarEvent.setRrule(cursor.getString(13));
			
			calendarEvents.add(calendarEvent);
		}

		return calendarEvents;
	}

	public static CalendarEvent readEvent(Context context, long eventId) {
		Calendar calendar = Calendar.getInstance();
		
		CalendarEvent calendarEvent = new CalendarEvent();

		long selectedEventId = eventId;
		String[] proj =
				new String[] {
						CalendarContract.Events.CALENDAR_ID,
						CalendarContract.Events._ID,
						CalendarContract.Events.DTSTART,
						CalendarContract.Events.DTEND,
						CalendarContract.Events.ALL_DAY,
						CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS,
						CalendarContract.Events.GUESTS_CAN_MODIFY,
						CalendarContract.Events.AVAILABILITY,
						CalendarContract.Events.TITLE,
						CalendarContract.Events.DESCRIPTION,
						CalendarContract.Events.EVENT_COLOR,
						CalendarContract.Events.EVENT_COLOR_KEY,
						CalendarContract.Events.EVENT_TIMEZONE,
						CalendarContract.Events.RRULE};
		Cursor cursor =	context.getContentResolver().query(CalendarContract.Events.CONTENT_URI,	proj,
				CalendarContract.Events._ID + " = ? and (deleted != 1) ", new String[] { Long.toString(selectedEventId) },	null);
		
		if (cursor.moveToFirst()) {
			calendarEvent.setCalendarId(cursor.getLong(0));
			calendarEvent.setEventId(cursor.getLong(1));

			calendar.setTimeInMillis(cursor.getLong(2));
			calendarEvent.setStart(calendar.getTime());

			calendar.setTimeInMillis(cursor.getLong(3));
			calendarEvent.setEnd(calendar.getTime());
			
			calendarEvent.setAllDay(cursor.getInt(4) == 1);
			calendarEvent.setGuestCanInvite(cursor.getInt(5) == 1);
			calendarEvent.setGuestCanModify(cursor.getInt(6) == 1);
			calendarEvent.setAvailability(cursor.getInt(7));
			calendarEvent.setTitle(cursor.getString(8));
			calendarEvent.setDescription(cursor.getString(9));
			calendarEvent.setColorId(cursor.getInt(10));
			calendarEvent.setColorKey(cursor.getInt(11));
			calendarEvent.setEventZone(cursor.getString(12));
			calendarEvent.setRrule(cursor.getString(13));
		}

		return calendarEvent;
	}

	public static int deleteEvent(Context context, long eventId) {
		String[] selArgs = new String[] { Long.toString(eventId) };

		int deleted = context.getContentResolver().
				delete(CalendarContract.Reminders.CONTENT_URI, CalendarContract.Reminders.EVENT_ID + " =? ", selArgs);
		
//		deleted = context.getContentResolver().
//				delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events._ID + " =? ", selArgs);
		
		Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
		deleted = context.getContentResolver().delete(deleteUri, null, null);		

		return deleted;
	}

}
