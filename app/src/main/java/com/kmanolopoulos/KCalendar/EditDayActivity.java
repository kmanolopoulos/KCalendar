package com.kmanolopoulos.KCalendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class EditDayActivity extends Activity  implements TimePicker.OnTimeChangedListener
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
		setContentView(R.layout.activity_edit_day);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		InitUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_day, menu);
		return true;
	}
	
	protected void InitUI()
	{
		DataFileBrowser browser = new DataFileBrowser(this);
		String ScheduleData;

		ExtractDate();
		DisplayDate();
		
		((TimePicker)findViewById(R.id.timePicker1)).setOnTimeChangedListener(this);
		((TimePicker)findViewById(R.id.timePicker2)).setOnTimeChangedListener(this);
		
		((TimePicker)findViewById(R.id.timePicker1)).setIs24HourView(true);
		((TimePicker)findViewById(R.id.timePicker2)).setIs24HourView(true);
		
		ScheduleData = browser.ReadDateData(SelectedDay, SelectedMonth, SelectedYear);
		
		if (ScheduleData.equals("No Information"))
		{
			((TimePicker)findViewById(R.id.timePicker1)).setCurrentHour(7);
			((TimePicker)findViewById(R.id.timePicker1)).setCurrentMinute(0);
			((TimePicker)findViewById(R.id.timePicker2)).setCurrentHour(15);
			((TimePicker)findViewById(R.id.timePicker2)).setCurrentMinute(0);
			((TimePicker)findViewById(R.id.timePicker1)).setEnabled(true);
	    	((TimePicker)findViewById(R.id.timePicker2)).setEnabled(true);
			((ToggleButton)findViewById(R.id.DayOff)).setChecked(false);
			((GradientDrawable)((ToggleButton)findViewById(R.id.DayOff)).getBackground()).setColor(Color.rgb(0, 128, 0));
		}
		else if (ScheduleData.equals("Day Off"))
		{
			((TimePicker)findViewById(R.id.timePicker1)).setCurrentHour(7);
			((TimePicker)findViewById(R.id.timePicker1)).setCurrentMinute(0);
			((TimePicker)findViewById(R.id.timePicker2)).setCurrentHour(15);
			((TimePicker)findViewById(R.id.timePicker2)).setCurrentMinute(0);
			((TimePicker)findViewById(R.id.timePicker1)).setEnabled(false);
	    	((TimePicker)findViewById(R.id.timePicker2)).setEnabled(false);
			((ToggleButton)findViewById(R.id.DayOff)).setChecked(true);
			((GradientDrawable)((ToggleButton)findViewById(R.id.DayOff)).getBackground()).setColor(Color.rgb(255, 0, 0));
   		}
		else
		{
			((TimePicker)findViewById(R.id.timePicker1)).setCurrentHour(Integer.parseInt(ScheduleData.substring(0, 2)));
			((TimePicker)findViewById(R.id.timePicker1)).setCurrentMinute(Integer.parseInt(ScheduleData.substring(3, 5)));
			((TimePicker)findViewById(R.id.timePicker2)).setCurrentHour(Integer.parseInt(ScheduleData.substring(8, 10)));
			((TimePicker)findViewById(R.id.timePicker2)).setCurrentMinute(Integer.parseInt(ScheduleData.substring(11, 13)));
			((TimePicker)findViewById(R.id.timePicker1)).setEnabled(true);
	    	((TimePicker)findViewById(R.id.timePicker2)).setEnabled(true);
			((ToggleButton)findViewById(R.id.DayOff)).setChecked(false);
			((GradientDrawable)((ToggleButton)findViewById(R.id.DayOff)).getBackground()).setColor(Color.rgb(0, 128, 0));
		}
	}
	
	protected void ExtractDate()
	{
		String   delim = "-";
		String[] tokens;
		
		// Get the message from the intent
        Intent intent = getIntent();
        SelectedDateAbbr = intent.getStringExtra("com.kmanolopoulos.KCalendar.EditDayMessage");
        
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
	
	public void OKChoice(View v) 
	{
		DataFileBrowser browser = new DataFileBrowser(this);
		boolean DayOff;
		String TimeFrom, TimeTo;
		String data;
		
		DayOff = ((ToggleButton)findViewById(R.id.DayOff)).isChecked();

		TimeFrom = String.format("%02d:%02d", 
				((TimePicker)findViewById(R.id.timePicker1)).getCurrentHour(),
				((TimePicker)findViewById(R.id.timePicker1)).getCurrentMinute()); 
		TimeTo =  String.format("%02d:%02d", 
				((TimePicker)findViewById(R.id.timePicker2)).getCurrentHour(),
				((TimePicker)findViewById(R.id.timePicker2)).getCurrentMinute());
		
		if (DayOff)
		{
			data = "Day Off";
		}
		else
		{
			data = TimeFrom + " - " + TimeTo;
		}
		
		browser.WriteDateData(SelectedDay, SelectedMonth, SelectedYear, data);

		setResult(Activity.RESULT_OK);
	    finish();
	}
	
	public void CancelChoice(View v)
	{
		setResult(Activity.RESULT_CANCELED);
	    finish();
	}
	
	public void onToggleClicked(View view) 
	{
	    if(((ToggleButton)view).isChecked()) 
	    {
	    	((TimePicker)findViewById(R.id.timePicker1)).setEnabled(false);
	    	((TimePicker)findViewById(R.id.timePicker2)).setEnabled(false);
	    	((GradientDrawable)((ToggleButton)view).getBackground()).setColor(Color.rgb(255, 0, 0));
	    }
	    else
	    {
	    	((TimePicker)findViewById(R.id.timePicker1)).setEnabled(true);
	    	((TimePicker)findViewById(R.id.timePicker2)).setEnabled(true);
	    	((GradientDrawable)((ToggleButton)view).getBackground()).setColor(Color.rgb(0, 128, 0));
	    }    
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) 
	{
		if (view.equals(((TimePicker)findViewById(R.id.timePicker1))))
		{
			((TimePicker)findViewById(R.id.timePicker2)).setCurrentHour((view.getCurrentHour() + 8) % 24);
			((TimePicker)findViewById(R.id.timePicker2)).setCurrentMinute(view.getCurrentMinute());
		}
		
	}
}
