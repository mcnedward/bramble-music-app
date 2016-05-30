package com.mcnedward.bramble.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.entity.media.Song;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by edward on 25/12/15.
 */
public final class MediaCache {
    private static final String TAG = "MediaCache";

    private static final String PREFERENCE_KEY = "preference_key";
    private static final String SONG_ID_LIST_KEY = "song_id_list";

    public static Artist getArtist(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        Artist artist = null;
        try {
            artist = Artist.get(sharedPreferences);
        } catch (NullPointerException e) {
            Log.w(TAG, "Could not create a artist from preferences.", e);
        }
        return artist;
    }

    public static boolean saveArtist(Context context, Artist artist) {
        if (artist == null) return false;
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        return artist.save(editor);
    }

    public static Album getAlbum(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        Album album = null;
        try {
            album = Album.get(sharedPreferences);
        } catch (NullPointerException e) {
            Log.w(TAG, "Could not create a album from preferences.", e);
        }
        return album;
    }

    public static boolean saveAlbum(Context context, Album album) {
        if (album == null) return false;
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        return album.save(editor);
    }

    public static Song getSong(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        Song song = null;
        try {
            song = Song.get(sharedPreferences);
        } catch (NullPointerException e) {
            Log.w(TAG, "Could not create a song from preferences.", e);
        }
        return song;
    }

    public static boolean saveSong(Context context, Song song) {
        if (song == null) return false;
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        return song.save(editor);
    }

    public static List<Long> getQueue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);

        List<Long> queue = new ArrayList<>();
        Gson gson = new Gson();
        Type typeToken = new TypeToken<List<Long>>() {}.getType();
        try {
            String songIdsAsJson = sharedPreferences.getString(SONG_ID_LIST_KEY, "");
            queue = gson.fromJson(songIdsAsJson, typeToken);
        } catch (NullPointerException e) {
            Log.w(TAG, "Could not get the song list from preferences.", e);
        }
        return queue;
    }

    public static boolean saveQueue(Context context, List<Long> queue) {
        if (queue == null) return false;
        Gson gson = new Gson();
        String songIdsAsJson = gson.toJson(queue);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(SONG_ID_LIST_KEY, songIdsAsJson);
        return editor.commit();
    }

    public static void setPlaybackLooping(boolean looping, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean("playback_looping", looping);
        editor.commit();
    }

    public static boolean isPlaybackLooping(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("playback_looping", false);
    }

    public static void setPlaybackShuffling(boolean shuffling, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean("playback_shuffling", shuffling);
        editor.commit();
    }

    public static boolean isPlaybackShuffling(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("playback_shuffling", false);
    }

}
