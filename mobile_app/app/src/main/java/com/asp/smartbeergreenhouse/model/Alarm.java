package com.asp.smartbeergreenhouse.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Alarm class
 *
 * <p>Represents an alarm retrieved from Thingsboard </p>
 *
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class Alarm {

    /**
     * Represents the alarm id, provided by Thingsboard
     */
    private String id;
    /**
     * Represents the alarm type name. Example: "High temperature"
     */
    private String type;
    /**
     * Indicates when the alarm was created
     */
    private String createdTime;
    /**
     * Indicates when the alarm was acknowledged.
     * <p>If alarm was not acknowledged, the default value is: "0"</p>
     */
    private String ackTime; //Timestamp
    /**
     * Indicates the alarm's severity
     * <p>Possible values: CRITICAL, MAJOR, MINOR, WARNING and INDETERMINATE</p>
     */
    private String severity; //CRITICAL, MAJOR, MINOR, WARNING, INDETERMINATE
    /**
     * Indicates the alarm's status
     * <p>Possible values: ACTIVE_UNACK, ACTIVE_ACK, CLEARED_UNACK, CLEARED_ACK</p>
     */
    private String status; //ACTIVE_UNACK, ACTIVE_ACK, CLEARED_UNACK, CLEARED_ACK
    /**
     * Indicates the alarm's originator name (device name from Thingsboard)
     *
     * <p>Example: "GH1_GHR_01_Row_01_pH"</p>
     */
    private String originatorName;  //originator - entity alias

    /**
     * Alarm constructor
     *
     * @param id alarm id
     * @param type alarm type
     * @param originatorName alarm originator name
     * @param createdTime createdTime timestamp
     * @param ackTimestamp  ack timestamp
     * @param severity     alarm's severity
     * @param status    alarm's status
     */
    public Alarm(String id, String type, String originatorName, long createdTime, long ackTimestamp, String severity, String status){
        this.id = id;
        this.type = type;
        this.originatorName = originatorName;
        this.createdTime = getDatefromTimestamp(createdTime);
        this.ackTime = ackTimestamp > 0 ? getDatefromTimestamp(ackTimestamp): "0";
        this.severity = severity;
        this.status = status;
    }

    /**
     * getId
     *
     * @return alarm id
     */
    public String getId() {
        return id;
    }

    /**
     * getType
     * @return alarm's type
     */
    public String getType() {
        return type;
    }

    /**
     * getCreatedTime
     *
     * @return alarm's created time, with format ("dd/mm/yyyy HH:mm:ss")
     */
    public String getCreatedTime() {
        return createdTime;
    }
    /**
     * getAckTime
     *
     * @return alarm's ack time, with format ("dd/mm/yyyy HH:mm:ss"). If not ack, will return "0"
     */
    public String getAckTime() {
        return ackTime;
    }

    /**
     * getOriginatorName
     * @return alarm's originator name
     */
    public String getOriginatorName() {
        return originatorName;
    }

    /**
     * getSeverity
     * @return alarm's severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * getStatus
     * @return alarm's status
     */
    public String getStatus() {
        return status;
    }

    /**
     * getDatefromTimestamp
     * <p>https://stackoverflow.com/questions/36831597/android-convert-int-timestamp-to-human-datetime</p>
     * @param timestamp
     * @return time and date with the following format: "dd/MM/yyyy HH:mm:ss"
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
