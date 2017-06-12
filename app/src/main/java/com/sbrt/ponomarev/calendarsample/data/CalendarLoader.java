package com.sbrt.ponomarev.calendarsample.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.sbrt.ponomarev.calendarsample.utlis.CalendarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ponomarev on 12.06.2017.
 */

public class CalendarLoader extends AsyncTaskLoader<List<CalendarEvent>> {

    private static final String TAG = CalendarLoader.class.getSimpleName();

    public CalendarLoader(Context context) {
        super(context);
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<CalendarEvent> loadInBackground() {
        List<CalendarEvent> events = new ArrayList<>();

        Cursor cursor = getCalendarEventCursor();
        if (cursor != null) {
            CalendarUtils.fillList(cursor, events);
            cursor.close();
        } else {
            Log.e(TAG, "cursor is null");
        }

        return events;
    }

    @SuppressWarnings("MissingPermission")
    private Cursor getCalendarEventCursor() {
        return getContext().getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                null, null, null, null
        );
    }
}
