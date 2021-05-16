package lt.lukasnakas.seatplanner.model;

import com.opencsv.bean.CsvBindByName;

import java.util.Objects;

public class Location {

    @CsvBindByName(column = "Office")
    private String office;

    @CsvBindByName(column = "Floor")
    private String floorNumb;

    @CsvBindByName(column = "Room")
    private String roomNumb;

    public Location(String office, String floorNumb, String roomNumb) {
        this.office = office;
        this.floorNumb = floorNumb;
        this.roomNumb = roomNumb;
    }

    public Location() {
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getFloorNumb() {
        return floorNumb;
    }

    public void setFloorNumb(String floorNumb) {
        this.floorNumb = floorNumb;
    }

    public String getRoomNumb() {
        return roomNumb;
    }

    public void setRoomNumb(String roomNumb) {
        this.roomNumb = roomNumb;
    }

    public boolean isValid() {
        return this.office != null && this.floorNumb != null && this.roomNumb != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(office, location.office) &&
                Objects.equals(floorNumb, location.floorNumb) &&
                Objects.equals(roomNumb, location.roomNumb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(office, floorNumb, roomNumb);
    }

    @Override
    public String toString() {
        return "Location{" +
                ", office='" + office + '\'' +
                ", floorNumb='" + floorNumb + '\'' +
                ", roomNumb='" + roomNumb + '\'' +
                '}';
    }
}
