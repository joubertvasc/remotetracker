package jv.android.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmSound {

	public static void alarmSound(Context context) {
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null) {
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alert == null) {
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(context, alert);
			AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (manager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				player.setAudioStreamType(AudioManager.STREAM_ALARM);
				player.setLooping(false);
				player.prepare();
				player.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
