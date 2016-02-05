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

    public String toString() {
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
                return "Grumbacher/Dheil Lot";
            case 105:
                return "Creek Crossing";
            case 106:
                return "North Side Commons";
            default:
                return "Incorrect stop ID";
        }
    }
}
