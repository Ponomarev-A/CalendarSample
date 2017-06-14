package com.sbrt.ponomarev.calendarsample.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.sbrt.ponomarev.calendarsample.CalendarApplication;
import com.sbrt.ponomarev.calendarsample.R;
import com.sbrt.ponomarev.calendarsample.data.CalendarDAO;
import com.sbrt.ponomarev.calendarsample.data.CalendarEvent;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CalendarActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 10;

    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    public static final int LOADER_ID = 42;

    private List<CalendarEvent> calendarEventsList = new ArrayList<>();
    private CalendarEventAdapter calendarEventAdapter;
    private RecyclerView calendarEventsView;
    private CalendarDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        initViews();
        dao = ((CalendarApplication) getApplicationContext()).getCalendarDao();

        if (isCalendarPermissionDenied()) {
            requestCalendarPermission();
        } else {
            initCalendarLoader();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && isCalendarPermissionDenied()) {
            initCalendarLoader();
        }
    }

    private void initViews() {
        calendarEventAdapter = new CalendarEventAdapter(CalendarActivity.this, calendarEventsList);

        calendarEventsView = (RecyclerView) findViewById(R.id.recycler_view);
        calendarEventsView.setLayoutManager(new LinearLayoutManager(this));
        calendarEventsView.setAdapter(calendarEventAdapter);
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
        CalendarEvent event = calendarEventsList.get(calendarEventsView.getChildLayoutPosition(v));
        Log.e(TAG, "item clicked: "+event);

        Intent intent = new Intent(CalendarActivity.this, CalendarEventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, event.id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_add:
                startActivity(new Intent(this, CalendarEventActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class CalendarLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(CalendarActivity.this,
                    CalendarContract.Events.CONTENT_URI,
                    null, null, null, null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            dao.getCalendarEvents(data, calendarEventsList);
            calendarEventAdapter.notifyDataSetChanged();

            Log.e(TAG, "events = " + calendarEventsList);
        }

        @Override
        public void onLoaderReset(Loader arg0) {
        }
    }
}
