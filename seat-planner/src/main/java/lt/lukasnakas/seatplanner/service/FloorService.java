package lt.lukasnakas.seatplanner.service;

import com.google.common.collect.Sets;
import lt.lukasnakas.seatplanner.model.*;
import lt.lukasnakas.seatplanner.model.request.AddFloorRequest;
import lt.lukasnakas.seatplanner.model.request.AddRoomRequest;
import lt.lukasnakas.seatplanner.model.request.EditFloorRequest;
import lt.lukasnakas.seatplanner.model.request.EditRoomRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static lt.lukasnakas.seatplanner.service.TeamService.DELIMITER;

@Service
public class FloorService implements Comparator<List<String>> {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(FloorService.class);
    private static final int MAX_FLOOR_SUBSET_SIZE = 4;
    private final OfficeService officeService;

    public FloorService(OfficeService officeService) {
        this.officeService = officeService;
    }

    public List<Company> findAllCompanies() {
        return officeService.findAllCompanies();
    }

    public List<OverviewFloor> findAllFloors() {
        List<OverviewFloor> allFloors = new ArrayList<>();
        officeService.findAllCompanies().forEach(
                company -> company.getOfficeList().forEach(
                        overviewOffice -> allFloors.addAll(overviewOffice.getOverviewFloorList())
                ));
        return allFloors;
    }

    public List<OverviewOffice> findAllOffices() {
        return officeService.findAllOffices();
    }

    public Company findCompanyById(String id) {
        return officeService.findCompanyById(id);
    }

    public OverviewOffice findOfficeById(String companyId, String id) {
        return officeService.findOfficeById(companyId, id);
    }

    public OverviewOffice findOfficeByName(String companyId, String name) {
        return officeService.findOfficeByName(companyId, name);
    }

    public void saveUpdatedCompany(Company company) {
        officeService.saveUpdatedCompany(company);
    }

    public List<OverviewFloor> findFloorsByOffice(String companyId, String officeId) {
        CONSOLE_LOGGER.info("Fetching floors in office ID: " + officeId);
        return officeService.findOfficeById(companyId, officeId).getOverviewFloorList().stream().sorted().collect(Collectors.toList());
    }

    public OverviewFloor findFloorById(String companyId, String officeId, String floorId) {
        CONSOLE_LOGGER.info("Fetching floor with ID: " + floorId);
        return findFloorsByOffice(companyId, officeId).stream()
                .filter(overviewFloor -> overviewFloor.getId().equals(floorId))
                .findFirst().
                        orElseThrow();
    }

    public OverviewFloor findFloorByName(String companyId, String officeId, String name) {
        CONSOLE_LOGGER.info("Fetching floor with name: " + name);
        return findFloorsByOffice(companyId, officeId).stream()
                .filter(overviewFloor -> overviewFloor.getFloorName().equals(name))
                .findFirst().
                        orElseThrow();
    }

    public OverviewFloor findFloorByName(String companyId, String name) {
        CONSOLE_LOGGER.info("Fetching floor with name: " + name);
        return findOfficeByName(companyId, name).getOverviewFloorList()
                .stream()
                .filter(overviewFloor -> overviewFloor.getFloorName().equals(name))
                .findFirst().
                        orElseThrow();
    }

    public OverviewFloor addFloor(AddFloorRequest addFloorRequest) {
        OverviewFloor overviewFloor = new OverviewFloor(addFloorRequest.getName());
        CONSOLE_LOGGER.info("Adding floor with name: " + addFloorRequest.getName());
        officeService.addFloor(addFloorRequest, overviewFloor);
        return overviewFloor;
    }

    public OverviewFloor editFloor(EditFloorRequest editFloorRequest) {
        OverviewFloor overviewFloor = findFloorById(editFloorRequest.getCompanyId(), editFloorRequest.getOfficeId(), editFloorRequest.getId());
        CONSOLE_LOGGER.info("Editing floor with name: " + editFloorRequest.getName());
        return officeService.editFloor(overviewFloor, editFloorRequest);
    }

    public void removeFloor(String companyId, String officeId, String floorId) {
        OverviewFloor overviewFloor = findFloorById(companyId, officeId, floorId);
        CONSOLE_LOGGER.info("Removing floor with name: " + overviewFloor.getFloorName());
        officeService.removeFloor(companyId, overviewFloor);
    }

    public OverviewFloor addRoom(AddRoomRequest addRoomRequest, Room room) {
        Company company = officeService.findCompanyById(addRoomRequest.getCompanyId());
        OverviewOffice overviewOffice = officeService.findOfficeById(company.getId(), addRoomRequest.getOfficeId());
        OverviewFloor overviewFloor = findFloorById(company.getId(), overviewOffice.getId(), addRoomRequest.getFloorId());

        Location location = new Location(overviewOffice.getOfficeName(), overviewFloor.getFloorName(), addRoomRequest.getName());
        room.setLocation(location);

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        overviewFloor.getRoomList().add(room);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        officeService.saveUpdatedCompany(company);
        return overviewFloor;
    }

    public Room editRoom(Room room, EditRoomRequest editRoomRequest) {
        Company company = officeService.findCompanyById(editRoomRequest.getCompanyId());
        OverviewOffice overviewOffice = officeService.findOfficeById(company.getId(), editRoomRequest.getOfficeId());
        OverviewFloor overviewFloor = findFloorsByOffice(company.getId(), overviewOffice.getId()).stream()
                .filter(floor -> floor.getRoomList().contains(room))
                .findFirst()
                .orElseThrow();

        Location location = new Location(overviewOffice.getOfficeName(), overviewFloor.getFloorName(), editRoomRequest.getName());

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        int roomIndex = overviewFloor.getRoomList().indexOf(room);
        Room updatableRoom = overviewFloor.getRoomList().get(roomIndex);
        updatableRoom.setLocation(location);
        updatableRoom.setMaxCapacity(editRoomRequest.getMaxCapacity());
        overviewFloor.getRoomList().remove(roomIndex);
        overviewFloor.getRoomList().add(updatableRoom);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        officeService.saveUpdatedCompany(company);
        return updatableRoom;
    }

    public void removeRoom(String companyId, String officeId, Room room) {
        Company company = officeService.findCompanyById(companyId);
        OverviewOffice overviewOffice = officeService.findOfficeById(company.getId(), officeId);
        OverviewFloor overviewFloor = findFloorsByOffice(company.getId(), overviewOffice.getId()).stream()
                .filter(floor -> floor.getRoomList().contains(room))
                .findFirst()
                .orElseThrow();

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        overviewFloor.getRoomList().remove(room);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        officeService.saveUpdatedCompany(company);
    }

    @Override
    public int compare(List<String> floorSubsetA, List<String> floorSubsetB) {
        int sizeDiff = floorSubsetA.size() - floorSubsetB.size();
        if (sizeDiff != 0) {
            return sizeDiff;
        }

        for (int i = 0; i < floorSubsetA.size(); i++) {
            int c = floorSubsetA.get(i).compareTo(floorSubsetB.get(i));
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    public static String extractFloorNumbWithoutDelimiter(String fullFloorNumb) {
        return fullFloorNumb.split(DELIMITER)[0];
    }

    public List<ArrayList<String>> generateFloorSubsets(Room currentTeamRoom, Set<String> floors, String requestLocation) {
        CONSOLE_LOGGER.info("Generating floor subsets");
        return Sets.powerSet(floors).stream()
                .map(ArrayList::new)
                .filter(floorSubsetSizePredicate().and(floorSubsetLocationPredicate(currentTeamRoom, requestLocation)))
                .sorted(this)
                .collect(Collectors.toList());
    }

    private Predicate<ArrayList<String>> floorSubsetLocationPredicate(Room currentTeamRoom, String requestLocation) {
        Optional<String> location = Optional.ofNullable(requestLocation);
        return floorSubset -> {
            if ((location.isPresent() && !currentTeamRoom.getLocation().getOffice().equals(location.get()))) {
                return true;
            }
            return floorSubset.contains(currentTeamRoom.getLocation().getFloorNumb());
        };
    }

    private Predicate<ArrayList<String>> floorSubsetSizePredicate() {
        return floorSubset -> floorSubset.size() < MAX_FLOOR_SUBSET_SIZE && !floorSubset.isEmpty();
    }

    public Map<Integer, List<String>> populateFloorMap(List<ArrayList<String>> floorSubsets) {
        CONSOLE_LOGGER.info("Populating floor map");
        Map<Integer, List<String>> floorMap = new HashMap<>();
        int index = 0;
        for (List<String> floorsSubset : floorSubsets) {
            floorMap.put(index, floorsSubset);
            index++;
        }
        return floorMap;
    }

}