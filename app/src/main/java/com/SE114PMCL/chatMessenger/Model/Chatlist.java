package com.SE114PMCL.chatMessenger.Model;

public class Chatlist {
    public String id, timkiemc;

    public Chatlist(String id, String timkiemc) {
        this.id = id;
        this.timkiemc = timkiemc;
    }

    public Chatlist() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimkiemc() {
        return timkiemc;
    }

    public void setTimkiemc(String timkiemc) {
        this.timkiemc = timkiemc;
    }
}
