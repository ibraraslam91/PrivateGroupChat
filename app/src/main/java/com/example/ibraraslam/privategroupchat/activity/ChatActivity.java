package com.example.ibraraslam.privategroupchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibraraslam.privategroupchat.R;
import com.example.ibraraslam.privategroupchat.adapter.MessageListAdapter;
import com.example.ibraraslam.privategroupchat.constant.FirebasePath;
import com.example.ibraraslam.privategroupchat.model.GroupDataModel;
import com.example.ibraraslam.privategroupchat.model.MessageModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ListView messageListView;
    Button sendBtn;
    ArrayList<MessageModel> messageList;
    MessageListAdapter adapter;
    String userID,conKey;

    final int REQUEST_FILE_SELECT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userID = getIntent().getStringExtra("userID");
        conKey = getIntent().getStringExtra("conKey");
        messageListView = (ListView) findViewById(R.id.messageList);
        sendBtn = (Button) findViewById(R.id.send_message_Btn);
        messageList = new ArrayList<MessageModel>();
        adapter = new MessageListAdapter(this,101,messageList,userID);
        messageListView.setAdapter(adapter);
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageModel messageModel = messageList.get(position);
                if(messageModel.getMessageType().equals("File")){

                }else {
                    Toast.makeText(ChatActivity.this,"No File to Download",Toast.LENGTH_SHORT).show();
                }
            }
        });

        GetMessagesData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_attach:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void getFile(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file to upload"), REQUEST_FILE_SELECT);
    }

    public void GetMessagesData(){
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference conversationNode = rootNode.getReference(FirebasePath.getConversationNode());
        conversationNode.child(conKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                messageList.add(messageModel);
                adapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
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

    private void scrollMyListViewToBottom() {
        messageListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                messageListView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {

            if(requestCode == REQUEST_FILE_SELECT) {
//                Uri uri = data.getData();
//                FileUploadTask task = new FileUploadTask(this,conKey,userID,this);
//                task.execute(uri,null,null);
            }
        }
        else {
            return;
        }

    }
}
