package com.example.chatapp;

public class Message {

    private  String mName ;
    private   String mMessage ;
    private  String  mImageUrl ;
    private String  mtime ;



     public Message(){}
     public Message(String name , String message , String ImageUrl , String time   ){
        this.mName = name ;
        this.mMessage = message ;
        this.mImageUrl = ImageUrl ;
        this.mtime = time ;
    }
    public String getName(){
        return mName ;
    }

    public String getMessage(){
        return mMessage ;
    }

    public String getImageUrl(){
        return mImageUrl ;
    }

    public void  setName(String name ){
         mName = name ;
    }

    public void setMessage(String message ){
         mMessage = message ;
    }
    public void setImageUrl(String imageUrl){
         mImageUrl = imageUrl ;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }
}
