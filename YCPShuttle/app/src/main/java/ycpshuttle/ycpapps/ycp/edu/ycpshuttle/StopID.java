package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

import android.location.Location;

/**
 * Created by Aaron on 2/4/2016.
 */
public enum StopID {


    SPRING_GARDEN(101,1),
    READCO(102,2),
    RAIL_TRAIL(103,3),
    GRUM(104,4),
    WOLF_TO_CREEK(100,5),
    CREEK(105,6),
    NSC(106,7),
    WOLF_TO_WEST(1100,8);

    private int id;
    private int num;

    StopID(int id, int num) {
        this.id = id;
        this.num = num;
    }

    public int getId() {
       return id;
    }
    public int getNum() {return num;}

    public String toString() { //should these be XML string resources?
        int intId = this.getId();
        switch(intId) {
            case 1100:
                return "Wolf Hall to West Campus";
            case 100:
                return "Wolf Hall to Creek Crossing";
            case 101:
               return "Spring Garden Appts.";
            case 102:
                return "Readco Lot";
            case 103:
                return "Rail Trail Lot";
            case 104:
                return "Grumbacher/Diehl Lot";
            case 105:
                return "Creek Crossing";
            case 106:
                return "North Side Commons";
            default:
                return "Incorrect stop ID";
        }
    }

    public String getGeoString() { //these should probably be XML string resources?
        int intId = this.getId();
        switch(intId) {
            case 1100:
                return "geo:39.945595,-76.730266?z=21&q=39.945595,-76.730266(Wolf Hall to West Campus)";
            case 100:
                return "geo:39.945595,-76.730266?z=21&q=39.945595,-76.730266(Wolf Hall to Creek Crossing)";
            case 101:
                return "geo:39.942915,-76.738942?z=21&q=39.942915,-76.738942(Spring Garden Appts.)";
            case 102:
                return "geo:39.944597,-76.739710?z=21&q=39.944597,-76.739710(Readco Lot)";
            case 103:
                return "geo:39.947355,-76.737266?z=21&q=39.947355,-76.737266(Rail Trail Lot)";
            case 104:
                return "geo:39.945805,-76.735376?z=21&q=39.945805,-76.735376(Grumbacher/Dhiel Lot)";
            case 105:
                return "geo:39.947611,-76.727925?z=21&q=39.947611,-76.727925(Creek Crossing)";
            case 106:
                return "geo:39.949733,-76.733897?z=21&q=39.949733,-76.733897(Northside Commons)";
            default:
                return "geo:0,0?q=York+College+of+Pennsylvania";
        }
    }

    public Location getLocation() {
        int intId = this.getId();
        Location loc = new Location("");
        switch(intId) {
            case 1100:
                loc.setLatitude(39.945595);
                loc.setLongitude(-76.730266);
                break;
            case 100:
                loc.setLatitude(39.945595);
                loc.setLongitude(-76.730266);
                break;
            case 101:
                loc.setLatitude(39.942915);
                loc.setLongitude(-76.738942);
                break;
            case 102:
                loc.setLatitude(39.944597);
                loc.setLongitude(-76.739710);
                break;
            case 103:
                loc.setLatitude(39.947355);
                loc.setLongitude(-76.737266);
                break;
            case 104:
                loc.setLatitude(39.945805);
                loc.setLongitude(-76.735376);
                break;
            case 105:
                loc.setLatitude(39.947611);
                loc.setLongitude(-76.727925);
                break;
            case 106:
                loc.setLatitude(39.949733);
                loc.setLongitude(-76.733897);
                break;
            default:

        }
        return loc;
    }

    public boolean equals(StopID s) {
        if(s.getId() == this.getId())
            return true;

        return false;
    }
}
