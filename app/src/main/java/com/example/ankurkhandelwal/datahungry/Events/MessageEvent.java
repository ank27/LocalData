package com.example.ankurkhandelwal.datahungry.Events;

public class MessageEvent {
    public String event;
    public String mobile;
    public String msg;
    public String type;

    public MessageEvent(String event, String mobile, String msg, String type) {
        this.event = event;
        this.mobile = mobile;
        this.msg = msg;
        this.type = type;
    }
}
