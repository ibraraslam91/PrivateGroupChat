package com.example.ibraraslam.privategroupchat.constant;

/**
 * Created by ibraraslam on 5/26/16.
 */
public class FirebasePath {
    public static final String userDataNode = "userData";
    public static final String privateDataNode = "privateGroupData";
    public static final String conversationNode = "conversationData";

    public static String getUserDataNode() {
        return userDataNode;
    }

    public static String getPrivateDataNode() {
        return privateDataNode;
    }

    public static String getConversationNode() {
        return conversationNode;
    }
}
