package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

/**
 * Created by Aaron on 2/4/2016.
 */
public enum StopID {

    WOLF_TO_WEST(1100),
    SPRING_GARDEN(101),
    READCO(102),
    RAIL_TRAIL(103),
    GRUM(104),
    WOLF_TO_CREEK(100),
    CREEK(105),
    NSC(106);

    private int id;

    StopID(int id) {
        this.id = id;
    }

    public int getId() {
       return id;
    }

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
                return "Grumbacher/Dhiel Lot";
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

    public boolean equals(StopID s) {
        if(s.getId() == this.getId())
            return true;

        return false;
    }
}
