package es.dit.gsi.rulesframework.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import es.dit.gsi.rulesframework.R;

/**
 * Created by afernandez on 14/03/16.
 */
public class TitilliumTextView extends TextView {
    public TitilliumTextView(Context context) {
        super(context);
        init(null);
    }

    public TitilliumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitilliumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TitilliumTextView);
            String fontName = a.getString(R.styleable.TitilliumTextView_fontName);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
