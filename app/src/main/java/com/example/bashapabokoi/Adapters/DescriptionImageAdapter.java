package com.example.bashapabokoi.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bashapabokoi.Models.DescriptionImageShower;
import com.example.bashapabokoi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DescriptionImageAdapter extends  RecyclerView.Adapter<DescriptionImageAdapter.DescriptionImageHolder>{

    private final List<DescriptionImageShower> descriptionImageShowers;

    public DescriptionImageAdapter(List<DescriptionImageShower> descriptionImageShowers) {
        this.descriptionImageShowers = descriptionImageShowers;
    }

    @NonNull
    @Override
    public DescriptionImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DescriptionImageAdapter.DescriptionImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.description_image_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionImageHolder holder, int position) {

        holder.setData(descriptionImageShowers.get(position));
    }

    @Override
    public int getItemCount() {
        return descriptionImageShowers.size();
    }

    static class DescriptionImageHolder extends RecyclerView.ViewHolder{

        ImageView imageHolder;

        public DescriptionImageHolder(@NonNull View itemView) {
            super(itemView);

            imageHolder = itemView.findViewById(R.id.description_image_holder);
        }

        void setData(DescriptionImageShower descriptionImageShower){

            Picasso.get().load(descriptionImageShower.imageUri).placeholder(R.drawable.ic_image_default).into(imageHolder);
        }
    }
}
