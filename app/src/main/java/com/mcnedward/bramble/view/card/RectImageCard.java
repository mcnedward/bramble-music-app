package com.mcnedward.bramble.view.card;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Edward on 5/15/2016.
 *
 * An ImageView that is set to a specified height.
 */
public class RectImageCard extends ImageView {

    private static final int HEIGHT = 128;

    public RectImageCard(Context context) {
        super(context);
    }

    public RectImageCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectImageCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), HEIGHT);
    }
}
