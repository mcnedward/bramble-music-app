package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingSubControlsView;

/**
 * Created by edward on 27/12/15.
 */
public class MoveableActivity extends Activity implements View.OnTouchListener {
    private final static String TAG = "MoveableActivity";

    private ViewGroup root;
    private TextView txtStuff;
    private NowPlayingSubControlsView bottomControls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moveable_view);
        root = (ViewGroup) findViewById(R.id.root);
        txtStuff = (TextView) findViewById(R.id.txt_stuff);

//        bottomControls = new NowPlayingSubControlsView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        bottomControls.setLayoutParams(layoutParams);
        root.addView(bottomControls);

        root.setOnTouchListener(this);
    }

    private boolean controlsTouched;
    private int anchorX = 0, anchorY = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int bX = (int) bottomControls.getX();
        int bY = (int) bottomControls.getY();
        int bHeight = bottomControls.getHeight();
        int bMid = bY + ((bY + bHeight) / 2);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                anchorX = (int) event.getX();
                anchorY = (int) event.getY();
                if (anchorY > bY && anchorY < (bY + bHeight))
                    controlsTouched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (controlsTouched) {
                    int newY;
                    int eventY = (int) event.getY();
                    if (eventY > (bY + bHeight)) {
                        newY = eventY - bHeight;
                    } else {
                        int diff = Math.abs(eventY - bY);
                        newY = eventY - diff;
                    }
                    bottomControls.setY(newY);
                    if (eventY >= root.getHeight()) {
                        bottomControls.setY(root.getHeight() - bHeight);
                    }
                    if (eventY < 0) {
                        // -5 to take shadow into account
                        bottomControls.setY(-5);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (controlsTouched) {
                    // TODO Need animations here!
                    if (event.getY() < root.getHeight() / 2) {
                        // -5 to take shadow into account
                        bottomControls.setY(-5);
                    } else {
                        bottomControls.setY(root.getHeight() - bHeight);
                    }
                }
                controlsTouched = false;
                break;
        }
        return true;
    }
}
