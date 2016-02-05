package ycpshuttle.ycpapps.ycp.edu.ycpshuttle;

/**
 * Created by Aaron on 2/4/2016.
 */
public enum StopID {

    WOLF_TO_WEST(1100),
    WOLF_TO_CREEK(100),
    SPRING_GARDEN(101),
    READCO(102),
    RAIL_TRAIL(103),
    GRUM(104),
    CREEK(105),
    NSC(106);

    private int id;

    StopID(int id) {
        this.id = id;
    }

    public int getId() {
       return id;
    }
}
