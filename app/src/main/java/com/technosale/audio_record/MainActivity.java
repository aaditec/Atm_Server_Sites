package com.technosale.audio_record;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.technosale.audio_record.Adapter.DeviceListAdapter;
import com.technosale.audio_record.Connection.API;
import com.technosale.audio_record.Model.AudioModel;
import com.technosale.audio_record.Model.DeviceModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView startTV, stopTV, statusTV;
    private MediaRecorder mRecorder;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static String mFileName;
    private String _audioBase64;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusTV = findViewById(R.id.idTVstatus);
        startTV = findViewById(R.id.btnRecord);
        stopTV = findViewById(R.id.btnStop);

        // receive bundle from adapter

        // getting bundle from recyclerview adapter
        Bundle bundle = getIntent().getExtras();
            DeviceModel deviceModel = new DeviceModel();
            String title = bundle.getString("device_name", deviceModel.device_name);
            System.out.println("pass gareko bundle ko value" + title);

        File dir = Environment.getExternalStorageDirectory();
        File target = new File(dir, "capture");
        if (!target.exists())
            target.mkdirs();

        startTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermissions()) {
                    mFileName = target.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3";
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile(mFileName);
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.i("TAG", "prepare() failed");
                    }

                    mRecorder.start();

                    statusTV.setText("Recording Started");
                } else {
                    RequestPermissions();
                }
            }
        });
        stopTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                statusTV.setText("Sending audio file...");
                Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
                encodeAudio(target,title);
            }
        });
    }

    private void sendFilesToServer(File target, String base, String title) {
        File[] files = target.listFiles();
        if (files.length > 0) {
            File uploadFile = new File(target, files[0].getName());
            Uri uri = Uri.fromFile(uploadFile);
            uploadFile = new File(uri.getPath());
            File finalUploadFile = uploadFile;
            AudioModel audioModel = new AudioModel();
            audioModel.device_name = title;
            audioModel.audio_base64 = null;
            audioModel.is_sent = false;
            audioModel.audio_base64_text = base;

            Call<ResponseBody> call = API.getClient().uploadAudio(audioModel);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println("audio value" + audioModel.audio_base64_text);
                    System.out.println("response code is" + response.code());
                    System.out.println("response is " + response.body());
                    if (response.code() == 201) {
                        System.out.println("audio file sent to the" + title);
                        finalUploadFile.delete();
                        statusTV.setText("Audio sent Successfully");
                    }
                    else if (response.code() == 400) {
                        statusTV.setText("byte limit crossed");
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
                        statusTV.setText("Could not send the audio");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("message", "onFailure: " + t);
                }
            });
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }


    private void encodeAudio(File target,String title) {
        byte[] audioBytes;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(mFileName));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();

            // Here goes the Base64 string
            _audioBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);
            sendFilesToServer(target,
                    _audioBase64,title);
        } catch (Exception e) {

        }
    }
}
