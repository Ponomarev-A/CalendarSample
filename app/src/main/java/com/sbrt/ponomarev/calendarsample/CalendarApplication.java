package com.sbrt.ponomarev.calendarsample;

import android.app.Application;
import com.sbrt.ponomarev.calendarsample.data.CalendarDAO;

/**
 * Created by Ponomarev on 13.06.2017.
 */

public class CalendarApplication extends Application {

    private final CalendarDAO dao;

    public CalendarApplication() {
        dao = new CalendarDAO(this);
    }


    public CalendarDAO getCalendarDao() {
        return dao;
    }
}
