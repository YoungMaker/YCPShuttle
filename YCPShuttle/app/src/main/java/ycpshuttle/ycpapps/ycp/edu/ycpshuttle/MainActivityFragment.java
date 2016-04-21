package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;



import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LocationListener {

    private TextView t;
    private ProgressBar b;
    private ArrayAdapter<Stop> adapter;

    private int locSampleCount =0;

    public MainActivityFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //super.onCreateOptionsMenu(menu, menuInflater);

        //menu.clear();
       // menuInflater.inflate(R.menu.list_frag_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v= inflater.inflate(R.layout.fragment_main, container, false);
        t = (TextView) v.findViewById(R.id.output_text);
        b = (ProgressBar) v.findViewById(R.id.progress_bar);
        b.setScaleY(3f);
        b.setScaleX(3f);
//        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
//        for (Stop item : Route.getInstance().getStops()) {
//            Map<String, String> datum = new HashMap<String, String>(2);
//            datum.put("Times", item.toString());
//            datum.put("Distance", "" + item.getDistanceTo());
//            data.add(datum);
//        }


        adapter = new ArrayAdapter<Stop>(getActivity(), R.layout.list_time_text, R.id.list_item_times, Route.getInstance().getStops());
        //adapter = new SimpleAdapter(getActivity(), data, R.layout.list_time_text, new String[] {"Times", "Distance"}, new int[] {R.id.list_item_times, R.id.list_item_sub});

        ListView list = (ListView) v.findViewById(R.id.wait_times_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(adapter.getContext(), DetailActivity.class);
                detailIntent.putExtra("ROUTE_STOP_REQUESTED", position);
                startActivity(detailIntent);
            }
        });
        String locationProvider = LocationManager.GPS_PROVIDER; //USES GPS for now, network selection for later
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(adapter.getContext().LOCATION_SERVICE);

        locationManager.requestLocationUpdates(locationProvider, 200, 10, this);
        //
        // this.v = v;
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getShuttleTimes();
    }

    private void getShuttleTimes() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("ycpapps.ycp.edu")
                .appendPath("transit")
                .appendPath("gettimes.php")
                .appendQueryParameter("sid", "");
        String URL = builder.build().toString();
        //Log.v("BUILT URL",URL);
        new ShuttleParser(this).execute(URL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            getShuttleTimes();

            String locationProvider = LocationManager.GPS_PROVIDER; //USES GPS for now, network selection for later
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(adapter.getContext().LOCATION_SERVICE);

            locationManager.requestLocationUpdates(locationProvider, 200, 10, this);

            b.setVisibility(View.VISIBLE);
            t.setText("Loading Content");
            return true;
        }
        else if(id == R.id.action_sort_time) {
            adapter.sort(new Comparator<Stop>() {
                @Override
                public int compare(Stop lhs, Stop rhs) {
                    return lhs.compareTo(rhs);
                }
            });
            adapter.notifyDataSetChanged();
        }
        else if(id == R.id.action_sort_default) {
            adapter.sort(new Comparator<Stop>() {
                @Override
                public int compare(Stop lhs, Stop rhs) {
                    return lhs.compareNum(rhs);
                }
            });
            adapter.notifyDataSetChanged();

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(adapter.getContext().LOCATION_SERVICE);
            locationManager.removeUpdates(this);
        }
        else if(id == R.id.action_sort_distance) {
            //TODO: turn on location updates, until accuracy < 50m
            sortByDistance();
        }

        return super.onOptionsItemSelected(item);
    }

//    public void editTextView(String message) {
//        t.setText(message);
//        b.setVisibility(View.GONE);
//    }
    public void setDone() {
        t.setText("Times Loaded");
        b.setVisibility(View.GONE);
    }

    public void popError(Stop s) {
        if(s.getErrorCode() != null) {
            Toast.makeText(adapter.getContext(), "Error: " +  s.getErrorCode().toString() + " on stop " + s.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sortByDistance() {
        adapter.sort(new Comparator<Stop>() {
            @Override
            public int compare(Stop lhs, Stop rhs) {
                return lhs.compareDistnace(rhs);
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void refreshData() {

        adapter.notifyDataSetChanged();


    }


    @Override
    public void onLocationChanged(Location location) {
        locSampleCount++;
        Log.v("GPS ACCURACY", "Accuracy is:  " + location.getAccuracy());
        if(location.getAccuracy() < 50) {
            Log.v("GPS ACCURACY", "Accuracy is <50m:  " + location.getAccuracy());
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(adapter.getContext().LOCATION_SERVICE);
            locationManager.removeUpdates(this); //stops GPS polling
            Route.getInstance().setCurrentLocation(location);
            sortByDistance(); //Sets Comparator
        }
        else if(locSampleCount > 10) { //stop
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(adapter.getContext().LOCATION_SERVICE);
            locationManager.removeUpdates(this); //stops GPS polling
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
