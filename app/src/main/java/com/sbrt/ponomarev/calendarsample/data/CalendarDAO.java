package com.sbrt.ponomarev.calendarsample.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import com.sbrt.ponomarev.calendarsample.utlis.CalendarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ponomarev on 13.06.2017.
 */

public class CalendarDAO {

    private Context context;

    public CalendarDAO(Context context) {
        this.context = context;
    }

    public CalendarEvent getCalendarEvent(long id) {

        CalendarEvent event = null;

        Cursor cursor = getCalendarEventCursor(id);
        if (cursor.moveToFirst()) {
            event = CalendarUtils.createCalendarEventFromCursor(cursor);
        }
        cursor.close();

        return event;
    }

    public List<CalendarEvent> getCalendarEvents() {

        List<CalendarEvent> events = new ArrayList<>();

        Cursor cursor = getCalendarEventCursor(null);
        if (cursor != null) {
            CalendarUtils.fillList(cursor, events);
            cursor.close();
        }

        return events;
    }

    public long insertCalendarEvent(CalendarEvent event) {
        // TODO: Don't forget implement method
        return 1;
    }

    public int deleteCalendarEvent(long id) {
        // TODO: Don't forget implement method
        return 1;
    }

    public int updateCalendarEvent(CalendarEvent event) {
        // TODO: Don't forget implement method
        return 1;
    }

    @SuppressWarnings("MissingPermission")
    private Cursor getCalendarEventCursor(Long id) {
        return context.getContentResolver().query( CalendarContract.Events.CONTENT_URI,
                null,
                id == null ? null : CalendarContract.Events._ID + "=?",
                id == null ? null : new String[] {String.valueOf(id)},
                null
        );
    }
}
