package com.sbrt.ponomarev.calendarsample.utlis;

import android.database.Cursor;
import android.provider.CalendarContract;
import android.widget.TextView;
import com.sbrt.ponomarev.calendarsample.data.CalendarEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Тичер on 10.06.2017.
 */
public class CalendarUtils {

    public static final Locale USES_LOCALE = Locale.ENGLISH;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd:MM:yyyy", USES_LOCALE);

    public static void fillList(Cursor source, List<CalendarEvent> target) {
        if (source.moveToFirst()) {
            while (!source.isAfterLast()) {
                target.add(createCalendarEventFromCursor(source));
                source.moveToNext();
            }
        }
    }

    public static CalendarEvent createCalendarEventFromCursor(Cursor cursor) {
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

    public static String getStringFromTime(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static long getTimeFromView(TextView view) {
        try {
            return DATE_FORMAT.parse(String.valueOf(view.getText())).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
