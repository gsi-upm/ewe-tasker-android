package es.dit.gsi.rulesframework.util;

/**
 * Created by afernandez on 24/11/15.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class Values implements Parcelable {

    private String id;
    private String type;
    private boolean required;
    private String activity;

    public static final Parcelable.Creator<Values> CREATOR = new Parcelable.Creator<Values>() {
        public Values createFromParcel(Parcel in) {
            return new Values(in);
        }

        public Values[] newArray(int size) {
            return new Values[size];
        }
    };

    public Values(String id, String type, boolean required, String activity) {
        this.id = id;
        this.type = type;
        this.required = required;
        this.activity = activity;
    }

    public Values(String id, String type, boolean required) {
        this.id = id;
        this.type = type;
        this.required = required;
        this.activity = null;
    }

    public Values(String id, String type) {
        this.id = id;
        this.type = type;
        this.required = false;
        this.activity = null;
    }

    private Values(Parcel in) {
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int arg) {
        out.writeString(id);
        out.writeString(type);
        out.writeString(required + "");
        out.writeString(activity);
    }

    public void readFromParcel(Parcel in) {
        id = in.readString();
        type = in.readString();
        required = Boolean.parseBoolean(in.readString());
        activity = in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Boolean isRequired() {
        return required;
    }

    public String getActivity() {
        return activity;
    }

}