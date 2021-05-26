package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.mapper.RoomMapper;
import lt.lukasnakas.seatplanner.model.*;
import lt.lukasnakas.seatplanner.model.exception.TeamNotFoundException;
import lt.lukasnakas.seatplanner.model.request.AddRoomRequest;
import lt.lukasnakas.seatplanner.model.request.AddTeamRequest;
import lt.lukasnakas.seatplanner.model.request.EditRoomRequest;
import lt.lukasnakas.seatplanner.model.request.EditTeamRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lt.lukasnakas.seatplanner.service.TeamService.TEAM_JOIN_DELIMITER;
import static org.apache.commons.lang3.math.NumberUtils.max;

@Service
public class RoomService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(RoomService.class);

    private final RoomMapper roomMapper;
    private final FloorService floorService;

    public RoomService(RoomMapper roomMapper, FloorService floorService) {
        this.roomMapper = roomMapper;
        this.floorService = floorService;
    }

    public List<Company> findAllCompanies() {
        return floorService.findAllCompanies();
    }

    public Company findCompanyById(String id) {
        return floorService.findCompanyById(id);
    }

    public List<Room> findAllRooms() {
        List<Room> allRooms = new ArrayList<>();
        floorService.findAllCompanies().forEach(
                company -> company.getOfficeList().forEach(
                        overviewOffice -> overviewOffice.getOverviewFloorList().forEach(
                                overviewFloor -> allRooms.addAll(overviewFloor.getRoomList())
                        )));
        return allRooms;
    }

    public List<OverviewFloor> findAllFloors() {
        return floorService.findAllFloors();
    }

    public List<OverviewOffice> findAllOffices() {
        return floorService.findAllOffices();
    }

    public OverviewOffice findOfficeById(String companyId, String id) {
        return floorService.findOfficeById(companyId, id);
    }

    public OverviewOffice findOfficeByName(String companyId, String name) {
        return floorService.findOfficeByName(companyId, name);
    }

    public OverviewFloor findFloorById(String companyId, String officeId, String id) {
        return floorService.findFloorById(companyId, officeId, id);
    }

    public OverviewFloor findFloorByName(String companyId, String officeId, String name) {
        return floorService.findFloorByName(companyId, officeId, name);
    }

    public void saveUpdatedCompany(Company company) {
        floorService.saveUpdatedCompany(company);
    }

    public List<Room> findRoomsByFloor(String companyId, String officeId, String floorId) {
        CONSOLE_LOGGER.info("Fetching rooms in floor ID: " + floorId);
        return floorService.findFloorById(companyId, officeId, floorId).getRoomList().stream().sorted().collect(Collectors.toList());
    }

    public Room findRoomById(String companyId, String officeId, String floorId, String roomId) {
        CONSOLE_LOGGER.info("Fetching room with ID: " + roomId);
        return floorService.findFloorById(companyId, officeId, floorId).getRoomList()
                .stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst()
                .orElseThrow();
    }

    public Room findRoomByName(String companyId, String officeId, String floorId, String name) {
        CONSOLE_LOGGER.info("Fetching room with name: " + name);
        return floorService.findFloorById(companyId, officeId, floorId).getRoomList()
                .stream()
                .filter(room -> room.getLocation().getRoomNumb().equals(name))
                .findFirst()
                .orElseThrow();
    }

    public Room findRoomByName(String companyId, String name) {
        CONSOLE_LOGGER.info("Fetching room with name: " + name);
        return floorService.findFloorByName(companyId, name).getRoomList()
                .stream()
                .filter(room -> room.getLocation().getRoomNumb().equals(name))
                .findFirst()
                .orElseThrow();
    }

    public OverviewFloor findFloorByName(String companyId, String name) {
        CONSOLE_LOGGER.info("Fetching room with name: " + name);
        return floorService.findFloorByName(companyId, name);
    }

    public Room addRoom(AddRoomRequest addRoomRequest) {
        Room room = new Room();
        room.setMaxCapacity(addRoomRequest.getMaxCapacity());
        room.setCurrentCapacity(0);
        CONSOLE_LOGGER.info("Adding room with number: " + addRoomRequest.getName());
        floorService.addRoom(addRoomRequest, room);
        return room;
    }

    public Room editRoom(EditRoomRequest editRoomRequest) {
        Room room = findRoomById(editRoomRequest.getCompanyId(), editRoomRequest.getOfficeId(), editRoomRequest.getFloorId(), editRoomRequest.getId());
        CONSOLE_LOGGER.info("Editing room with number" + editRoomRequest.getName());
        return floorService.editRoom(room, editRoomRequest);
    }

    public void removeRoom(String companyId, String officeId, String floorId, String roomId) {
        Room room = findRoomById(companyId, officeId, floorId, roomId);
        CONSOLE_LOGGER.info("Removing room with number: " + room.getLocation().getRoomNumb());
        floorService.removeRoom(companyId, officeId, room);
    }

    public Room addTeam(AddTeamRequest addTeamRequest, Team team) {
        Company company = floorService.findCompanyById(addTeamRequest.getCompanyId());
        OverviewOffice overviewOffice = floorService.findOfficeById(company.getId(), addTeamRequest.getOfficeId());
        OverviewFloor overviewFloor = floorService.findFloorById(company.getId(), overviewOffice.getId(), addTeamRequest.getFloorId());
        Room room = findRoomById(company.getId(), overviewOffice.getId(), overviewFloor.getId(), addTeamRequest.getRoomId());

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        overviewFloor.getRoomList().remove(room);
        room.getTeams().add(team);
        overviewFloor.getRoomList().add(room);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        floorService.saveUpdatedCompany(company);
        return room;
    }

    public Team editTeam(Team team, EditTeamRequest editTeamRequest) {
        Company company = floorService.findCompanyById(editTeamRequest.getCompanyId());
        OverviewOffice overviewOffice = floorService.findOfficeById(company.getId(), editTeamRequest.getOfficeId());
        OverviewFloor overviewFloor = floorService.findFloorById(company.getId(), overviewOffice.getId(), editTeamRequest.getFloorId());
        Room room = findRoomsByFloor(company.getId(), overviewOffice.getId(), overviewFloor.getId()).stream()
                .filter(r -> r.getTeams().contains(team))
                .findFirst()
                .orElseThrow();

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        overviewFloor.getRoomList().remove(room);
        int teamIndex = room.getTeams().indexOf(team);
        Team updatableTeam = room.getTeams().get(teamIndex);
        updatableTeam.setStack(editTeamRequest.getStack());
        updatableTeam.setName(editTeamRequest.getName());
        updatableTeam.setConditions(editTeamRequest.getCondition());
        room.getTeams().remove(teamIndex);
        room.getTeams().add(updatableTeam);
        overviewFloor.getRoomList().add(room);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        floorService.saveUpdatedCompany(company);
        return updatableTeam;
    }

    public void removeTeam(String companyId, String officeId, String floorId, Team team) {
        Company company = floorService.findCompanyById(companyId);
        OverviewOffice overviewOffice = floorService.findOfficeById(company.getId(), officeId);
        OverviewFloor overviewFloor = floorService.findFloorById(company.getId(), overviewOffice.getId(), floorId);
        Room room = findRoomsByFloor(company.getId(), overviewOffice.getId(), overviewFloor.getId()).stream()
                .filter(r -> r.getTeams().contains(team))
                .findFirst()
                .orElseThrow();

        company.getOfficeList().remove(overviewOffice);
        overviewOffice.getOverviewFloorList().remove(overviewFloor);
        overviewFloor.getRoomList().remove(room);
        room.getTeams().remove(team);
        overviewFloor.getRoomList().add(room);
        overviewOffice.getOverviewFloorList().add(overviewFloor);
        company.getOfficeList().add(overviewOffice);

        floorService.saveUpdatedCompany(company);
    }

    public Room findByTeam(String companyId, String officeId, Team team) {
        OverviewOffice office = findOfficeById(companyId, officeId);
        for (OverviewFloor floor : office.getOverviewFloorList()) {
            for (Room room : floor.getRoomList()) {
                for (Team t: room.getTeams()) {
                    if (t.getName().equals(team.getName())) {
                        return room;
                    }
                }
            }
        }
        throw new TeamNotFoundException(String.format("Team referenced by %s not found", team.getName()));
    }

    public int findMaxRoomCapacityInOffice(String companyId, String officeId) {
        int maxCapacity = 0;
        OverviewOffice office = floorService.findOfficeById(companyId, officeId);

        for (OverviewFloor floor : office.getOverviewFloorList()) {
            for (Room room : floor.getRoomList()) {
                maxCapacity = max(maxCapacity, room.getMaxCapacity());
            }
        }

        return maxCapacity;
    }


    public int findMaxCapacity(Room room) {
        return room.getMaxCapacity();
    }

    public List<Room> subtractCurrentTeamSizes(List<Team> mappedTeams, Team currentTeam, int originalTeamSize) {
        List<Room> filteredRooms = new ArrayList<>();
        mappedTeams.forEach(team -> {
            Room room = team.getRoom();
            if (!hasRoomBeenAdded(filteredRooms, room)) {
                int roomCapacity = getRoomCapacity(currentTeam, originalTeamSize, team, room);
                Room newRoom = buildNewRoom(room, roomCapacity);
                filteredRooms.add(newRoom);
            } else {
                Room filteredRoom = getFilteredRoom(filteredRooms, room);
                int roomCapacity = filteredRoom.getCurrentCapacity() - team.getSize();
                filteredRoom.setCurrentCapacity(Math.max(roomCapacity, 0));
            }
        });
        return filteredRooms;
    }

    public Room buildRoomWithoutTeams(Room room) {
        return roomMapper.buildRoomWithoutTeams(room);
    }

    public void setCurrentTeamRoom(Team currentTeam, Room currentTeamRoom) {
        if (currentTeam.getRoom() == null) {
            Room room = buildRoomWithoutTeams(currentTeamRoom);
            currentTeam.setRoom(room);
        }
    }

    private Room getFilteredRoom(List<Room> filteredRooms, Room room) {
        return filteredRooms.stream()
                .filter(filteredRoom -> filteredRoom.getId().equals(room.getId()))
                .findAny()
                .orElse(new Room());
    }

    private Room buildNewRoom(Room room, int roomCapacity) {
        Room newRoom = new Room();
        newRoom.setId(room.getId());
        newRoom.setMaxCapacity(room.getMaxCapacity());
        newRoom.setLocation(room.getLocation());
        newRoom.setCurrentCapacity(roomCapacity);
        return newRoom;
    }

    private int getRoomCapacity(Team currentTeam, int originalTeamSize, Team team, Room room) {
        int roomCapacity = team.getId().equals(currentTeam.getId()) && !team.getName().contains(TEAM_JOIN_DELIMITER)
                ? room.getCurrentCapacity() - originalTeamSize
                : room.getCurrentCapacity() - team.getSize();
        return Math.max(roomCapacity, 0);
    }

    private boolean hasRoomBeenAdded(List<Room> rooms, Room roomToAdd) {
        return rooms.stream().anyMatch(room -> room.getId().contains(roomToAdd.getId()));
    }

}