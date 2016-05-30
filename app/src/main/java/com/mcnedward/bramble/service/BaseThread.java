package com.mcnedward.bramble.service;

import android.content.Context;
import android.util.Log;

/**
 * Created by Edward on 5/15/2016.
 */
public abstract class BaseThread extends Thread implements IThread {
    private static final String TAG = "BaseThread";

    private boolean mStarted, mRunning, mPaused, mEnabled;

    public BaseThread(String name) { this(name, true); }

    public BaseThread(String name, boolean enabled) {
        super(name);
        mStarted = false;
        mRunning = false;
        mEnabled = enabled;
    }

    /**
     * Run the thread.
     */
    @Override
    public void run() {
        while (mRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mPaused || !mEnabled) continue;
            doRunAction();
        }
    }

    /**
     * Start the thread.
     */
    @Override
    public void start() {
        mStarted = true;
        super.start();
    }

    /**
     * Starts the thread and runs the abstract start action.
     */
    @Override
    public void startThread() {
        Log.d(TAG, "Starting " + getName() + " thread!");
        if (!mStarted)
            start();
        mRunning = mEnabled;
        doStartAction();
    }

    /**
     * Stops the thread and runs the abstract stop action.
     */
    @Override
    public void stopThread() {
        Log.d(TAG, "Stopping " + getName() + " thread!");
        mRunning = false;
        doStopAction();
        boolean retry = true;
        while (retry) {
            try {
                join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Pause the thread.
     * @param pause
     */
    @Override
    public void pauseThread(boolean pause) {
        mPaused = pause;
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return mEnabled;
    }
}
