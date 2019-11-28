package jv.android.utils;

import android.content.Context;

public class Carrier {

    public static final String VIVO = "Vivo";
    public static final String TIM = "Tim";
    public static final String OI = "Oi";
    public static final String CLARO = "Claro";
    public static final String NEXTEL = "Nextel";
    public static final String AMAZONIA = "Amazonia Celular";
    public static final String CTBC = "CTBC";
    public static final String PORTO_SEGURO = "Porto Seguro";
    public static final String SERCONTEL = "Sercontel";
    public static final String UNICEL = "Unicel";
    public static final String DESCONHECIDO = "Desconhecido";

	public static final String MCC_BRASIL = "724";

    public static String simCardCarrierName(Context context) {
		String mcc = PhoneUtils.getMccFromSim(context);
		String mnc = PhoneUtils.getMncFromSim(context);

		if (!mcc.equals("") && !mnc.equals("")) {
			return getFromMNC(mcc, mnc);
		} else {
			return "";
		}
	}

	public static String simCardCarrierName(Context context, String iccid) {
		if (iccid != null && iccid.length() > 6) {
			String mnc = iccid.substring(4, 6);
			String mcc = PhoneUtils.getMccFromSim(context);

			return getFromMNC(mcc, mnc);
		} else {
			return "";
		}
	}

	public static String getFromMNC(int mcc, int mnc) {
		return getFromMNC(Format.zeroFill(String.valueOf(mcc), 3), Format.zeroFill(String.valueOf(mnc), 2));
	}

	public static String getFromMNC(String mcc, String mnc) {
		if (mcc.equals(MCC_BRASIL)) {
			if (mnc.equals("01") || mnc.equals("06") || mnc.equals("10") || mnc.equals("11") || mnc.equals("19") || mnc.equals("23")) {
				return VIVO;
			} else if (mnc.equals("05") || mnc.equals("12") || mnc.equals("38")) {
				return CLARO;
			} else if (mnc.equals("02") || mnc.equals("03") || mnc.equals("04") || mnc.equals("08")) {
				return TIM;
			} else if (mnc.equals("16") || mnc.equals("24") || mnc.equals("30") || mnc.equals("31")) {
				return OI;
            } else if (mnc.equals("32") || mnc.equals("33") || mnc.equals("34")) {
                return CTBC;
            } else if (mnc.equals("00") || mnc.equals("39")) {
                return NEXTEL;
            } else if (mnc.equals("24")) {
                return AMAZONIA;
            } else if (mnc.equals("07") || mnc.equals("32") || mnc.equals("33") || mnc.equals("34")) {
                return CTBC;
            } else if (mnc.equals("54")) {
                return PORTO_SEGURO;
            } else if (mnc.equals("15")) {
                return SERCONTEL;
            } else if (mnc.equals("37")) {
                return UNICEL;
			} else {
				return DESCONHECIDO;
			}
		} else {
			return DESCONHECIDO;
		}
	}

}
