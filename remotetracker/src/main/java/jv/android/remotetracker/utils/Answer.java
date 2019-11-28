package jv.android.remotetracker.utils;

import jv.android.remotetracker.R;
import jv.android.remotetracker.commands.CommandStructure;
import jv.android.remotetracker.commands.CommandTable;
import jv.android.utils.Email.Email;
import jv.android.utils.FTP;
import jv.android.utils.Format;
import jv.android.utils.Logs;
import jv.android.utils.network.Network;
import jv.android.utils.PhoneUtils;
import jv.android.utils.Sms;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class Answer {

    private static boolean isFake = false;
    private static Preferences preferences;

    public static void sendAnswer(final Context context, final CommandStructure commandStructure, String answer, boolean fake) {
        isFake = fake;

        if (answer == null || answer.equals("")) {
            Logs.warningLog("Answer.sendAnswer. No answer to send");
            answer = context.getString(R.string.msgErrorWhileProcessingCommand);
        }

        // if the command requires a network and it's not available, try to turn 3G and WiFi on.
        if (!Network.isNetworkAvailable(context) && (commandStructure.getIsEMailCommand() || commandStructure.getIsFTPCommand())) {
            Network.turnNetworkOn(context, true, true);
        }

        Logs.infoLog("Answer.sendAnswer. Sending answer: " + answer);

        preferences = new Preferences(context);

        if (commandStructure != null) {
            RemoteTrackerDataHelper db = new RemoteTrackerDataHelper(context);
            Date data = Calendar.getInstance().getTime();

            Logs.infoLog("Answer.sendAnswer. Saving command in history");
            CommandTable ct = new CommandTable();
            ct.setCommand(commandStructure.getCommand());
            ct.setData(jv.android.utils.Format.formatDateTime(data));
            ct.setFrom(commandStructure.getFrom());
            ct.setResult(answer);

            if (commandStructure.getIsFTPCommand())
                ct.setTo("FTP: " + preferences.getFtpServer());
            else if (commandStructure.getIsEMailCommand())
                ct.setTo(commandStructure.getReturnEMail());
            else
                ct.setTo(commandStructure.getReturnNumber());

            db.insertCommand(ct);
            Logs.infoLog("Answer.sendAnswer. Command saved in history");

            if (commandStructure.getIsFTPCommand()) {
                if (!Network.isNetworkAvailable(context)) {
                    Logs.warningLog("Answer.sendAnswer. No network available to send FTP answer");
                } else {
                    Answer.sendFTP(context, commandStructure, answer);
                }
            } else if (commandStructure.getIsEMailCommand()) {
                if (!Network.isNetworkAvailable(context)) {
                    Logs.warningLog("Answer.sendAnswer. No network available to send e-mail answer");

                    Answer.sendSMS(context, commandStructure.getReturnNumber(), answer);
                } else {
                    Answer.sendEMail(context, commandStructure, answer);
                }
            } else {
                if (!commandStructure.getReturnNumber().equals(""))
                    Answer.sendSMS(context, commandStructure.getReturnNumber(), answer);
            }

            // Inserir resultado no servidor WEB
            if (commandStructure.isReturnToWeb()) {
                if (Network.isNetworkAvailable(context)) {
                    final String _answer = answer;

                    AsyncTask<Void, Void, Void> a = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                PhoneUtils pu = new PhoneUtils(context);
                                String imei = pu.getIMEI();

                                HttpPost httpPost = new HttpPost("http://www.jvsoftwares.net/remotetracker/ws/setresultado.php");
                                List<NameValuePair> p = new ArrayList<>();
                                p.add(new BasicNameValuePair("imei", imei));
                                p.add(new BasicNameValuePair("comando", commandStructure.getCommand()));
                                p.add(new BasicNameValuePair("resultado", _answer));
                                p.add(new BasicNameValuePair("origem", commandStructure.getImeiOrigem()));

                                httpPost.setEntity(new UrlEncodedFormEntity(p, HTTP.UTF_8));
                                HttpResponse response = new DefaultHttpClient().execute(httpPost);

                                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
                                StringBuilder result = new StringBuilder();

                                String line;
                                while ((line = reader.readLine()) != null) {
                                    result.append(line);
                                }

                                Logs.infoLog("Answer.sendAnswer. Retorno do HTTP: " + result);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }; //.execute(null, null, null);

                    int corePoolSize = 60;
                    int maximumPoolSize = 80;
                    int keepAliveTime = 10;

                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(maximumPoolSize);
                    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
                    a.executeOnExecutor(threadPoolExecutor);
                }
            }
        }
    }

    private static void sendSMS(Context context, String cellular, String message) {
        Logs.infoLog("Answer.sendSMS. Sending SMS to " + cellular);

        if (isFake) {
            Toast.makeText(context, "RemoteTracker Fake result: " + message, Toast.LENGTH_LONG).show();
        } else {
            Sms sms = new Sms();

            sms.sendSms(context, cellular, Format.removeAccents(message));
        }
    }

    private static void sendFTP(Context context, CommandStructure commandStructure, String message) {
        Logs.infoLog("Answer.sendFTP. Sending message to FTP server.");

        if (isFake) {
            Toast.makeText(context, "RemoteTracker Fake result: " + message, Toast.LENGTH_LONG).show();
        } else {
            File root;
            FileOutputStream out;

            Date data = Calendar.getInstance().getTime();

            String fileName = commandStructure.getCommand() + "-" + Format.formatDateTimeOnlyDigits(data) + ".txt";

            root = Environment.getExternalStorageDirectory();
            File file = new File(root, fileName);

            try {
                Logs.infoLog("Answer.sendFTP. Creating temporary file: " + file.getPath());
                out = new FileOutputStream(file, true);
                try {
                    Logs.infoLog("Answer.sendFTP. Writing message to temporary file: " + message);
                    out.write(message.getBytes());
                } finally {
                    out.flush();
                    out.close();
                }
            } catch (Exception ex) {
                Logs.errorLog("Answer.sendFTP. Error writing to temporary file.", ex);
            }

            try {
                Logs.infoLog("Answer.sendFTP. Sending file to FTPServer.");
                if (FTP.sendFile(context, preferences.getFtpServer(), preferences.getFtpUserName(), preferences.getFtpPassword(), preferences.getFtpRemotePath(), fileName)) {
                    Logs.infoLog("Answer.sendFTP. File was sent to FTP server.");
                } else {
                    Logs.warningLog("Answer.sendFTP. Could not send file to FTP server.");
                }
            } finally {
                Logs.infoLog("Answer.sendFTP. Deleting temporary file");
                if (file.exists())
                    if (file.delete())
                        Logs.infoLog("Answer.sendFTP. Temporary file was deleted");
                    else
                        Logs.warningLog("Answer.sendFTP. Could not delete temporary file");
            }
        }

        Logs.infoLog("Answer.sendFTP. Ending");
    }

    private static void sendEMail(Context context, CommandStructure commandStructure, String message) {
        Logs.infoLog("Answer.sendEMail. Sending message to e-mail address");
        if (isFake) {
            Toast.makeText(context, "RemoteTracker Fake result: " + message, Toast.LENGTH_LONG).show();
        } else {
            Logs.infoLog("Answer.sendEMail. EMail.User: " + preferences.getEmailUser());
            Logs.infoLog("Answer.sendEMail. EMail.UserName: " + preferences.getEmailUserName());
            Logs.infoLog("Answer.sendEMail. EMail.Address: " + preferences.getEmailAddress());
            Logs.infoLog("Answer.sendEMail. EMail.Password is set: " + (preferences.getEmailPassword().equals("") ? "No" : "Yes"));
            Logs.infoLog("Answer.sendEMail. EMail.Server: " + preferences.getEmailServer());
            Logs.infoLog("Answer.sendEMail. EMail.Port: " + preferences.getEmailPort());
            Logs.infoLog("Answer.sendEMail. EMail.Auth: " + (preferences.isEmailSMTPAuth() ? "Yes" : "No"));

            String user = preferences.getEmailUser();
            String address = preferences.getEmailAddress();

            if (user.equals("") && !address.equals("")) {
                //				user = address.substring(0, address.indexOf("@"));
                user = address;
                Logs.infoLog("Answer.sendEMail. EMail.User2: " + user);
            }

            if (commandStructure.getReturnEMail().equals("")) {
                Logs.warningLog("Answer.sendEMail. No returning e-mail address");
                Answer.sendSMS(context, commandStructure.getReturnNumber(), context.getString(R.string.msgNoReturnEMail));
            } else if (user.equals("") || address.equals("") || preferences.getEmailServer().equals("") || preferences.getEmailPassword().equals("")) {
                Logs.warningLog("Answer.sendEMail. E-Mail configurations are incomplete.");
                Answer.sendSMS(context, commandStructure.getReturnNumber(), context.getString(R.string.msgInvalidEMailConfigurations));
            } else {
                Email email = new Email(user, preferences.getEmailPassword(), preferences.getEmailUserName(), address);

                email.setTo(new String[]{commandStructure.getReturnEMail()});
                email.setBody(message);
                email.setSMTP(preferences.getEmailServer());
                email.setPort(preferences.getEmailPort());
                email.setAuth(preferences.isEmailSMTPAuth());
                email.setSubject(context.getString(R.string.msgSubject));

                try {
                    Logs.infoLog("Answer.sendEMail. Sending e-mail to: " + commandStructure.getReturnEMail());

                    if (email.send()) {
                        Logs.infoLog("Answer.sendEMail. E-Mail sent!");
                    } else {
                        Logs.errorLog("Answer.sendEMail. E-Mail was not sent!");
                        Answer.sendSMS(context, commandStructure.getReturnNumber(), context.getString(R.string.msgErrorSendingEMail));
                    }
                } catch (Exception e) {
                    Logs.errorLog("Answer.sendEMail. Error sending e-mail", e);
                }
            }
        }
    }
}
