package com.sbrt.ponomarev.calendarsample.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.sbrt.ponomarev.calendarsample.CalendarApplication;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ponomarev on 12.06.2017.
 */

public class CalendarLoader extends AsyncTaskLoader<List<CalendarEvent>> {

    private static final String TAG = CalendarLoader.class.getSimpleName();

    public enum CalendarEventTask {
        CREATE,
        READ,
        UPDATE,
        DELETE
    }

    private CalendarEventTask task;
    private CalendarEvent event;
    private CalendarDAO dao;


    public CalendarLoader(Context context, CalendarEventTask task, CalendarEvent event) {
        super(context);
        this.task = task;
        this.event = event;
        this.dao = ((CalendarApplication) (getContext().getApplicationContext())).getCalendarDao();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<CalendarEvent> loadInBackground() {

        List<CalendarEvent> result = null;

        Log.e(TAG, "loadInBackground, event: "+event);

        switch (task) {

            case CREATE:
                event.id = dao.insertCalendarEvent(event);
                result = Collections.singletonList(event);
                break;

            case READ:
                result = event == null ?
                        dao.getCalendarEvents() :
                        Collections.singletonList(dao.getCalendarEvent(event.id));
                break;

            case UPDATE:
                boolean isUpdated = dao.updateCalendarEvent(event) > 0;
                if (isUpdated) {
                    result = Collections.singletonList(event);
                }
                break;

            case DELETE:
                boolean isDeleted = dao.deleteCalendarEvent(event.id) > 0;
                if (isDeleted) {
                    result = Collections.singletonList(event);
                }
                break;
        }

        return result;
    }
}
