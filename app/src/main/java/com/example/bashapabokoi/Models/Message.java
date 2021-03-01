package com.example.bashapabokoi.Models;

public class Message {

    private String messageId, message, senderId;
    private long timeStamp;

    public Message() {
    }

    public Message(String message, String senderId, long timeStamp) {
        this.message = message;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
