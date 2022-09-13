package com.technosale.audio_record;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Call;
import android.widget.Toast;

import com.technosale.audio_record.Adapter.approved_adapter;

import com.technosale.audio_record.Model.DeviceModel;

import java.util.ArrayList;

public class ApprovalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        Context context = this;
        RecyclerView rvMain = (RecyclerView) findViewById(R.id.rvmain);

        ArrayList<DeviceModel> pendingList = (ArrayList<DeviceModel>) getIntent().getSerializableExtra("pendingList");
        approved_adapter adapter = new approved_adapter(pendingList, context);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setAdapter(adapter);
        rvMain.refreshDrawableState();
    }
}