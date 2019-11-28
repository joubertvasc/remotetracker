package jv.android.utils.apn;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import java.util.Locale;

import jv.android.utils.Carrier;
import jv.android.utils.DebugLog;

public class APNUtils {

    private static final Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers");
    private static final Uri APN_PREFER_URI = Uri.parse("content://telephony/carriers/preferapn");
    public static final String DEBUGAPN = "Debug_APN";

    private static ContentValues prepareValues(APN apn) {
        ContentValues values = new ContentValues();

        if (!apn.getName().trim().equals(""))
            values.put("name", apn.getName());

        if (!apn.getApn().trim().equals(""))
            values.put("apn", apn.getApn());

        if (!apn.getMcc().trim().equals(""))
            values.put("mcc", apn.getMcc());

        if (!apn.getMnc().trim().equals(""))
            values.put("mnc", apn.getMnc());

        if (!apn.getMcc().trim().equals("") && !apn.getMnc().trim().equals(""))
            values.put("numeric", apn.getMcc() + apn.getMnc());

        if (!apn.getUser().trim().equals(""))
            values.put("user", apn.getUser());

        if (!apn.getPassword().trim().equals(""))
            values.put("password", apn.getPassword());

        if (!apn.getServer().trim().equals(""))
            values.put("server", apn.getServer());

        if (!apn.getProxy().trim().equals(""))
            values.put("proxy", apn.getProxy());

        if (!apn.getPort().trim().equals(""))
            values.put("port", apn.getPort());

        if (!apn.getMmsProxy().trim().equals(""))
            values.put("mmsproxy", apn.getMmsProxy());

        if (!apn.getMmsPort().trim().equals(""))
            values.put("mmsport", apn.getMmsPort());

        if (!apn.getMmsc().trim().equals(""))
            values.put("mmsc", apn.getMmsc());

        if (!apn.getType().trim().equals(""))
            values.put("type", apn.getType());

        if (!apn.getCurrent().trim().equals(""))
            values.put("current", apn.getCurrent());

        values.put("authtype", apn.getAuthType());

        return values;
    }

    public static int createNewApn(Context context, APN apn, boolean setAsDefaultAPN) {
        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.createNewApn.");
        int apnid = -1;

        try {
            if (apn != null) {
                DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.createNewApn. Reading APN list.");
                Uri APN_URI = Uri.parse("content://telephony/carriers");
                ContentResolver resolver = context.getContentResolver();

                DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.createNewApn. Creating new registry based on parameters.");
                ContentValues values = prepareValues(apn);

                DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.createNewApn. Inserting new APN.");
                Cursor c = null;
                Uri newRow = resolver.insert(APN_URI, values);

                if (newRow != null) {
                    DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.createNewApn. Getting new ID.");
                    c = resolver.query(newRow, null, null, null, null);

                    int tableIndex = c.getColumnIndex("_id");
                    c.moveToFirst();
                    apnid = c.getShort(tableIndex);
                } else
                    DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.createNewApn. New APN was not found. Inserting failed?");

                if (c != null) {
                    c.close();
                }

                if (apnid > -1 && setAsDefaultAPN) {
                    DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.createNewApn. Setting new APN as default.");
                    ContentValues v = new ContentValues(1);
                    v.put("apn_id", apnid);

                    context.getContentResolver().update(APN_PREFER_URI, v, null, null);
                }
            } else
                DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.createNewApn. Invalid apn (null).");
        } catch (Exception e) {
            DebugLog.errorLog(APNUtils.DEBUGAPN, "createNewApn: error", e);
        }

        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.createNewApn. Returning ID " + String.valueOf(apnid));
        return apnid;
    }

    public static boolean updateApn(Context context, int id, APN apn) {
        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.updateApn.");

        if (apn != null) {
            try {
                DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.updateApn. Reading APN list.");
                Uri APN_URI = Uri.parse("content://telephony/carriers");
                ContentResolver resolver = context.getContentResolver();

                DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.updateApn. Creating new registry based on parameters.");
                ContentValues values = prepareValues(apn);

                DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.updateApn. Inserting new APN.");
                int result = resolver.update(APN_URI, values, "_id = " + String.valueOf(id), null);

                if (result != -1) {
                    DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.updateApn. APN updated.");
                    return true;
                } else {
                    DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.updateApn. Invalid ID (" + String.valueOf(id) + ").");
                    return false;
                }
            } catch (Exception e) {
                DebugLog.errorLog(APNUtils.DEBUGAPN, "APNUtils.updateApn error: ", e);
                return false;
            }
        } else {
            DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.updateApn. Invalid apn (null).");

            return false;
        }
    }

    public static boolean deleteApn(Context context, int id) {
        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.deleteApn.");

        if (id > -1) {
            try {
                DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.deleteApn. Reading APN id=" + String.valueOf(id));
                Uri APN_URI = Uri.parse("content://telephony/carriers");
                ContentResolver resolver = context.getContentResolver();

                DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.deleteApn. Erasing APN.");
                int result = resolver.delete(APN_URI, "_id = " + String.valueOf(id), null);

                if (result != -1) {
                    DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.deleteApn. APN deleted.");
                    return true;
                } else {
                    DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.deleteApn. Invalid ID (" + String.valueOf(id) + ").");
                    return false;
                }
            } catch (Exception e) {
                DebugLog.errorLog(APNUtils.DEBUGAPN, "APNUtils.deleteApn error: ", e);
                return false;
            }
        } else {
            DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.deleteApn. Invalid apn (-1).");

            return false;
        }
    }

    public static boolean verifyApn(Context context, String apn) {
        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.verifyApn.");
        return getApn(context, apn) > -1;
    }

    public static int getApn(Context context, String apn) {
        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.getApn.");
        int result = -1;

        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.getApn. Looking for APN " + apn);
        String columns[] = new String[]{"_ID", "NAME"};
        String where = "name = ?";
        String wargs[] = new String[]{apn};
        String sortOrder = null;
        try {
            Cursor cur = context.getContentResolver().query(APN_TABLE_URI, columns, where, wargs, sortOrder);

            if (cur != null) {
                int tableIndex = cur.getColumnIndex("_id");

                if (cur.moveToFirst()) {
                    DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.getApn. APN found.");
                    result = cur.getShort(tableIndex);
                }

                cur.close();
            }

            if (result == -1)
                DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.getApn. APN not found.");
        } catch (Exception e) {
            DebugLog.errorLog(APNUtils.DEBUGAPN, "APNutils.getApn exception", e);
        }
        return result;
    }

    public static short getPreferredApnID(Context context) {
        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.getPreferredApn.");
        Short id = -1;

        Cursor cur = context.getContentResolver().query(Uri.parse("content://telephony/carriers/current"), null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                int index = cur.getColumnIndex("_id");
                id = cur.getShort(index);
            }

            cur.close();
        }

        return id;
    }

    public static int setPreferredApn(Context context, String apn) {
        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn.");
        int changed = -1;

        try {
            DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. Looking for APN " + apn);
            String columns[] = new String[]{"_ID", "NAME"};
            String where = "name = ?";
            String wargs[] = new String[]{apn};
            String sortOrder = null;
            Cursor cur = context.getContentResolver().query(APN_TABLE_URI, columns, where, wargs, sortOrder);

            if (cur != null) {
                if (cur.moveToFirst()) {
                    DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. APN found. Setting as default.");
                    ContentValues values = new ContentValues(1);
                    values.put("apn_id", cur.getLong(0));

                    if (context.getContentResolver().update(APN_PREFER_URI, values, null, null) == 1) {
                        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. APN marked as default.");
                        changed = cur.getInt(0);
                    }
                }

                cur.close();
            }

            if (changed > -1)
                DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. APN not found or could not be marked as default.");
        } catch (Exception e) {
            DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. Error", e);
        }

        return changed;
    }

    public static boolean setPreferredApn(Context context, int apnId) {
        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn.");
        boolean changed = false;

        try {
            DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. Looking for APN " + String.valueOf(apnId));
            String columns[] = new String[]{"_ID", "NAME"};
            String where = "_ID = ?";
            String wargs[] = new String[]{String.valueOf(apnId)};
            String sortOrder = null;
            Cursor cur = context.getContentResolver().query(APN_TABLE_URI, columns, where, wargs, sortOrder);

            if (cur != null) {
                if (cur.moveToFirst()) {
                    DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. APN found. Setting as default.");
                    ContentValues values = new ContentValues(1);
                    values.put("apn_id", cur.getLong(0));

                    if (context.getContentResolver().update(APN_PREFER_URI, values, null, null) == 1) {
                        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. APN marked as default.");
                        changed = true;
                    }
                }

                cur.close();
            }

            if (!changed)
                DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. APN not found or could not be marked as default.");
        } catch (Exception e) {
            DebugLog.warningLog(APNUtils.DEBUGAPN, "APNutils.setPreferredApn. Error", e);
        }

        return changed;
    }

    public static APN[] getAllApnList(Context context) {
        return getAllApnList(context, false, "", "");
    }

    public static APN[] getAllApnList(Context context, String startedWith) {
        return getAllApnList(context, false, startedWith, "");
    }

    public static APN[] getAllApnList(Context context, boolean onlyCurrent, String startedWith) {
        return getAllApnList(context, onlyCurrent, startedWith, "");
    }

    public static APN[] getAllApnList(Context context, boolean onlyCurrent, String startedWith, String mnc) {
        DebugLog.infoLog(APNUtils.DEBUGAPN, "APNutils.getAllApnList.");

        APN[] result = null;
        Uri contentUri;
        String filtro = "";

        if (onlyCurrent) {
            contentUri = Uri.parse("content://telephony/carriers/preferapn");
            filtro = "current = ? ";
        } else {
            contentUri = Uri.parse("content://telephony/carriers/");
        }

        if (!startedWith.equals("")) {
            filtro += (filtro.equals("") ? "" : "and ") + "lower(name) like '" + startedWith.toLowerCase(Locale.getDefault()) + "%'";
        }

        if (!mnc.equals("")) {
            filtro += (filtro.equals("") ? "" : "and ") + "mcc = '" + Carrier.MCC_BRASIL + "' and mnc = '" + mnc + "'";
        }

        Cursor cursor = null;
        try {
            int elements = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 23 : 22);
            String[] fields = new String[elements];
            fields[0] = "_ID";
            fields[1] = "name";
            fields[2] = "apn";
            fields[3] = "mcc";
            fields[4] = "mnc";
            fields[5] = "numeric";
            fields[6] = "user";
            fields[7] = "password";
            fields[8] = "server";
            fields[9] = "proxy";
            fields[10] = "port";
            fields[11] = "mmsproxy";
            fields[12] = "mmsport";
            fields[13] = "mmsc";
            fields[14] = "type";
            fields[15] = "current";
            fields[16] = "authtype";
            fields[17] = "carrier_enabled";
            fields[18] = "bearer";
            fields[19] = "mvno_match_data";
            fields[20] = "mvno_type";
            fields[21] = "roaming_protocol";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fields[22] = "sub_id";
            }

            cursor = context.getContentResolver().query(contentUri, fields, (!filtro.equals("") ? filtro : null), (onlyCurrent ? new String[] {"1"} : null), null);
            if (cursor != null) {
                result = new APN[cursor.getCount()];
                int i = 0;

                while (cursor.moveToNext()) {
                    APN apn = new APN();

                    apn.setId(cursor.getInt(0));
                    apn.setName(cursor.getString(1));
                    apn.setApn(cursor.getString(2));
                    apn.setMcc(cursor.getString(3));
                    apn.setMnc(cursor.getString(4));
                    apn.setUser(cursor.getString(6));
                    apn.setPassword(cursor.getString(7));
                    apn.setServer(cursor.getString(8));
                    apn.setProxy(cursor.getString(9));
                    apn.setPort(cursor.getString(10));
                    apn.setMmsProxy(cursor.getString(11));
                    apn.setMmsPort(cursor.getString(12));
                    apn.setMmsc(cursor.getString(13));
                    apn.setType(cursor.getString(14));
                    apn.setCurrent(cursor.getString(15));
                    apn.setAuthType(cursor.getInt(16));
                    apn.setCarrier_enabled(cursor.getString(17));
                    apn.setBearer(cursor.getString(18));
                    apn.setMvno_match_data(cursor.getString(19));
                    apn.setMvno_type(cursor.getString(20));
                    apn.setRoaming_protocol(cursor.getString(21));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        apn.setSub_id(cursor.getString(22));
                    }

                    result[i] = apn;
                    i++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //Handle exceptions here
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result;
    }
}
