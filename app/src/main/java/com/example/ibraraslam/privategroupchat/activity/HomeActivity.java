package com.example.ibraraslam.privategroupchat.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ibraraslam.privategroupchat.constant.FirebasePath;
import com.example.ibraraslam.privategroupchat.model.GroupDataModel;
import com.example.ibraraslam.privategroupchat.R;
import com.example.ibraraslam.privategroupchat.adapter.GroupListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton addGroupFat;
    GroupListAdapter adapter;
    ArrayList<GroupDataModel> groupList;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userID = getIntent().getStringExtra("userID");
        groupList = new ArrayList<GroupDataModel>();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new GroupListAdapter(this,R.layout.group_list_item,groupList);
        listView.setAdapter(adapter);
        addGroupFat = (FloatingActionButton) findViewById(R.id.add_group_fat);
        addGroupFat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupDataModel model = groupList.get(position);
                //Intent intent = new Intent(HomeActivity.this,);
            }
        });
        getGroupsList();


    }


    public void getGroupsList(){
        FirebaseDatabase rootRef = FirebaseDatabase.getInstance();
        DatabaseReference privateGroupNode = rootRef.getReference(FirebasePath.getPrivateDataNode());
        privateGroupNode.child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GroupDataModel model = dataSnapshot.getValue(GroupDataModel.class);
                groupList.add(model);
                adapter.notifyDataSetChanged();
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
}
