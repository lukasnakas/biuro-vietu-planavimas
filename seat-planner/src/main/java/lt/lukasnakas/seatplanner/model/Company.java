package lt.lukasnakas.seatplanner.model;

import org.bson.codecs.pojo.annotations.BsonId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Company {

    @BsonId
    private String id;
    private String name;
    private List<OverviewOffice> officeList;

    public Company(String name,
                   List<OverviewOffice> officeList) {
        this.name = name;
        this.officeList = officeList;
    }

    public Company(String name) {
        this.name = name;
        this.officeList = new ArrayList<>();
    }

    public Company() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OverviewOffice> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<OverviewOffice> officeList) {
        this.officeList = officeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        return id.equals(company.id) && name.equals(company.name) && Objects
                .equals(officeList, company.officeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, officeList);
    }
}
