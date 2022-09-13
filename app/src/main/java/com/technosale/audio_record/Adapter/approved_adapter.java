package com.technosale.audio_record.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.telecom.Call.Details;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.technosale.audio_record.ApprovalActivity;
import com.technosale.audio_record.Connection.API;
import com.technosale.audio_record.DashboardActivity;
import com.technosale.audio_record.Model.DeviceModel;
import com.technosale.audio_record.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class approved_adapter extends RecyclerView.Adapter<approved_adapter.ViewHolder>{
    private List<DeviceModel> deviceList;
    private Context context;

    public approved_adapter(ArrayList<DeviceModel> ListData, Context context) {
        this.deviceList = ListData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.approval_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DeviceModel myListData = deviceList.get(position);
        holder.txtDevice_name.setText(myListData.device_name);
        Picasso.get().load("https://realsec.com/wp-content/uploads/2017/06/ATM-sign.png").into(holder.ivImage);
        holder.itemView.setOnClickListener(view -> {
        });
        holder.BtnApprove.setOnClickListener(new View.OnClickListener() {
            String deviceName = holder.txtDevice_name.getText().toString().trim();
            @Override
            public void onClick(View view) {
                Call<DeviceModel> call = API.getClient().approveDevices(deviceName);
                call.enqueue(new Callback<DeviceModel>() {
                    @Override
                    public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                        if (response.code() ==200) {
                            Intent intent = new Intent(context, DashboardActivity.class);
                            context.startActivity(intent);
                        } else if (response.code() == 403) {
                            JSONObject error1 = null;
                            try {
                                error1 = new JSONObject(response.errorBody().string());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (error1.has("message")) {
                                try {
                                    String resultDescription = error1.getString("message");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                JSONObject error = new JSONObject(response.errorBody().string());
                                String resultDescription = "";
                                if (error.has("message")) {
                                    resultDescription = error.getString("message");
                                } else {
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DeviceModel> call, Throwable t) {
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDevice_name;
        public Button BtnApprove;
        public ImageView ivImage;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtDevice_name = (TextView) itemView.findViewById(R.id.device_name);
            this.ivImage = (ImageView) itemView.findViewById(R.id.imageView);
            this.BtnApprove = (Button) itemView.findViewById(R.id.BtnApprove);
        }
    }
}