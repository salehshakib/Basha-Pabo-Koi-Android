package com.example.bashapabokoi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.bashapabokoi.Adapters.AdsAdapter;
import com.example.bashapabokoi.Models.CreateAd;
import com.example.bashapabokoi.databinding.FragmentWishlistViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class WishListFragment extends Fragment {


    FragmentWishlistViewBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ArrayList<CreateAd> wishListAd = new ArrayList<>();
    ArrayList<String> key = new ArrayList<>();
    AdsAdapter adsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            Objects.requireNonNull(getContext()).setTheme(R.style.Theme_BashaPaboKoi_Dark);
        } else{

            Objects.requireNonNull(getContext()).setTheme(R.style.Theme_BashaPaboKoi);
        }

        super.onCreate(savedInstanceState);

        binding = FragmentWishlistViewBinding.inflate(inflater, container, false);

        adsAdapter = new AdsAdapter(getContext(), wishListAd);
        binding.recyclerView.setAdapter(adsAdapter);

        database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("Wishlist").orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                     key.add(dataSnapshot.getKey());


                }
                Collections.reverse(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        database.getReference().child("All_ad").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String s : key){

                    for (DataSnapshot ds : snapshot.getChildren()){
                        if (s.equals(ds.getKey())){
                            CreateAd ad = ds.getValue(CreateAd.class);
                            wishListAd.add(ad);
                        }
                    }
                }
                adsAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();

    }

}
