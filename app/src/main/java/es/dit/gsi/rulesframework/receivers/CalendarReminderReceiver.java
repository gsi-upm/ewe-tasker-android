package es.dit.gsi.rulesframework.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import es.dit.gsi.rulesframework.RuleExecutionModule;

/**
 * Created by afernandez on 8/01/16.
 */
public class CalendarReminderReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(CalendarContract.ACTION_EVENT_REMINDER)){
            RuleExecutionModule ruleExecutionModule = new RuleExecutionModule(context);

            Uri uri = intent.getData();

            String alertTime = uri.getLastPathSegment();
            Log.i("CALENDAR", "alertTime: " + alertTime);

            String selection = CalendarContract.CalendarAlerts.ALARM_TIME + "=?";
            Log.i("CALENDAR", "Selection: " + selection);

            Cursor cursor = context.getContentResolver().query(CalendarContract.CalendarAlerts.CONTENT_URI_BY_INSTANCE, new String[]{CalendarContract.CalendarAlerts.TITLE}, selection, new String[]{alertTime}, null);
            Log.i("CALENDAR", "Cursor column size: " + cursor.getColumnCount());

            cursor.moveToFirst();
            String nameEventTriggered = cursor.getString(0);
            Log.i("CALENDAR", "Event triggered: " + nameEventTriggered);

            //TODO:Contains keyword Meeting
            String pattern = "Meeting";
            if(nameEventTriggered.contains(pattern)){
                ruleExecutionModule.sendInputToEye("Calendar","mobile");//Match pattern
            }else{
            }

        }
    }
}