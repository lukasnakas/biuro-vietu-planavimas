package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.Company;
import lt.lukasnakas.seatplanner.model.OverviewOffice;
import lt.lukasnakas.seatplanner.model.request.AddCompanyRequest;
import lt.lukasnakas.seatplanner.model.request.EditCompanyRequest;
import lt.lukasnakas.seatplanner.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(CompanyService.class);
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findCompanies() {
        return companyRepository.findAll();
    }

    public Company findCompanyById(String id) {
        CONSOLE_LOGGER.info("Fetching company by ID: " + id);
        return companyRepository.findById(id).orElseThrow();
    }

    public Company addCompany(AddCompanyRequest addCompanyRequest) {
        Company company = new Company(addCompanyRequest.getName());
        return companyRepository.save(company);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public Company editCompany(EditCompanyRequest editCompanyRequest) {
        Company company = companyRepository.findById(editCompanyRequest.getId())
                .orElseThrow();
        company.setName(editCompanyRequest.getName());
        return companyRepository.save(company);
    }

    public void removeCompany(String id) {
        Company company = companyRepository.findById(id).orElseThrow();
        companyRepository.delete(company);
    }

    public Collection<String> getOfficeNames(String companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        return company.getOfficeList().stream()
                .map(OverviewOffice::getOfficeName)
                .collect(Collectors.toList());
    }

    public Company addOffice(String companyId, OverviewOffice overviewOffice) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        company.getOfficeList().add(overviewOffice);
        return companyRepository.save(company);
    }

    public OverviewOffice editOffice(OverviewOffice overviewOffice, String updatedName) {
        Company company = companyRepository.findAll()
                .stream()
                .filter(c -> c.getOfficeList().contains(overviewOffice))
                .findFirst()
                .orElseThrow();
        int officeIndex = company.getOfficeList().indexOf(overviewOffice);
        OverviewOffice updatableOffice = company.getOfficeList().get(officeIndex);
        updatableOffice.setOfficeName(updatedName);
        company.getOfficeList().remove(officeIndex);
        company.getOfficeList().add(updatableOffice);
        companyRepository.save(company);
        return updatableOffice;
    }

    public void removeOffice(OverviewOffice overviewOffice) {
        Company company = companyRepository.findAll().stream()
                .filter(c -> c.getOfficeList().contains(overviewOffice))
                .findFirst()
                .orElseThrow();
        company.getOfficeList().remove(overviewOffice);
        companyRepository.save(company);
    }

}
