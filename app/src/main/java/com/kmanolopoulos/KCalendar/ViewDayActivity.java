package com.kmanolopoulos.KCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ViewDayActivity extends Activity 
{
	private String SelectedDateAbbr;
	private String SelectedDateFull;
	private String SelectedDateMessage;
	private int    SelectedDay;
	private int    SelectedMonth;
	private int    SelectedYear;
	private String MonthNames[] = 
		{
			"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"
		};

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_day);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ExtractDate();
		
		DisplayDate();
		
		DisplaySchedule();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_day, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		setResult(Activity.RESULT_OK);
		finish();
	}
	
	protected void ExtractDate()
	{
		String   delim = "-";
		String[] tokens;
		
		// Get the message from the intent
        Intent intent = getIntent();
        SelectedDateAbbr = intent.getStringExtra("com.kmanolopoulos.KCalendar.ViewDayMessage");
        
        if (SelectedDateAbbr == null)
        {
        	return;
        }
        
        tokens = SelectedDateAbbr.split(delim);
        
        SelectedDay   = Integer.parseInt(tokens[0]);
        SelectedMonth = Integer.parseInt(tokens[1]) + 1;
        SelectedYear  = Integer.parseInt(tokens[2]);
        
        SelectedDateFull = tokens[0] + " " + 
        				   MonthNames[Integer.parseInt(tokens[1])] + " " + 
        				   tokens[2];
        
        SelectedDateMessage = getString(R.string.schedule_string) + " " + SelectedDateFull;
	}
	
	protected void DisplayDate()
	{
		((TextView)findViewById(R.id.CaptionText)).setText(SelectedDateMessage);
	}
	
	protected void DisplaySchedule()
	{
		DataFileBrowser browser = new DataFileBrowser(this);
		String ScheduleData;  
		
		ScheduleData = browser.ReadDateData(SelectedDay, SelectedMonth, SelectedYear);
		((TextView)findViewById(R.id.DataText)).setText(ScheduleData);
	}
	
	public void onClick(View v) 
	{
		String message;
		Intent intent = new Intent(this, EditDayActivity.class);

		message = SelectedDateAbbr;
	    
	    intent.putExtra("com.kmanolopoulos.KCalendar.EditDayMessage", message);
		startActivityForResult(intent, 1);
	}

}
