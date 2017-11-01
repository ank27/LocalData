package com.example.ankurkhandelwal.datahungry.Models;

import java.util.Date;

public class BrowsingData {
    public String bookmark;
    public Date date;
    public String id;
    public String title;
    public String url;

    public BrowsingData(String id, Date date, String title, String url, String bookmark) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.url = url;
        this.bookmark = bookmark;
    }
}
