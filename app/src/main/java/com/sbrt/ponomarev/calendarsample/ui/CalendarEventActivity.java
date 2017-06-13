package com.sbrt.ponomarev.calendarsample.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import com.sbrt.ponomarev.calendarsample.R;
import com.sbrt.ponomarev.calendarsample.data.CalendarEvent;
import com.sbrt.ponomarev.calendarsample.data.CalendarLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.sbrt.ponomarev.calendarsample.ui.CalendarActivity.LOADER_ID;

public class CalendarEventActivity extends AppCompatActivity {

    private static final Locale USES_LOCALE = Locale.ENGLISH;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd:MM:yyyy", USES_LOCALE);
    private static final long DEFAULT_EVENT_ID = -1;

    private EditText titleView;
    private EditText descriptionView;
    private EditText toDateView;
    private EditText fromDateView;
    private Long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_event);

        titleView = (EditText) findViewById(R.id.event_title);
        descriptionView = (EditText) findViewById(R.id.event_description);
        fromDateView = (EditText) findViewById(R.id.event_date_from);
        toDateView = (EditText) findViewById(R.id.event_date_to);

        eventId = getIntent().getLongExtra(CalendarActivity.EXTRA_EVENT_ID, DEFAULT_EVENT_ID);
        getSupportLoaderManager().initLoader(LOADER_ID, null, new CalendarEventLoaderCallbacks());
    }

    private void fillViews(CalendarEvent event) {
        titleView.setText(event.title);
        descriptionView.setText(event.description);
        fromDateView.setText(getString(R.string.date_from, DATE_FORMAT.format(new Date(event.dtstart))));
        toDateView.setText(getString(R.string.date_from, DATE_FORMAT.format(new Date(event.dtend))));
    }

    private class CalendarEventLoaderCallbacks implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<CalendarEvent>> {

        @Override
        public Loader<List<CalendarEvent>> onCreateLoader(int id, Bundle args) {
            return new CalendarLoader(CalendarEventActivity.this, eventId == DEFAULT_EVENT_ID ? null : eventId);
        }

        @Override
        public void onLoadFinished(Loader<List<CalendarEvent>> loader, List<CalendarEvent> data) {
            if (data.size() > 0) {
                CalendarEvent event = data.get(0);

                fillViews(event);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<CalendarEvent>> loader) {

        }
    }
}
