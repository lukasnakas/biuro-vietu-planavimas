import OverviewService from "../services/OverviewService";
import { useState } from "react";
import OfficeDropdown from "../components/dropdown/OfficeDropdown";
import FloorDropdown from "../components/dropdown/FloorDropdown";
import RoomDropdown from "../components/dropdown/RoomDropdown";
import RoomDetailsComponent from "../components/overview/RoomDetailsComponent";

const OverviewPage = (props) => {
  const { data } = OverviewService(props);
  const [floors, setFloors] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [teams, setTeams] = useState([]);
  const [selectedOffice, setSelectedOffice] = useState({});
  const [selectedFloor, setSelectedFloor] = useState({});
  const [selectedRoom, setSelectedRoom] = useState({});
  const [selectedTeam, setSelectedTeam] = useState({});
  const [officeDropdownValue, setOfficeDropdownValue] = useState(
    "Pasirinkite biurą.."
  );
  const [floorDropdownValue, setFloorDropdownValue] = useState(
    "Pasirinkite aukštą.."
  );
  const [roomDropdownValue, setRoomDropdownValue] = useState(
    "Pasirinkite kabinetą.."
  );
  const [teamDropdownValue, setTeamDropdownValue] = useState(
    "Pasirinkite komandą.."
  );
  const [isMoreDetailButtonDisabled, setMoreDetailButtonDisabled] = useState(
    true
  );
  const [showRoomDetails, setShowRoomDetails] = useState(false);

  const getSelectedLocationPath = () => {
    let selectedLocationPath = "";
    if (Object.keys(selectedOffice).length !== 0) {
      selectedLocationPath += selectedOffice;
    }
    if (Object.keys(selectedFloor).length !== 0) {
      selectedLocationPath += " > " + selectedFloor;
    }
    if (Object.keys(selectedRoom).length !== 0) {
      selectedLocationPath += " > " + selectedRoom;
    }
    return selectedLocationPath;
  };

  const resetFloorsDropdown = () => {
    resetRoomsDropdown();
    setFloorDropdownValue("Pasirinkite aukštą..");
    setRooms([]);
    setTeams([]);
    setSelectedFloor({});
  };

  const resetRoomsDropdown = () => {
    resetTeamsDropdown();
    setRoomDropdownValue("Pasirinkite kabinetą..");
    setTeams([]);
    setSelectedRoom({});
    setMoreDetailButtonDisabled(true);
  };

  const resetTeamsDropdown = () => {
    setTeamDropdownValue("Pasirinkite komandą..");
    setSelectedTeam({});
  };

  const hideMoreDetailsComponent = () => {
    setShowRoomDetails(false);
  };

  const showMoreDetailsComponent = () => {
    setShowRoomDetails(true);
  };

  return (
    <div className="main-body">
      <div className="container h-100">
        <div className="row h-100">
          <div className="col my-auto">
            <div className="page-container rounded-20">
              {props.isLoggedIn ? (
                showRoomDetails ? (
                  <RoomDetailsComponent
                    onBack={hideMoreDetailsComponent}
                    selectedRoom={selectedRoom}
                    selectedLocationPath={getSelectedLocationPath()}
                    teams={teams}
                  />
                ) : (
                  <div className="row">
                    <div className="col-4">
                      <h1>Nustatymai</h1>
                      <div className="row">
                        <div className="col">
                          <div className="row">
                            <div className="col text-left">
                              <label
                                htmlFor="officeDropdown"
                                className="control-label"
                              >
                                Biuras
                              </label>
                            </div>
                          </div>
                          <div className="row">
                            <div className="col">
                              <OfficeDropdown
                                dropdownValue={officeDropdownValue}
                                dropdownSetter={setOfficeDropdownValue}
                                offices={data}
                                setFloors={setFloors}
                                setSelectedOffice={setSelectedOffice}
                                resetLowerDropdowns={resetFloorsDropdown}
                                screen="overview"
                              />
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="row">
                        <div className="col">
                          <div className="row">
                            <div className="col text-left">
                              <label
                                htmlFor="floorDropdown"
                                className="control-label"
                              >
                                Aukštas
                              </label>
                            </div>
                          </div>
                          <div className="row">
                            <div className="col">
                              <FloorDropdown
                                dropdownValue={floorDropdownValue}
                                dropdownSetter={setFloorDropdownValue}
                                floors={floors}
                                setRooms={setRooms}
                                setSelectedFloor={setSelectedFloor}
                                resetLowerDropdowns={resetRoomsDropdown}
                              />
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="row">
                        <div className="col">
                          <div className="row">
                            <div className="col text-left">
                              <label
                                htmlFor="roomDropdown"
                                className="control-label"
                              >
                                Kabinetas
                              </label>
                            </div>
                          </div>
                          <div className="row">
                            <div className="col">
                              <RoomDropdown
                                dropdownValue={roomDropdownValue}
                                dropdownSetter={setRoomDropdownValue}
                                rooms={rooms}
                                setTeams={setTeams}
                                setSelectedRoom={setSelectedRoom}
                                resetLowerDropdowns={resetTeamsDropdown}
                                setMoreDetailButtonDisabled={
                                  setMoreDetailButtonDisabled
                                }
                              />
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="row" style={{ marginTop: "20px" }}>
                        <div className="col">
                          <button
                            className="btn btn-primary"
                            type="button"
                            disabled={isMoreDetailButtonDisabled}
                            onClick={showMoreDetailsComponent}
                          >
                            Daugiau informacijos
                          </button>
                        </div>
                      </div>
                    </div>
                    <div className="col-8">
                      <div className="row justify-content-center">
                        <h1>{getSelectedLocationPath()}</h1>
                      </div>
                      <div className="row h-100 justify-content-center">
                        <div className="col my-auto">
                          <h1>Pasirinkite biurą ir aukštą...</h1>
                        </div>
                      </div>
                    </div>
                  </div>
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

export default OverviewPage;
