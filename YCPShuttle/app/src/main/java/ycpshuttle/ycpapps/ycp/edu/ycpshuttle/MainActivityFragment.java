package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Activity mainActivity;
    private TextView t;
    private ProgressBar b;
    private RelativeLayout rel;
    private SwipeRefreshLayout ref;
    private ListView lv;
    private ArrayAdapter<Stop> adapter;


    private GoogleApiClient mGoogleApiClient;

    private int locSampleCount =0; //counts location samples so that when they exceed 10 it quits trying.

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
        rel = (RelativeLayout) v.findViewById(R.id.main_layout);
        lv = (ListView) v.findViewById(R.id.wait_times_list);
        ref = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);

        ref.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(mGoogleApiClient.isConnected()) {
                            beginLocationUpdates();
                        }
                        else {
                            mGoogleApiClient.connect();
                        }
                        getShuttleTimes();
                        setLoading();
                    }
                }
        );

        b.setScaleY(1.5f);
        b.setScaleX(1.5f); //scales loading spinner

//        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
//        for (Stop item : Route.getInstance().getStops()) {
//            Map<String, String> datum = new HashMap<String, String>(2);
//            datum.put("Times", item.toString());
//            datum.put("Distance", "" + item.getDistanceTo());
//            data.add(datum);
//        }


        adapter = new ArrayAdapter<Stop>(getActivity(), R.layout.list_time_text, R.id.list_item_times, Route.getInstance().getStops());
        //adapter = new SimpleAdapter(getActivity(), data, R.layout.list_time_text, new String[] {"Times", "Distance"}, new int[] {R.id.list_item_times, R.id.list_item_sub}); //tried to use simple adapter

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

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
//        String locationProvider = LocationManager.GPS_PROVIDER; //USES GPS for now, network selection for later
//        String locationProdiver = LocationManager.NETWORK_PROVIDER;
//        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(adapter.getContext().LOCATION_SERVICE);

       // locationManager.requestLocationUpdates(locationProvider, 200, 10, this);
        mGoogleApiClient = new GoogleApiClient.Builder(adapter.getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        GregorianCalendar cTime = new GregorianCalendar();
        long elapsedMins = (( (cTime.getTimeInMillis() - Route.getInstance().getLastLoadedTime().getTimeInMillis())/1000)/60);
        Log.v("Elapsed minutes ", "" + elapsedMins);
        if(elapsedMins >= 2) {
            mGoogleApiClient.connect();
            getShuttleTimes();
            setLoading();
        }
    }

    public void beginLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(250);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //ballanced power/accuracy

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mainActivity = activity;
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

//            String locationProvider = LocationManager.GPS_PROVIDER; //USES GPS for now, network selection for later
//            LocationManager locationManager = (LocationManager) mainActivity.getSystemService(adapter.getContext().LOCATION_SERVICE);
//
//            locationManager.requestLocationUpdates(locationProvider, 200, 10, this);
            if(mGoogleApiClient.isConnected()) {
                beginLocationUpdates();
            }
            else {
                mGoogleApiClient.connect();
            }

            setLoading();
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

//            LocationManager locationManager = (LocationManager) mainActivity.getSystemService(adapter.getContext().LOCATION_SERVICE);
//            locationManager.removeUpdates(this);
            if(mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
            }

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
        rel.setBackgroundColor(Color.TRANSPARENT);
        setEnabled(true);
        ref.setRefreshing(false);
        ref.setEnabled(true);

        Route.getInstance().setLastLoadedTime(new GregorianCalendar());
    }

    public void setLoading()
    {
        t.setText("Loading content");
        b.setVisibility(View.VISIBLE);
        rel.setBackgroundColor(Color.argb(204, 83, 83, 83));
        setEnabled(false);
        ref.setEnabled(false);
    }

    public void setEnabled(boolean enable) {
        lv.setEnabled(enable);
    }

    public void popError(Stop s) {
        if(s.getErrorCode() != null) {
            Toast.makeText(adapter.getContext(), "Error: " +  s.getErrorCode().toString() + " on stop " + s.getName(), Toast.LENGTH_SHORT).show();
        }
    }
    public void popLocError(String err) {
        Toast.makeText(adapter.getContext(), "Error: " +  err + " when setting up location services", Toast.LENGTH_SHORT).show();
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
//            LocationManager locationManager = (LocationManager) mainActivity.getSystemService(adapter.getContext().LOCATION_SERVICE);
//            locationManager.removeUpdates(this); //stops GPS polling
            stopLocationUpdates();
            mGoogleApiClient.disconnect(); //no longer using API connection, close the connection.
            Route.getInstance().setCurrentLocation(location);
            sortByDistance(); //Sets Comparator
        }
        else if(locSampleCount > 10) { //stop
//            LocationManager locationManager = (LocationManager) mainActivity.getSystemService(adapter.getContext().LOCATION_SERVICE);
//            locationManager.removeUpdates(this); //stops GPS polling
            stopLocationUpdates();
            mGoogleApiClient.disconnect(); //no longer using API connection, close the connection.
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()) {
            stopLocationUpdates(); //pauses location updates
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("API connection","Connected to API");
        beginLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        popLocError("connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        popLocError("connection failed");
    }
}
