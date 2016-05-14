package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

/**
 * Created by edward on 30/12/15.
 */
public class NowPlayingActivity extends Activity {
    private final static String TAG = "NowPlayingActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NowPlayingView nowPlayingView = new NowPlayingView(this);
        setContentView(nowPlayingView);
        nowPlayingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "TOUCHED FROM NOWPLAYINGACTIVITY");
                return true;
            }
        });
        runThis();
    }

    private void runThis() {
        final MediaPlayer player = MediaService.getPlayer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player != null
                        && player.getCurrentPosition() < player.getDuration()) {
                    // Sleep for 100 milliseconds
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Start a new thread on the NowPlayingActivity UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Get the current time and total duration and display on the UI
                            String currentTime = Extension.getTimeString(player.getCurrentPosition());
                            String duration = Extension.getTimeString(player.getDuration());
                            Log.i(TAG, String.valueOf(currentTime));
                        }
                    });
                }
            }
        }).start();
    }

}
