package com.codinginflow.smartalarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    private TimePicker alarmTimePicker;
    private TextView alarmTextView;
    MainActivity inst;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        alarmTextView = (TextView) findViewById(R.id.tv1);
        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Calendar calendar = Calendar.getInstance();
        alarmTimePicker = (TimePicker) findViewById(R.id.tp);


        Button start_alarm= (Button) findViewById(R.id.start_alarm);
        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                calendar.add(Calendar.SECOND, 3);
                //setAlarmText("You clicked a button");

                final int hour = alarmTimePicker.getCurrentHour();
                final int minute = alarmTimePicker.getCurrentMinute();

                Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                setAlarmText("You clicked a " + hour + " and " + minute);


                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                myIntent.putExtra("extra", "yes");
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);


                // now you should change the set Alarm text so it says something nice


                setAlarmText("Alarm set to " + hour + ":" + minute);

                //Toast.makeText(getApplicationContext(), "You set the alarm", Toast.LENGTH_SHORT).show();

            }

        });

        Button stop_alarm= (Button) findViewById(R.id.stop_alarm);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent.putExtra("extra", "no");
                sendBroadcast(myIntent);
                alarmManager.cancel(pending_intent);
                setAlarmText("Alarm canceled");
                //setAlarmText("You clicked a " + " canceled");
            }
        });

    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }


}