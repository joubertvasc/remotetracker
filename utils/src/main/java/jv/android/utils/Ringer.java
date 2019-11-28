package jv.android.utils;

import android.content.Context;
import android.media.AudioManager;

public class Ringer {

	public static void turnOnRinger(Context context) {
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int max = am.getStreamMaxVolume(AudioManager.STREAM_RING);
		int prev = am.getStreamVolume(AudioManager.STREAM_RING);
		Logs.infoLog("Chanching ringer from " + prev + " to " + max);
		am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		am.setStreamVolume(AudioManager.STREAM_RING, max, 0);
	}

}
