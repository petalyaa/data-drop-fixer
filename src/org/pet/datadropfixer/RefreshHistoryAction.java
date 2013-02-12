package org.pet.datadropfixer;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class RefreshHistoryAction {
	
	private static final String TAG = "RefreshAction";

	private View view;
	
	private Context parent;
	
	public RefreshHistoryAction(View view, Context parent){
		this.view = view;
		this.parent = parent;
	}
	
	public boolean refresh(){
		Log.v(TAG, "Refreshing history");
		RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.mainNoHistory);
		HistoryFileReader historyFileReader = new HistoryFileReader();
		ListView listView = (ListView) view.findViewById(R.id.historyListItems);
		ArrayList<HistoricalData> historicalDataList = historyFileReader.getHistoricalDataList();
		if(historicalDataList.size() <= 0){
			relativeLayout.setVisibility(View.VISIBLE);
		} else {
			relativeLayout.setVisibility(View.GONE);
		}
		Collections.sort(historicalDataList);
		HistoricalListAdapter adapter = new HistoricalListAdapter(parent, historicalDataList);
		listView.setAdapter(adapter);
		return true;
	}
	
}
