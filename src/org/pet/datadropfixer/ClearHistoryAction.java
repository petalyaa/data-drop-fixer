package org.pet.datadropfixer;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class ClearHistoryAction {
	
	private static final String TAG = "ClearAction";

	private View view;
	
	private Context parent;
	
	public ClearHistoryAction(View view, Context parent){
		this.view = view;
		this.parent = parent;
	}
	
	public boolean clear(){
		Log.v(TAG, "Clearing history");
		File file = Environment.getExternalStorageDirectory();
		if(file != null){
			Log.v(TAG, "Path to external storage found : " + file.getAbsolutePath());
		} else {
			Log.v(TAG, "Path to external not found.");
		}
		File logFile = new File(file, "DataDropFixer/drop.log");
		boolean result = false;
		if(logFile.delete()){
			result = true;
			RefreshHistoryAction refresh = new RefreshHistoryAction(view, parent);
			refresh.refresh();
		}
		return result;
	}

}
