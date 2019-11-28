package jv.android.utils;

import jv.android.utils.interfaces.IEulaResult;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class Eula {

	private IEulaResult mActivity; 

	private String appName = "";
	private int eulaResource = -1;
	private int updatesResource = -1;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getEulaResource() {
		return eulaResource;
	}

	public void setEulaResource(int eulaResource) {
		this.eulaResource = eulaResource;
	}

	public int getUpdatesResource() {
		return updatesResource;
	}

	public void setUpdatesResource(int updatesResource) {
		this.updatesResource = updatesResource;
	}

	public Eula(IEulaResult context) {
		mActivity = context; 
	}

	private PackageInfo getPackageInfo() {
		PackageInfo pi = null;
		try {
			pi = ((Context)mActivity).getPackageManager().getPackageInfo(((Context)mActivity).getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi; 
	}

	public void show() {
		PackageInfo versionInfo = getPackageInfo();

		// Show the Eula
		String title = appName + " v" + versionInfo.versionName;

		//Includes the updates as well so users know what changed. 
		String message = ((Context)mActivity).getString(updatesResource) + "\n\n" + ((Context)mActivity).getString(eulaResource);

		AlertDialog.Builder builder = new AlertDialog.Builder((Context)mActivity)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				// Mark this version as read.
				mActivity.onEulaAccepted();
			}
		})
		.setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Close the activity as they have declined the EULA
				mActivity.onEulaRejected();
			}

		});
		builder.create().show();
	}

}
