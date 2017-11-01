package com.example.ankurkhandelwal.datahungry.Models;

import java.util.Date;

public class CalendarEventsData {
    public String calendar_account_name;
    public String description;
    public Date end_date;
    public String id;
    public String location;
    public Date start_date;
    public String title;

    public CalendarEventsData(String id, String title, String description, String calendar_account_name, Date start_date, Date end_date, String location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.calendar_account_name = calendar_account_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.location = location;
    }
}
