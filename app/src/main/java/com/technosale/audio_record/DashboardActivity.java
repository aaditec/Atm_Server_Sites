package com.technosale.audio_record;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technosale.audio_record.Adapter.DeviceListAdapter;
import com.technosale.audio_record.Adapter.LogAdapter;
import com.technosale.audio_record.Connection.API;
import com.technosale.audio_record.Model.DeviceModel;
import com.technosale.audio_record.Model.DeviceResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardActivity extends AppCompatActivity {
    TextView txtCreateLog;
    RecyclerView recyclerView;
    DeviceListAdapter deviceListAdapter;
    ArrayList<DeviceModel> pendingList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        TextView tvPending = (TextView) findViewById(R.id.tvPending);
        TextView txtCreateLog = (TextView) findViewById(R.id.txtCreateLog);
        recyclerView = findViewById(R.id.recyclerview);
        txtCreateLog.setText("Create Log");

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        getDeviceList();

        tvPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ApprovalActivity.class);
                intent.putExtra("pendingList", pendingList);
                startActivity(intent);
            }
        });
        txtCreateLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, LogFile.class);
                startActivity(i);
            }
        });
    }

    private void getDeviceList()  {
        Call<DeviceResponseModel> call = API.getClient().getAllDevices();
        call.enqueue(new Callback<DeviceResponseModel>() {
            @Override
            public void onResponse(Call<DeviceResponseModel> call, Response<DeviceResponseModel> response) {
                if (response.isSuccessful()) {
                    DeviceResponseModel respModel = response.body();
                    ArrayList<DeviceModel> responseDeviceList = respModel.devices;
                    txtCreateLog.setText("Log Remaining: " + String.valueOf(respModel.pendingRecords));
                    ArrayList<DeviceModel> approvedList = new ArrayList<>();
                    if (!responseDeviceList.isEmpty()){
                        for (DeviceModel deviceModel : responseDeviceList){
                            if (deviceModel.is_approved) {
                                approvedList.add(deviceModel);
                            } else {
                                pendingList.add(deviceModel);
                            }
                        }
                        deviceListAdapter = new DeviceListAdapter(getApplicationContext(), approvedList);
                        recyclerView.setAdapter(deviceListAdapter);

                    } else {
                        //no device found
                    }

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
            public void onFailure(Call<DeviceResponseModel> call, Throwable t) {
            }
        });
    }

}