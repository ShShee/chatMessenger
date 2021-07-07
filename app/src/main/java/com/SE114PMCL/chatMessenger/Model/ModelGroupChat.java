package com.SE114PMCL.chatMessenger.Model;

public class ModelGroupChat {
    String message, sender, timestamp, type;

    public ModelGroupChat(){

    }
    public ModelGroupChat(String message, String sender, String timestamp,String type){
        this.sender=sender;
        this.message=message;
        this.timestamp=timestamp;
        this.type=type;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message=message;
    }

    public String getSender(){
        return sender;
    }

    public void setSender(String sender){
        this.message=sender;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type=type;
    }









}
