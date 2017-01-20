package com.pwsturk.meg.megplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if("PV_ACTION".equals(action)) {
            //Toast.makeText(context, "pressed pv", Toast.LENGTH_SHORT).show();
            Player.prev();
        } else if("PP_ACTION".equals(action)) {
            //Toast.makeText(context, "pressed pp", Toast.LENGTH_SHORT).show();
            Player.playorstop();
        } else if("NX_ACTION".equals(action)) {
            //Toast.makeText(context, "pressed nx", Toast.LENGTH_SHORT).show();
            Player.next();
        }
    }
}
