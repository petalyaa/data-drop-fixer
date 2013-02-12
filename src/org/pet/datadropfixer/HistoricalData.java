package org.pet.datadropfixer;

import java.util.Date;


public class HistoricalData implements Comparable<HistoricalData>{
	
	private int id;
	
	private Date date;
	
	private long timeTakenToFix;
	
	public HistoricalData(int id, Date date, long timeTakenToFix){
		this.id = id;
		this.date = date;
		this.timeTakenToFix = timeTakenToFix;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getTimeTakenToFix() {
		return timeTakenToFix;
	}

	public void setTimeTakenToFix(long timeTakenToFix) {
		this.timeTakenToFix = timeTakenToFix;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(HistoricalData another) {
		Date dateAnother = another.getDate();
		Date thisDate = getDate();
		if(dateAnother == null)
			dateAnother = new Date();
		if(thisDate == null)
			thisDate = new Date();
		return dateAnother.compareTo(thisDate);
	}
	
}
