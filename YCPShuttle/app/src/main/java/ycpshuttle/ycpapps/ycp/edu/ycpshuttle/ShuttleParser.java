package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import android.support.*;

/**
 * Created by Aaron on 2/4/2016.
 */
public class ShuttleParser extends AsyncTask<String, Stop, ArrayList<Stop>> {
        MainActivityFragment fragInstance;

        public ShuttleParser(MainActivityFragment frag ) {
            this.fragInstance = frag;
        }

        @Override
        protected ArrayList<Stop> doInBackground(String... uris) {
            ArrayList output = new ArrayList<Stop>();
            for(StopID id: StopID.values()) {
                Stop location = loadPage(uris[0], id); //why is this an array?
                //output.add(location);
                publishProgress(location);
            }

            return output;
        }
        @Override
        protected void onPreExecute() {
            fragInstance.editTextView("Loading Content");
            //TODO: progress bar
        }

    @Override
    protected void onProgressUpdate(Stop... value) {
        super.onProgressUpdate(value);
        Route.getInstance().setStop(value[0]); // why is this an array?
        fragInstance.popError(value[0]);
        fragInstance.refreshData();
    }

    @Override
        protected void onPostExecute(ArrayList<Stop> list){
//            try {
//                Route.getInstance().updateRoute(list);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            fragInstance.refreshData();
            fragInstance.editTextView("Times Loaded");
        }


        protected Stop loadPage(String URL, StopID stopID) {
            String times[] = {"0", "0", "0", "0", "0", "0", "0", "0"};
            String log = "";
            try {
                Document doc = Jsoup.connect(URL + stopID.getId()).get(); //loads html via http, TODO: use URI builder
                Elements elements = doc.getElementsByAttributeValue("width", "80%");
                if (elements.size() > 0) {
                    for (int i = 0; i < elements.size(); i++) {
                        String text = elements.get(i).text();
                        if (text.equalsIgnoreCase("Arriving")) {
                            times[i] = "0";
                            log += text + ", ";
                        } else if (text.length() > 0 && !text.equalsIgnoreCase("minutes") && text != null) {
                            times[i] = text;
                            //Log.d("PARSE_DATA", "" +  Integer.parseInt(text));
                            log += text + ", ";
                        }
                    }
                   // Log.d("RETURN_DATA", log);
                } else {
                    Log.e("NO_DATA", "No Shuttle Times for stop ID " + stopID.toString());
                    return new Stop(stopID, 0, 0, Error.NO_SHUTLE_TIMES);
                }
            }catch (InterruptedIOException iioe) {
                Log.e("IO_EXCEPTION", iioe.toString());
                return new Stop(stopID, 0, 0, Error.REQUEST_TIMED_OUT);
            }
            catch(IOException e) {
                Log.e("IO_EXCEPTION", e.toString());
                return new Stop(stopID, 0, 0, Error.NETWORK_ERROR);
            }
            return new Stop(stopID, Integer.parseInt(times[0]), Integer.parseInt(times[1]));
        }

}
