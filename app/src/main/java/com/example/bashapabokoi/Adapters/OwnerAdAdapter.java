package com.example.bashapabokoi.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bashapabokoi.Models.OwnerAdShower;
import com.example.bashapabokoi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OwnerAdAdapter extends RecyclerView.Adapter<OwnerAdAdapter.OwnerAdHolder>{

    private final List<OwnerAdShower> ownerAdShowers;

    public OwnerAdAdapter(List<OwnerAdShower> ownerAdShowers) {
        this.ownerAdShowers = ownerAdShowers;
    }

    @NonNull
    @Override
    public OwnerAdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new OwnerAdHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_container_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerAdHolder holder, int position) {
        holder.setAdData(ownerAdShowers.get(position));
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
        void setAdData(OwnerAdShower ownerAdShower){

            Picasso.get().load(ownerAdShower.imageUri).placeholder(R.drawable.ic_image_default).into(adHolder);
            textRent.setText(ownerAdShower.rent);
            textLocation.setText(ownerAdShower.location);
            textFlatType.setText(ownerAdShower.flatType);
            textGenre.setText(ownerAdShower.genre);
            textStarRating.setText(ownerAdShower.starRating + "/5");
        }
    }
}
