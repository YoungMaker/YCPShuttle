package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotifyService extends Service {
    public NotifyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         int v = super.onStartCommand(intent, flags, startId);
        Log.v("Notify_Service flagged", "Service ran");
        NotificationCompat.Builder mBuilder = //v4 notification builder
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_notify)
                        .setContentTitle("Shuttle Arriving")
                        .setContentText("A shuttle is arriving in 2 minutes");
                mBuilder.setPriority(0);
                mBuilder.setCategory(Notification.CATEGORY_ALARM);
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(0, mBuilder.build()); //fires notification off
        vibratePls();
        return v;
    }

    private void vibratePls() { //ooh baby this is full of inunendo :3
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long vPattern[] = {50, 80,50,80,300,80,50,80};
        v.vibrate(vPattern, -1);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Notify_Service created", "Created");
    }
}
