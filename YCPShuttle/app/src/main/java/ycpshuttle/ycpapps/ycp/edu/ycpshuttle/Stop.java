package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.location.Location;
import android.provider.CalendarContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Aaron on 2/4/2016.
 */
public class Stop implements Comparable<Stop> {


    private StopID id;
    private int time; //time till next shuttle in minutes, 0 if arriving.
    private int nextTime; //time till shuttle after next.



    private boolean isTracking = false;

    private Error errorCode;

    public Stop(StopID id) {
        this.id = id;
    }

    public StopID getId() {
        return id;
    }

    public Stop(StopID id, int time, int nextTime) {
        this.id = id;
        this.time = time;
        this.nextTime = nextTime;
    }

    public Stop(StopID id, int time, int nextTime, Error error) {
        this.id = id;
        this.time = time;
        this.nextTime = nextTime;
        this.errorCode = error;
    }

    public boolean isTracking() {
        return isTracking;
    }

    public void setIsTracking(boolean isTracking) {
        this.isTracking = isTracking;
    }

    public Error getErrorCode() {
        return errorCode;
    }

    public void setNextTime(int nextTime) {
        this.nextTime = nextTime;
    }

    public void setTime(int time) {
        this.time = time;
    }


    public int getTime() {
        return time;
    }

    public String getName() {
        return id.toString();
    }

    public int getNextTime() {
        return nextTime;
    }

    public String getNextArrivalTime() { //uses code from http://stackoverflow.com/questions/18734452/display-current-time-in-12-hour-format-with-am-pm
        GregorianCalendar c = new GregorianCalendar();
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");
        c.add(Calendar.MINUTE, nextTime);
        return fmt.format(c.getTime());
    }

    public String getArrivalTime() { //uses code from http://stackoverflow.com/questions/18734452/display-current-time-in-12-hour-format-with-am-pm
        GregorianCalendar c = new GregorianCalendar();
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");
        c.add(Calendar.MINUTE, time);
        return fmt.format(c.getTime());
    }

    public GregorianCalendar getCalendarArrivalTime() { //uses code from http://stackoverflow.com/questions/18734452/display-current-time-in-12-hour-format-with-am-pm
        GregorianCalendar c = new GregorianCalendar();
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");
        c.add(Calendar.MINUTE, time);
        return c;
    }

    public double getDistanceTo() {
        Location cLoc = Route.getInstance().getCurrentLoc();
        Location stopLoc = getId().getLocation();
        Log.v("locations", "cLoc " + cLoc.toString() + " stopLoc " + stopLoc.toString());
        return cLoc.distanceTo(stopLoc);
    }

    public int compareTo(Stop other) { //sorting by time
        if(other.getTime() < this.getTime()) {
            return 1;
        }
        else if(other.getTime() == this.getTime()) {
            return 0;
        }
        else {
            return -1;
        }
    }

    public int compareDistnace(Stop other) { //sorting by time
        if(other.getDistanceTo() < this.getDistanceTo()) {
            return 1;
        }
        else if(other.getDistanceTo() == this.getDistanceTo()) {
            return 0;
        }
        else {
            return -1;
        }
    }

    public int compareNum(Stop other) { //default sorting

        if(other.getId().getNum() < this.getId().getNum()) {

            return 1;
        }
        else if(other.getId().getNum() == this.getId().getNum()) {
            return 0;
        }
        else {
            return -1;
        }
    }


    public String toString() {
        return id.toString() + "-  " + time + " min, " + nextTime + " min";
    }

}
