package es.dit.gsi.rulesframework.performers;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by afernandez on 25/01/16.
 */
public class AudioPerformer {
    Context context;
    AudioManager am;

    public AudioPerformer(Context context){
        this.context = context;
    }

    public void setNormalMode(){
        am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
    public void setSilentMode(){
        am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }
    public void setVibrateMode(){
        am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }
}
