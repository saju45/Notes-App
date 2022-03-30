package com.example.notesapp.Model;

public class Notes {

    private String title;
    private String body;
    private String pushId;

    public Notes() {
    }

    public Notes(String title, String body, String pushId) {
        this.title = title;
        this.body = body;
        this.pushId = pushId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
