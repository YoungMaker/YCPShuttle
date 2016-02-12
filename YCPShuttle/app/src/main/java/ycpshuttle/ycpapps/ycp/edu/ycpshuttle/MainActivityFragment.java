package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.content.Intent;
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


import java.net.URI;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private TextView t;
    private ArrayAdapter<Stop> adapter;

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

        adapter = new ArrayAdapter<Stop>(getActivity(), R.layout.list_time_text, R.id.list_item_times, Route.getInstance().getStops());

        ListView list = (ListView) v.findViewById(R.id.wait_times_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent= new Intent(adapter.getContext(), DetailActivity.class  );
                detailIntent.putExtra("ROUTE_STOP_REQUESTED", position);
                startActivity(detailIntent);
            }
        } );


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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void editTextView(String message) {
        t.setText(message);
    }

    public void popError(Stop s) {
        if(s.getErrorCode() != null) {
            Toast.makeText(adapter.getContext(), "Error: " +  s.getErrorCode().toString() + " on stop " + s.getName(), Toast.LENGTH_SHORT).show();
        }
    }


    public void refreshData() {
        adapter.notifyDataSetChanged();
    }



}
