package com.technosale.audio_record.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DeviceResponseModel {

    @SerializedName("devices")
    public ArrayList<DeviceModel> devices;
    @SerializedName("num_pending_records")
    public int pendingRecords;
}
