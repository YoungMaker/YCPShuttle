package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Aaron on 2/4/2016.
 */
public class Route {
    private static Route r;
    private static boolean initalized = false;
    private ArrayList<Stop> stops;

    public static boolean isInitalized() {
        return initalized;
    }

    public static void initalizeRoute() {
        r = new Route();

    }

    public static Route getInstance() {
        if(!Route.isInitalized()) {
            Route.initalizeRoute();
        }
        return r;
    }

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
    }

    public Stop getStop(int index) {
        return stops.get(index);
    }


    public Route() { //fills with empty stops
        stops = new ArrayList<Stop>();
        for(StopID stop_id : StopID.values()) {
            this.stops.add(new Stop(stop_id));
        }
        initalized = true;
    }

    public int getDistanceToStop(Stop stop) {
        //TODO: Implement route distance calculating
        return -1;
    }

    public void setStop(Stop stop) {
        for(int i=0; i < stops.size(); i++) {
            Stop cStop = stops.get(i);
            if(cStop.getId().equals(stop.getId())) {
                stops.set(i, stop);
                return;
            }
        }
    }

    public Stop getStopByID(int id) { //DOESN"T WORK
       for(Stop s: stops) {
           //Log.v("STOP_ID_INT", ""+ s.getId().getId());
           if(s.getId().getId() == id)
               return s;
       }
        return null;
    }


    public void updateRoute(ArrayList<Stop> times) { //DEPRECIATED
        for(int i=0; i<times.size(); i++) {
            stops.set(i, times.get(i));
        }
    }
}
