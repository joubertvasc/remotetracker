package jv.android.utils.Email;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import jv.android.utils.Logs;
import jv.android.utils.R;

public class Email extends javax.mail.Authenticator {
//public class Email {
    private String _user;
    private String _pass;
    private String[] _to;
    private String _from;
    private String _port;
    private String _sport;
    private String _host;
    private String _subject;
    private String _body;
    private boolean _auth;
    private boolean _debuggable;
    private Multipart _multipart;

    public Email() {
        _host = "smtp.gmail.com"; // default smtp server
        _port = "465"; // default smtp port
        _sport = "465"; // default socketfactory port
        _user = ""; // username
        _pass = ""; // password
        _from = ""; // email sent from
        _subject = ""; // email subject
        _body = ""; // email body
        _debuggable = false;
        // debug mode on or off - default off
        _auth = true; // smtp authentication - default on
        _multipart = new MimeMultipart();

        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");

        CommandMap.setDefaultCommandMap(mc); /**/
    }

    public Email(String user, String pass) {
        this();

        _user = user;
        _pass = pass;
    }

    public Email(String user, String pass, String fullName, String email) {
        this();

        _user = user;
        _pass = pass;
        _from = email;
    }

    public boolean send() throws Exception {
        Logs.infoLog("Email.Send: starting");
        Properties props = _setProperties();

        if ((_user == null || _user.equals("")) && _from != null && !_from.equals("")) {
            _user = _from;
        }

        if (!_user.equals("") && !_pass.equals("") && _to.length > 0 && !_from.equals("") && !_subject.equals("") && !_body.equals("")) {
            String to = "";

            for (String s : _to) {
                if (s != null && !s.trim().equals(""))
                    to += s + ", ";
            }

            Logs.infoLog("Email.Send: configuring message: user " + _user + ", pass " + _pass + ", from " + _from + ", to " + to + ", subject " + _subject + " body " + _body);
            Session session = Session.getInstance(props, this);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(_from));
            InternetAddress[] addressTo = new InternetAddress[_to.length];

            for (int i = 0; i < _to.length; i++) {
                if (_to[i] != null && !_to[i].trim().equals(""))
                    addressTo[i] = new InternetAddress(_to[i]);
            }

            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
            msg.setSubject(_subject);
            msg.setSentDate(new Date());

            // setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(_body);
            _multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            msg.setContent(_multipart);

            // send email
            Logs.infoLog("Email.Send: sending");
            try {
                Transport.send(msg);
                Logs.infoLog("Email.Send: message sent.");
            } catch (Exception e) {
                Logs.errorLog("Email.send: error sending message", e);
                return false;
            }
/**/
            Logs.infoLog("Email.Send: ending");
            return true;
        } else {
            Logs.warningLog("Email.Send: error. User, Password, From, Subject or Body is null.");
            return false;
        }
    }

    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        _multipart.addBodyPart(messageBodyPart); /**/
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        Logs.infoLog("Email.PasswordAuthentication: authenticating");

        PasswordAuthentication pa = null;

        try {
            pa = new PasswordAuthentication(_user, _pass);
            Logs.infoLog("Email.PasswordAuthentication: no error, authenticated.");
        } catch (Exception e) {
            Logs.errorLog("Email.PasswordAuthentication: error.", e);
        }

        return pa;
    }

    private Properties _setProperties() {
        Logs.infoLog("Email._setProperties: starting");
        Properties props = new Properties();
        props.put("mail.smtp.host", _host);

        if (_debuggable) {
            props.put("mail.debug", "true");
        }

        if (_auth) {
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.port", _port);
        props.put("mail.smtp.socketFactory.port", _sport);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        Logs.infoLog("Email._setProperties: ending");
        return props;
    }

    // the getters and setters
    public String getBody() {
        return _body;
    }

    public void setBody(String _body) {
        this._body = _body;
    }

    public void setSubject(String subject) {
        this._subject = subject;
    }

    public void setFrom(String from) {
        this._from = from;
    }

    public void setTo(String[] to) {
        this._to = to;
    }

    public void setSMTP(String host) {
        _host = host;
    }

    public void setPort(int port) {
        _port = Integer.toString(port);
        _sport = _port;
    }

    public void setAuth(boolean auth) {
        _auth = auth;
    }
/**/
    public static void sendSimpleMessage(Context context, String caption, String to, String subject, String message) {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");

        if (to != null && !to.trim().equals(""))
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});

        if (subject != null && !subject.trim().equals(""))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (message != null && !message.trim().equals(""))
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        context.startActivity(Intent.createChooser(emailIntent, caption));
    }

    // http://stackoverflow.com/questions/2264622/android-multiple-email-attachments-using-intent
    public static void sendSimpleMessage(Context context, String caption, String emailTo, String emailCC,
                                         String subject, String emailText, List<String> filePaths) {
        //need to "send multiple" to get more than one attachment
        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");

        if (emailTo != null && !emailTo.trim().equals(""))
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});

        if (emailCC != null && !emailCC.trim().equals(""))
            emailIntent.putExtra(Intent.EXTRA_CC, new String[]{emailCC});

        if (subject != null && !subject.trim().equals(""))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (emailText != null && !emailText.trim().equals(""))
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);

        if (filePaths != null) {
            //has to be an ArrayList
            ArrayList<Uri> uris = new ArrayList<Uri>();
            //convert from paths to Android friendly Parcelable Uri's
            for (String file : filePaths) {
                File fileIn = new File(file);

                if (fileIn.exists()) {
                    Uri u = Uri.fromFile(fileIn);
                    uris.add(u);
                }
            }

            if (uris.size() > 0) {
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            }
        }

        context.startActivity(Intent.createChooser(emailIntent, caption));
    }

    public static String justSentEMail(Context context, String user, String pass, String fullName, String email,
                                       String subject, String[] to, String body) {
        return justSentEMail(context, user, pass, fullName, email, subject, to, body, null);
    }

    public static String justSentEMail(Context context, String user, String pass, String fullName, String email,
                                       String subject, String[] to, String body, String file) {
        return justSentEMail(context, user, pass, fullName, email, subject, to, body, "smtp.google.com", 465, true, file);
    }

    public static String justSentEMail(Context context, String user, String pass, String fullName, String email,
                                       String subject, String[] to, String body, String smtp, int port, boolean auth) {
        return justSentEMail(context, user, pass, fullName, email, subject, to, body, smtp, port, auth, null);
    }

    public static String justSentEMail(Context context, String user, String pass, String fullName, String email,
                                       String subject, String[] to, String body, String smtp, int port, boolean auth, String file) {
        String result = "";

        Email _email = new Email(user, pass, fullName, email);
        _email.setSubject(subject);
        _email.setTo(to);
        _email.setBody(body);
        _email.setSMTP(smtp);
        _email.setPort(port);
        _email.setAuth(auth);


        try {
            Logs.infoLog("Email.justSentEMail. Sending e-mail to: " + to.toString());

            if (file != null && !file.equals("")) {
                _email.addAttachment(file);
            }

            if (_email.send()) {
                Logs.infoLog("Email.justSentEMail. E-Mail sent!");
                result = context.getString(R.string.emailsent);
            } else {
                Logs.errorLog("Email.justSentEMail. E-Mail was not sent!");
                result = context.getString(R.string.emailnotsent);
            }
        } catch (Exception e) {
            Logs.errorLog("Email.justSentEMail. Error sending e-mail", e);
            result = context.getString(R.string.emailerror) + " " + e.getMessage();
        }
/**/
        return result;
    }
}
