package com.example.bashapabokoi.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

            //holder.binding.basicTitle.setText(ad.getGas());
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

