package me.zeratul.darian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class CustomDarianDateActivity extends AbstractDarian {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Calendar default_date = Calendar.getInstance();
        int year = intent.getIntExtra("YEAR", default_date.get(Calendar.YEAR));
        int month = intent.getIntExtra("MONTH", default_date.get(Calendar.MONTH));
        int day = intent.getIntExtra("DAY", default_date.get(Calendar.DAY_OF_MONTH));
        int hour = intent.getIntExtra("HOUR", 0);
        int minute = intent.getIntExtra("MINUTE", 0);
        Calendar earth_date = new GregorianCalendar(year, month, day, hour, minute);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String type = prefs.getString("pref_calendar_type", "");
        MartianDate md = new MartianDate(earth_date, type);
        fill_layout(md, earth_date);

    }

}
