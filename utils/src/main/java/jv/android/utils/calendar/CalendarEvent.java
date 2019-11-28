// http://www.grokkingandroid.com/androids-calendarcontract-provider/
package jv.android.utils.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.provider.CalendarContract;

public class CalendarEvent {
	
	private long calendarId;
	private long eventId;
	private Date start;
	private Date end;
	private String title;
	private String description;
	private String eventZone;
	private String location;
	private int colorId;
	private int colorKey;
	private boolean allDay;
	private boolean guestCanInvite;
	private boolean guestCanModify;
	private String rrule;
	private int availability;
	private List<CalendarReminder> reminders;
	
	public CalendarEvent() {
		calendarId = 0;
		eventId = 0;
		start = new Date();
		end = new Date();
		title = "";
		description = "";
		location = "";
		eventZone = "UTC";
		colorId = 0;
		colorKey = 1;
		allDay = false;
		guestCanInvite = false;
		guestCanModify = false;
		rrule = "";
		availability = CalendarContract.Events.AVAILABILITY_FREE;
		reminders = new ArrayList<CalendarReminder>();
	}
	
	public void addReminder(CalendarReminder reminder) {
		reminders.add(reminder);
	}

	public long getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(long calendarId) {
		this.calendarId = calendarId;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventZone() {
		return eventZone;
	}

	public void setEventZone(String eventZone) {
		this.eventZone = eventZone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getColorId() {
		return colorId;
	}

	public void setColorId(int colorId) {
		this.colorId = colorId;
	}

	public int getColorKey() {
		return colorKey;
	}

	public void setColorKey(int colorKey) {
		this.colorKey = colorKey;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public String getRrule() {
		return rrule;
	}

	public void setRrule(String rrule) {
		this.rrule = rrule;
	}

	public boolean isGuestCanInvite() {
		return guestCanInvite;
	}

	public void setGuestCanInvite(boolean guestCanInvite) {
		this.guestCanInvite = guestCanInvite;
	}

	public boolean isGuestCanModify() {
		return guestCanModify;
	}

	public void setGuestCanModify(boolean guestCanModify) {
		this.guestCanModify = guestCanModify;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public List<CalendarReminder> getReminders() {
		return reminders;
	}

	public void setReminders(List<CalendarReminder> reminders) {
		this.reminders = reminders;
	}

}
