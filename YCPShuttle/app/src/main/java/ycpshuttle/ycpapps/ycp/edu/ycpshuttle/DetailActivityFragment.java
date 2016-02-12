package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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


        arriveTime.setText(s.getArrivalTime());
        arriveTime2.setText(s.getNextArrivalTime());
        time1.setText("" + s.getTime() + " min");
        time2.setText("" + s.getNextTime() + " min");

        return v;
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
