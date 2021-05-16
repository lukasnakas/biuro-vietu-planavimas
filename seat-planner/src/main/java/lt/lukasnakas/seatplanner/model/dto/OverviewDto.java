package lt.lukasnakas.seatplanner.model.dto;

import lt.lukasnakas.seatplanner.model.OverviewOffice;

import java.util.List;

public class OverviewDto {

    private List<OverviewOffice> overviewOfficeList;

    public OverviewDto(
            List<OverviewOffice> overviewOfficeList) {
        this.overviewOfficeList = overviewOfficeList;
    }

    public List<OverviewOffice> getOverviewOfficeList() {
        return overviewOfficeList;
    }

    public void setOverviewOfficeList(
            List<OverviewOffice> overviewOfficeList) {
        this.overviewOfficeList = overviewOfficeList;
    }
}
