package me.zeratul.darian;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by zeratul on 23.06.15.
 */
public abstract class AbstractDarian extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public int picked_year;
    public int picked_month;
    public int picked_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_darian);
        LinearLayout layout = (LinearLayout)findViewById(R.id.main_layout);
        layout.setOrientation(getResources().getConfiguration().orientation);
    }

    public void fill_layout(MartianDate martian_date, Calendar earth_date) {



        final TextView sol_textview = (TextView)findViewById(R.id.sol_of_week);
        sol_textview.setText(martian_date.week_sol_name);
        final TextView martian_date_textview = (TextView)findViewById(R.id.martian_date);
        martian_date_textview.setText(String.format("%02d. %s %d",
                martian_date.sol, martian_date.month_name, martian_date.year));
        final TextView martian_time = (TextView)findViewById(R.id.martian_time);
        martian_time.setText(String.format("%02d:%02d:%02d",
                martian_date.hour, martian_date.min, martian_date.sec));

        final TextView day_textview = (TextView)findViewById(R.id.day_of_week);
        day_textview.setText(earth_date.getDisplayName(Calendar.DAY_OF_WEEK,
                Calendar.LONG,
                Locale.getDefault()));
        final TextView earth_date_textview = (TextView)findViewById(R.id.earth_date);
        earth_date_textview.setText(String.format("%02d. %s %04d",
                earth_date.get(Calendar.DAY_OF_MONTH),
                earth_date.getDisplayName(Calendar.MONTH, Calendar.LONG,
                        Locale.getDefault()),
                earth_date.get(Calendar.YEAR)));
        final TextView earth_time = (TextView)findViewById(R.id.earth_time);
        earth_time.setText(String.format("%02d:%02d:%02d",
                earth_date.get(Calendar.HOUR_OF_DAY),
                earth_date.get(Calendar.MINUTE),
                earth_date.get(Calendar.SECOND)));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String type = prefs.getString("pref_calendar_type", "");
        String[] entries = getResources().getStringArray(R.array.entries_pref_calendar_type);



        String[] values = getResources().getStringArray(R.array.entryvalues_pref_calendar_type);

        int idx = java.util.Arrays.asList(values).indexOf(type);
        if (idx == -1) {
            idx = 0;
        }

        type = entries[idx];

        final TextView calender_type_textview = (TextView)findViewById(R.id.calendar_type);
        calender_type_textview.setText(type);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            //return true;
        } else if (id == R.id.action_choose_date) {
            Calendar date = Calendar.getInstance();
            DatePickerDialog picker = new DatePickerDialog(this, this,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH));
            picker.show();
        } else if (id == R.id.action_what){
            String url = "https://en.wikipedia.org/wiki/Darian_calendar";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_darian, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.picked_year = year;
        this.picked_month = monthOfYear;
        this.picked_day = dayOfMonth;
        TimePickerDialog picker = new TimePickerDialog(this, this, 0, 0, true);
        picker.show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        Intent intent = new Intent(this, CustomDarianDateActivity.class);
        intent.putExtra("YEAR", this.picked_year);
        intent.putExtra("MONTH", this.picked_month);
        intent.putExtra("DAY", this.picked_day);
        intent.putExtra("HOUR", hour);
        intent.putExtra("MINUTE", minute);
        startActivity(intent);
    }


}
