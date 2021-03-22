package com.example.bashapabokoi.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bashapabokoi.AdDescriptionActivity;
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

            try{

                String[] date = ad.getVacFrom().split("-");
                String month;

                switch (date[1]) {
                    case "01":

                        month = "January";

                        break;
                    case "02":

                        month = "February";

                        break;
                    case "03":

                        month = "March";

                        break;
                    case "04":

                        month = "April";

                        break;
                    case "05":

                        month = "May";

                        break;
                    case "06":

                        month = "June";

                        break;
                    case "07":

                        month = "July";

                        break;
                    case "08":

                        month = "August";

                        break;
                    case "09":

                        month = "September";

                        break;
                    case "10":

                        month = "October";

                        break;
                    case "11":

                        month = "November";

                        break;
                    case "12":

                        month = "December";

                        break;
                    default:

                        month = ad.getVacFrom();
                }

                holder.binding.vacantFromListViewText.setText(month);

            } catch (ArrayIndexOutOfBoundsException e){

                holder.binding.vacantFromListViewText.setText(ad.getVacFrom());
            }

            Glide.with(context).load(ad.getImageUrl1()).placeholder(R.drawable.ic_image_default).into(holder.binding.listViewImage);

            holder.itemView.setOnClickListener(v -> {

                Intent intent = new Intent(context, AdDescriptionActivity.class);
                intent.putExtra("imageUri1", ad.getImageUrl1());
                intent.putExtra("imageUri2", ad.getImageUrl2());
                intent.putExtra("imageUri3", ad.getImageUrl3());
                intent.putExtra("imageUri4", ad.getImageUrl4());
                intent.putExtra("imageUri5", ad.getImageUrl5());
                context.startActivity(intent);
            });
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
