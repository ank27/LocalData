package com.example.ankurkhandelwal.datahungry.Models;

public class CallLogsData {
    public String duration;
    public int in_or_out;
    public String name;
    public String number;

    public CallLogsData(String number, String name, String duration, int in_or_out) {
        this.number = number;
        this.name = name;
        this.duration = duration;
        this.in_or_out = in_or_out;
    }
}
