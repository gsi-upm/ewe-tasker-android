package es.dit.gsi.rulesframework.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import es.dit.gsi.rulesframework.services.RuleExecutionModule;
import es.dit.gsi.rulesframework.util.CacheMethods;

/**
 * Created by afernandez on 8/01/16.
 */
public class CalendarReminderReceiver extends BroadcastReceiver {

    String input = "ewe-calendar:Calendar rdf:type ewe-calendar:EventStart. ewe-calendar:Calendar ewe:eventTitle \"#PARAM_1#\".";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(CalendarContract.ACTION_EVENT_REMINDER)){
            CacheMethods cacheMethods = CacheMethods.getInstance(context);
            String user=cacheMethods.getFromPreferences("beaconRuleUser","public");

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

            //Replace param and send Input
            input = input.replace("#PARAM_1#",nameEventTriggered);
            String prefix = ruleExecutionModule.getPrefixes("Calendar","Event Start");
            String statement = prefix + " " + input;
            Log.i("Calendar", statement);
            ruleExecutionModule.sendInputToEye(statement,user);

            }else{

        }
    }
}