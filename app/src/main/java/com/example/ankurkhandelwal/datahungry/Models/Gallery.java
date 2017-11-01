package com.example.ankurkhandelwal.datahungry.Models;

import java.util.Date;

public class Gallery {
    public Date dateTaken;
    public String description;
    public String folder_name;
    public String mime_type;
    public String path;

    public Gallery(String path, String folder_name, Date date, String description, String mime_type) {
        this.path = path;
        this.folder_name = folder_name;
        this.dateTaken = date;
        this.description = description;
        this.mime_type = mime_type;
    }
}
