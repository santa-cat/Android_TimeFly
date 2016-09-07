package com.example.santa.lookupapp.mainactivity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by santa on 16/8/29.
 */
public class SwitchOptions {
    private static boolean mSwitchState = false;
    private final static String USERDATA = "userDatas";
    private final static String STATE = "state";


    public static void saveSiwtchState(Context context, boolean state) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USERDATA, Context.MODE_PRIVATE).edit();
        editor.putBoolean(STATE, state);
        editor.apply();
    }

    public static boolean readSwitchState(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        return sp.getBoolean(STATE, false);
    }

}
