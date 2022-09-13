package com.technosale.audio_record.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class LogResponseModel {
    @SerializedName("device_name")
    public String devices;
    public ArrayList<String> timestamp;
}