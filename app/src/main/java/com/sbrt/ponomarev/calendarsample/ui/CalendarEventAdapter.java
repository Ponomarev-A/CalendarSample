package com.sbrt.ponomarev.calendarsample.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sbrt.ponomarev.calendarsample.R;
import com.sbrt.ponomarev.calendarsample.data.CalendarEvent;

import java.util.List;

/**
 * Created by Ponomarev on 12.06.2017.
 */

class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventViewHolder> {

    private Context context;
    private List<CalendarEvent> events;

    public CalendarEventAdapter(Context context, List<CalendarEvent> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public CalendarEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_event_item_layout, parent, false);
        view.setOnClickListener((View.OnClickListener) context);
        return new CalendarEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalendarEventViewHolder holder, int position) {
        holder.bindView(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
