package com.example.ibraraslam.privategroupchat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibraraslam.privategroupchat.R;
import com.example.ibraraslam.privategroupchat.model.MessageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibraraslam on 5/26/16.
 */
public class MessageListAdapter extends ArrayAdapter<MessageModel> {
    Context context;
    ArrayList<MessageModel> messagelist;
    int resource;
    String userID;

    private final static int LEFT_MESSAGE = 0;
    private final static int RIGHT_MESSAGE = 1;


    public MessageListAdapter(Context context, int resource, ArrayList<MessageModel> objects,String userID) {
        super(context, resource, objects);
        this.context = context;
        messagelist = objects;
        this.resource = resource;
        this.userID = userID;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int viewType = this.getItemViewType(position);
        MessageModel messageModel = messagelist.get(position);
        if(convertView== null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            viewHolder = new ViewHolder();
            switch (viewType) {
                case LEFT_MESSAGE:
                    layoutInflater = LayoutInflater.from(context);
                    convertView = layoutInflater.inflate(R.layout.incoming_chat_item, parent, false);
                    viewHolder.messageText = (TextView) convertView.findViewById(R.id.message_text_Txt);
                    viewHolder.downloadIcon = (ImageView) convertView.findViewById(R.id.download_imageView);
                    break;

                case RIGHT_MESSAGE:
                    layoutInflater = LayoutInflater.from(context);
                    convertView = layoutInflater.inflate(R.layout.out_going_chat_item, parent, false);
                    viewHolder.messageText = (TextView) convertView.findViewById(R.id.message_text_Txt);
                    viewHolder.downloadIcon = (ImageView) convertView.findViewById(R.id.download_imageView);
                    break;
            }
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (viewType) {
            case LEFT_MESSAGE:
                viewHolder.messageText.setText(messageModel.getMessageTxt());
                if (messageModel.getMessageType().equals("File")){
                    viewHolder.downloadIcon.setImageResource(R.drawable.download_icon);
                }
                else{
                     viewHolder.downloadIcon.setImageDrawable(null);
                 }
                break;
            case RIGHT_MESSAGE:
                viewHolder.messageText.setText(messageModel.getMessageTxt());
                if (messageModel.getMessageType().equals("File")){
                    viewHolder.downloadIcon.setImageResource(R.drawable.download_icon);
                }else{
                    viewHolder.downloadIcon.setImageDrawable(null);
                }
                break;
        }


        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        MessageModel message = messagelist.get(position);
        if(message.getSenderID().equals(userID)){
            return RIGHT_MESSAGE;
        }
        else {
            return LEFT_MESSAGE;
        }
    }

    public static class ViewHolder{
        TextView messageText;
        ImageView downloadIcon;
    }
}
