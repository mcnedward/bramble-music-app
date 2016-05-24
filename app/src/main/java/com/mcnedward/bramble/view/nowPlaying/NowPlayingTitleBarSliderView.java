package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RippleUtil;

import java.util.List;

/**
 * Created by Edward on 5/21/2016.
 *
 * View for
 */
public class NowPlayingTitleBarSliderView extends HorizontalSlidingView<Song> {

    public NowPlayingTitleBarSliderView(Context context, List<Song> songs) {
        super(context, songs);
    }

    @Override
    protected View getView(int position, View convertView) {
        ViewHolder holder;

        final NowPlayingTitleBarView titleBarView;
        Song item = getItem(position);
        if (convertView == null) {
            titleBarView = new NowPlayingTitleBarView(mContext, item);
            convertView = titleBarView;

            holder = new ViewHolder(titleBarView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            titleBarView = holder.mTitleBar;
        }
        titleBarView.update(item, null);
        RippleUtil.setRippleBackground(titleBarView, mContext);
        return convertView;
    }

    @Override
    protected void notifyMainViewUpdated(Song newItem, View mainView, boolean isMovingRight) {
        if (isMovingRight) {
            // Play the previous song
            MusicUtil.doPreviousButtonAction(mContext);
        } else {
            // Play the next song
            MusicUtil.doForwardButtonAction(mContext);
        }
    }

    protected static class ViewHolder {
        public NowPlayingTitleBarView mTitleBar;
        public ViewHolder(NowPlayingTitleBarView titleBar) {
            mTitleBar = titleBar;
        }
    }
}
