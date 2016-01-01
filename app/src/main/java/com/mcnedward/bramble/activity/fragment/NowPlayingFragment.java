package com.mcnedward.bramble.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

/**
 * Created by edward on 31/12/15.
 */
public class NowPlayingFragment extends Fragment {

    private NowPlayingView nowPlayingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        nowPlayingView = new NowPlayingView(getContext());
        return nowPlayingView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public NowPlayingView getNowPlayingView() {
        return nowPlayingView;
    }
}
