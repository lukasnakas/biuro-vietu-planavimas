package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.OverviewOffice;
import lt.lukasnakas.seatplanner.model.dto.OverviewDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(OverviewService.class);
    private final CompanyService companyService;

    public OverviewService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public OverviewDto getOverviewData(String companyId) {
        CONSOLE_LOGGER.info("Overview data fetched");
        return new OverviewDto(getOverviewOffices(companyId));
    }

    private List<OverviewOffice> getOverviewOffices(String companyId) {
        return companyService.findCompanyById(companyId).getOfficeList();
    }

}
