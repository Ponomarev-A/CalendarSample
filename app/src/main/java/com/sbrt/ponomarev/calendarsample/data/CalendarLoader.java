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

    private Long eventId;

    public CalendarLoader(Context context, Long eventId) {
        super(context);
        this.eventId = eventId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<CalendarEvent> loadInBackground() {
        List<CalendarEvent> events = new ArrayList<>();

        Cursor cursor = getCalendarEventCursor(eventId);
        if (cursor != null) {
            CalendarUtils.fillList(cursor, events);
            cursor.close();
        } else {
            Log.e(TAG, "cursor is null");
        }

        return events;
    }

    @SuppressWarnings("MissingPermission")
    private Cursor getCalendarEventCursor(Long id) {
        return getContext().getContentResolver().query( CalendarContract.Events.CONTENT_URI,
                null,
                id == null ? null : CalendarContract.Events._ID + "=?",
                id == null ? null : new String[] {String.valueOf(id)},
                null
        );
    }
}
