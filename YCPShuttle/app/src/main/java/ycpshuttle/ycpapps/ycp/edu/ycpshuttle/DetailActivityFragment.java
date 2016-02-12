package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Stop s;
    private View v;
    private TextView time1;
    private TextView time2;
    private TextView arriveTime;
    private TextView arriveTime2;
    private Button track1;
    private Button track2;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent i = getActivity().getIntent();
        Log.v("STOP_ID", ""+ i.getIntExtra("ROUTE_STOP_REQUESTED", 0));
        s = Route.getInstance().getStop(i.getIntExtra("ROUTE_STOP_REQUESTED", 0)); //pull out extra from activity intent.

        //header = (TextView) v.findViewById(R.id); //obtain references to TextViews
        time1 = (TextView) v.findViewById(R.id.detail_time_text);
        time2 = (TextView) v.findViewById(R.id.detail_time2_text);
        arriveTime = (TextView) v.findViewById(R.id.detail_arrival_time);
        arriveTime2 = (TextView) v.findViewById(R.id.detail_arrival_time2);
        track1 = (Button) v.findViewById(R.id.detail_track1);
        track2 = (Button) v.findViewById(R.id.detail_track2);
        track1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmNotification(0);
            }
        });

        track2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmNotification(1);
            }
        });

        if(s.getErrorCode() == null) {
            arriveTime.setText(s.getArrivalTime());
            arriveTime2.setText(s.getNextArrivalTime());
            time1.setText("" + s.getTime() + " min");
            time2.setText("" + s.getNextTime() + " min");
            track1.setEnabled(true);
            track2.setEnabled(true);
        }
        else {
            arriveTime.setText(s.getErrorCode().toString());
            arriveTime2.setText(s.getErrorCode().toString());
            time1.setText("Error: ");
            time2.setText("Error: ");
            track1.setEnabled(false);
            track2.setEnabled(false);
        }



        return v;
    }

    private void setAlarmNotification(int track) {
        if(!canSetNotification(track)) { //error out if we cannot set a notification.
            Toast.makeText(v.getContext(), "Error: Cannot track this shuttle ", Toast.LENGTH_SHORT).show();
            return;
        }
        AlarmManager almMgr = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        Intent intent = new Intent(getActivity().getApplicationContext(), NotifyService.class);
        PendingIntent pending = PendingIntent.getService(getActivity().getApplicationContext(), 0, intent, 0);

        GregorianCalendar calendar = new GregorianCalendar();
        if(track == 0) {
            calendar.add(Calendar.MINUTE, (s.getTime() - 2)); //first shuttle. Make the minutes behind a setting?
        }
        else {
            calendar.add(Calendar.MINUTE, (s.getNextTime() - 2)); //next shuttle
        }
        Log.v("API_LEVEL", Build.VERSION.SDK);
        if (Integer.valueOf(Build.VERSION.SDK) > 19) {
            almMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending); //exact timing > 19. Do we need this? I'll have to see. Battery intensive.
        }
        else {
            almMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);//will not be exact >19
        }
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");
        Toast.makeText(v.getContext(), "Alarm Set for " + fmt.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
    }

    public boolean canSetNotification(int track) { //pass in 1/2
        if(s.getErrorCode() == null) {
            if(track == 0 && s.getTime() == 0) {
                return false;
            }
            else if(track >=1  && s.getTime() == 0) {
                return false;
            }
            return true;
        }
        return false;
    }


}
