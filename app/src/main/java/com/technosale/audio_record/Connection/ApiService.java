package com.technosale.audio_record.Connection;

import com.technosale.audio_record.Model.AudioModel;
import com.technosale.audio_record.Constants;
import com.technosale.audio_record.Model.DeviceModel;
import com.technosale.audio_record.Model.DeviceResponseModel;

import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

   @POST(Constants.AUDIO_URL)
    Call<ResponseBody> uploadAudio (@Body AudioModel audiomodel);

    @GET(Constants.DEVICE_LIST)
    Call<DeviceResponseModel> getDevices();

    @GET(Constants.APPROVE)
    Call<DeviceModel> approveDevices(@Query("device_name") String device_name);

   @GET(Constants.TIME_STAMP)
   Call<ArrayList<DeviceModel>> createFile();
       }
