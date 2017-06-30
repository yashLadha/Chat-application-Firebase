package io.github.yashladha.chat_application;

import android.app.Activity;

public class Message {
    private String ID;
    private String Text;

    public Message() {
    }

    public Message(String ID, String text) {
        this.ID = ID;
        Text = text;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
