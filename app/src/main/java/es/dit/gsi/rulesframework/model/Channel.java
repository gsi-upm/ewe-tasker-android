package es.dit.gsi.rulesframework.model;

import java.util.List;

/**
 * Created by afernandez on 26/01/16.
 */
public class Channel {
    public String title;
    public String description;
    public List<Event> events;
    public List<Action> actions;

    public Channel(String title,String description, List<Action> actions, List<Event> events) {
        this.title = title;
        this.actions = actions;
        this.events = events;
        this.description = description;
    }

    public boolean hasEvents(){
        if(events.size()>0 && !events.get(0).title.equals("")){
            return true;
        }else{
            return false;
        }
    }

    public boolean hasActions(){
        if(actions.size()>0 && !actions.get(0).title.equals("")){
            return true;
        }else{
            return false;
        }
    }
}
