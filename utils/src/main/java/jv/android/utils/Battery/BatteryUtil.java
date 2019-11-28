package jv.android.utils.Battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by joubert on 03/03/18.
 */

public class BatteryUtil {

    public static Battery getBatteryCurrentStatus(Context context) {
        Battery b = new Battery();

        Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        b.setRawLevel(batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
        b.setScale(batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1));
        int level = 0;

        // Error checking that probably isn't needed but I added just in case.
        if (b.getRawLevel() == -1 || b.getScale() == -1) {
            level = 50;
        } else {
            level = (int) (((float)b.getRawLevel() / (float)b.getScale()) * 100.0f);
        }
        b.setLevel(level);

        b.setChargePlug(batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1));
        b.setUsbCharge(b.getChargePlug() == BatteryManager.BATTERY_PLUGGED_USB);
        b.setAcCharge(b.getChargePlug() == BatteryManager.BATTERY_PLUGGED_AC);
        b.setWirelessCharge(b.getChargePlug() == BatteryManager.BATTERY_PLUGGED_WIRELESS);
        b.setPlugged(b.isUsbCharge() ? Battery.USB_CHARGE : b.isAcCharge() ? Battery.AC_CHARGE : b.isWirelessCharge() ? Battery.WIRELESS_CHARGE : Battery.NO_CHARGE);

        b.setStatus(batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1));
        b.setCharging(b.getStatus() == BatteryManager.BATTERY_STATUS_CHARGING || b.getStatus() == BatteryManager.BATTERY_STATUS_FULL);
        b.setFull(b.getStatus() == BatteryManager.BATTERY_STATUS_FULL);

        b.setEhealth(batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1));
        b.setCold(b.getEhealth() == BatteryManager.BATTERY_HEALTH_COLD);
        b.setGood(b.getEhealth() == BatteryManager.BATTERY_HEALTH_GOOD);
        b.setDead(b.getEhealth() == BatteryManager.BATTERY_HEALTH_DEAD);
        b.setOverVoltage(b.getEhealth() == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE);
        b.setOverHeat(b.getEhealth() == BatteryManager.BATTERY_HEALTH_OVERHEAT);

        b.setHealth(b.isCold() ? Battery.HEALTH_COLD : b.isGood() ? Battery.HEALTH_GOOD : b.isDead()
                ? Battery.HEALTH_DEAD : b.isOverVoltage() ? Battery.HEALTH_OVER_VOLTAGE : b.isOverHeat()
                ? Battery.HEALTH_OVER_HEAT : Battery.HEALTH_UNKNOW);

        return b;
    }
}
