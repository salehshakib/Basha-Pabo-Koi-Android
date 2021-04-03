package com.example.bashapabokoi.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bashapabokoi.AdDescriptionActivity;
import com.example.bashapabokoi.Models.CreateAd;
import com.example.bashapabokoi.Models.OwnerAdShower;
import com.example.bashapabokoi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class OwnerAdAdapter extends RecyclerView.Adapter<OwnerAdAdapter.OwnerAdHolder>{

    private final List<OwnerAdShower> ownerAdShowers;
    private final List<CreateAd> ads;
    private final Context context;

    public OwnerAdAdapter( Context context, List<OwnerAdShower> ownerAdShowers, List<CreateAd> ads) {
        this.context = context;
        this.ownerAdShowers = ownerAdShowers;
        this.ads = ads;
    }

    @NonNull
    @Override
    public OwnerAdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new OwnerAdHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_container_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerAdHolder holder, int position) {

        CreateAd ad = ads.get(position);
        holder.setAdData(ownerAdShowers.get(position), ad.getKey());

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
            intent.putExtra("FROM_ACTIVITY", "ProfileActivity");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ownerAdShowers.size();
    }

    static class OwnerAdHolder extends RecyclerView.ViewHolder{

        private final TextView textRent;
        private final TextView textLocation;
        private final TextView textStarRating;
        private final TextView textFlatType;
        private final TextView textGenre;
        private final ImageView adHolder;


        public OwnerAdHolder(@NonNull View itemView) {
            super(itemView);

            adHolder = itemView.findViewById(R.id.owner_ad_holder);
            textRent = itemView.findViewById(R.id.rent_profile);
            textLocation = itemView.findViewById(R.id.profile_ad_thana);
            textGenre = itemView.findViewById(R.id.genre_profile);
            textFlatType = itemView.findViewById(R.id.flat_type_profile);
            textStarRating = itemView.findViewById(R.id.profile_ad_rating);

        }

        @SuppressLint("SetTextI18n")
        void setAdData(OwnerAdShower ownerAdShower, String key){

            Picasso.get().load(ownerAdShower.imageUri).placeholder(R.drawable.ic_image_default).into(adHolder);
            textRent.setText(ownerAdShower.rent);
            textLocation.setText(ownerAdShower.location);
            textFlatType.setText(ownerAdShower.flatType);
            textGenre.setText(ownerAdShower.genre);

            FirebaseDatabase.getInstance().getReference().child("All_ad").child(key).addValueEventListener(new ValueEventListener() {
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

                        DecimalFormat df2 = new DecimalFormat("#.##");

                        d = d/(2*snapshot.child("Ratings").getChildrenCount());

                        df2.setRoundingMode(RoundingMode.UP);
                        textStarRating.setText(df2.format(d) + "/5");


                        /*holder.binding.ratingBarTextListView.setText(df2.format(d));
                        holder.binding.ratingBarValueListView.setProgress((int) (2*d));*/

                    }
                    else {
                        /*holder.binding.ratingBarValueListView.setProgress(0);
                        holder.binding.ratingBarTextListView.setText("Unrated");*/
                        textStarRating.setText("Unrated");

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
