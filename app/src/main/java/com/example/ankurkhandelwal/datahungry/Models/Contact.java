package com.example.ankurkhandelwal.datahungry.Models;

public class Contact {
    public String contact_id;
    public String id;
    public String mobile;
    public String name;

    public Contact(String id, String name, String mobile, String contact_id) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.contact_id = contact_id;
    }
}
