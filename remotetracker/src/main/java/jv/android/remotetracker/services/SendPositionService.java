package jv.android.remotetracker.services;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jv.android.remotetracker.commands.CommandService;
import jv.android.remotetracker.commands.CommandStructure;
import jv.android.remotetracker.commands.CommandTracker;
import jv.android.remotetracker.receiver.SendPositionSchedReceiver;
import jv.android.remotetracker.utils.Preferences;
import jv.android.utils.Logs;
import jv.android.utils.network.Network;
import jv.android.utils.PhoneUtils;
import jv.android.utils.Schedule;
import jv.android.utils.ThreadUtils;
import jv.android.utils.http.CustomHttp;
import jv.android.utils.http.CustomHttpResult;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class SendPositionService extends IntentService {

    private Context context = null;
    private CommandStructure cs = null;
    private Preferences pref = null;

    public Preferences preferences;

    public SendPositionService() {
        super("SendPositionService");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Logs.infoLog("SendPositionService.onBind");

        return null;
    }

    @Override
    public void onCreate() {
        Logs.infoLog("SendPositionService.onCreate");

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Logs.infoLog("SendPositionService.onDestroy");

        super.onDestroy();
    }

    @Override
    public void onHandleIntent(Intent intent) {
        context = this.getApplicationContext();
        Logs.infoLog("SendPositionService.onHandleIntent. Servi�o iniciado.");

        pref = new Preferences(context);
        int interval = pref.getTrackerInterval();

        if (interval > 0) {
            // Processar o GPS
            Intent commandService = new Intent(context, CommandService.class);
            commandService.putExtra("fake", false);

            if (cs != null) {
                commandService.putExtra("commandstructure", cs);
            }

            context.startService(commandService);
            int tentativas = 0;

            while (tentativas < 120) {
                ThreadUtils.waitms(1000);

                if (pref.getTrackerGPSProcessed()) {
                    break;
                }

                tentativas++;
            }

            enviarRegistro();

            // Reagendar
            Calendar time = Calendar.getInstance();
            time.add(Calendar.MINUTE, interval);

            Schedule.setRecurringAlarmId(context, SendPositionSchedReceiver.class,
                    time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), CommandTracker.alarmId);
        }

        Logs.infoLog("SendPositionService.onHandleIntent. Parando servi�o.");
    }

    private void enviarRegistro() {
        if (Network.isNetworkAvailable(context)) {
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Void, Void> a = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    PhoneUtils pu = new PhoneUtils(context);
                    String imei = pu.getIMEI();

                    Map<String, String> dataToSend = new HashMap<>();
                    dataToSend.put("imei", imei);
                    dataToSend.put("satelites", "0");
                    dataToSend.put("tipo", pref.getTrackerGPSType());
                    dataToSend.put("resultado", pref.getTrackerGPSMessage());
                    dataToSend.put("altitude", String.valueOf(pref.getTrackerGPSAltitude()));
                    dataToSend.put("velocidade", String.valueOf(pref.getTrackerGPSSpeed()));
                    dataToSend.put("latitude", String.valueOf(pref.getTrackerGPSLatitude()));
                    dataToSend.put("longitude", String.valueOf(pref.getTrackerGPSLongitude()));

                    CustomHttpResult result = CustomHttp.httpPost("http://www.jvsoftwares.net/remotetracker/ws/setcoordenada.php", dataToSend);

                    if (result.isSuccess()) {
                        if (result.getServerRequestCode() == 600) {
                            Logs.infoLog("enviarRegistro Ok");
                        } else {
                            Logs.errorLog("enviarRegistro error " + result.getHttpResult());
                        }
                    } else {
                        Logs.errorLog("enviarRegistro error " + result.getException());
                    }

                    return null;
                }
            };

            int corePoolSize = 60;
            int maximumPoolSize = 80;
            int keepAliveTime = 10;

            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(maximumPoolSize);
            Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
            a.executeOnExecutor(threadPoolExecutor);
        }
    }
}
