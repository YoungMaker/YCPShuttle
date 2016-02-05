package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

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
        return r;
    }

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
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

    public void updateTimes() {

    }
}
