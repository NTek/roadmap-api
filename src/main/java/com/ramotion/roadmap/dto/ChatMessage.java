package com.ramotion.roadmap.dto;

/**
 * Created by Oleg Vasiliev on 14.11.2014.
 */
public class ChatMessage {

    private String text;
    private String username;


    public ChatMessage() {
    }

    public ChatMessage(String text, String username) {
        this.text = text;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
