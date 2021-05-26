package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.Company;
import lt.lukasnakas.seatplanner.model.OverviewFloor;
import lt.lukasnakas.seatplanner.model.OverviewOffice;
import lt.lukasnakas.seatplanner.model.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfficeService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(OfficeService.class);
    private final CompanyService companyService;

    public OfficeService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public List<Company> findAllCompanies() {
        return companyService.findCompanies();
    }

    public List<OverviewOffice> findAllOffices() {
        List<OverviewOffice> allOffices = new ArrayList<>();
        findAllCompanies().forEach(
                company -> allOffices.addAll(company.getOfficeList())
        );
        return allOffices;
    }

    public Company findCompanyById(String id) {
        return companyService.findCompanyById(id);
    }

    public void saveUpdatedCompany(Company company) {
        companyService.save(company);
    }

    public List<OverviewOffice> findOffices(String companyId) {
        CONSOLE_LOGGER.info("Fetching offices in company ID: " + companyId);
        return companyService.findCompanyById(companyId).getOfficeList().stream().sorted().collect(Collectors.toList());
    }

    public Collection<String> getOfficeNames(String companyId) {
        return companyService.getOfficeNames(companyId);
    }

    public OverviewOffice findOfficeById(String companyId, String id) {
        CONSOLE_LOGGER.info("Fetching office by ID: " + id);
        return companyService.findCompanyById(companyId).getOfficeList()
                .stream()
                .filter(office -> office.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public OverviewOffice findOfficeByName(String companyId, String name) {
        CONSOLE_LOGGER.info("Fetching office by name: " + name);
        return companyService.findCompanyById(companyId).getOfficeList()
                .stream()
                .filter(office -> office.getOfficeName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    public OverviewOffice addOffice(AddOfficeRequest addOfficeRequest) {
        OverviewOffice overviewOffice = new OverviewOffice(addOfficeRequest.getName());
        CONSOLE_LOGGER.info("Adding office with name: " + addOfficeRequest.getName());
        companyService.addOffice(addOfficeRequest.getCompanyId(), overviewOffice);
        return overviewOffice;
    }

    public OverviewOffice editOffice(EditOfficeRequest editOfficeRequest) {
        OverviewOffice overviewOffice = findOfficeById(editOfficeRequest.getCompanyId(), editOfficeRequest.getId());
        CONSOLE_LOGGER.info("Editing office with name: " + editOfficeRequest.getName());
        return companyService.editOffice(overviewOffice, editOfficeRequest.getName());
    }

    public void removeOffice(String companyId, String id) {
        OverviewOffice office = findOfficeById(companyId, id);
        CONSOLE_LOGGER.info("Removing office with name: " + office.getOfficeName());
        companyService.removeOffice(office);
    }

    public OverviewOffice addFloor(AddFloorRequest addFloorRequest, OverviewFloor overviewFloor) {
        Company company = companyService.findCompanyById(addFloorRequest.getCompanyId());

        OverviewOffice overviewOffice = findOfficeById(company.getId(), addFloorRequest.getOfficeId());
        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        companyService.save(company);
        return overviewOffice;
    }

    public OverviewFloor editFloor(OverviewFloor floor, EditFloorRequest editFloorRequest) {
        Company company = companyService.findCompanyById(editFloorRequest.getCompanyId());

        OverviewOffice overviewOffice = findOffices(company.getId()).stream()
                .filter(office -> office.getOverviewFloorList().contains(floor))
                .findFirst()
                .orElseThrow();
        company.getOfficeList().remove(overviewOffice);
        int floorIndex = overviewOffice.getOverviewFloorList().indexOf(floor);
        OverviewFloor updatableFloor = overviewOffice.getOverviewFloorList().get(floorIndex);
        updatableFloor.setFloorName(editFloorRequest.getName());
        overviewOffice.getOverviewFloorList().remove(floorIndex);
        overviewOffice.getOverviewFloorList().add(updatableFloor);
        company.getOfficeList().add(overviewOffice);

        companyService.save(company);
        return updatableFloor;
    }

    public void removeFloor(String companyId, OverviewFloor floor) {
        Company company = companyService.findCompanyById(companyId);

        OverviewOffice overviewOffice = findOffices(companyId).stream()
                .filter(office -> office.getOverviewFloorList().contains(floor))
                .findFirst()
                .orElseThrow();
        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(floor);
        company.getOfficeList().add(overviewOffice);
        companyService.save(company);
    }

    public List<OverviewOffice> findOfficesByLocation(FindSeatRequest findSeatRequest) {
        CONSOLE_LOGGER.info("Fetching offices by location");
        List<OverviewOffice> offices = new ArrayList<>();
        for (Company company : companyService.findCompanies()) {
            for (OverviewOffice overviewOffice : company.getOfficeList()) {
                if (overviewOffice.getOfficeName().equals(findSeatRequest.getLocation())) {
                    offices.add(overviewOffice);
                }
            }
        }
        return offices;
    }

}