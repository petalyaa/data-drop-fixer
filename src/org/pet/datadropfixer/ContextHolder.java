package org.pet.datadropfixer;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ContextHolder {
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.US);
	
	public static final SimpleDateFormat DATE_ONLY_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
	
	public static final SimpleDateFormat TIME_ONLY_FORMAT = new SimpleDateFormat("hh:mm:ss", Locale.US);

	private int checkingInterval;

	private boolean isServiceStart;

	private boolean hasAnyChanges;

	private boolean fixUsingAirplaneMode;

	private String logFilePath;

	private DBHelper dbHelper;

	private long offset;

	private boolean startOnBoot;

	private int intervalTimeType;

	public static final int SECONDS = 0;

	public static final int MINUTES = 1;

	public static final int HOURS = 2;

	public static final long SECONDS_OFFSET = 1000;

	public static final long MINUTES_OFFSET = 1000 * 60;

	public static final long HOURS_OFFSET = 1000 * 60 * 60;

	private static final String TAG = "DataDropFixerDatabase";

	private static ContextHolder instance;

	private ContextHolder(Context context){
		this.dbHelper = new DBHelper(context);
		init();
	}

	public static final ContextHolder getInstance(Context context){
		if(instance == null)
			instance = new ContextHolder(context);
		return instance;
	}

	private void insertNew(SQLiteDatabase db, String key, String value){
		ContentValues contentValue = new ContentValues();
		contentValue.put("key", key);
		contentValue.put("value", value);
		db.insert(DBHelper.TABLE_NAME, null, contentValue);
	}

	private void init(){

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		//db.delete(DBHelper.TABLE_NAME, null, null);

		// Get offset
		Cursor cursor = db.query(DBHelper.TABLE_NAME, new String[]{"key","value"}, "key=" + "'offset'", null, null, null, "key");
		String offsetStr = null;
		try{
			if(cursor.moveToNext()){
				offsetStr = cursor.getString(1);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(offsetStr == null){
			Log.v(TAG, "Checking offset is null in the database. Insert default value");
			insertNew(db, "offset", "60000");
			offsetStr = "60000";
		}
		setOffset(Long.parseLong(offsetStr));

		// Get interval time type
		db = dbHelper.getWritableDatabase();
		cursor = db.query(DBHelper.TABLE_NAME, new String[]{"key","value"}, "key=" + "'intervalTimeType'", null, null, null, "key");
		String intervalTimeTypeStr = null;
		try{
			if(cursor.moveToNext()){
				intervalTimeTypeStr = cursor.getString(1);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(intervalTimeTypeStr == null){
			Log.v(TAG, "Checking interval time type is null in the database. Insert default value");
			insertNew(db, "intervalTimeType", "2");
			intervalTimeTypeStr = "2";
		}
		setIntervalTimeType(Integer.parseInt(intervalTimeTypeStr));

		// Get checking interval 
		db = dbHelper.getWritableDatabase();
		cursor = db.query(DBHelper.TABLE_NAME, new String[]{"key","value"}, "key=" + "'checkingInterval'", null, null, null, "key");
		String checkingIntervalStr = null;
		try{
			if(cursor.moveToNext()){
				checkingIntervalStr = cursor.getString(1);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(checkingIntervalStr == null){
			Log.v(TAG, "Checking interval value is null in the database. Insert default value");
			insertNew(db, "checkingInterval", "5");
			checkingIntervalStr = "5";
		}
		setCheckingInterval(Integer.parseInt(checkingIntervalStr));

		// Get isservice start
		db = dbHelper.getWritableDatabase();
		cursor = db.query(DBHelper.TABLE_NAME, new String[]{"key","value"}, "key=" + "'isServiceStart'", null, null, null, "key");
		String isServiceStartStr = null;
		try{
			if(cursor.moveToNext()){
				isServiceStartStr = cursor.getString(1);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(isServiceStartStr == null){
			Log.v(TAG, "Is Service Start value is null in the database. Insert default value");
			insertNew(db, "isServiceStart", "false");
			isServiceStartStr = "false";
		}
		setServiceStart(Boolean.valueOf(isServiceStartStr));

		// Get hasanychanges
		db = dbHelper.getWritableDatabase();
		cursor = db.query(DBHelper.TABLE_NAME, new String[]{"key","value"}, "key=" + "'hasAnyChange'", null, null, null, "key");
		String hasAnyChangeStr = null;
		try{
			if(cursor.moveToNext()){
				hasAnyChangeStr = cursor.getString(1);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(hasAnyChangeStr == null){
			Log.v(TAG, "Has Any Change value is null in the database. Insert default value");
			insertNew(db, "hasAnyChange", "false");
			hasAnyChangeStr = "false";
		}
		setHasAnyChanges(Boolean.valueOf(hasAnyChangeStr));

		// Get logfilepath
		db = dbHelper.getWritableDatabase();
		cursor = db.query(DBHelper.TABLE_NAME, new String[]{"key","value"}, "key=" + "'logFilePath'", null, null, null, "key");
		String logFilePath = null;
		try{
			if(cursor.moveToNext()){
				logFilePath = cursor.getString(1);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(logFilePath == null){
			Log.v(TAG, "Log file path value is null in the database. Insert default value");
			insertNew(db, "logFilePath", "/DataDropFixer/drop.log");
			logFilePath = "/DataDropFixer/drop.log";
		}
		setLogFilePath(logFilePath);

		// Get start on boot
		db = dbHelper.getWritableDatabase();
		cursor = db.query(DBHelper.TABLE_NAME, new String[]{"key","value"}, "key=" + "'startOnBoot'", null, null, null, "key");
		String startOnBootStr = null;
		try{
			if(cursor.moveToNext()){
				startOnBootStr = cursor.getString(1);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(startOnBootStr == null){
			Log.v(TAG, "Start on boot value is null in the database. Insert default value");
			insertNew(db, "startOnBoot", "false");
			startOnBootStr = "false";
		}
		setStartOnBoot(Boolean.valueOf(startOnBootStr));

		// Get start on boot
		db = dbHelper.getWritableDatabase();
		cursor = db.query(DBHelper.TABLE_NAME, new String[]{"key","value"}, "key=" + "'fixUsingAirplaneMode'", null, null, null, "key");
		String fixUsingAirplaneModeStr = null;
		try{
			if(cursor.moveToNext()){
				fixUsingAirplaneModeStr = cursor.getString(1);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(fixUsingAirplaneModeStr == null){
			Log.v(TAG, "Fix using airplane mode value is null in the database. Insert default value");
			insertNew(db, "fixUsingAirplaneMode", "false");
			fixUsingAirplaneModeStr = "false";
		}
		setFixUsingAirplaneMode(Boolean.valueOf(fixUsingAirplaneModeStr));

		db.close();
	}

	public int getCheckingInterval() {
		return checkingInterval;
	}

	public void setCheckingInterval(int checkingInterval) {
		this.checkingInterval = checkingInterval;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValue = new ContentValues();
		contentValue.put("key", "checkingInterval");
		contentValue.put("value", String.valueOf(checkingInterval));
		db.update(DBHelper.TABLE_NAME, contentValue, "key='checkingInterval'", null);
		db.close();
	}

	public boolean isServiceStart() {
		return isServiceStart;
	}

	public void setServiceStart(boolean isServiceStart) {
		this.isServiceStart = isServiceStart;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValue = new ContentValues();
		contentValue.put("key", "isServiceStart");
		contentValue.put("value", String.valueOf(isServiceStart));
		db.update(DBHelper.TABLE_NAME, contentValue, "key='isServiceStart'", null);
		db.close();
	}

	public boolean isHasAnyChanges() {
		return hasAnyChanges;
	}

	public void setHasAnyChanges(boolean hasAnyChanges) {
		this.hasAnyChanges = hasAnyChanges;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValue = new ContentValues();
		contentValue.put("key", "hasAnyChange");
		contentValue.put("value", String.valueOf(hasAnyChanges));
		db.update(DBHelper.TABLE_NAME, contentValue, "key='hasAnyChange'", null);
		db.close();
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValue = new ContentValues();
		contentValue.put("key", "logFilePath");
		contentValue.put("value", String.valueOf(logFilePath));
		db.update(DBHelper.TABLE_NAME, contentValue, "key='logFilePath'", null);
		db.close();
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValue = new ContentValues();
		contentValue.put("key", "offset");
		contentValue.put("value", offset);
		db.update(DBHelper.TABLE_NAME, contentValue, "key='offset'", null);
		db.close();
	}

	public int getIntervalTimeType() {
		return intervalTimeType;
	}

	public void setIntervalTimeType(int intervalTimeType) {
		this.intervalTimeType = intervalTimeType;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValue = new ContentValues();
		contentValue.put("key", "intervalTimeType");
		contentValue.put("value", intervalTimeType);
		db.update(DBHelper.TABLE_NAME, contentValue, "key='intervalTimeType'", null);
		db.close();
	}

	public boolean isStartOnBoot() {
		return startOnBoot;
	}

	public void setStartOnBoot(boolean startOnBoot) {
		this.startOnBoot = startOnBoot;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValue = new ContentValues();
		contentValue.put("key", "startOnBoot");
		contentValue.put("value", String.valueOf(startOnBoot));
		db.update(DBHelper.TABLE_NAME, contentValue, "key='startOnBoot'", null);
		db.close();
	}

	public boolean isFixUsingAirplaneMode() {
		return fixUsingAirplaneMode;
	}

	public void setFixUsingAirplaneMode(boolean fixUsingAirplaneMode) {
		this.fixUsingAirplaneMode = fixUsingAirplaneMode;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValue = new ContentValues();
		contentValue.put("key", "fixUsingAirplaneMode");
		contentValue.put("value", String.valueOf(fixUsingAirplaneMode));
		db.update(DBHelper.TABLE_NAME, contentValue, "key='fixUsingAirplaneMode'", null);
		db.close();
	}

}
