package es.dit.gsi.rulesframework.receivers;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by afernandez on 8/01/16.
 */
public class CalendarReminderReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(CalendarContract.ACTION_EVENT_REMINDER)){

            Uri uri = intent.getData();

            String alertTime = uri.getLastPathSegment();
            Log.i("CALENDAR", "alertTime: " + alertTime);

            String selection = CalendarContract.CalendarAlerts.ALARM_TIME + "=?";
            Log.i("CALENDAR", "Selectiom: " + selection);

            Cursor cursor = context.getContentResolver().query(CalendarContract.CalendarAlerts.CONTENT_URI_BY_INSTANCE, new String[]{CalendarContract.CalendarAlerts.TITLE}, selection, new String[]{alertTime}, null);

            String nameEventTriggered = cursor.getString(0);
            Log.i("CALENDAR", "Event triggered: " + nameEventTriggered);

            //TODO:Contains keyword Meeting

        }
    }
}