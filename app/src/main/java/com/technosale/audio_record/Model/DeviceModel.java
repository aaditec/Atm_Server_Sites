package com.technosale.audio_record.Model;
import java.io.Serializable;
import java.util.ArrayList;

public class DeviceModel implements Serializable {
    public Integer id;
    public String device_name;
    public Boolean is_approved;
    public Boolean is_active;
    public String last_req_time;

}
