package com.SE114PMCL.chatMessenger.Model;

public class NoteRequest {
    String id;

    public NoteRequest(String id) {
        this.id = id;
    }

    public NoteRequest(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
