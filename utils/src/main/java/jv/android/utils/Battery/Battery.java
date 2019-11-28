package jv.android.utils.Battery;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by joubert on 03/03/18.
 */

public class Battery {

    public static final String USB_CHARGE = "U";
    public static final String AC_CHARGE = "T";
    public static final String WIRELESS_CHARGE = "W";
    public static final String NO_CHARGE = "N";

    public static final String HEALTH_COLD = "G";
    public static final String HEALTH_GOOD = "B";
    public static final String HEALTH_DEAD = "M";
    public static final String HEALTH_OVER_VOLTAGE = "V";
    public static final String HEALTH_OVER_HEAT = "Q";
    public static final String HEALTH_UNKNOW = "N";

    private int rawLevel;
    private int scale;
    private int level;
    private int chargePlug;
    private boolean usbCharge;
    private boolean acCharge;
    private boolean wirelessCharge;
    private String plugged ;
    private int status;
    private boolean isCharging;
    private boolean isFull;
    private int ehealth;
    private boolean isCold;
    private boolean isGood;
    private boolean isDead;
    private boolean isOverVoltage;
    private boolean isOverHeat;
    private String health;

    public void setRawLevel(int rawLevel) {
        this.rawLevel = rawLevel;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setChargePlug(int chargePlug) {
        this.chargePlug = chargePlug;
    }

    public void setUsbCharge(boolean usbCharge) {
        this.usbCharge = usbCharge;
    }

    public void setAcCharge(boolean acCharge) {
        this.acCharge = acCharge;
    }

    public void setWirelessCharge(boolean wirelessCharge) {
        this.wirelessCharge = wirelessCharge;
    }

    public void setPlugged(String plugged) {
        this.plugged = plugged;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public void setEhealth(int ehealth) {
        this.ehealth = ehealth;
    }

    public void setCold(boolean cold) {
        isCold = cold;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void setOverVoltage(boolean overVoltage) {
        isOverVoltage = overVoltage;
    }

    public void setOverHeat(boolean overHeat) {
        isOverHeat = overHeat;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public Battery() {

        rawLevel= -1;
        scale = -1;
        level = -1;
        chargePlug = -1;
        usbCharge = false;
        acCharge = false;
        wirelessCharge = false;
        plugged = "";
        status = -1;
        isCharging = false;
        isFull = false;
        ehealth = -1;
        isCold = false;
        isGood = false;
        isDead = false;
        isOverVoltage = false;
        isOverHeat = false;
        health = "";
    }


    public int getRawLevel() {
        return rawLevel;
    }

    public int getScale() {
        return scale;
    }

    public int getLevel() {
        return level;
    }

    public int getChargePlug() {
        return chargePlug;
    }

    public boolean isUsbCharge() {
        return usbCharge;
    }

    public boolean isAcCharge() {
        return acCharge;
    }

    public boolean isWirelessCharge() {
        return wirelessCharge;
    }

    public String getPlugged() {
        return plugged;
    }

    public int getStatus() {
        return status;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public boolean isFull() {
        return isFull;
    }

    public int getEhealth() {
        return ehealth;
    }

    public boolean isCold() {
        return isCold;
    }

    public boolean isGood() {
        return isGood;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isOverVoltage() {
        return isOverVoltage;
    }

    public boolean isOverHeat() {
        return isOverHeat;
    }

    public String getHealth() {
        return health;
    }
}
