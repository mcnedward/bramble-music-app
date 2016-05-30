package com.mcnedward.bramble.activity.artist;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.mcnedward.bramble.R;
import com.mcnedward.bramble.adapter.grid.ArtistImageGridAdapter;
import com.mcnedward.bramble.controller.ArtistImageResponse;
import com.mcnedward.bramble.controller.WebController;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.enums.IntentKey;
import com.mcnedward.bramble.exception.EntityAlreadyExistsException;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.repository.data.ArtistImageRepository;
import com.mcnedward.bramble.repository.data.IArtistImageRepository;

import java.util.List;

/**
 * Created by Edward on 5/29/2016.
 * This is an Activity for choosing new images to use for an Artist. The user can choose between images that have already been loaded, or they can
 * do a refined search.
 */
public class ArtistImageChooserActivity extends AppCompatActivity {
    private static final String TAG = "ArtistImageChooserAct";

    private Artist mArtist;
    private WebController mController;
    private IArtistImageRepository mRepository;
    private ArtistImageGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_image);

        mArtist = (Artist) getIntent().getSerializableExtra(IntentKey.ARTIST.name());
        mController = new WebController(this);
        mRepository = new ArtistImageRepository(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.choose_artist_title));

        initialize();
    }

    private void initialize() {
        TextView txtTitle = (TextView) findViewById(R.id.text_chooser_title);
        txtTitle.setText(getString(R.string.choose_artist_background) + " " + mArtist.getArtistName());

        GridView gridView = (GridView) findViewById(R.id.grid_artist_images);
        mAdapter = new ArtistImageGridAdapter(this, mArtist.getArtistImages());
        gridView.setAdapter(mAdapter);

        final Context context = this;
        final EditText editRefineSearch = (EditText) findViewById(R.id.edit_refine_search);
        editRefineSearch.setText(mArtist.getArtistName());
        editRefineSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchForArtistImages(context, editRefineSearch.getText().toString());
                    View view = ((Activity) context).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        Button btnRefineSearch = (Button) findViewById(R.id.button_refine_search);
        btnRefineSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editRefineSearch.getText().toString();
                searchForArtistImages(context, query);
                View view = ((Activity) context).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    private void searchForArtistImages(Context context, String query) {
        if (query.equals("")) {
            Toast.makeText(context, getString(R.string.choose_artist_search_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        mController.requestArtistImages(query, mArtist, new Response.Listener<ArtistImageResponse>() {
            @Override
            public void onResponse(ArtistImageResponse response) {
                Artist requestArtist = response.getArtist();
                List<ArtistImage> artistImages = response.getArtistImages();
                if (artistImages.isEmpty()) {
                    Log.d(TAG, "No images found for artist: " + requestArtist.getArtistName());
                    return;
                }
                // Delete the old images
                List<ArtistImage> oldImages = mRepository.getForArtistId(requestArtist.getId());
                for (ArtistImage ai : oldImages) {
                    try {
                        mRepository.delete(ai);
                    } catch (EntityDoesNotExistException e) {
                        Log.w(TAG, e.getMessage());
                    }
                }

                // Get the artist images from the response, save them in the database, and handle the bitmap for the first one
                for (ArtistImage ai : artistImages) {
                    ai.setArtistId(requestArtist.getId());
                    try {
                        mRepository.save(ai);
                    } catch (EntityAlreadyExistsException e1) {
                        Log.w(TAG, e1.getMessage());
                    }
                }
                mAdapter.setGroups(artistImages);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}
