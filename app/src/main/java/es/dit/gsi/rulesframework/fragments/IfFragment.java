package es.dit.gsi.rulesframework.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by afernandez on 27/11/15.
 */
public class IfFragment extends Fragment {
    // newInstance constructor for creating fragment with arguments
    public static IfFragment newInstance(int page, String title) {
        IfFragment fragmentFirst = new IfFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
}
