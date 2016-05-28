package com.mcnedward.bramble.repository.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcnedward.bramble.exception.EntityAlreadyExistsException;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.media.Playlist;
import com.mcnedward.bramble.provider.PlaylistContentProvider;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/26/2016.
 */
public class PlaylistRepository extends DataRepository<Playlist> implements IPlaylistRepository {

    private Gson mGson;
    private Type mTypeToken;

    public PlaylistRepository(Context context) {
        super(context);
        mGson = new Gson();
        mTypeToken = new TypeToken<List<String>>() {}.getType();
    }

    @Override
    public void saveCurrentPlaylist(List<String> songKeys) {
        // First remove the old playlist, if there is one
        try {
            delete(1);
        } catch (EntityDoesNotExistException e) {
            e.printStackTrace();
        }
        Playlist playlist = new Playlist(1, songKeys);
        try {
            save(playlist);
        } catch (EntityAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getCurrentPlaylist() {
        try {
            return get(1).getSongKeys();
        } catch (EntityDoesNotExistException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected Playlist generateEntityFromCursor(Cursor cursor) {
        Playlist playlist = new Playlist();
        playlist.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        String songIdsAsJson = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.P_SONG_KEYS));
        playlist.setSongIds(mGson.<List<String>>fromJson(songIdsAsJson, mTypeToken));
        return playlist;
    }

    @Override
    protected ContentValues generateContentValuesFromEntity(Playlist entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.P_SONG_KEYS, mGson.toJson(entity.getSongKeys()));
        return values;
    }

    @Override
    protected Uri getUri() {
        return PlaylistContentProvider.CONTENT_URI;
    }

    @Override
    protected String getOrderBy() {
        return DatabaseHelper.P_SONG_KEYS + " ASC";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.P_SONG_KEYS,
        };
    }
}
