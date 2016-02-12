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

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);

        Intent i = getActivity().getIntent();
        Log.v("STOP_ID", ""+ i.getIntExtra("ROUTE_STOP_REQUESTED", 0));
        s = Route.getInstance().getStop(i.getIntExtra("ROUTE_STOP_REQUESTED", 0)); //pull out extra from activity intent.

        //header = (TextView) v.findViewById(R.id); //obtain references to TextViews
        time1 = (TextView) v.findViewById(R.id.detail_time_text);
        time2 = (TextView) v.findViewById(R.id.detail_time2_text);
        arriveTime = (TextView) v.findViewById(R.id.detail_arrival_time);
        arriveTime2 = (TextView) v.findViewById(R.id.detail_arrival_time2);
        track1 = (Button) v.findViewById(R.id.detail_track1);
        track1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmNotification();

            }
        });
        if(s.getErrorCode() == null) {
            arriveTime.setText(s.getArrivalTime());
            arriveTime2.setText(s.getNextArrivalTime());
            time1.setText("" + s.getTime() + " min");
            time2.setText("" + s.getNextTime() + " min");
        }
        else {
            arriveTime.setText(s.getErrorCode().toString());
            arriveTime2.setText(s.getErrorCode().toString());
            time1.setText("Error: ");
            time2.setText("Error: ");
        }



        return v;
    }

    private void setAlarmNotification() {
        AlarmManager almMgr = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        Intent intent = new Intent(getActivity().getApplicationContext(), NotifyService.class);
        PendingIntent pending = PendingIntent.getService(getActivity().getApplicationContext(), 0, intent, 0);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, 1);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            //getShuttleTimes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
