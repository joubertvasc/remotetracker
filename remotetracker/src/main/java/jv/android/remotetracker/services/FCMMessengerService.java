package jv.android.remotetracker.services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import jv.android.remotetracker.commands.CommandProcessor;
import jv.android.remotetracker.utils.Preferences;
import jv.android.utils.Logs;

/**
 * Created by joubertvasc on 18/11/2016.
 */

public class FCMMessengerService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Preferences p = new Preferences (getApplicationContext());
        p.setRegId(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Context context = getApplicationContext();

        String messageText = "";

        if (message.getData().size() > 0) {
            messageText = message.getData().toString();
        }

        if (message.getNotification() != null) {
            messageText = message.getNotification().getBody();
        }

        process(context, messageText);
    }

    public static void process(Context context, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            jsonObject = new JSONObject(jsonObject.getString("message"));

            String cmd = jsonObject.getString("cmd");
            String extra = jsonObject.getString("extra");
            String retorno = jsonObject.getString("retorno");
            String senha = jsonObject.getString("senha");
            String origem = jsonObject.getString("origem");

            String parametros = "rt#" + cmd.trim() + (extra.trim().equals("") ? "" : "," + extra) + "#" + retorno.trim() + (senha.trim().equals("") ? "" : "#" + senha.trim());

            CommandProcessor commands = new CommandProcessor(context, (!retorno.contains("@") ? retorno.trim() : ""), (retorno.contains("@") ? retorno.trim() : ""), parametros, true, false, origem);
            commands.processCommands(context);
        } catch (JSONException e) {
            Logs.errorLog("FCMMessengerService.handleMessage error." , e);
        }
    }

}
