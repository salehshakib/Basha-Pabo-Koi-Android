package com.example.bashapabokoi.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bashapabokoi.Models.BottomSheetImageShower;
import com.example.bashapabokoi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BottomSheetImageAdapter extends RecyclerView.Adapter<BottomSheetImageAdapter.BottomSheetImageHolder>{

    private final List<BottomSheetImageShower> bottomSheetImageShowers;

    public BottomSheetImageAdapter(List<BottomSheetImageShower> bottomSheetImageShowers) {
        this.bottomSheetImageShowers = bottomSheetImageShowers;
    }

    @NonNull
    @Override
    public BottomSheetImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BottomSheetImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_image_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetImageHolder holder, int position) {

        holder.setData(bottomSheetImageShowers.get(position));
    }

    @Override
    public int getItemCount() {
        return bottomSheetImageShowers.size();
    }

    public static class BottomSheetImageHolder extends RecyclerView.ViewHolder {

        ImageView imageHolder;

        public BottomSheetImageHolder(@NonNull View itemView) {
            super(itemView);

            imageHolder = itemView.findViewById(R.id.bottom_sheet_image_holder);
        }

        void setData(BottomSheetImageShower bottomSheetImageShower){

            Picasso.get().load(bottomSheetImageShower.imageUri).placeholder(R.drawable.ic_image_default).into(imageHolder);
        }
    }
}
