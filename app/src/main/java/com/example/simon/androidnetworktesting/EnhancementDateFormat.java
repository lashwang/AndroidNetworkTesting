package com.example.simon.androidnetworktesting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class EnhancementDateFormat extends SimpleDateFormat {

    private String currentId = TimeZone.getDefault().getID();

    public EnhancementDateFormat(String pattern, Locale locale){
        super(pattern, locale);
    }

    public String enhFormat(Date date){
        if (!currentId.equals(TimeZone.getDefault().getID())){
            TimeZone currentTimezone = TimeZone.getDefault();
            currentId = currentTimezone.getID();
            super.setTimeZone(currentTimezone);

        }
        return super.format(date);
    }
}
