package com.example.bashapabokoi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bashapabokoi.Adapters.MessagesAdapters;
import com.example.bashapabokoi.Models.Message;
import com.example.bashapabokoi.Models.User;
import com.example.bashapabokoi.Notifications.APIService;
import com.example.bashapabokoi.Notifications.Client;
import com.example.bashapabokoi.Notifications.Data;
import com.example.bashapabokoi.Notifications.MyResponse;
import com.example.bashapabokoi.Notifications.Sender;
import com.example.bashapabokoi.Notifications.Token;
import com.example.bashapabokoi.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    MessagesAdapters adapter;
    ArrayList<Message> messages;

    String senderRoom, receiverRoom;


    FirebaseDatabase database;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    APIService apiService;

    boolean notify = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = Client.getClient().create(APIService.class);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messages = new ArrayList<>();
        adapter = new MessagesAdapters(this, messages);
        binding.recylerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recylerView.setAdapter(adapter);


        //String name = getIntent().getStringExtra("name");
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

            notify = true;

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


            // for sorting
            database.getReference()
                    .child("Chat_time")
                    .child(senderRoom)
                    .setValue(date.getTime())
                    .addOnSuccessListener(aVoid -> {

                    });
            database.getReference()
                    .child("Chat_time")
                    .child(receiverRoom)
                    .setValue(date.getTime())
                    .addOnSuccessListener(aVoid -> {

                    });

            // for sorting


            assert randomKey != null;
            database.getReference().child("Chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(randomKey)
                    .setValue(message).addOnSuccessListener(aVoid -> {
                assert senderUid != null;
                database.getReference().child("User_friends")
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

                                        })));
            });


            // this is new

            final String msg = messageTxt;
            database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(notify){
                        sendNotification(receiverUid, user.getName(), msg);

                    }
                    notify = false;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        });

    }

    private void sendNotification(String receiverUid, String name, final String msg) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        //Query query = tokens.orderByKey().equalTo(receiverUid);

        tokens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(receiverUid.equals(dataSnapshot.getKey())){
                        Token token = dataSnapshot.getValue(Token.class);
                        String s = token.getToken();
                        Log.d("token", s);

                        Data data = new Data(auth.getUid(), R.drawable.ic_baseline_attach_email_24,
                                name +": "+ msg, "New Message", receiverUid);
                        Sender sender = new Sender(data, token.getToken());

                        apiService.sendNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                                        if (response.code() == 200){
                                            if(response.body().success != 1){
                                                Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}