package org.pet.datadropfixer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class DataConnectionManager {
	
	private Context context;

	public DataConnectionManager(Context context) {
		this.context = context;
	}

	// ------------------------------------------------------
	// ------------------------------------------------------
	public void switchState(boolean enable) {
		try{
			final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    final Class<?> conmanClass = Class.forName(conman.getClass().getName());
		    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		    iConnectivityManagerField.setAccessible(true);
		    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		    final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		    setMobileDataEnabledMethod.setAccessible(true);
		    setMobileDataEnabledMethod.invoke(iConnectivityManager, enable);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------
	// ------------------------------------------------------
	public boolean isEnabled() {
		Object connectivityService = context.getSystemService(Context.CONNECTIVITY_SERVICE); 
	    ConnectivityManager cm = (ConnectivityManager) connectivityService;
	    boolean isConnected = false;
	    try {
	        Class<?> c = Class.forName(cm.getClass().getName());
	        Method m = c.getDeclaredMethod("getMobileDataEnabled");
	        m.setAccessible(true);
	        isConnected = (Boolean)m.invoke(cm);
	    } catch (Exception e) {
	        e.printStackTrace();
	        isConnected = false;
	    }
	    return isConnected;
	}
	
	public boolean isConnectedThroughWifi(){
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = conMan.getNetworkInfo(1).getState();
		boolean isWifiConnected = false;
		if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) 
			isWifiConnected = true;
		return isWifiConnected;
	}

}
