package com.example.bashapabokoi.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bashapabokoi.Models.CheckBoxValueShower;
import com.example.bashapabokoi.R;

import java.util.List;

public class CheckboxValueAdapter extends RecyclerView.Adapter<CheckboxValueAdapter.ValueHolder>{

    private final List<CheckBoxValueShower> checkBoxValueShowers;

    public CheckboxValueAdapter(List<CheckBoxValueShower> checkBoxValueShowers) {
        this.checkBoxValueShowers = checkBoxValueShowers;
    }

    @NonNull
    @Override
    public ValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ValueHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_values_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ValueHolder holder, int position) {

        holder.setData(checkBoxValueShowers.get(position));
    }

    @Override
    public int getItemCount() {
        return checkBoxValueShowers.size();
    }

    public static class ValueHolder extends RecyclerView.ViewHolder{

        private final ImageView checkBoxImage;
        private final TextView checkBoxName;
        private final TextView checkboxValue;

        public ValueHolder(@NonNull View itemView) {
            super(itemView);

            checkBoxImage = itemView.findViewById(R.id.checkbox_image);
            checkBoxName = itemView.findViewById(R.id.checkbox_name);
            checkboxValue = itemView.findViewById(R.id.checkbox_value);
        }

        @SuppressLint("SetTextI18n")
        void setData(CheckBoxValueShower checkBoxValueShower){

            checkBoxImage.setImageDrawable(checkBoxValueShower.imageId);
            checkBoxName.setText(checkBoxValueShower.checkBoxName);

            if(checkBoxValueShower.value.equals("true")){

                checkboxValue.setText("Yes");

            } else{

                checkboxValue.setText("No");
            }
        }
    }

}