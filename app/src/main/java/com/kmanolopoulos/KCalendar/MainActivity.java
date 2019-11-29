package com.kmanolopoulos.KCalendar;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity 
{
	private TextView label;
	private GridView gridView;
	private Calendar cal;
	private ButtonAdapter adapter;
	private String   MonthNames[] = 
		{ 
			"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		gridView = (GridView)findViewById(R.id.calendar);
		cal = Calendar.getInstance();
    	label = (TextView)findViewById(R.id.currentmonth);
		adapter = new ButtonAdapter(this);

		gridView.setAdapter(adapter);

		DrawCalendar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	    	case R.id.action_about:
	    		Intent intent = new Intent(this, AboutActivity.class);
				startActivityForResult(intent, 1);
	    		return true;
	        case R.id.action_exit:
	            finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		DrawCalendar();
	}
	
	protected void DrawCalendar()
	{
		String value;

		value = MonthNames[GetMonth()];
		value = value.concat(" ");
		value = value.concat(String.valueOf(GetYear()));
		
		SetLabel(value);

		adapter.notifyDataSetChanged();
	}
	
	public int GetMonth()
	{
		return cal.get(Calendar.MONTH);
	}
	
	public int GetYear()
	{
		return cal.get(Calendar.YEAR);
	}
	
	public void IncreaseMonth(View v)
	{
		cal.add(Calendar.MONTH, 1);
		DrawCalendar();
	}
	
	public void DecreaseMonth(View v)
	{
		cal.add(Calendar.MONTH, -1);
		DrawCalendar();
	}
	
	public String GetLabel()
	{
		return (String)label.getText();
	}
	
	public void SetLabel(String value)
	{
		label.setText(value);
	}

}
