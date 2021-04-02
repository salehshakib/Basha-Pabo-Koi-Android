package com.example.bashapabokoi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bashapabokoi.Adapters.AdsAdapter;
import com.example.bashapabokoi.Models.CreateAd;
import com.example.bashapabokoi.Models.Ratings;
import com.example.bashapabokoi.Notifications.Data;
import com.example.bashapabokoi.databinding.FragmentListadsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ListViewFragment extends Fragment {

    FragmentListadsBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ArrayList<CreateAd> allAds;
    AdsAdapter adsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentListadsBinding.inflate(inflater, container, false);

        allAds = new ArrayList<>();
        adsAdapter = new AdsAdapter(getContext(), allAds);
        binding.recyclerView.setAdapter(adsAdapter);





        database.getReference().child("All_ad").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){

                    String[] arrOfStr = Objects.requireNonNull(ds.getKey()).split("-");
                    for (String s : arrOfStr){
                        if(!Objects.equals(auth.getUid(), s)){

                            CreateAd showAds = ds.getValue(CreateAd.class);

                            allAds.add(showAds);

                            break;
                        }
                        break;
                    }



                }
                Collections.reverse(allAds);
                adsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });














        return binding.getRoot();
    }
}
