package com.sbrt.ponomarev.calendarsample;

import android.database.Cursor;
import android.provider.CalendarContract;

import java.util.List;

/**
 * Created by Тичер on 10.06.2017.
 */
public class CalendarUtils {

    public static void fillList(Cursor source, List<CalendarEvent> target) {
        if (source.moveToFirst()) {
            while (!source.isAfterLast()) {
                target.add(createCalendarEventFromCursor(source));
                source.moveToNext();
            }
        }
    }

    private static CalendarEvent createCalendarEventFromCursor(Cursor cursor) {
        CalendarEvent event = new CalendarEvent();
        event.id = getLong(cursor, CalendarContract.Events._ID);
        event.dtstart = getLong(cursor, CalendarContract.Events.DTSTART);
        event.dtend = getLong(cursor, CalendarContract.Events.DTEND);
        event.calendarId = getLong(cursor, CalendarContract.Events.CALENDAR_ID);
        event.title = getString(cursor, CalendarContract.Events.TITLE);
        event.description = getString(cursor, CalendarContract.Events.DESCRIPTION);
        event.eventTimezone = getString(cursor, CalendarContract.Events.EVENT_TIMEZONE);
        event.rdate = getString(cursor, CalendarContract.Events.RDATE);
        return event;
    }

    private static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    private static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}
