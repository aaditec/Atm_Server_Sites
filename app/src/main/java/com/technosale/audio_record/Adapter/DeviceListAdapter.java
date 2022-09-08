package com.technosale.audio_record.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.technosale.audio_record.DashboardActivity;
import com.technosale.audio_record.MainActivity;
import com.technosale.audio_record.Model.DeviceModel;
import com.technosale.audio_record.R;

import java.net.URL;
import java.util.ArrayList;


public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder>{
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
        return new ViewHolder(itemView,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceModel models = deviceModels.get(position);
        holder.deviceName.setText(models.device_name);
        Picasso.get().load("https://realsec.com/wp-content/uploads/2017/06/ATM-sign.png").into(holder.imageView);
        holder.itemView.setOnClickListener(view -> {
            System.out.println("pressed item is " + deviceModels.get(position).device_name);
            Intent intent=new Intent(context,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("device_name",deviceModels.get(position).device_name);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        if(models.is_active){
            holder.deviceName.setTextColor(Color.parseColor("#00FF00"));
        }
        else{
            holder.deviceName.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    @Override
    public int getItemCount() {
        return deviceModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView deviceName;
        public ImageView imageView;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.deviceName = (TextView) itemView.findViewById(R.id.devicename);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
