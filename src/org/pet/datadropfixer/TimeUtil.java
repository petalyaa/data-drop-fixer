package org.pet.datadropfixer;

import android.content.Context;

public class TimeUtil {
	
	private static final int seconds = 1000;
	
	private static final int minutes = 60;
	
	private static final int hours = 60;
	
	public static final String getLabel(long time, Context context){
		StringBuilder sb = new StringBuilder();
		if(time < seconds){
			sb.append("< 1 ").append(context.getString(R.string.seconds));
		} else if (time < (seconds * minutes)){
			sb.append((time / seconds)).append(" ").append(context.getString(R.string.seconds));
		} else if(time < (seconds * minutes * hours)){
			sb.append((time / seconds) / minutes).append(" ").append(context.getString(R.string.minutes));
		} else {
			sb.append(((time /seconds)/minutes)/hours).append(" ").append(context.getString(R.string.hours));
		}
		return sb.toString();
	}
}
