package com.sbrt.ponomarev.calendarsample.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.sbrt.ponomarev.calendarsample.R;
import com.sbrt.ponomarev.calendarsample.data.CalendarEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Тичер on 10.06.2017.
 */
public class CalendarEventViewHolder extends RecyclerView.ViewHolder {

    private static final Locale USES_LOCALE = Locale.ENGLISH;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd:MM:yyyy", USES_LOCALE);
    private final Context context;

    public CalendarEventViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    public void bindView(CalendarEvent event) {
        ((TextView) itemView.findViewById(R.id.date_start)).setText(context.getString(R.string.date_from, DATE_FORMAT.format(new Date(event.dtstart))));
        ((TextView) itemView.findViewById(R.id.date_end)).setText(context.getString(R.string.date_to, DATE_FORMAT.format(new Date(event.dtend))));
        ((TextView) itemView.findViewById(R.id.title)).setText(event.title);
        ((TextView) itemView.findViewById(R.id.description)).setText(event.description);
    }

}
