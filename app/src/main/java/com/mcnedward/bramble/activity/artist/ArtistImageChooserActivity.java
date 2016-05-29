package com.mcnedward.bramble.activity.artist;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.enums.ArtistImageChooserType;
import com.mcnedward.bramble.enums.IntentKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/29/2016.
 */
public class ArtistImageChooserActivity extends AppCompatActivity {

    private Artist mArtist;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_image);

        mArtist = (Artist) getIntent().getSerializableExtra(IntentKey.ARTIST.name());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_artist_image);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_artist_image);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements
            ViewPager.OnPageChangeListener {

        final private static int PAGE_COUNT = 2;
        private List<ArtistImageChooserFragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            FragmentTransaction f = getSupportFragmentManager().beginTransaction();
            for (ArtistImageChooserType type : ArtistImageChooserType.values()) {
                switch (type) {
                    case THUMBNAIL:
                        fragments.add(new ArtistThumbnailChooserFragment());
                        break;
                    case BACKGROUND:
                        fragments.add(new ArtistBackgroundChooserFragment());
                        break;
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a MediaFragment (defined as a static inner class below).
            return ArtistImageChooserFragment.newInstance(mArtist, ArtistImageChooserType.values()[position]);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ArtistImageChooserType.values()[position].type();
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
