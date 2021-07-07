package com.SE114PMCL.chatMessenger.Model;

public class Friends {
    private String  imageURL, username, timkiem;

    public Friends( String imageURL, String username, String timkiem){
        this.imageURL=imageURL;
        this.username=username;
        this.timkiem = timkiem;
    }

    public Friends(){

    }

    public String getTimkiem() {
        return timkiem;
    }

    public void setTimkiem(String timkiem) {
        this.timkiem = timkiem;
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
