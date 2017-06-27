package io.github.yashladha.chat_application;

import android.app.Activity;

public class Message {
    private Boolean status;
    private String Text;

    public Message() {
    }

    public Message(Boolean status, String text) {
        this.status = status;
        Text = text;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
