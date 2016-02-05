package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import android.support.*;

/**
 * Created by Aaron on 2/4/2016.
 */
public class ShuttleParser extends AsyncTask<String, Void, ArrayList<Stop>> {
        MainActivityFragment fragInstance;

        public ShuttleParser(MainActivityFragment frag ) {
            this.fragInstance = frag;
        }

        @Override
        protected ArrayList<Stop> doInBackground(String... uris) {
            ArrayList output = new ArrayList<Stop>();
            if(!Route.isInitalized()) {
                Route.initalizeRoute();
            }
            for(StopID id: StopID.values()) {
                output.add(loadPage(id));
            }

            return output;
        }
        @Override
        protected void onPreExecute() {
            fragInstance.editTextView("Loading Content");
            //TODO: progress bar
        }

        @Override
        protected void onPostExecute(ArrayList<Stop> list){
            if(!Route.isInitalized()) {
                Route.initalizeRoute();
            }
            try {
                Route.getInstance().updateRoute(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragInstance.updateAdapter();
            fragInstance.editTextView("Times Loaded");
        }


        protected Stop loadPage(StopID stopID) {
            String times[] = {"0", "0", "0", "0", "0", "0", "0", "0"};
            String log = "";
            try {
                Document doc = Jsoup.connect("https://ycpapps.ycp.edu/transit/gettimes.php?sid=" + stopID.getId()).get(); //loads html via http, TODO: use URI builder
                Elements elements = doc.getElementsByAttributeValue("width", "80%");
                for(int i=0; i<elements.size(); i++) {
                    String text =  elements.get(i).text();
                    if(text.equalsIgnoreCase("Arriving")) {
                        times[i] = "0";
                        log +=  text + ", ";
                    }
                    else if(text.length() > 0 && !text.equalsIgnoreCase("minutes") && text != null) {
                        times[i] = text;
                        //Log.d("PARSE_DATA", "" +  Integer.parseInt(text));
                        log += text  + ", ";
                    }
                }
                Log.d("RETURN_DATA", log);
            }
            catch(IOException e) {
                Log.e("IO_EXCEPTION", e.toString());
            }
            return new Stop(stopID, Integer.parseInt(times[0]), Integer.parseInt(times[1]));
        }
}
