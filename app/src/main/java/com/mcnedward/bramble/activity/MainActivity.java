package com.mcnedward.bramble.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.MediaCache;
import com.mcnedward.bramble.view.MainView;
import com.mcnedward.bramble.view.fragment.AlbumFragment;
import com.mcnedward.bramble.view.fragment.ArtistFragment;
import com.mcnedward.bramble.view.fragment.MediaFragment;
import com.mcnedward.bramble.view.fragment.SongFragment;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private NowPlayingView nowPlayingView;

    public static MediaCache mediaCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainView(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // TODO This should probably be replaced with an SQLite database or something similar
        mediaCache = new MediaCache();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "BRAMBLE CLOSED");
        stopService(new Intent(this, MediaService.class));
        super.onDestroy();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements
            ViewPager.OnPageChangeListener {

        final private static int PAGE_COUNT = 3;
        private List<MediaFragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            for (MediaType mediaType : MediaType.values()) {
                switch (mediaType) {
                    case ARTIST:
                        fragments.add(new ArtistFragment());
                        break;
                    case ALBUM:
                        fragments.add(new AlbumFragment());
                        break;
                    case SONG:
                        fragments.add(new SongFragment());
                        break;
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a MediaFragment (defined as a static inner class below).
            return MediaFragment.newInstance(MediaType.values()[position]);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return MediaType.values()[position].type();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
