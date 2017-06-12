package com.sbrt.ponomarev.calendarsample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = CalendarActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final int LOADER_ID = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

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

    private boolean isCalendarPermissionDenied() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED;
    }

    private void requestCalendarPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
    }

    private void initCalendarLoader() {
        getSupportLoaderManager().initLoader(LOADER_ID, null, new CalendarLoaderCallbacks());
    }

    private class CalendarLoaderCallbacks implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<CalendarEvent>> {

        @Override
        public Loader<List<CalendarEvent>> onCreateLoader(int id, Bundle args) {
            return new CalendarLoader(CalendarActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<List<CalendarEvent>> loader, List<CalendarEvent> data) {
            Log.e(TAG, "events = " + data);
        }

        @Override
        public void onLoaderReset(Loader<List<CalendarEvent>> loader) {

        }
    }
}
