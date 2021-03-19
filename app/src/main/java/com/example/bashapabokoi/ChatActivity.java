package com.example.bashapabokoi;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bashapabokoi.Adapters.MessagesAdapters;
import com.example.bashapabokoi.Models.Message;
import com.example.bashapabokoi.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    MessagesAdapters adapter;
    ArrayList<Message> messages;

    String senderRoom, receiverRoom;


    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messages = new ArrayList<>();
        adapter = new MessagesAdapters(this, messages);
        binding.recylerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recylerView.setAdapter(adapter);


        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        database = FirebaseDatabase.getInstance();

        database.getReference().child("Chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
                            messages.add(message);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.sendBtn.setOnClickListener(v -> {
            String messageTxt = binding.messageBox.getText().toString();

            Date date = new Date();


            Message message = new Message(messageTxt, senderUid, date.getTime());
            binding.messageBox.setText("");


            String randomKey = database.getReference().push().getKey();

            HashMap<String, Object> lastMsgObj = new HashMap<>();
            lastMsgObj.put("lastMsg", message.getMessage());
            lastMsgObj.put("lastMsgTime", date.getTime());

            database.getReference().child("Chats").child(senderRoom).updateChildren(lastMsgObj);
            database.getReference().child("Chats").child(receiverRoom).updateChildren(lastMsgObj);

            database.getReference()
                    .child("Chat_time")
                    .child(senderRoom)
                    .setValue(date.getTime())
                    .addOnSuccessListener(aVoid -> {

                    });

            database.getReference().child("Chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(randomKey)
                    .setValue(message).addOnSuccessListener(aVoid -> database.getReference().child("User_friends")
                            .child(senderUid)
                            .child("Friends")
                            .child(senderRoom)
                            .setValue(receiverUid).addOnSuccessListener(aVoid1 -> database.getReference().child("Chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .child(randomKey)
                                    .setValue(message).addOnSuccessListener(aVoid11 -> database.getReference().child("User_friends")
                                            .child(receiverUid)
                                            .child("Friends")
                                            .child(receiverRoom)
                                            .setValue(senderUid).addOnSuccessListener(aVoid111 -> {

                                            }))));

        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}