package com.example.bashapabokoi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bashapabokoi.Adapters.UsersAdapter;
import com.example.bashapabokoi.Models.User;
import com.example.bashapabokoi.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ChatsFragment extends Fragment {

    FragmentChatsBinding binding;
    FirebaseDatabase database;

    ArrayList<User> users;
    UsersAdapter usersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = FragmentChatsBinding.inflate(inflater, container, false);


        database = FirebaseDatabase.getInstance();
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(getContext(), users);
        binding.recyclerView.setAdapter(usersAdapter);

        String senderUid = FirebaseAuth.getInstance().getUid();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();

        assert senderUid != null;
        database.getReference().child("User_friends").child(senderUid).child("Friends").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                            list.add(Objects.requireNonNull(snapshot2.getValue()).toString());
                            Log.d("frnd", snapshot2.getValue().toString());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        database.getReference().child("Chat_time").orderByValue().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        keys.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            if(Objects.requireNonNull(ds.getKey()).substring(0, 28).equals(senderUid)) {
                                String s = ds.getKey().replaceAll(senderUid, "");
                                keys.add(s);
                                Log.d("aaa", s);
                            }

                        }
                        Collections.reverse(keys);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users.clear();
                        for(String i : keys){
                            Log.d("keys",i);
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                User user = snapshot1.getValue(User.class);
                                /*//for (String i : list) //TODO eida enable korte hobe
                                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid())){ //TODO eida disable
                                    //if (user.getUid().equals(i)) { //TODO eda enable korte hobe
                                        users.add(user);
                                    }*/ //


                                assert user != null;
                                if(user.getUid().equals(i)) {
                                //if(!user.getUid().equals(FirebaseAuth.getInstance().getUid())){
                                    users.add(user);
                                }
                            }

                        }
                        usersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return binding.getRoot();
    }
}
