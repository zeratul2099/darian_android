package me.zeratul.darian;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class Darian extends AbstractDarian {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler h = new Handler();
        final Timer timer = new Timer();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        TimerTask timerTask = new TimerTask() {
            public void run() {
                h.post(new Runnable() {
                    Calendar date;
                    MartianDate md;
                    @Override
                    public void run() {
                        date = Calendar.getInstance();

                        final String type = prefs.getString("pref_calendar_type", "");
                        md = new MartianDate(date, type);
                        fill_layout(md, date);

                    }
                });
            }
        };
        timer.schedule(timerTask, 100, 100);

    }






}
