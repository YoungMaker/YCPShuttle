package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

/**
 * Created by Aaron on 2/9/2016.
 */
public enum Error {
    NO_SHUTLE_TIMES(1),
    REQUEST_TIMED_OUT(2),
    NETWORK_ERROR(3);

    private int error;
    Error(int id) {
        error = id;
    }
    public int getId() {
        return error;
    }

    public String toString() {
        int intId = this.getId();
        switch(intId) {
            case 1:
                return "No Shuttle Information";
            case 2:
                return "Network Request Timed Out";
            default:
                return "Generic Network Error";
        }
    }
}
