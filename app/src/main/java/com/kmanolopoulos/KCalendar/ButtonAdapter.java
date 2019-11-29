package com.kmanolopoulos.KCalendar;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ButtonAdapter extends BaseAdapter implements OnClickListener
{
    private Context   mContext;
    private final int GridPlaces = 49;
    private Button[]  board;
    private String    DayAbbr[] = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
     
    public ButtonAdapter(Context context) 
    {  
    	mContext = context;
    	board = new Button[GridPlaces];
    }  
     
    public int getCount() 
    {  
    	return GridPlaces;  
    }  
     
    public Object getItem(int position) 
    {  
    	return board[position];  
    }  
     
    public long getItemId(int position) 
    {  
    	return position;  
    }  
     
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	Calendar c = Calendar.getInstance();
    	int TodayDay = c.get(Calendar.DATE);
    	int TodayMonth = c.get(Calendar.MONTH);
    	int TodayYear = c.get(Calendar.YEAR);
    	int CurrentDay = 1;
    	int CurrentMonth = ((MainActivity)mContext).GetMonth();
    	int CurrentYear = ((MainActivity)mContext).GetYear();
    	MonthDisplayHelper mdh = new MonthDisplayHelper(CurrentYear, CurrentMonth, Calendar.SUNDAY);
    	View v;
    	Button b;

		// Days literals
		if ((position >= 0) && ((position < 7)))
		{
			v = LayoutInflater.from(mContext).inflate(R.layout.day_button_layout, null);
			b = (Button)v.findViewById(R.id.day_button_layout);
			board[position] = b;
			board[position].setText(DayAbbr[position]);
		}
		// Blank buttons
		else if ((position < (mdh.getOffset() + 7)) || (position  >= (mdh.getOffset() + mdh.getNumberOfDaysInMonth() + 7)))
		{
			v = LayoutInflater.from(mContext).inflate(R.layout.blank_button_layout, null);
			b = (Button)v.findViewById(R.id.blank_button_layout);
			board[position] = b;
			board[position].setText("");
		}
		// Days of month buttons
		else
		{
			CurrentDay = position - mdh.getOffset() - 6;
			v = LayoutInflater.from(mContext).inflate(R.layout.date_button_layout, null);
			b = (Button)v.findViewById(R.id.date_button_layout);
			((GradientDrawable)b.getBackground()).setColor(getDayColor(CurrentDay, CurrentMonth, CurrentYear));

			if ((CurrentDay == TodayDay) && (CurrentMonth == TodayMonth) && (CurrentYear == TodayYear))
			{
				((GradientDrawable)b.getBackground()).setStroke(2, Color.rgb(0, 0, 255));
			}
			else
			{
				((GradientDrawable)b.getBackground()).setStroke(0, Color.rgb(0, 0, 0));
			}

			board[position] = b;
			board[position].setText("" + CurrentDay);
			board[position].setOnClickListener(this);
		}

     	return v;  
	}
    
	private int getDayColor(int day, int month, int year) 
	{
		DataFileBrowser browser = new DataFileBrowser(mContext);
		String ScheduleData;
		int StartHour, RetColor;
		
		ScheduleData = browser.ReadDateData(day, month+1, year);

		if (ScheduleData.equals("No Information"))
		{
			RetColor = Color.rgb(68, 68, 68);
		}
		else if (ScheduleData.equals("Day Off"))
		{
			RetColor = Color.rgb(255, 0, 0);
		}
		else
		{
			StartHour = Integer.parseInt(ScheduleData.substring(0, 2));
			if (StartHour < 12)
			{
				RetColor = Color.rgb(160 - 13 * StartHour, 255, 160 - 13 * StartHour);
			}
			else
			{
				RetColor = Color.rgb(0, 255 - 13 * (StartHour - 12), 0);
			}
		}
		
		return RetColor;
	}

	@Override
	public void onClick(View v) 
	{
		String message;
		Intent intent = new Intent(mContext, ViewDayActivity.class);

		message = ((Button)v).getText() + "-" +
				  ((MainActivity)mContext).GetMonth() + "-" +
				  ((MainActivity)mContext).GetYear();

	    intent.putExtra("com.kmanolopoulos.KCalendar.ViewDayMessage", message);
		((MainActivity)mContext).startActivityForResult(intent, 1);
	}
}  