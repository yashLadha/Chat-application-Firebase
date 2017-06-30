package io.github.yashladha.chat_application;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class Chat_person extends Activity {

    private String TAG = getClass().getSimpleName();

    private EditText editText;
    private Button submit;
    private RecyclerView messageList;

    private String uid;
    private String curUid;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_person);

        database = FirebaseDatabase.getInstance();

        final DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss:SSS");

        Intent i = getIntent();
        uid = i.getStringExtra("User uid");
        curUid = i.getStringExtra("Current uid");
        Log.d(TAG, "User selected is " + uid);


        Log.d(TAG, database.getReference().child("/"+uid+"/chat/"+curUid).toString());
        final DatabaseReference ref_chat = database.getReference().child("/"+uid+"/allMessage/"+curUid);
        final DatabaseReference ref_user = database.getReference().child("/"+curUid+"/allMessage/"+uid);

        editText = (EditText) findViewById(R.id.etMessageInput);
        submit = (Button) findViewById(R.id.btSubmit);
        messageList = (RecyclerView) findViewById(R.id.rvMessageBox);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        messageList.setLayoutManager(layoutManager);

        final HashSet<String> readMessages = new HashSet<>();

        final ArrayList<Message> messages = new ArrayList<>();

        ref_chat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> messageLogs = dataSnapshot.getChildren();
                for (DataSnapshot messageL : messageLogs) {
                    Message item = messageL.getValue(Message.class);
                    messages.add(item);
                    readMessages.add(messageL.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (curUid != null) {
            final MessageAdapter messageAdapter = new MessageAdapter(curUid, messages);
            messageList.setAdapter(messageAdapter);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Submit message button pressed");
                    String textMessage = editText.getText().toString();
                    Message tempObj = new Message(curUid, textMessage);
                    if (!textMessage.equals("")) {
                        editText.setText("");
                        messages.add(tempObj);
                        messageAdapter.notifyDataSetChanged();
                        String dateSnap = UniqueId();
                        readMessages.add("\"" + dateSnap + "\"");
                        SendAllMessage(tempObj, dateSnap, ref_chat, ref_user);
                        messageList.smoothScrollToPosition(readMessages.size());

                    }
                }
            });

            ref_chat.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for ( DataSnapshot items : dataSnapshot.getChildren() ) {
                        if (!readMessages.contains(items.getKey())) {
                            // TODO: Implement a smart logic for listener
                            // PlayNotificationSound();

                            Message temp = items.getValue(Message.class);

                            // In Case if same uid gets triggered
                            if (!temp.getID().equals(curUid)) {
                                Log.d(TAG, items.getKey() + " Key received");
                                readMessages.add(items.getKey());
                                messages.add(temp);
                                messageAdapter.notifyDataSetChanged();
                                messageList.smoothScrollToPosition(readMessages.size());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void PlayNotificationSound() {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
        r.play();
    }

    @NonNull
    private String UniqueId() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.getTimeInMillis());
    }

    private void SendAllMessage(Message tempObj, String dateSnap, DatabaseReference ref_chat, DatabaseReference ref_user) {
        Log.d(TAG, "Database gets updated");
        ref_chat.child("\""+ dateSnap + "\"").setValue(tempObj);
        ref_user.child("\""+ dateSnap + "\"").setValue(tempObj);
    }

}
