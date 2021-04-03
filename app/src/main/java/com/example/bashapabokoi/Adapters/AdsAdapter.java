package com.example.bashapabokoi.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

            FirebaseDatabase.getInstance().getReference().child("All_ad").child(ad.getKey()).addValueEventListener(new ValueEventListener() {
                Double d = 0.0;
                String ratings;
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("Ratings").exists()){
                        for (DataSnapshot dataSnapshot1 : snapshot.child("Ratings").getChildren() ){
                            ratings = dataSnapshot1.getValue().toString();
                            d = d + Double.parseDouble(ratings);


                            Log.d("ratings", ratings);




                        }
                        d = d/(2*snapshot.child("Ratings").getChildrenCount());
                        holder.binding.ratingBarTextListView.setText(d.toString());
                        holder.binding.ratingBarValueListView.setProgress((int) (2*d));

                    }
                    else {
                        holder.binding.ratingBarValueListView.setProgress(0);
                        holder.binding.ratingBarTextListView.setText("Unrated");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

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
                intent.putExtra("title", ad.getTitle());
                intent.putExtra("address", ad.getAddress());
                intent.putExtra("flatType", ad.getFlatType());
                intent.putExtra("washroom", ad.getWashroom());
                intent.putExtra("vacantFrom", ad.getVacFrom());
                intent.putExtra("veranda", ad.getVeranda());
                intent.putExtra("bedroom", ad.getBedroom());
                intent.putExtra("floor", ad.getFloor());
                intent.putExtra("religion", ad.getReligion());
                intent.putExtra("genre", ad.getGenre());
                intent.putExtra("electricityBill", ad.getCurrentBill());
                intent.putExtra("waterBill", ad.getWaterBill());
                intent.putExtra("gasBill", ad.getGasBill());
                intent.putExtra("serviceCharge", ad.getOtherCharges());
                intent.putExtra("lift", ad.getLift());
                intent.putExtra("generator", ad.getGenerator());
                intent.putExtra("parking", ad.getParking());
                intent.putExtra("security", ad.getSecurity());
                intent.putExtra("gas", ad.getGas());
                intent.putExtra("wifi", ad.getWifi());
                intent.putExtra("details", ad.getDescription());
                intent.putExtra("rent", ad.getRent());
                intent.putExtra("lat", ad.getLatitude());
                intent.putExtra("long", ad.getLongitude());
                intent.putExtra("ownerKey", ad.getKey());
                intent.putExtra("thana", ad.getThana());
                intent.putExtra("FROM_ACTIVITY", "ListViewFragment");
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
