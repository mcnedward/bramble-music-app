package com.mcnedward.bramble.service;

import com.mcnedward.bramble.listener.MediaListener;

import java.util.Set;

/**
 * Created by Edward on 5/15/2016.
 */
public class NowPlayingThread extends BaseThread implements IThread {

    private Set<MediaListener> mListeners;

    public NowPlayingThread(Set<MediaListener> listeners) {
        super("NowPlaying");
        mListeners = listeners;
    }

    public void attachMediaListener(MediaListener listener) {
        mListeners.add(listener);
    }

    public void removeMediaListener(MediaListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void doRunAction() {
        for (final MediaListener listener : mListeners) {
            listener.getView().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    listener.update();
                }
            });
        }
    }

    @Override
    public void doStartAction() {

    }

    @Override
    public void doStopAction() {

    }
}
