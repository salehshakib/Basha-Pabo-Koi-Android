package com.example.bashapabokoi.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bashapabokoi.Models.CreateAd;
import com.example.bashapabokoi.R;
import com.example.bashapabokoi.databinding.RowAdsBinding;

import java.util.ArrayList;


public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.AdsViewHolder>{

        Context context;
        ArrayList<CreateAd> ads;

        public AdsAdapter(Context context, ArrayList<CreateAd> ads) {
            this.context = context;
            this.ads = ads;
        }

        @NonNull
        @Override
        public AdsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new AdsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_ads, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdsViewHolder holder, int position) {
            CreateAd ad = ads.get(position);

            holder.binding.adTitleListView.setText(ad.getTitle());
            holder.binding.rentListView.setText(ad.getRent());
            holder.binding.thanaListViewText.setText(ad.getThana());
            holder.binding.genreListViewText.setText(ad.getGenre());
            holder.binding.flatTypeListViewText.setText(ad.getFlatType());
            holder.binding.religionListViewText.setText(ad.getReligion());
            holder.binding.vacantFromListViewText.setText(ad.getVacFrom());

            Glide.with(context).load(ad.getImageUrl1()).placeholder(R.drawable.user).into(holder.binding.listViewImage);

            //TODO Fetch data here
        }

        @Override
        public int getItemCount() {
            return ads.size();
        }


        public static class AdsViewHolder extends RecyclerView.ViewHolder{
            RowAdsBinding binding;

            public AdsViewHolder(@NonNull View itemView) {
                super(itemView);

                binding = RowAdsBinding.bind(itemView);
            }
        }
}

