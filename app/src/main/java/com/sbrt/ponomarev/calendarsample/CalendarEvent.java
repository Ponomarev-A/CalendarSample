package com.sbrt.ponomarev.calendarsample;

/**
 * Created by Ponomarev on 12.06.2017.
 */

public class CalendarEvent {

    public long id;
    public long calendarId;
    public long dtstart;
    public long dtend;
    public String rdate;
    public String eventTimezone;
    public String title;
    public String description;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarEvent)) return false;

        CalendarEvent that = (CalendarEvent) o;

        if (id != that.id) return false;
        if (calendarId != that.calendarId) return false;
        if (dtstart != that.dtstart) return false;
        if (dtend != that.dtend) return false;
        if (rdate != null ? !rdate.equals(that.rdate) : that.rdate != null) return false;
        if (eventTimezone != null ? !eventTimezone.equals(that.eventTimezone) : that.eventTimezone != null)
            return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (calendarId ^ (calendarId >>> 32));
        result = 31 * result + (int) (dtstart ^ (dtstart >>> 32));
        result = 31 * result + (int) (dtend ^ (dtend >>> 32));
        result = 31 * result + (rdate != null ? rdate.hashCode() : 0);
        result = 31 * result + (eventTimezone != null ? eventTimezone.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}