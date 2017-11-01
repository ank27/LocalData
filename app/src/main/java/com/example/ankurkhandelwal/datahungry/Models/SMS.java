package com.example.ankurkhandelwal.datahungry.Models;

public class SMS {
    public String id;
    public String message;
    public String person;
    public String read;
    public String sender;
    public int status;
    public String time;
    public String type;

    public SMS(String id, String sender, String time, String message, String type, String read, String person) {
        this.id = id;
        this.sender = sender;
        this.time = time;
        this.message = message;
        this.type = type;
        this.read = read;
        this.person = person;
    }

    public SMS(String time, String message, String type, String read, int status) {
        this.time = time;
        this.message = message;
        this.type = type;
        this.read = read;
        this.status = status;
    }

    public SMS(String sender, String time, String message, String type, String read) {
        this.sender = sender;
        this.time = time;
        this.message = message;
        this.type = type;
        this.read = read;
    }
}
