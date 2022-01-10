package com.asp.smartbeergreenhouse.utils.alarms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OnItemActivatedListener;

import com.asp.smartbeergreenhouse.model.Alarm;

import java.util.List;

public class AlarmOnItemActivatedListener implements OnItemActivatedListener {

    private static final String TAG = "ListOfItems, MyOnItemActivatedListener";

    private final Context context;
    private final List<Alarm> items;

    public AlarmOnItemActivatedListener(Context context, List<Alarm> listItems) {
        this.context = context;
        this.items = listItems;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails item,
                                   @NonNull MotionEvent e) {

        Log.d(TAG, "Clicked item with position = " + item.getPosition());

        //String uriItem = items.get(item.getPosition()).getURI();
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriItem));
        //Intent i = new Intent(context, SecondActivity.class);
        //i.putExtra("text", "Clicked item with position = " + item.getPosition());
        //context.startActivity(intent);
        return true;
    }
}
