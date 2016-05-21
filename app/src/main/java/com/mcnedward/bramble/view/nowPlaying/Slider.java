package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.RippleUtil;

import java.util.List;

/**
 * Created by Edward on 5/21/2016.
 */
public class Slider extends HorizontalSlidingView {

    public Slider(Context context, List<Song> songs) {
        super(context, songs);
    }

    @Override
    protected View getView(int position, View convertView) {
        ViewHolder holder;

        final NowPlayingTitleBarView2 titleBarView;
        Song item = getItem(position);
        if (convertView == null) {
            titleBarView = new NowPlayingTitleBarView2(mContext, item);
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
    protected void updateReplacementView() {

    }

    @Override
    protected void updateLeftContent(View view) {

    }

    @Override
    protected void updateRightContent(View view) {

    }

    protected static class ViewHolder {
        public NowPlayingTitleBarView2 mTitleBar;

        public ViewHolder(NowPlayingTitleBarView2 titleBar) {
            mTitleBar = titleBar;
        }
    }
}
