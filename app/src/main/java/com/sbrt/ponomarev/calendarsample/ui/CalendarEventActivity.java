package com.sbrt.ponomarev.calendarsample.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

    private static final String TAG = CalendarEventActivity.class.getSimpleName();

    private static final Locale USES_LOCALE = Locale.ENGLISH;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd:MM:yyyy", USES_LOCALE);
    private static final long DEFAULT_EVENT_ID = -1;
    private static final String EXTRA_LOADER_TASK = "EXTRA_LOADER_TASK";

    private EditText titleView;
    private EditText descriptionView;
    private EditText toDateView;
    private EditText fromDateView;
    private EditText[] editTextViews;

    private boolean isTextChanged = false;
    private CalendarEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_event);

        titleView = (EditText) findViewById(R.id.event_title);
        descriptionView = (EditText) findViewById(R.id.event_description);
        fromDateView = (EditText) findViewById(R.id.event_date_from);
        toDateView = (EditText) findViewById(R.id.event_date_to);

        editTextViews = new EditText[]{titleView, descriptionView, fromDateView, toDateView};
        for (EditText editText : editTextViews) {
            editText.addTextChangedListener(new TextWatcherImpl());
        }

        event = new CalendarEvent();
        event.id = getIntent().getLongExtra(CalendarActivity.EXTRA_EVENT_ID, DEFAULT_EVENT_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        restartLoader(CalendarLoader.CalendarEventTask.READ);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isTextChanged) {
            restartLoader(CalendarLoader.CalendarEventTask.UPDATE);
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void fillViews(CalendarEvent event) {
        titleView.setText(event.title);
        descriptionView.setText(event.description);
        fromDateView.setText(getString(R.string.date_from, DATE_FORMAT.format(new Date(event.dtstart))));
        toDateView.setText(getString(R.string.date_from, DATE_FORMAT.format(new Date(event.dtend))));
    }

    private void restartLoader(CalendarLoader.CalendarEventTask task) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LOADER_TASK, task);
        getSupportLoaderManager().restartLoader(LOADER_ID, args, new CalendarEventLoaderCallbacks());
    }

    private class CalendarEventLoaderCallbacks implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<CalendarEvent>> {

        @Override
        public Loader<List<CalendarEvent>> onCreateLoader(int id, Bundle args) {

            CalendarLoader.CalendarEventTask task = (CalendarLoader.CalendarEventTask) args.getSerializable(EXTRA_LOADER_TASK);
            Log.e(TAG, "onCreateLoader: " + task.toString() + ", event : " + event);

            return new CalendarLoader(
                    CalendarEventActivity.this,
                    CalendarLoader.CalendarEventTask.READ,
                    event
            );

        }

        @Override
        public void onLoadFinished(Loader<List<CalendarEvent>> loader, List<CalendarEvent> data) {
            CalendarEvent event = data.get(0);
            Log.e(TAG, "onLoadFinished, event: " + event);

            fillViews(event);
        }

        @Override
        public void onLoaderReset(Loader<List<CalendarEvent>> loader) {

        }
    }

    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            isTextChanged = true;
        }
    }
}
