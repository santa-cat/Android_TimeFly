package com.example.santa.lookupapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.santa.lookupapp.service.TimeService;

/**
 * Created by santa on 16/9/6.
 */
public class UserPresentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DEBUG", "onReceive success");
        Intent startIntent = new Intent(context, TimeService.class);
        context.startService(startIntent);
        Toast.makeText(context, "receiver", Toast.LENGTH_SHORT).show();
    }
}
