package com.asp.smartbeergreenhouse.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Alarm {
    //private GHRow row;
    private String id;
    private String type; //alarm type name
    private String createdTime; //Timestamp
    private String ackTime; //Timestamp
    private String severity; //CRITICAL, MAJOR, MINOR, WARNING, INDETERMINATE
    private String status; //ACTIVE_UNACK, ACTIVE_ACK, CLEARED_UNACK, CLEARED_ACK
    private String originatorName;  //originator - entity alias

    public Alarm(String id, String type, String originatorName, long createdTime, long ackTimestamp, String severity, String status){
        this.id = id;
        this.type = type;
        this.originatorName = originatorName;
        this.createdTime = getDatefromTimestamp(createdTime);
        this.ackTime = ackTimestamp > 0 ? getDatefromTimestamp(ackTimestamp): "0";
        this.severity = severity;
        this.status = status;
    }
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getAckTime() {
        return ackTime;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public String getSeverity() {
        return severity;
    }

    public String getStatus() {
        return status;
    }

    /**
     *
     * https://stackoverflow.com/questions/36831597/android-convert-int-timestamp-to-human-datetime
     * @param timestamp
     * @return
     */
    private String getDatefromTimestamp(long timestamp){
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());

    }
}
