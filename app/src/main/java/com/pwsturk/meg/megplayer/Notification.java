package com.pwsturk.meg.megplayer;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notification {

    private Context context;
    private Intent pv,pp,nx;

    public Notification(Context context){
        pv=new Intent();
        pp=new Intent();
        nx=new Intent();
        this.context=context;

    }
    public void bildirimEkle(String songName) {
        pv.setAction("PV_ACTION");
        PendingIntent pendingIntentpv = PendingIntent.getBroadcast(context, 0, pv, PendingIntent.FLAG_UPDATE_CURRENT);
        pp.setAction("PP_ACTION");
        PendingIntent pendingIntentpp = PendingIntent.getBroadcast(context, 0, pp, PendingIntent.FLAG_UPDATE_CURRENT);
        nx.setAction("NX_ACTION");
        PendingIntent pendingIntentnx = PendingIntent.getBroadcast(context, 0, nx, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.megau)
                        .setContentTitle("MEG-AU Player")
                        .addAction(R.drawable.kk, "PV",pendingIntentpv)
                        .addAction(R.drawable.kk, "II", pendingIntentpp)
                        .addAction(R.drawable.kk, "NX", pendingIntentnx)
                        .setContentText(songName.toString().replace(".mp3", ""));

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(388, builder.build());
    }
}
