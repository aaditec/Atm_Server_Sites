package com.technosale.audio_record.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.technosale.audio_record.MainActivity;
import com.technosale.audio_record.Model.DeviceModel;
import com.technosale.audio_record.R;

import java.util.ArrayList;


public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    Context context;
    private ArrayList<DeviceModel> deviceModels;

    public DeviceListAdapter(Context context, ArrayList<DeviceModel> deviceModels) {
        this.context = context;
        this.deviceModels = deviceModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list, parent, false);
        return new ViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceModel models = deviceModels.get(position);
        holder.txtDeviceName.setText(models.device_name);
        Picasso.get().load("https://realsec.com/wp-content/uploads/2017/06/ATM-sign.png").into(holder.ivImage);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("device_name", deviceModels.get(position).device_name);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        if (models.is_active) {
            holder.txtDeviceName.setTextColor(Color.parseColor("#00FF00"));
        } else {
            holder.txtDeviceName.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    @Override
    public int getItemCount() {
        return deviceModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDeviceName;
        public ImageView ivImage;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.txtDeviceName = (TextView) itemView.findViewById(R.id.devicename);
            this.ivImage = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
