package es.dit.gsi.rulesframework.services;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import es.dit.gsi.rulesframework.util.Constants;
import es.dit.gsi.rulesframework.ListRulesActivity;
import es.dit.gsi.rulesframework.model.Channel;
import es.dit.gsi.rulesframework.model.Event;
import es.dit.gsi.rulesframework.performers.AudioPerformer;
import es.dit.gsi.rulesframework.performers.NotificationPerformer;
import es.dit.gsi.rulesframework.performers.ToastPerformer;
import es.dit.gsi.rulesframework.performers.WifiPerformer;
import es.dit.gsi.rulesframework.util.Tasks;

/**
 * Created by afernandez on 27/10/15.
 */
public class RuleExecutionModule {
    Context context;

    public RuleExecutionModule(Context context){
        this.context=context;
    }

    public void sendInputToEye(String input,String user){
        //TODO: Input to Server
        String[] params = {input,user};
        String response = "";
        try {
            response = new Tasks.PostInputToServerTask().execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        handleServerResponse(response);


        //DEBUG

    }

    //Handle EYE result
    public void handleServerResponse(String respJson){
        String doElement, doAction, doParameter;
        try {
            JSONObject jsonObject = new JSONObject(respJson);
            //SUCCESS
            int success = jsonObject.getInt("success");
            if(success == 1){
                JSONArray actions = jsonObject.getJSONArray("actions");
                for(int i = 0 ;i< actions.length(); i++){
                    JSONObject itemAction = (JSONObject) actions.get(i);
                    doElement = itemAction.getString("channel");
                    doAction = itemAction.getString("action");
                    doParameter = itemAction.getString("parameter");
                    executeDoResponse(doElement,doAction,doParameter);
                    Log.i("DoResponse","channel: "+ doElement + " action: "+ doAction + " parameter: " + doParameter);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void executeDoResponse(String doElement, String doAction,String parameter){
        //Create instance of manager name
        try {
            switch (doElement){
                //FilterManagers
                case("Notification"):
                    NotificationPerformer np = new NotificationPerformer(context);
                    switch (doAction){//Filter actions
                        case "Show":np.show(parameter);break;
                    }
                    break;
                case ("Toast"):
                    ToastPerformer tp = new ToastPerformer(context);
                    switch (doAction){//Filter actions
                        case "Show":
                            tp.show(parameter);break;
                    }
                    break;
                case ("Wifi"):
                    WifiPerformer wp = new WifiPerformer(context);
                    switch(doAction) {//FilterActions
                        case "ON": wp.turnOn();
                        case "OFF":wp.turnOff();
                    }
                case ("AudioManager"):
                    AudioPerformer ap = new AudioPerformer(context);
                    switch(doAction) {//FilterActions
                        case "Normal": ap.setNormalMode();break;
                        case "Silence": ap.setSilentMode();break;
                        case "Vibrate": ap.setVibrateMode();break;
                    }
                default:
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String generateInput(String channel, String event) {
        String json = Constants.readPreferences(context, "channelsJson", "");
        //Log.i("Execution",json);
        List<Channel> channelList = null;
        try {
            channelList = ListRulesActivity.translateJSONtoList(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String input = "";
        for (Channel ch : channelList){
            if(ch.title.equalsIgnoreCase(channel)){
                for(Event e : ch.events){
                    if (e.title.equalsIgnoreCase(event)){
                        String statement = e.rule.replace("?event","ewe-"+channel.toLowerCase()+":"+channel);
                        input = e.prefix + "\n" + statement;
                    }
                }
            }
        }
        Log.i("Execution",input);
        return input;
    }

    public String getPrefixes(String channel,String event){
        String json = Constants.readPreferences(context,"channelsJson","");
        //Log.i("Execution",json);
        List<Channel> channelList = null;
        try {
            channelList = ListRulesActivity.translateJSONtoList(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String prefix = "";
        for (Channel ch : channelList){
            if(ch.title.equalsIgnoreCase(channel)){
                for(Event e : ch.events){
                    if (e.title.equalsIgnoreCase(event)){
                        prefix = e.prefix ;
                    }
                }
            }
        }

        return prefix;
    }
}
