package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import java.util.ArrayList;

/**
 * Created by Aaron on 2/4/2016.
 */
public class Route {
    public static Route r;

    public static void initalizeRoute() {
        r = new Route();
    }

    public static Route getInstance() {
        return r;
    }

    private ArrayList<Stop> stops;

    public Route() { //fills with empty stops
        for(StopID stop_id : StopID.values()) {
            this.stops.add(new Stop(stop_id));
        }
    }

    public int getDistanceToStop(Stop stop) {
        //TODO: Implement route distance calculating
        return -1;
    }

    public void updateTimes() {

    }
}
