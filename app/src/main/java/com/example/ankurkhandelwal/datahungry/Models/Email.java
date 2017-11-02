package com.example.ankurkhandelwal.datahungry.Models;

public class Email {
    public String email_body;
    public String email_snippet;
    public String email_sender;
    public Long email_date;

    public Email(String email_sender, String email_snippet, String email_body, Long email_date) {
        this.email_body = email_body;
        this.email_sender = email_sender;
        this.email_snippet = email_snippet;
        this.email_date = email_date;
    }
}
