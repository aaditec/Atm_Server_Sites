package com.technosale.audio_record;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.technosale.audio_record.Adapter.LogAdapter;
import com.technosale.audio_record.Connection.API;
import com.technosale.audio_record.Model.LogResponseModel;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogFile extends AppCompatActivity {
    private RecyclerView rvMain;
    private ArrayList<LogResponseModel> responseList = new ArrayList<>();
    private LogAdapter logAdapter;
    String h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_file);
        rvMain = findViewById(R.id.rvmain);
        rvMain.setHasFixedSize(true);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        getLogList();
    }

    private void getLogList() {
        Call<ArrayList<LogResponseModel>> call = API.getClient().getLog();
        call.enqueue(new Callback<ArrayList<LogResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LogResponseModel>> call, Response<ArrayList<LogResponseModel>> response) {
                if (response.isSuccessful()) {
                    responseList = response.body();
                    logAdapter = new LogAdapter(getApplicationContext(), responseList);
                    rvMain.setAdapter(logAdapter);
                    generateTxtFiles();
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
            public void onFailure(Call<ArrayList<LogResponseModel>> call, Throwable t) {
            }
        });
    }

    private void generateTxtFiles() {
        try {
            h = DateFormat.format("MM-dd-yyyy-h-mm-ss-aa", System.currentTimeMillis()).toString();
            File root = new File(Environment.getExternalStorageDirectory(), "LogFiles");
            if (!root.exists()) {
                root.mkdirs();
            }
            File filepath = new File(root, h + ".txt");
            FileWriter writer = new FileWriter(filepath);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonArray = gson.toJson(responseList);
            String modifiedString = jsonArray.replace("[","").replace("{","").
                    replace("}","").replace("]","").replace(",","");
            writer.append(modifiedString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}