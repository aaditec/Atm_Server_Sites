package com.technosale.audio_record.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technosale.audio_record.Model.LogResponseModel;
import com.technosale.audio_record.R;

import java.util.ArrayList;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ItemViewHolder> {
    Context context;
    private ArrayList<LogResponseModel> mList;

    public LogAdapter(Context context, ArrayList<LogResponseModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public LogAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loglist, parent, false);
        return new LogAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.ItemViewHolder holder, int position) {
        LogResponseModel model = mList.get(position);
        holder.txtDeviceName.setText(model.devices);

        ArrayList<String> logList = new ArrayList<>();
        logList = model.timestamp;
        for (String logTime : logList) {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(context);
            tv.setTextSize(25);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setLayoutParams(lParams);
            tv.setText(logTime);
            holder.blankLinear.addView(tv);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout blankLinear;
        private TextView txtDeviceName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            blankLinear = itemView.findViewById(R.id.blankLinear);
            txtDeviceName = itemView.findViewById(R.id.device_name);
        }
    }
}