package jv.android.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class Schedule {
	private static final String CATEGORIA = "RT";
	Context mContext; 

	public Schedule (Context context) {
		mContext = context;	
	}
	
	// Agenda o alarme/serviço para executar daqui a X minutos
	public void sched(String intentName, int minutos, int tempoRepetir) {
		// Intent para disparar o broadcast
		Intent it = new Intent(intentName);
		PendingIntent p = PendingIntent.getBroadcast(mContext, 0, it, 0);
	
		// Para executar o alarme depois de x segundos a partir de agora
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.add(Calendar.MINUTE, minutos);
	
		// Agenda o alarme
		AlarmManager alarme = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		long time = c.getTimeInMillis();
		
		if (tempoRepetir == 0)
			alarme.set(AlarmManager.RTC_WAKEUP, time, p);
		else
			alarme.setRepeating(AlarmManager.RTC_WAKEUP, time, tempoRepetir, p);		
	
		Log.i(CATEGORIA, "Alarme agendado para daqui a " + minutos + " minutos.");
	
	}

	public static void cancelAlarm(Context context, Class<?> cls) {
		cancelAlarmId(context, cls, 0);
	}	

	public static void cancelAlarmId(Context context, Class<?> cls, int id) {
	    Intent alarm = new Intent(context, cls);
	    PendingIntent recurringAlarm = PendingIntent.getBroadcast(context, id, alarm, PendingIntent.FLAG_CANCEL_CURRENT);
	    
	    AlarmManager alarms = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	    alarms.cancel(recurringAlarm);
	}	

	public static void setRecurringAlarm(Context context, Class<?> cls, int time, int minute) {
		setRecurringAlarmId(context, cls, time, minute, 0);
	}	
	
	public static void setRecurringAlarmHour(Context context, String filter) {
		PendingIntent pintent = PendingIntent.getBroadcast(context, 0, new Intent(filter), PendingIntent.FLAG_CANCEL_CURRENT);
	    AlarmManager manager = (AlarmManager)(context.getSystemService( Context.ALARM_SERVICE ));

	    manager.setInexactRepeating( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HOUR , pintent );	
	}

	public static void setRecurringAlarmId(Context context, Class<?> cls, int time, int minute, int id) {
		Calendar updateTime = Calendar.getInstance();
		updateTime.setTimeZone(TimeZone.getDefault());
		updateTime.set(Calendar.HOUR_OF_DAY, time);
		updateTime.set(Calendar.MINUTE, minute);
		updateTime.set(Calendar.SECOND, 0);
		updateTime.set(Calendar.MILLISECOND, 0);

		Intent alarm = new Intent(context, cls);
		PendingIntent recurringAlarm = PendingIntent.getBroadcast(context, id, alarm, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarms = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarms.setRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, recurringAlarm);
	}

	public static void setAlarmId(Context context, Class<?> cls, int time, int minute, int id) {
		Calendar updateTime = Calendar.getInstance();
		updateTime.setTimeZone(TimeZone.getDefault());
		updateTime.set(Calendar.HOUR_OF_DAY, time);
		updateTime.set(Calendar.MINUTE, minute);
		updateTime.set(Calendar.SECOND, 0);
		updateTime.set(Calendar.MILLISECOND, 0);

		Intent alarmIntent = new Intent(context, cls);
		PendingIntent alarm = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarms = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        setSingleExactAlarm(alarms, updateTime.getTimeInMillis(), alarm);
	}

    public static void setAlarmId(Context context, Class<?> cls, int year, int month, int day, int time, int minute, int id) {
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(System.currentTimeMillis());
        cal.clear();
        cal.set(year, month, day, time, minute);

        Intent alarmIntent = new Intent(context, cls);
        PendingIntent alarm = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarms = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        setSingleExactAlarm(alarms, cal.getTimeInMillis(), alarm);
    }

    public static void setAlarmId(Context context, Class<?> cls, Date date, int id) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Intent alarmIntent = new Intent(context, cls);
        PendingIntent alarm = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarms = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        setSingleExactAlarm(alarms, cal.getTimeInMillis(), alarm);
    }

    @SuppressLint("NewApi")
    private static void setSingleExactAlarm(AlarmManager mAlarmManager, long time, PendingIntent pIntent) {
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pIntent);
        } else {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, pIntent);
        }
    }}
