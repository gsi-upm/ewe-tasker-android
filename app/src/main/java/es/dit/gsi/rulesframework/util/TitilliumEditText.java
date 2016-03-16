package es.dit.gsi.rulesframework.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import es.dit.gsi.rulesframework.R;

/**
 * Created by afernandez on 14/03/16.
 */
public class TitilliumEditText extends EditText {
    public TitilliumEditText(Context context) {
        super(context);
        init(null);
    }

    public TitilliumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitilliumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
