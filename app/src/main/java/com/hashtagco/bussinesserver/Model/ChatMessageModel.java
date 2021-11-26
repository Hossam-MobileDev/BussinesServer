package com.hashtagco.bussinesserver.Model;

public class ChatMessageModel {

    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicturlink() {
        return picturlink;
    }

    public void setPicturlink(String picturlink) {
        this.picturlink = picturlink;
    }

    public boolean isIspicture() {
        return ispicture;
    }

    public void setIspicture(boolean ispicture) {
        this.ispicture = ispicture;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private String name;
    private String content;
    private String picturlink;
    private  boolean ispicture;
    private long timestamp;

    public ChatMessageModel(){

    }

}
