package org.pet.datadropfixer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class HistoryFileReader {

	private static final String TAG = "FileReader";
	
	public HistoryFileReader(){
		
	}
	
	public ArrayList<HistoricalData> getHistoricalDataList(){
		ArrayList<HistoricalData> historicalDataList = new ArrayList<HistoricalData>();
		File file = Environment.getExternalStorageDirectory();
		if(file != null){
			Log.v(TAG, "Path to external storage found : " + file.getAbsolutePath());
		} else {
			Log.v(TAG, "Path to external not found.");
		}
		File logFile = new File(file, "DataDropFixer/drop.log");
		if(logFile.exists()){
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;
			try {
				fileReader = new FileReader(logFile);
				bufferedReader = new BufferedReader(fileReader);
				String line;
				int i = 0;
				while((line = bufferedReader.readLine()) != null){
					String dateOccurStr = null;
					String timeTakenStr = null;
					if(line.contains("|")){
						String[] arr = line.split("\\|");
						dateOccurStr = arr[0];
						timeTakenStr = arr[1];
					} else {
						dateOccurStr = line;
						timeTakenStr = "0";
					}
					try {
						Date dateOccur = dateOccurStr != null ? ContextHolder.DATE_FORMAT.parse(dateOccurStr.trim()) : null;
						long timeTaken = timeTakenStr != null ? Long.parseLong(timeTakenStr.trim()) : 0;
						HistoricalData historicalData = new HistoricalData(i, dateOccur, timeTaken);
						historicalDataList.add(historicalData);
						i++;
					} catch (Exception e) {
						Log.e(TAG, "Fail to parse log date", e);
					}
					
				}
			} catch (FileNotFoundException e) {
				Log.e(TAG, "Fail to read file", e);
			} catch (IOException e) {
				Log.e(TAG, "Fail to read file", e);
			} finally {
				try {
					if(bufferedReader != null)
						bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(fileReader != null)
						fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return historicalDataList;
	}
	
}
