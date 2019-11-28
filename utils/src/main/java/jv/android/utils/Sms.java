package jv.android.utils;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Classe que encapsula a lógica para enviar e receber um SMS
 * 
 * @author ricardo
 * 
 */
public class Sms {
	private static final String CATEGORIA = "JV";

	public boolean sendSms(final Context context, String address, String message, boolean somenteACobrar) {
		return _sendSms(context, address, message, somenteACobrar);
	}

	public boolean sendSms(final Context context, String address, String message) {
		return _sendSms(context, address, message, true);
	}

	private boolean _sendSms(final Context context, String address, String message, boolean somenteACobrar) {
		if (somenteACobrar && !address.startsWith("90")) {
			Logs.infoLog("Sms.sendSMS: Não é possível enviar mensagens a cobrar para o número " + address);
			return false;
		} else {		
			Logs.infoLog("Sms.sendSMS: to: " + address + ", message: " + message);
			try {
				SmsManager smsMgr = SmsManager.getDefault();
				ArrayList<String> parts =smsMgr.divideMessage(message);

				String[] phones = address.split(";");

				Logs.infoLog("Sms.sendSMS: sending " + String.valueOf(phones.length) + " messages.");
				for (int i = 0; i < phones.length; i++) {
					Logs.infoLog("Sms.sendSMS: sending message " + String.valueOf(i) + " (to: " + phones[i] + ").");

					try {
						smsMgr.sendMultipartTextMessage(phones[i], null, parts, null, null);					
					} catch (Exception e) {
						Logs.errorLog("Sms.sendSMS: Error sending SMS.", e);
					}
				}

				return true;
			} catch (Exception e) {
				Logs.errorLog("Sms.sendSMS: Error sending SMS.", e);
				e.printStackTrace();

				return false;
			}
		}
	}

	//Lê uma mensagem da Intent. A Intent é recebida por um IntentFilter
	//configurado para a ação "android.provider.Telephony.SMS_RECEIVED"
	public SmsMessage receberMensagem(Intent intent) {
		SmsMessage[] mensagens = getMessagesFromIntent(intent);
		if (mensagens != null) {
			return mensagens[0];
		}
		return null;
	}

	public String receberMensagemString(Intent intent) {
		String result = "";

		SmsMessage[] mensagens = getMessagesFromIntent(intent);
		if (mensagens != null) {
			for (int i = 0; i < mensagens.length; i++) {
				result += mensagens[i].getDisplayMessageBody();
			}
		}

		return result;
	}

	private SmsMessage[] getMessagesFromIntent(Intent intent) {
		Log.d(CATEGORIA, "Sms.getMessagesFromIntent: " + intent.getAction());

		Object messages[] = (Object[]) (Object[]) intent.getSerializableExtra("pdus");

		byte pduObjs[][] = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++)
			pduObjs[i] = (byte[]) (byte[]) messages[i];

		byte pdus[][] = new byte[pduObjs.length][];

		int pduCount = pdus.length;

		if (pduCount == 0) {
			return null;
		}

		SmsMessage msgs[] = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);

			String celular = msgs[0].getDisplayOriginatingAddress();
			String mensagem = msgs[0].getDisplayMessageBody();

			Log.d(CATEGORIA, "Sms.Mensagem: " + celular + " -> " + mensagem);
		}

		return msgs;
	}
}
