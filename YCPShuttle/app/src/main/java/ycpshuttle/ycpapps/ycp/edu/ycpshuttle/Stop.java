package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

/**
 * Created by Aaron on 2/4/2016.
 */
public class Stop {

    private StopID id;
    private int time; //time till next shuttle in minutes, 0 if arriving.
    private int nextTime; //time till shuttle after next.

    public Stop(StopID id) {
        this.id = id;
    }

    public Stop(StopID id, int time, int nextTime) {
        this.id = id;
        this.time = time;
        this.nextTime = nextTime;
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

    public int getNextTime() {
        return nextTime;
    }

    public String toString() {
        return id.toString() + "-  " + time + "  " + nextTime;
    }

}
