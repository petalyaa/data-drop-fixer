package org.pet.datadropfixer;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoricalListAdapter extends BaseAdapter {
	
	private static final String TAG = "Adapter";
	
	private ArrayList<HistoricalData> data;
	
	private LayoutInflater inflater;
	
	private Context context;
	
	public HistoricalListAdapter(Context context, ArrayList<HistoricalData> data){
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data == null ? null : data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data == null ? null : data.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(convertView == null)
			view = inflater.inflate(R.layout.historical_items_view, null);
		HistoricalData historicalData = data.get(position);
		TextView dateOccur = (TextView) view.findViewById(R.id.historicalItemsDate);
		TextView timeToFix = (TextView) view.findViewById(R.id.historicalItemsTimeTaken);
		TextView timeOccur = (TextView) view.findViewById(R.id.historicalItemsTime);
		dateOccur.setText(ContextHolder.DATE_ONLY_FORMAT.format(historicalData.getDate()));
		timeOccur.setText(ContextHolder.TIME_ONLY_FORMAT.format(historicalData.getDate()));
		timeToFix.setText(TimeUtil.getLabel(historicalData.getTimeTakenToFix(), context));
		return view;
	}

	

}
