package com.kmanolopoulos.KCalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataFileBrowser extends SQLiteOpenHelper
{
    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scheduleDB";
	private final String TABLE_NAME = "schedule";
	private final String DATE_ENTRY = "date_entry";
	private final String TIME_ENTRY = "time_entry";
	
	public DataFileBrowser(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase dbase) 
	{
		String CREATE_TABLE = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
			"(" +
	        	DATE_ENTRY	+ " TEXT PRIMARY KEY, " +
	        	TIME_ENTRY	+ " TEXT " +
	        ")";
		
		dbase.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase dbase, int oldVersion, int newVersion) 
	{
        // Actions here only if database schema changes
	}
		
	public String ReadDateData(int day, int month, int year)
	{
		SQLiteDatabase dbase = this.getReadableDatabase();
		String   ARGS = String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
		String[] COLUMNS  = {DATE_ENTRY,TIME_ENTRY};
		String   SELECTION = DATE_ENTRY + " = ?";
		String[] SEL_ARGS = {ARGS};
		String   RESPONSE;
		
	    Cursor result = dbase.query(TABLE_NAME, COLUMNS, SELECTION, SEL_ARGS, null, null, null, null);

	    if (result == null)
	    {
	    	RESPONSE = "No Information";
	    }
	    else if (result.getCount() == 0)
	    {
	    	RESPONSE = "No Information";
	    	result.close();
	    }
	    else
	    {
	    	result.moveToFirst();
	    	RESPONSE = result.getString(1);
	    	result.close();
	    }
	    
	    dbase.close();

		return RESPONSE;
	}
	
	public void WriteDateData(int day, int month, int year, String data)
	{
		SQLiteDatabase dbase = this.getWritableDatabase();
		String   ARGS = String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
		String[] COLUMNS  = {DATE_ENTRY,TIME_ENTRY};
		String   SELECTION = DATE_ENTRY + " = ?";
		String[] SEL_ARGS = {ARGS};
		ContentValues INSERTVALUES = new ContentValues();
		ContentValues UPDATEVALUES = new ContentValues();
		
		Cursor result = dbase.query(TABLE_NAME, COLUMNS, SELECTION, SEL_ARGS, null, null, null, null);

		if (result == null)
	    {
			INSERTVALUES.put(DATE_ENTRY, ARGS);
			INSERTVALUES.put(TIME_ENTRY, data);
	    	dbase.insert(TABLE_NAME, null, INSERTVALUES);
	    }
		else if (result.getCount() == 0)
		{
			INSERTVALUES.put(DATE_ENTRY, ARGS);
			INSERTVALUES.put(TIME_ENTRY, data);
	    	dbase.insert(TABLE_NAME, null, INSERTVALUES);
	    	result.close();
		}
	    else
	    {
	    	UPDATEVALUES.put(TIME_ENTRY, data);
	    	dbase.update(TABLE_NAME, UPDATEVALUES, SELECTION, SEL_ARGS);
	    	result.close();
	    }
		
		dbase.close();

	    return;
	}

}
