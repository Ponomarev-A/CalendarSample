package com.sbrt.ponomarev.calendarsample.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import com.sbrt.ponomarev.calendarsample.utlis.CalendarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Ponomarev on 13.06.2017.
 */

public class CalendarDAO {

    private static final int DEFAULT_CALENDAR_ID = 1;
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

    @SuppressWarnings("ResourceType")
    public long insertCalendarEvent(CalendarEvent event) {
        return context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, createContentValuesFromEvent(event)) != null ?
                1 : 0;
    }

    @SuppressWarnings("MissingPermission")
    public int deleteCalendarEvent(long id) {
        return context.getContentResolver().delete(CalendarContract.Events.CONTENT_URI,
                CalendarContract.Events._ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    @SuppressWarnings("MissingPermission")
    public int updateCalendarEvent(CalendarEvent event) {
        return context.getContentResolver().update(CalendarContract.Events.CONTENT_URI,
                createContentValuesFromEvent(event),
                CalendarContract.Events._ID + "=?",
                new String[]{String.valueOf(event.id)}
        );
    }

    private ContentValues createContentValuesFromEvent(CalendarEvent event) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events._ID, event.id);
        values.put(CalendarContract.Events.TITLE, event.title);
        values.put(CalendarContract.Events.DESCRIPTION, event.description);
        values.put(CalendarContract.Events.DTSTART, event.dtstart);
        values.put(CalendarContract.Events.DTEND, event.dtend);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.CALENDAR_ID, DEFAULT_CALENDAR_ID);
        return values;
    }

    @SuppressWarnings("MissingPermission")
    private Cursor getCalendarEventCursor(Long id) {
        return context.getContentResolver().query(CalendarContract.Events.CONTENT_URI,
                null,
                id == null ? null : CalendarContract.Events._ID + "=?",
                id == null ? null : new String[]{String.valueOf(id)},
                null
        );
    }
}
