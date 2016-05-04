package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotifyService extends Service {

    private Stop s;

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

        s = Route.getInstance().getStopByID(intent.getIntExtra("ROUTE_STOP_REQUESTED", 0)); //gets by ID rather than by array position. Standardize?


        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent mainIntentPending = PendingIntent.getActivity(getApplicationContext(), 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = //v4 notification builder
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_notify)
                        .setContentTitle("Shuttle Arriving Soon")
                        .setContentText("A shuttle is arriving at " + s.getName() + " in " + s.getTime()  + " minutes"); //shows minutes
                mBuilder.setPriority(1);
                mBuilder.setCategory(Notification.CATEGORY_ALARM);
                mBuilder.setContentIntent(mainIntentPending); //sets intent to open the main app activity on click
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(0, mBuilder.build()); //fires notification off

        vibratePls();

        s.setIsTracking(false);
        return v;
    }

    private void vibratePls() { //ooh baby this is full of inunendo :3
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long vPattern[] = {50, 80,50,80,300,80,50,80}; //the vibrate pattern, da da (wait) da da
        v.vibrate(vPattern, -1);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
