package com.example.bashapabokoi.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bashapabokoi.ChatActivity;
import com.example.bashapabokoi.Models.User;
import com.example.bashapabokoi.R;
import com.example.bashapabokoi.databinding.RowConversationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder>{

    Context context;
    ArrayList<User> users;

    public UsersAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.users = users;

    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation, parent, false);

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);

        String senderId = FirebaseAuth.getInstance().getUid();

        String senderRoom = senderId + user.getUid();

        FirebaseDatabase.getInstance().getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            long time = snapshot.child("lastMsgTime").getValue(Long.class);

                            assert lastMsg != null;
                            if(lastMsg.length() > 40){

                                lastMsg = lastMsg.substring(0, 39) + "...";
                            }

                            Calendar msgTime = Calendar.getInstance();
                            Calendar now = Calendar.getInstance();

                            msgTime.setTimeInMillis(time);

                            final String timeFormatString = "h:mm aa";
                            final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";

                            if (now.get(Calendar.DATE) == msgTime.get(Calendar.DATE) ) {

                                holder.binding.msgTime.setText("Today, " + DateFormat.format(timeFormatString, msgTime));
                            } else if (now.get(Calendar.DATE) - msgTime.get(Calendar.DATE) == 1  ){

                                holder.binding.msgTime.setText("Yesterday, " + DateFormat.format(timeFormatString, msgTime));
                            } else if (now.get(Calendar.YEAR) == msgTime.get(Calendar.YEAR)) {

                                holder.binding.msgTime.setText(DateFormat.format(dateTimeFormatString, msgTime).toString());
                            } else {

                                holder.binding.msgTime.setText( DateFormat.format("MMMM dd yyyy, h:mm aa", msgTime).toString());
                            }

                            holder.binding.lastMsg.setText(lastMsg);
                        } else {
                            holder.binding.lastMsg.setText("Tap to chat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
        });

        holder.binding.userName.setText(user.getName());
        Glide.with(context).load(user.getProfileImage()).placeholder(R.drawable.user).into(holder.binding.profile);

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("name", user.getName());
            intent.putExtra("uid", user.getUid());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        RowConversationBinding binding;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RowConversationBinding.bind(itemView);
        }
    }
}
