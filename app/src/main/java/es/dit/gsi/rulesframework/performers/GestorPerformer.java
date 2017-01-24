package es.dit.gsi.rulesframework.performers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import es.dit.gsi.rulesframework.GestorWebViewActivity;
import es.dit.gsi.rulesframework.ListRulesActivity;
import es.dit.gsi.rulesframework.R;
import es.dit.gsi.rulesframework.services.RuleExecutionModule;
import es.dit.gsi.rulesframework.util.CacheMethods;
import es.dit.gsi.rulesframework.util.Tasks;

/**
 * Created by afernandez on 25/01/16.
 */
public class GestorPerformer {
    Context context;
    public GestorPerformer(Context context){
        this.context = context;
    }

    public void haveToShow(String place){
        CacheMethods cacheMethods = CacheMethods.getInstance(context);

        //Wrong prefix
        String event = "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . @prefix ewe: <http://gsi.dit.upm.es/ontologies/ewe/ns/#> . @prefix ewe-place: <http://gsi.dit.upm.es/ontologies/ewe-place/ns/#> ." +

                " ewe-place:Place rdf:type ewe-place:Inside. ewe-place:Place ewe:placeID \""+place+"\".";
        String user = cacheMethods.getFromPreferences("beaconRuleUser","public");


        String [] params = {event,user};
        //Task send to sever
        String response = "";
        try {
            response = new Tasks.PostInputToServerTask().execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i("RESPONSE",event);
        Log.i("RESPONSE",user);

        //Show url received from response
        RuleExecutionModule ruleExecutionModule = new RuleExecutionModule(context);
        ruleExecutionModule.handleServerResponse(response);
    }

    public void sendPlace(String url){
        String [] params = {url};
        //Task send to sever
        String response = "";
        try {
            response = new Tasks.GetUrlFromCMS().execute(params).get();
            Log.i("RESP",response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Show url received from response
        show(response);
    }

    public void show (String response){
        String url = "";
        String title="";
        String description="";
        try {
            JSONObject jsonObject = new JSONObject(response);
            //SUCCESS
            url = jsonObject.getString("url");
            title = jsonObject.getString("title");
            description = jsonObject.getString("description");
        }catch (Exception e){
            e.printStackTrace();
        }

        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(context, GestorWebViewActivity.class);
        notificationIntent.putExtra("url",url);
        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(ListRulesActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.logo)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.logo))
                .setColor(Color.GREEN)
                .setContentTitle(title)
                .setContentText(description)
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());    }
}
