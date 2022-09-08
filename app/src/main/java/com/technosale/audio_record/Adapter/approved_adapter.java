package com.technosale.audio_record.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<DeviceModel> devicelist;
    private Context context;

    // RecyclerView recyclerView;
    public approved_adapter(ArrayList<DeviceModel> ListData, Context context) {
        this.devicelist = ListData;
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
        final DeviceModel myListData = devicelist.get(position);
        holder.device_name.setText(myListData.device_name);
        Picasso.get().load("https://realsec.com/wp-content/uploads/2017/06/ATM-sign.png").into(holder.imageView);
        holder.itemView.setOnClickListener(view -> {
            System.out.println("pressed item is " + devicelist.get(position).device_name);
        });
        holder.approveBtn.setOnClickListener(new View.OnClickListener() {
            String deviceName = holder.device_name.getText().toString().trim();
            @Override
            public void onClick(View view) {
                Call<DeviceModel> call = API.getClient().approveDevices(deviceName);
                call.enqueue(new Callback<DeviceModel>() {
                    @Override
                    public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                        System.out.println("response code is" + response.code());
                        System.out.println("response is " + response.body());
                        if (response.code() ==200) {
                            System.out.println("approved successfully");
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
                        Log.d("message", "onFailure: " + t);
                    }
                });
                /*Approve api call here*/

            }
        });
    }


    @Override
    public int getItemCount() {
        return devicelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView device_name;
        public TextView approveBtn;
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.device_name = (TextView) itemView.findViewById(R.id.device_name);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.approveBtn = (TextView) itemView.findViewById(R.id.approveBtn);
        }
    }
}