package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.*;
import lt.lukasnakas.seatplanner.model.dto.OverviewDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        List<OverviewOffice> offices = companyService.findCompanyById(companyId).getOfficeList();

        offices.forEach(overviewOffice -> {
            List<OverviewFloor> floors = overviewOffice.getOverviewFloorList();
            floors.forEach(floor -> {
                List<Room> rooms = floor.getRoomList();
                rooms.forEach(room -> {
                    List<Team> teams = room.getTeams();
                    teams.forEach(team -> {
                        List<Member> members = team.getMembers();
                        Collections.sort(members);
                        team.setMembers(members);
                    });

                    Collections.sort(teams);
                    room.setTeams(teams);
                });

                Collections.sort(rooms);
                floor.setRoomList(rooms);
            });

            Collections.sort(floors);
            overviewOffice.setOverviewFloorList(floors);
        });
        Collections.sort(offices);

        return offices;
    }

}
