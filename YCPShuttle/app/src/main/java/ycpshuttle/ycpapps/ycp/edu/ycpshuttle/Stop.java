package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.provider.CalendarContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Aaron on 2/4/2016.
 */
public class Stop {


    private StopID id;
    private int time; //time till next shuttle in minutes, 0 if arriving.
    private int nextTime; //time till shuttle after next.

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

    public String getNextArrivalTime() {
        GregorianCalendar c = new GregorianCalendar();
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm");
        c.add(Calendar.MINUTE, time);
        return fmt.format(c.getTime());
    }


    public String toString() {
        return id.toString() + "-  " + time + " min " +"(" + getNextArrivalTime() + ") " + nextTime + " min";
    }

}
