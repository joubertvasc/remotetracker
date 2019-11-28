package jv.android.utils.telephony;

import android.content.Context;
import android.provider.Settings;

import jv.android.utils.Logs;

/**
 * Created by joubertvasc on 26/02/2018.
 */

public class SimInfo {

    public static final String VOICE_CALL_SIM_SETTING = "voice_call_sim_setting";
    public static final String GPRS_CONNECTION_SIM_SETTING = "gprs_connection_sim_setting";
    public static final String SMS_SIM_SETTING = "sms_sim_setting";
    public static final String VIDEO_CALL_SIM_SETTING = "video_call_sim_setting";
    public static final String SIP_CALL_SIM_SETTING = "sip_call_sim_setting";

    private static boolean setDefaultSIM(Context context, String businessType, long simId) {
        try {
            Settings.System.putLong(context.getContentResolver(), businessType, simId);
            Logs.infoLog("SimInfo.setDefaultSIM success");
            return true;
        } catch (Exception e) {
            Logs.errorLog("SimInfo.setDefaultSIM error", e);
            return false;
        }
    }

    public static boolean setDefaultVoiceCallSIM(Context context, long simId) {
        return setDefaultSIM(context, VOICE_CALL_SIM_SETTING, simId);
    }

    public static boolean setDefaultGPRSSIM(Context context, long simId) {
        return setDefaultSIM(context, GPRS_CONNECTION_SIM_SETTING, simId);
    }

    public static boolean setDefaultSMSSIM(Context context, long simId) {
        return setDefaultSIM(context, SMS_SIM_SETTING, simId);
    }

    public static boolean setDefaultVideoCallSIM(Context context, long simId) {
        return setDefaultSIM(context, VIDEO_CALL_SIM_SETTING, simId);
    }

    public static boolean setDefaultSipCallSIM(Context context, long simId) {
        return setDefaultSIM(context, SIP_CALL_SIM_SETTING, simId);
    }
}
