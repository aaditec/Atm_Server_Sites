package com.technosale.audio_record.Connection;

import com.technosale.audio_record.Constants;
import com.technosale.audio_record.Model.AudioModel;
import com.technosale.audio_record.Model.DeviceModel;
import com.technosale.audio_record.Model.DeviceResponseModel;
import com.technosale.audio_record.Model.LogResponseModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST(Constants.AUDIO_URL)
    Call<ResponseBody> uploadAudio(@Body AudioModel audiomodel);

    @GET(Constants.DEVICE_LIST)
    Call<DeviceResponseModel> getAllDevices();

    @GET(Constants.APPROVE_DEVICE)
    Call<DeviceModel> approveDevices(@Query("device_name") String device_name);

    @GET(Constants.TIME_STAMP)
    Call<ArrayList<LogResponseModel>> getLog();
}
