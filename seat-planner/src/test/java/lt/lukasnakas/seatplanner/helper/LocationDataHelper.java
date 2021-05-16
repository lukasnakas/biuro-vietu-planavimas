package lt.lukasnakas.seatplanner.helper;

import lt.lukasnakas.seatplanner.model.Location;

public class LocationDataHelper {

    public static Location getValidLocation() {
        Location location = new Location();
        location.setOffice("Office");
        location.setFloorNumb("6 aukštas");
        location.setRoomNumb("6.06");
        return location;
    }

}
