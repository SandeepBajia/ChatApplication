package com.example.chatapp;

public class Friend {

    public String musername ;
    public String  mimageurl ;

    public Friend(){}
    public Friend(String username , String  imageurl){
        this.musername = username ;
        this.mimageurl = imageurl ;
    }

    public String getMusername() {
        return musername;
    }

    public void setMusername(String musername) {
        this.musername = musername;
    }

    public String getMimageurl() {
        return mimageurl;
    }

    public void setMimageurl(String mimageurl) {
        this.mimageurl = mimageurl;
    }
}
