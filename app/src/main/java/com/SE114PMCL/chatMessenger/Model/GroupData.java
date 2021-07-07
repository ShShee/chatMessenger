package com.SE114PMCL.chatMessenger.Model;

public class GroupData {
    private String groupId, groupTitle, groupDescription, groupIcon, timkiemG, timestamp, createdBy;

    public GroupData(String groupId, String groupTitle, String groupDescription, String groupIcon, String timkiemG, String timestamp, String createdBy){
        this.groupId=groupId;
        this.groupTitle=groupTitle;
        this.groupDescription=groupDescription;
        this.groupIcon=groupIcon;
        this.timkiemG = timkiemG;
        this.timestamp=timestamp;
        this.createdBy=createdBy;
    }
    public GroupData(){

    }


    public String getGroupId(){
        return groupId;
    }

    public void setGroupId(String groupId){
        this.groupId=groupId;
    }

    public String getGroupTitle(){
        return groupTitle;
    }

    public  void setGroupTitle(String groupTitle){
        this.groupTitle=groupTitle;
    }

    public String getGroupDescription(){
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription){
        this.groupDescription=groupDescription;
    }

    public String getGroupIcon(){
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon){
        this.groupIcon=groupIcon;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }

    public String getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(String createdBy){
        this.createdBy=createdBy;
    }

    public String getTimkiemG() {
        return timkiemG;
    }

    public void setTimkiemG(String timkiemG) {
        this.timkiemG = timkiemG;
    }
}
