package lt.lukasnakas.seatplanner.model;

import org.bson.codecs.pojo.annotations.BsonId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OverviewOffice implements Comparable<OverviewOffice> {

    @BsonId
    private String id;
    private String officeName;
    private List<OverviewFloor> overviewFloorList;

    public OverviewOffice(String officeName,
                          List<OverviewFloor> overviewFloorList) {
        this.officeName = officeName;
        this.overviewFloorList = overviewFloorList;
        this.id = UUID.randomUUID().toString();
    }

    public OverviewOffice(String officeName) {
        this.officeName = officeName;
        this.overviewFloorList = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    public OverviewOffice() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public List<OverviewFloor> getOverviewFloorList() {
        return overviewFloorList;
    }

    public void setOverviewFloorList(
            List<OverviewFloor> overviewFloorList) {
        this.overviewFloorList = overviewFloorList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OverviewOffice that = (OverviewOffice) o;
        return Objects.equals(id, that.id) && Objects
                .equals(officeName, that.officeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, officeName);
    }


    @Override
    public int compareTo(OverviewOffice o) {
        return this.getOfficeName().compareTo(o.getOfficeName());
    }
}
