package org.pet.datadropfixer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 2;
	
	private static final String DATABASE_NAME = "DataDropFixer";
	
	public static final String TABLE_NAME = "preferences";
	
	private static final String DATA_DROP_TABLE_CREATE = "create table " + TABLE_NAME + " (_id integer primary key autoincrement, key text, value text);";

	public DBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATA_DROP_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
