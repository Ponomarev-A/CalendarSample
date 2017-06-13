package com.sbrt.ponomarev.calendarsample.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sbrt.ponomarev.calendarsample.CalendarApplication;
import com.sbrt.ponomarev.calendarsample.R;
import com.sbrt.ponomarev.calendarsample.data.CalendarDAO;
import com.sbrt.ponomarev.calendarsample.data.CalendarEvent;
import com.sbrt.ponomarev.calendarsample.utlis.CalendarUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.sbrt.ponomarev.calendarsample.ui.CalendarActivity.LOADER_ID;

public class CalendarEventActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CalendarEventActivity.class.getSimpleName();

    private static final long DEFAULT_EVENT_ID = -1;
    private static final String TAG_DATE_PICKER = "datePicker";
    private static final String EXTRA_VIEW_ID = "EXTRA_VIEW_ID";
    private static final String EXTRA_DATE = "EXTRA_DATE";

    private EditText titleView;
    private EditText descriptionView;
    private TextView toDateView;
    private TextView fromDateView;

    private CalendarEvent event;
    private CalendarDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_event);

        titleView = (EditText) findViewById(R.id.event_title);
        descriptionView = (EditText) findViewById(R.id.event_description);
        fromDateView = (TextView) findViewById(R.id.event_date_from);
        toDateView = (TextView) findViewById(R.id.event_date_to);
        fromDateView.setOnClickListener(this);
        toDateView.setOnClickListener(this);

        event = new CalendarEvent();
        event.id = getIntent().getLongExtra(CalendarActivity.EXTRA_EVENT_ID, DEFAULT_EVENT_ID);

        dao = ((CalendarApplication) getApplicationContext()).getCalendarDao();

        if (event.id == DEFAULT_EVENT_ID) {
            event.id = dao.insertCalendarEvent(event);
            setResult(RESULT_OK);
            Toast.makeText(this, R.string.event_created, Toast.LENGTH_SHORT).show();
        } else {
            setResult(RESULT_CANCELED);
            getSupportLoaderManager().initLoader(LOADER_ID, null, new CalendarEventLoaderCallbacks());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_delete:
                int deletedRows = dao.deleteCalendarEvent(event.id);
                if (deletedRows > 0) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, R.string.event_deleted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.event_no_action_performed, Toast.LENGTH_SHORT).show();
                }
                finish();
                return true;

            case R.id.event_save:
                fillCalendarEventFromViews(event);
                int updatedRows = dao.updateCalendarEvent(event);
                if (updatedRows > 0) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, R.string.event_saved, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.event_no_action_performed, Toast.LENGTH_SHORT).show();
                }
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        DialogFragment newFragment = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putInt(EXTRA_VIEW_ID, viewId);
        args.putLong(EXTRA_DATE, CalendarUtils.getTimeFromView((TextView) findViewById(viewId)));

        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), TAG_DATE_PICKER);
    }

    private void fillViews(CalendarEvent event) {
        titleView.setText(event.title);
        descriptionView.setText(event.description);
        fromDateView.setText(CalendarUtils.getStringFromTime(new Date(event.dtstart)));
        toDateView.setText(CalendarUtils.getStringFromTime(new Date(event.dtend)));
    }

    private CalendarEvent fillCalendarEventFromViews(CalendarEvent event) {
        event.title = String.valueOf(titleView.getText());
        event.description = String.valueOf(descriptionView.getText());
        event.dtstart = CalendarUtils.getTimeFromView((TextView) findViewById(fromDateView.getId()));
        event.dtend = CalendarUtils.getTimeFromView((TextView) findViewById(toDateView.getId()));
        return event;
    }

    private class CalendarEventLoaderCallbacks implements LoaderManager.LoaderCallbacks {

        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            return new CursorLoader(CalendarEventActivity.this,
                    CalendarContract.Events.CONTENT_URI,
                    null, CalendarContract.Events._ID + "=?", new String[]{String.valueOf(event.id)}, null
            );
        }

        @Override
        public void onLoadFinished(Loader loader, Object data) {
            Cursor cursor = (Cursor) data;
            CalendarEvent event = null;

            if (cursor.moveToFirst()) {
                event = CalendarUtils.createCalendarEventFromCursor(cursor);
                fillViews(event);
            }

            Log.e(TAG, "onLoadFinished, event: " + event);
        }

        @Override
        public void onLoaderReset(Loader loader) {
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private int viewId;
        private long timeMs;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();
            timeMs = args.getLong(EXTRA_DATE);
            viewId = args.getInt(EXTRA_VIEW_ID);

            final Calendar c = new GregorianCalendar();
            c.setTime(new Date(timeMs));
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            ((TextView) getActivity().findViewById(viewId)).setText(CalendarUtils.getStringFromTime(new Date(year - 1900, month, dayOfMonth)));
        }
    }
}
