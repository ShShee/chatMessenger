package com.SE114PMCL.chatMessenger.Model;

public class Friends {
    private String  imageURL, username;

    public Friends( String imageURL, String username){
        this.imageURL=imageURL;
        this.username=username;
    }

    public Friends(){

    }


    public String getImageURL(){
        return imageURL;
    }

    public void setImageURL(String imageURL){
        this.imageURL=imageURL;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }

}
