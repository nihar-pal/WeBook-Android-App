package com.spacester.webook;

public class ModelPost {

    String description, link, title, user, username;
    long id;

    public ModelPost() {
    }

    public ModelPost(String description, String link, String title, String user, String username, long id) {
        this.description = description;
        this.link = link;
        this.title = title;
        this.user = user;
        this.username = username;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

