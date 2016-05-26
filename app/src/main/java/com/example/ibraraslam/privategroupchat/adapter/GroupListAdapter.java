package com.example.ibraraslam.privategroupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ibraraslam.privategroupchat.model.GroupDataModel;
import com.example.ibraraslam.privategroupchat.R;

import java.util.ArrayList;

/**
 * Created by ibraraslam on 5/26/16.
 */
public class GroupListAdapter extends ArrayAdapter<GroupDataModel> {

    ArrayList<GroupDataModel> list ;
    int resource;
    Context context;
    public GroupListAdapter(Context context, int resource, ArrayList<GroupDataModel> objects) {
        super(context, resource, objects);
        list = objects;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resource,parent,false);
        }

        TextView groupName = (TextView) convertView.findViewById(R.id.group_name);
        GroupDataModel model = list.get(position);
        groupName.setText(model.getName());
        return convertView;

    }
}
