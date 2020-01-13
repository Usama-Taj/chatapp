package com.example.mychatapp.models;


public class Chat  {
    public String receiver;
    public String senderUid;
    public String receiverUid;

    public String sender_name;
    public String reciever_name;
    public String sender_email;
    public String reciever_email;
    public String message;

    public String sender;
    public String sender_image;


    public static String indicator;

    public Chat(String sender_name, String reciever_name, String sender_email, String reciever_email, String message) {
        this.sender_name = sender_name;
        this.reciever_name = reciever_name;
        this.sender_email = sender_email;
        this.reciever_email = reciever_email;
        this.message = message;
    }

    public Chat(String sender_name, String message, String sender_image) {
        this.sender_name = sender_name;
        this.message = message;
        this.sender_image = sender_image;
    }

    public String getMessage() {
        return message;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender_image() {
        return sender_image;
    }

    public void setSender_image(String sender_image) {
        this.sender_image = sender_image;
    }



}