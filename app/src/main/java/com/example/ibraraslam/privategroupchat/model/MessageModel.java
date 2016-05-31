package com.example.ibraraslam.privategroupchat.model;




/**
 * Created by ibraraslam on 5/26/16.
 */
public class MessageModel {
    String messageTxt;
    String messageType;
    String senderID;
    String url;

    public String getMessageTxt() {
        return messageTxt;
    }

    public void setMessageTxt(String messageTxt) {
        this.messageTxt = messageTxt;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
