package org.pet.datadropfixer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "BootBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ContextHolder holder = ContextHolder.getInstance(context);
//		Toast.makeText(context, "Start on boot : " + holder.isStartOnBoot() + " and is service start " + holder.isServiceStart(), Toast.LENGTH_LONG).show();
		if(holder.isServiceStart() && holder.isStartOnBoot()){
			Log.v(TAG, "Starting up dataDropFixer service on boot");
			Intent serviceIntent = new Intent(context, DataDropFixerService.class);
			context.startService(serviceIntent);
			Toast.makeText(context, "Succesfully started DataDropService", Toast.LENGTH_LONG).show();
		} else if (!holder.isStartOnBoot()) {
			holder.setServiceStart(false);
		}
	}

}
