package com.sbrt.ponomarev.calendarsample.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.sbrt.ponomarev.calendarsample.R;
import com.sbrt.ponomarev.calendarsample.data.CalendarEvent;
import com.sbrt.ponomarev.calendarsample.data.CalendarLoader;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CalendarActivity.class.getSimpleName();

    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    public static final int LOADER_ID = 42;

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final int EVENT_REQUEST_CODE = 1;

    private List<CalendarEvent> calendarEventsList;
    private CalendarEventAdapter calendarEventAdapter;
    private RecyclerView calendarEventsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        if (isCalendarPermissionDenied()) {
            requestCalendarPermission();
        } else {
            initCalendarLoader();
        }

        calendarEventsList = new ArrayList<>();
        calendarEventAdapter = new CalendarEventAdapter(CalendarActivity.this, calendarEventsList);

        calendarEventsView = (RecyclerView) findViewById(R.id.recycler_view);
        calendarEventsView.setLayoutManager(new LinearLayoutManager(this));
        calendarEventsView.setAdapter(calendarEventAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && isCalendarPermissionDenied()) {
            initCalendarLoader();
        }
    }

    private boolean isCalendarPermissionDenied() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED;
    }

    private void requestCalendarPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
    }

    private void initCalendarLoader() {
        getSupportLoaderManager().initLoader(LOADER_ID, null, new CalendarLoaderCallbacks());
    }

    @Override
    public void onClick(View v) {
        int itemPosition = calendarEventsView.getChildLayoutPosition(v);
        CalendarEvent event = calendarEventsList.get(itemPosition);
        Log.e(TAG, "item clicked: "+event);

        Intent intent = new Intent(CalendarActivity.this, CalendarEventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, event.id);
        startActivityForResult(intent, EVENT_REQUEST_CODE);
    }

    private class CalendarLoaderCallbacks implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<CalendarEvent>> {

        @Override
        public Loader<List<CalendarEvent>> onCreateLoader(int id, Bundle args) {
            return new CalendarLoader(CalendarActivity.this, null);
        }

        @Override
        public void onLoadFinished(Loader<List<CalendarEvent>> loader, List<CalendarEvent> data) {
            Log.e(TAG, "events = " + data);
            calendarEventsList.clear();
            calendarEventsList.addAll(data);

            calendarEventAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<List<CalendarEvent>> loader) {

        }
    }
}
