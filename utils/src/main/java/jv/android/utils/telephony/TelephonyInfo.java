package jv.android.utils.telephony;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TelephonyInfo {

	private static TelephonyInfo telephonyInfo;
	private String imeiSIM1;
	private String imeiSIM2;
	private String imsiSIM1;
	private String imsiSIM2;
	private String iccidSIM1;
	private String iccidSIM2;
	private boolean isSIM1Ready;
	private boolean isSIM2Ready;
	private String numberSIM1;
	private String numberSIM2;
    private int mcc1;
    private int mcc2;
    private int mnc1;
    private int mnc2;
	private int defaultSimm;

	private int smsDefaultSimm;

	public String getNumberSIM1() {
		return numberSIM1 == null ? "" : numberSIM1;
	}

	public String getNumberSIM2() {
		return numberSIM2 == null ? "" : numberSIM2;
	}

	public String getImeiSIM1() {
		return imeiSIM1 == null ? "" : imeiSIM1;
	}

	public String getImeiSIM2() {
		return imeiSIM2 == null ? "" : imeiSIM2;
	}

	public String getImsiSIM1() {
		return imsiSIM1 == null ? "" : imsiSIM1;
	}

	public String getImsiSIM2() {
		return imsiSIM2 == null ? "" : imsiSIM2;
	}

	public String getIccidSIM1() {
		return iccidSIM1 == null ? "" : iccidSIM1;
	}

	public String getIccidSIM2() {
		return iccidSIM2 == null ? "" : iccidSIM2;
	}

	public boolean isSIM1Ready() {
		return isSIM1Ready;
	}

	public boolean isSIM2Ready() {
		return isSIM2Ready;
	}

	public boolean isDualSIM() {
		return imeiSIM2 != null && !imeiSIM2.equals("");
	}

    public int getMcc1() {
        return mcc1;
    }

    public int getMcc2() {
        return mcc2;
    }

    public int getMnc1() {
        return mnc1;
    }

    public int getMnc2() {
        return mnc2;
    }

	public int getDefaultSimm() {
		return defaultSimm;
	}

	public int getSmsDefaultSimm() {
		return smsDefaultSimm;
	}

	private TelephonyInfo() {

	}

	@SuppressLint("MissingPermission")
	public static TelephonyInfo getInstance(Context context) {
		if (telephonyInfo == null) {
			telephonyInfo = new TelephonyInfo();

			TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

			telephonyInfo.imeiSIM1 = telephonyManager.getDeviceId();
			telephonyInfo.imeiSIM2 = "";
			telephonyInfo.imsiSIM1 = telephonyManager.getSubscriberId();
			telephonyInfo.imsiSIM2 = "";
			telephonyInfo.iccidSIM1 = telephonyManager.getSimSerialNumber();
			telephonyInfo.iccidSIM2 = "";
			telephonyInfo.numberSIM1 = "";
			telephonyInfo.numberSIM2 = "";
			telephonyInfo.defaultSimm = -1;
			telephonyInfo.smsDefaultSimm = -1;

			telephonyInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
			telephonyInfo.isSIM2Ready = false;

			try {
				telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
				telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
			} catch (GeminiMethodNotFoundException e) {
				e.printStackTrace();

				try {
					telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
					telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
				} catch (GeminiMethodNotFoundException e1) {
					//Call here for next manufacturer's predicted method name if you wish
					e1.printStackTrace();
				}
			}

			try {
				telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
				telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
			} catch (GeminiMethodNotFoundException e) {
				e.printStackTrace();

				try {
					telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
					telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
				} catch (GeminiMethodNotFoundException e1) {
					//Call here for next manufacturer's predicted method name if you wish
					e1.printStackTrace();
				}
			}

			try {
				telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getSubscriberIdGemini", 0);
				telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getSubscriberIdGemini", 1);
			} catch (GeminiMethodNotFoundException e) {
				e.printStackTrace();

				try {
					telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getSubscriberId", 0);
					telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getSubscriberId", 1);
				} catch (GeminiMethodNotFoundException e1) {
					//Call here for next manufacturer's predicted method name if you wish
					e1.printStackTrace();
				}
			}

			try {
				telephonyInfo.iccidSIM1 = getDeviceIdBySlot(context, "getSimSerialNumberGemini", 0);
				telephonyInfo.iccidSIM2 = getDeviceIdBySlot(context, "getSimSerialNumberGemini", 1);
			} catch (GeminiMethodNotFoundException e) {
				e.printStackTrace();

				try {
					telephonyInfo.iccidSIM1 = getDeviceIdBySlot(context, "getSimSerialNumber", 0);
					telephonyInfo.iccidSIM2 = getDeviceIdBySlot(context, "getSimSerialNumber", 1);
				} catch (GeminiMethodNotFoundException e1) {
					//Call here for next manufacturer's predicted method name if you wish
					e1.printStackTrace();
				}
			}

			Object tm = context.getSystemService(Context.TELEPHONY_SERVICE);
			Method method_getDefaultSim;

			try {
				method_getDefaultSim = tm.getClass().getDeclaredMethod("getDefaultSim");
				method_getDefaultSim.setAccessible(true);
				telephonyInfo.defaultSimm = (Integer) method_getDefaultSim.invoke(tm);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Method method_getSmsDefaultSim;
			try {
				method_getSmsDefaultSim = tm.getClass().getDeclaredMethod("getSmsDefaultSim");
				telephonyInfo.smsDefaultSimm = (Integer) method_getSmsDefaultSim.invoke(tm);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
				SubscriptionManager sm = SubscriptionManager.from(context);

				// it returns a list with a SubscriptionInfo instance for each simcard
				// there is other methods to retrieve SubscriptionInfos (see [2])
				List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();

				telephonyInfo.iccidSIM1 = "";
				telephonyInfo.iccidSIM2 = "";

				if (sis != null) {
					for (int i = 0; i < sis.size(); i++) {
						SubscriptionInfo si = sis.get(i);

						if (si.getSimSlotIndex() == 0) {
							telephonyInfo.iccidSIM1 = si.getIccId();
							telephonyInfo.numberSIM1 = si.getNumber();
                            telephonyInfo.mcc1 = si.getMcc();
                            telephonyInfo.mnc1 = si.getMnc();
						} else {
							telephonyInfo.iccidSIM2 = si.getIccId();
							telephonyInfo.numberSIM2 = si.getNumber();
                            telephonyInfo.mcc2 = si.getMcc();
                            telephonyInfo.mnc2 = si.getMnc();
						}
					}
				}
			}
		}

		return telephonyInfo;
	}

	private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
		String imei = null;

		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		try {
			Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimID.invoke(telephony, obParameter);

			if(ob_phone != null){
				imei = ob_phone.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return imei;
	}

	private static  boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

		boolean isReady = false;

		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		try {
			Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

			if(ob_phone != null){
				int simState = Integer.parseInt(ob_phone.toString());
				if(simState == TelephonyManager.SIM_STATE_READY){
					isReady = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return isReady;
	}

	private static class GeminiMethodNotFoundException extends Exception {

		private static final long serialVersionUID = -996812356902545308L;

		public GeminiMethodNotFoundException(String info) {
			super(info);
		}
	}

	// Falta completar a saída
	@SuppressLint("MissingPermission")
	public static void samsungTwoSims(Context context) {
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		try {
			Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getFirstMethod = telephonyClass.getMethod("getDefault", parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = 0;
			TelephonyManager first = (TelephonyManager) getFirstMethod.invoke(null, obParameter);
			Toast.makeText(context, "Device Id: " + first.getDeviceId() + ", device status: " + first.getSimState() + ", operator: " + first.getNetworkOperator() + "/" + first.getNetworkOperatorName(), Toast.LENGTH_LONG).show();

			obParameter[0] = 1;
			TelephonyManager second = (TelephonyManager) getFirstMethod.invoke(null, obParameter);

			Toast.makeText(context, "Device Id: " + second.getDeviceId() + ", device status: " + second.getSimState() + ", operator: " + second.getNetworkOperator() + "/" + second.getNetworkOperatorName(), Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}   
	}

	public static List<String> printTelephonyManagerMethodNamesForThisDevice(Context context) {
		List<String> result = new ArrayList<String>();

		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		Class<?> telephonyClass;
		try {
			telephonyClass = Class.forName(telephony.getClass().getName());
			Method[] methods = telephonyClass.getMethods();
			for (int idx = 0; idx < methods.length; idx++) {
				result.add(methods[idx] + " declared by " + methods[idx].getDeclaringClass() + "\n");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return result;
	} 
}
