package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private TextView t;
    private ArrayList<String> data;
    private ArrayAdapter<String> adapter;

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

        initializeAdapterData();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_time_text, R.id.list_item_times, data);

        ListView list = (ListView) v.findViewById(R.id.wait_times_list);
        list.setAdapter(adapter);

        new ShuttleParser(this).execute("");
        return v;
    }

    private void initializeAdapterData() {
        data = new ArrayList<String>();

        if(!Route.isInitalized()) {
            Route.initalizeRoute();
        }
        ArrayList<Stop> stops = Route.getInstance().getStops();
        for (Stop s : stops) {
            data.add(s.toString()); //TEMPORARY, fix
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("ycpapps.ycp.edu")
                    .appendPath("transit")
                    .appendPath("gettimes");
            String URL = builder.build().toString();
             new ShuttleParser(this).execute(URL);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void editTextView(String message) {
        t.setText(message);
    }


    public void updateAdapter() {
        if(!Route.isInitalized()) {
            Route.initalizeRoute();
        }
        ArrayList<Stop> stops = Route.getInstance().getStops();
        for (int i=0; i<stops.size(); i++) {
            data.set(i, stops.get(i).toString()); //copies data back in from model when updated
        }
        adapter.notifyDataSetChanged();
    }
}
