package es.dit.gsi.rulesframework.util;

/**
 * Created by afernandez on 24/11/15.
 */
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class ParameterBundle implements Parcelable {

    private HashMap<String, Object> bundle = new HashMap<String, Object>();

    public static final Parcelable.Creator<ParameterBundle> CREATOR = new Parcelable.Creator<ParameterBundle>() {
        public ParameterBundle createFromParcel(Parcel in) {
            return new ParameterBundle(in);
        }

        public ParameterBundle[] newArray(int size) {
            return new ParameterBundle[size];
        }
    };

    HashMap<String, Object> toHashMap() {
        return bundle;
    }

    void fromHashMap(HashMap<String, Object> bundle) {
        this.bundle = bundle;
    }

    public void put(String key, Integer value) {
        bundle.put(key, value);
    }

    public void put(String key, Double value) {
        bundle.put(key, value);
    }

    public void put(String key, String value) {
        bundle.put(key, value);
    }

    public Object get(String key) {
        return bundle.get(key);
    }

    public ParameterBundle() {

    }

    private ParameterBundle(Parcel in) {
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int arg) {
        out.writeSerializable(bundle);
    }

    public void readFromParcel(Parcel in) {
        bundle = (HashMap<String, Object>)in.readSerializable();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

}