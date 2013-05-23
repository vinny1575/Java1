package com.ivinny.tempcalc;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mikel on 5/23/13.
 */
public class WeatherContentProvider extends ContentProvider {
    static final String TAG = "ProviderDemo";

    static final String AUTHORITY = "com.ivinny.application.weathercontentprovider";
    public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);
    static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.marakana.android.lifecycle.status";
    static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.marakana.android.lifecycle.status";

    public static class WeatherData implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/items");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ivinny.weathercontentprovider.item";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.ivinny.weathercontentprovider.item";

        public static final String CITY_COLUMN = "city";
        public static final String TEMP_COLUMN = "temp";
        public static final String URL_COLUMN = "url";

        public static final String[] PROJECTION = {"_Id", CITY_COLUMN, TEMP_COLUMN, URL_COLUMN};

        private WeatherData() {};
    }

    public static final int ITEMS = 1;
    public static final int ITMES_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY,"items/",ITEMS);
        uriMatcher.addURI(AUTHORITY,"items/#", ITMES_ID);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        return true;
    }

    @Override
    public String getType(Uri uri) {

        switch(uriMatcher.match(uri)){
            case ITEMS:
                return WeatherData.CONTENT_TYPE;

            case ITMES_ID:
                return WeatherData.CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String content = text;
        File file = new File(getCacheDir(), "appCache");
        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath(), true);

        try {
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "insert uri: " + uri.toString());
        return null;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query with uri: " + uri.toString());

        MatrixCursor result = new MatrixCursor(new String[] {"_id","Column1"});

        String JSONString = Storage.readStringFile

        switch(uriMatcher.match(uri)){
            case ITEMS:
                return WeatherData.CONTENT_TYPE;

            case ITMES_ID:
                return WeatherData.CONTENT_ITEM_TYPE;
        }
        return null;

    }

}
