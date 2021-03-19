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
            View view = LayoutInflater.from(context).inflate(R.layout.row_ads, parent, false);
            return new AdsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdsViewHolder holder, int position) {
            CreateAd ad = ads.get(position);

            holder.binding.basicTitle.setText(ad.getGas());

        }

        @Override
        public int getItemCount() {
            return ads.size();
        }




        public class AdsViewHolder extends RecyclerView.ViewHolder{
            RowAdsBinding binding;

            public AdsViewHolder(@NonNull View itemView) {
                super(itemView);

                binding = RowAdsBinding.bind(itemView);
            }
        }
}

