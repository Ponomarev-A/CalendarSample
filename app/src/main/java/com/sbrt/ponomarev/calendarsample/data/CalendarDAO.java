package com.sbrt.ponomarev.calendarsample.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import com.sbrt.ponomarev.calendarsample.utlis.CalendarUtils;

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

    public CalendarEvent getCalendarEvent(Cursor cursor) {

        CalendarEvent event = null;

        if (cursor.moveToFirst()) {
            event = CalendarUtils.createCalendarEventFromCursor(cursor);
        }
        cursor.close();

        return event;
    }

    public void getCalendarEvents(Cursor source, List<CalendarEvent> target) {

        if (source != null) {
            CalendarUtils.fillList(source, target);
            source.close();
        }
    }

    @SuppressWarnings("ResourceType")
    public long insertCalendarEvent(CalendarEvent event) {
        return ContentUris.parseId(context.getContentResolver()
                .insert(CalendarContract.Events.CONTENT_URI, createContentValuesFromEvent(event)));
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
        values.put(CalendarContract.Events.TITLE, event.title);
        values.put(CalendarContract.Events.DESCRIPTION, event.description);
        values.put(CalendarContract.Events.DTSTART, event.dtstart);
        values.put(CalendarContract.Events.DTEND, event.dtend);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.CALENDAR_ID, DEFAULT_CALENDAR_ID);
        return values;
    }
}
