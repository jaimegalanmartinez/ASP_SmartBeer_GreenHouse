package com.asp.smartbeergreenhouse.utils.alarms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.asp.smartbeergreenhouse.R;
import com.asp.smartbeergreenhouse.model.Alarm;

/**
 * AlarmViewHolder class
 * <p>Viewholder specific for alarms recycler view</p>
 * @see RecyclerView.ViewHolder
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class AlarmViewHolder extends RecyclerView.ViewHolder {

    // Holds references to individual item views
    Context context;
    TextView alarmName;
    TextView originatorName;
    TextView createdTime;
    TextView severity;
    TextView alarmStatus;
    TextView ackTime;
    ImageView image;

    private static final String TAG = "ListOfItems, MyViewHolder";

    public AlarmViewHolder(Context ctxt, View itemView) {
        super(itemView);
        context = ctxt;
        alarmName = itemView.findViewById(R.id.cv_item_alarm_name);
        originatorName = itemView.findViewById(R.id.cv_item_alarm_originator);
        createdTime = itemView.findViewById(R.id.cv_item_alarm_createdTime);
        alarmStatus = itemView.findViewById(R.id.cv_item_alarm_status);
        severity = itemView.findViewById(R.id.cv_item_alarm_severity);
        ackTime = itemView.findViewById(R.id.cv_item_alarm_ack_time);
        image = itemView.findViewById(R.id.img_alarm_icon);
    }

    void bindValues(Alarm item, Boolean isSelected) {
        // give values to the elements contained in the item view
        alarmName.setText("Alarm "+item.getType());
        originatorName.setText(item.getOriginatorName());
        createdTime.setText("Created at: " + item.getCreatedTime());

        if (item.getSeverity().equals("CRITICAL")){
            severity.setText("Severity: ");
            Spannable severityAlarm = new SpannableString(item.getSeverity());
            severityAlarm.setSpan(new ForegroundColorSpan(Color.RED), 0, severityAlarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            severity.append(severityAlarm);

        }else if (item.getSeverity().equals("MAJOR")){
            severity.setText("Severity: ");
            Spannable severityAlarm = new SpannableString(item.getSeverity());
            severityAlarm.setSpan(new ForegroundColorSpan(Color.argb(255,255,165,0)), 0, severityAlarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            severity.append(severityAlarm);

        }else if (item.getSeverity().equals("MINOR")){
            severity.setText("Severity: ");
            Spannable severityAlarm = new SpannableString(item.getSeverity());
            severityAlarm.setSpan(new ForegroundColorSpan(Color.BLUE), 0, severityAlarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            severity.append(severityAlarm);

        }else if (item.getSeverity().equals("WARNING")){
            severity.setText("Severity: ");
            Spannable severityAlarm = new SpannableString(item.getSeverity());
            severityAlarm.setSpan(new ForegroundColorSpan(Color.RED), 0, severityAlarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            severity.append(severityAlarm);

        }else if (item.getSeverity().equals("INDETERMINATE")){
            severity.setText("Severity: ");
            Spannable severityAlarm = new SpannableString(item.getSeverity());
            severityAlarm.setSpan(new ForegroundColorSpan(Color.GRAY), 0, severityAlarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            severity.append(severityAlarm);
        }

        if (item.getStatus().equals("ACTIVE_UNACK")){
            alarmStatus.setText("Status: Active Unacknowledged");
        }else if (item.getStatus().equals("ACTIVE_ACK")){
            alarmStatus.setText("Status: Active Acknowledged");
        }

        if(item.getAckTime().equals("0")){
            ackTime.setText("ACK Time: Not defined");
        }else{
            ackTime.setText("ACK Time: "+ item.getAckTime());
        }

        image.setImageResource(R.drawable.alarm_green_ic);


        if(isSelected) {
            //alarmName.setTextColor(Color.BLUE);
        } else {
            //alarmName.setTextColor(Color.BLACK);
        }
    }

    @SuppressLint("LongLogTag")
    @Nullable
    public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {

        Log.d(TAG, "getItemDetails() called");

        ItemDetailsLookup.ItemDetails<Long> itemdet = new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                Log.d(TAG, "ItemDetailsLookup.ItemDetails<Long>.getPosition() called");
                return getAdapterPosition();
            }

            @Nullable
            @Override
            public Long getSelectionKey() {
                Log.d(TAG, "ItemDetailsLookup.ItemDetails<Long>.getSelectionKey() called");
                return (long) (getAdapterPosition());
            }

        };

        return itemdet;
    }
}