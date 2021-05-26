import { useState } from "react";
import EditableFloorListComponent from "../components/offices/EditableFloorListComponent";
import EditableOfficeListComponent from "../components/offices/EditableOfficeListComponent";
import EditableRoomListComponent from "../components/offices/EditableRoomListComponent";
import EditableTeamListComponent from "../components/offices/EditableTeamListComponent";
import OfficesServices from "../services/OfficesService";
import FloorsService from "../services/FloorService";
import RoomService from "../services/RoomService";
import TeamService from "../services/TeamService";

const OfficesPage = (props) => {
  const {
    fetchOfficeData,
    officeData,
    setOfficeData,
    updateOfficeState,
    setUpdateOfficeState,
    deleteOfficeState,
    setDeleteOfficeState,
    additionOfficeState,
    setAdditionOfficeState,
    updateOffice,
    deleteOffice,
    addOffice,
    addedOffice,
    setAddedOffice,
  } = OfficesServices(props);
  const {
    floorData,
    setFloorData,
    updateFloorState,
    setUpdateFloorState,
    deleteFloorState,
    setDeleteFloorState,
    additionFloorState,
    setAdditionFloorState,
    updateFloor,
    deleteFloor,
    addFloor,
    fetchFloorData,
  } = FloorsService(props);
  const {
    roomData,
    setRoomData,
    updateRoomState,
    setUpdateRoomState,
    deleteRoomState,
    setDeleteRoomState,
    additionRoomState,
    setAdditionRoomState,
    updateRoom,
    deleteRoom,
    addRoom,
    fetchRoomData,
  } = RoomService(props);
  const {
    teamData,
    setTeamData,
    updateTeamState,
    setUpdateTeamState,
    deleteTeamState,
    setDeleteTeamState,
    additionTeamState,
    setAdditionTeamState,
    updateTeam,
    deleteTeam,
    addTeam,
    fetchTeamData,
  } = TeamService(props);
  const [selectedOffice, setSelectedOffice] = useState({});
  const [selectedFloor, setSelectedFloor] = useState({});
  const [selectedRoom, setSelectedRoom] = useState({});
  const [selectedTeam, setSelectedTeam] = useState({});
  const [selectedOfficeId, setSelectedOfficeId] = useState("");
  const [selectedFloorId, setSelectedFloorId] = useState("");
  const [selectedRoomId, setSelectedRoomId] = useState("");

  return (
    <div className="main-body">
      <div className="container h-100">
        <div className="row h-100">
          <div className="col my-auto">
            <div className="page-container rounded-20">
              {props.isLoggedIn ? (
                props.isAdmin ? (
                  <div>
                    <div className="row">
                      <div className="col">
                        <EditableOfficeListComponent
                          fetchOfficeData={fetchOfficeData}
                          itemList={officeData}
                          listName="Biurai"
                          officeData={officeData}
                          setOfficeData={setOfficeData}
                          selectedOffice={selectedOffice}
                          setSelectedOffice={setSelectedOffice}
                          companyId={props.companyId}
                          updateState={updateOfficeState}
                          setUpdateState={setUpdateOfficeState}
                          deleteState={deleteOfficeState}
                          setDeleteState={setDeleteOfficeState}
                          additionState={additionOfficeState}
                          setAdditionState={setAdditionOfficeState}
                          updateOffice={updateOffice}
                          deleteOffice={deleteOffice}
                          addOffice={addOffice}
                          fetchFloorData={fetchFloorData}
                          setSelectedOfficeId={setSelectedOfficeId}
                          setTeamData={setTeamData}
                          setRoomData={setRoomData}
                        />
                      </div>
                      <div className="col">
                        <EditableFloorListComponent
                          itemList={floorData}
                          listName="Aukštai"
                          floorData={floorData}
                          setFloorData={setFloorData}
                          selectedFloor={selectedFloor}
                          setSelectedFloor={setSelectedFloor}
                          companyId={props.companyId}
                          updateState={updateFloorState}
                          setUpdateState={setUpdateFloorState}
                          deleteState={deleteFloorState}
                          setDeleteState={setDeleteFloorState}
                          additionState={additionFloorState}
                          setAdditionState={setAdditionFloorState}
                          updateFloor={updateFloor}
                          deleteFloor={deleteFloor}
                          addFloor={addFloor}
                          fetchRoomData={fetchRoomData}
                          setSelectedFloorId={setSelectedFloorId}
                          selectedOfficeId={selectedOfficeId}
                          setTeamData={setTeamData}
                          fetchFloorData={fetchFloorData}
                        />
                      </div>
                    </div>
                    <div className="row" style={{ marginTop: "30px" }}>
                      <div className="col">
                        <EditableRoomListComponent
                          itemList={roomData}
                          listName="Kabinetai"
                          roomData={roomData}
                          setRoomData={setRoomData}
                          selectedRoom={selectedRoom}
                          setSelectedRoom={setSelectedRoom}
                          companyId={props.companyId}
                          updateState={updateRoomState}
                          setUpdateState={setUpdateRoomState}
                          deleteState={deleteRoomState}
                          setDeleteState={setDeleteRoomState}
                          additionState={additionRoomState}
                          setAdditionState={setAdditionRoomState}
                          updateRoom={updateRoom}
                          deleteRoom={deleteRoom}
                          addRoom={addRoom}
                          fetchTeamData={fetchTeamData}
                          setSelectedRoomId={setSelectedRoomId}
                          selectedOfficeId={selectedOfficeId}
                          selectedFloorId={selectedFloorId}
                          fetchRoomData={fetchRoomData}
                        />
                      </div>
                      <div className="col">
                        <EditableTeamListComponent
                          itemList={teamData}
                          listName="Komandos"
                          teamData={teamData}
                          setTeamData={setTeamData}
                          selectedTeam={selectedTeam}
                          setSelectedTeam={setSelectedTeam}
                          companyId={props.companyId}
                          updateState={updateTeamState}
                          setUpdateState={setUpdateTeamState}
                          deleteState={deleteTeamState}
                          setDeleteState={setDeleteTeamState}
                          additionState={additionTeamState}
                          setAdditionState={setAdditionTeamState}
                          updateTeam={updateTeam}
                          deleteTeam={deleteTeam}
                          addTeam={addTeam}
                          selectedRoomId={selectedRoomId}
                          selectedOfficeId={selectedOfficeId}
                          selectedFloorId={selectedFloorId}
                          fetchTeamData={fetchTeamData}
                        />
                      </div>
                    </div>
                  </div>
                ) : (
                  <h1>Tik administratoriai turi teisę matyti šį puslapį</h1>
                )
              ) : (
                <h1>Norėdami matyti šį puslapį, turite prisijungti</h1>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OfficesPage;
