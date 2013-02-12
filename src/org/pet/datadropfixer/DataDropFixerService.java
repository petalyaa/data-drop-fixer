package org.pet.datadropfixer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class DataDropFixerService extends Service {
	
	private static final String TAG_SERVICE = "DataDropFixer_Service";

	private File logFile;
	
	private ContextHolder holder;
	
	public DataDropFixerService(){
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// code to execute when the service is first created
	}

	@Override
	public void onDestroy() {
		// code to execute when the service is shutting down
		holder.setServiceStart(false);
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		holder = ContextHolder.getInstance(getApplicationContext());
		holder.setServiceStart(true);
		Thread t = new CheckingThread(getBaseContext());
		t.start();
		
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/DataDropFixer");
		if(!dir.exists())
			dir.mkdirs();
		logFile = new File(dir, "drop.log");
	}
	
	private class CheckingThread extends Thread {
		
		private static final String host = "www.google.com";
		
		private Context context;
		
		public CheckingThread(Context context){
			this.context = context;
		}
		
		public void run(){
			try {
				DataConnectionManager connManager = new DataConnectionManager(context);
				while (true) {
					Log.v(TAG_SERVICE, "Checking connection for data drop.");
					
					boolean isConnected = isReachable(host);
					Log.v(TAG_SERVICE, "Connection status : " + (isConnected ? "connected" : "not connected"));
					
					if(!connManager.isConnectedThroughWifi() && !isConnected){
						Log.d(TAG_SERVICE, "Re-connect to mobile connection.");
						if(connManager.isEnabled()){
							Log.d(TAG_SERVICE, "Network is connected. Data drop occur. >>>>>>>>>>>>>>>>>>>>>>>>>");
							long startRecovery = System.currentTimeMillis();
							fixIt(connManager);
							long endRecovery = System.currentTimeMillis();
							try{
								logToFile(endRecovery - startRecovery);
							} catch (Exception e){
								e.printStackTrace();
								Log.e(TAG_SERVICE, "Fail to update historical data for data drop.");
							}
							Log.d(TAG_SERVICE, ">>>>>> Succefully recovered from data drop. Time taken to recover : " + (endRecovery - startRecovery) + " ms");
						} else {
							Log.d(TAG_SERVICE, "Network is not connected. Possibly manually turned off. Do nothing for now.");
						}
					}
					if(connManager.isConnectedThroughWifi())
						Log.d(TAG_SERVICE, "Connection is through wifi, no need to check for data drop.");
					if(!holder.isServiceStart())
						break;

					long sleepDuration = holder.getCheckingInterval() * holder.getOffset();
					Log.v(TAG_SERVICE, "Sleeping for " + sleepDuration + " miliseconds");
					Thread.sleep(sleepDuration);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		private void fixIt(DataConnectionManager connManager) throws InterruptedException{
//			if(!holder.isFixUsingAirplaneMode()){
				fixByNormalMethod(connManager);
//			} 
		}
		
		private void fixByNormalMethod(DataConnectionManager connManager) throws InterruptedException{
			connManager.switchState(false);
			while(connManager.isEnabled()){
				Thread.sleep(1000);
				Log.d(TAG_SERVICE, "Waiting for connection to be close.");
			}
			connManager.switchState(true);
		}
		
		
		
		private void logToFile(long recoveryTimeMs){
			FileWriter fileWriter = null;
			BufferedWriter writer = null;
			Date dateNow = new Date();
			try {
				fileWriter = new FileWriter(logFile, true);
				writer = new BufferedWriter(fileWriter);
				writer.append(ContextHolder.DATE_FORMAT.format(dateNow) + "|" + recoveryTimeMs);
				writer.append(System.getProperty("line.separator"));
				holder.setHasAnyChanges(true);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(writer != null)
						writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(fileWriter != null)
						fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		private boolean isReachable(String host){
			Socket socket = null;
			boolean reachable = false;
			try {
				socket = new Socket(host, 80);
				reachable = true;
			} catch (UnknownHostException e) {
			} catch (IOException e) {
			} finally {
				if (socket != null)
					try {
						socket.close();
					} catch (IOException e) {
					}
			}
			return reachable;
		}
		
	}

}
