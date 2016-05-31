package com.example.ibraraslam.privategroupchat.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ibraraslam.privategroupchat.R;
import com.example.ibraraslam.privategroupchat.constant.FirebasePath;
import com.example.ibraraslam.privategroupchat.model.GroupDataModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddPriavteGroupActivity extends AppCompatActivity {

    ListView userListView;
    ArrayList<UserDataModel> userList;
    EditText groupName;
    Button addPrivatGroup;
    UserListAdapter adapter;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_priavte_group);
        userID = getIntent().getStringExtra("userID");
        userList = new ArrayList<UserDataModel>();
        adapter = new UserListAdapter(this,101,userList);
        userListView = (ListView) findViewById(R.id.user_list_view);
        userListView.setAdapter(adapter);
        groupName = (EditText) findViewById(R.id.private_name_Edt);
        getUsersData();
        addPrivatGroup = (Button) findViewById(R.id.add_private_group_Btn);
        addPrivatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupName.getText().toString().trim().isEmpty()){

                }else {
                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                    DatabaseReference privateGroupData = rootNode.getReference(FirebasePath.getPrivateDataNode());
                    String conKey = privateGroupData.push().getKey();
                    GroupDataModel model = new GroupDataModel();
                    model.setConKey(conKey);
                    model.setName(groupName.getText().toString());
                    privateGroupData.child(userID).push().setValue(model);
                    for(UserDataModel userData : userList){
                        if(userData.isSelected()){
                            privateGroupData.child(userData.getUserID()).push().setValue(model);
                        }
                    }
                }
            }
        });


    }

    public void getUsersData(){
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference userData = rootNode.getReference(FirebasePath.getUserDataNode());
        userData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!dataSnapshot.getKey().equals(userID)){
                    UserDataModel userDataModel = new UserDataModel();
                    userDataModel.setName((String) dataSnapshot.getValue());
                    userDataModel.setUserID(dataSnapshot.getKey());
                    userDataModel.setSelected(false);
                    userList.add(userDataModel);
                    adapter.notifyDataSetChanged();
                    Log.d("TAG",userDataModel.getName());
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class UserListAdapter extends ArrayAdapter<UserDataModel>{

        public UserListAdapter(Context context, int resource, List<UserDataModel> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView== null){
                LayoutInflater layoutInflater = LayoutInflater.from(AddPriavteGroupActivity.this);
                convertView = layoutInflater.inflate(R.layout.user_list_item,parent,false);
            }

            UserDataModel userData = userList.get(position);

            CheckBox selected = (CheckBox) convertView.findViewById(R.id.select);
            TextView userName = (TextView) convertView.findViewById(R.id.userName);
            userName.setText(userData.getName());
            selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    userList.get(position).setSelected(isChecked);
                }
            });

            return convertView;
        }
    }

    class UserDataModel{
        String name,userID;
        boolean selected;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
