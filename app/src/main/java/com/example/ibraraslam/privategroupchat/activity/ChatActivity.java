package com.example.ibraraslam.privategroupchat.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibraraslam.privategroupchat.R;
import com.example.ibraraslam.privategroupchat.adapter.MessageListAdapter;
import com.example.ibraraslam.privategroupchat.constant.FirebasePath;
import com.example.ibraraslam.privategroupchat.model.GroupDataModel;
import com.example.ibraraslam.privategroupchat.model.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ListView messageListView;
    Button sendBtn;
    ArrayList<MessageModel> messageList;
    MessageListAdapter adapter;
    String userID,conKey;
    FirebaseDatabase rootNode;
    DatabaseReference conversationNode;
    EditText messageEdt;
    final int REQUEST_FILE_SELECT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userID = getIntent().getStringExtra("userID");
        conKey = getIntent().getStringExtra("conKey");
        Log.d("TAG", "Chat Activity userID"+userID);
        Log.d("TAG","Chat Activity conKey "+ conKey);
        rootNode = FirebaseDatabase.getInstance();
        conversationNode = rootNode.getReference(FirebasePath.getConversationNode()).child(conKey);
        messageEdt = (EditText) findViewById(R.id.message_Edt);
        messageListView = (ListView) findViewById(R.id.messageList);
        sendBtn = (Button) findViewById(R.id.send_message_Btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!messageEdt.getText().toString().trim().isEmpty()){
                    MessageModel messageModel = new MessageModel();
                    messageModel.setSenderID(userID);
                    messageModel.setMessageType("Text");
                    messageModel.setMessageTxt(messageEdt.getText().toString());
                    //messageModel.setUrl("");
                    sendMessage(messageModel);
                    messageEdt.setText("");
                }
            }
        });
        messageList = new ArrayList<MessageModel>();
        adapter = new MessageListAdapter(this,101,messageList,userID);
        messageListView.setAdapter(adapter);
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageModel messageModel = messageList.get(position);
                if(messageModel.getMessageType().equals("File")){
                    DownloadManager.Request request =new DownloadManager.Request(Uri.parse(messageModel.getUrl()));
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, messageModel.getMessageTxt());
                    request.allowScanningByMediaScanner();
                    request.setTitle("Downloading "+messageModel.getMessageTxt());
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
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
                getFile();
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
        rootNode = FirebaseDatabase.getInstance();
        conversationNode.addChildEventListener(new ChildEventListener() {
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


    public void sendMessage(MessageModel messageModel){
        conversationNode.push().setValue(messageModel);
    }
    String name;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {

            if(requestCode == REQUEST_FILE_SELECT) {
                Uri uri = data.getData();
                String temp = rootNode.getReference().push().getKey();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference fileRef = storage.getReferenceFromUrl(FirebasePath.getStoragePath()).child("PrivateChatFiles").child(temp).child(uri.getLastPathSegment());
                UploadTask uploadTask =  fileRef.putFile(uri);
                name = uri.getLastPathSegment();
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        MessageModel messageModel = new MessageModel();
                        messageModel.setMessageTxt(name);
                        messageModel.setMessageType("File");
                        messageModel.setSenderID(userID);
                        messageModel.setUrl(taskSnapshot.getDownloadUrl().toString());
                        sendMessage(messageModel);
                    }
                });
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","Upload Failure " + e.getMessage());
                    }
                });
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Upload",taskSnapshot.getBytesTransferred()+"");
                    }
                });
            }
        }
        else {
            return;
        }

    }
}
