import { useState } from "react";
import AddTeamModal from "./team-modals/AddTeamModal";
import UpdateTeamModal from "./team-modals/UpdateTeamModal";
import DeleteTeamModal from "./team-modals/DeleteTeamModal";

const EditableTeamListComponent = (props) => {
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAdditionModal, setShowAdditionModal] = useState(false);
  const [inputs, setInputs] = useState({});

  const updateSelectedTeam = (team) => {
    props.setSelectedTeam(team);
    setInputs({
      companyId: props.companyId,
      officeId: props.selectedOfficeId,
      floorId: props.selectedFloorId,
      roomId: props.selectedRoomId,
      id: team.id,
      name: team.name,
      stack: team.stack,
    });
    setShowUpdateModal(true);
  };

  const deleteSelectedTeam = (team) => {
    props.setSelectedTeam(team);
    setShowDeleteModal(true);
  };

  return (
    <div className="row">
      <div className="col">
        <div className="row">
          <div className="col text-left">
            <h3>{props.listName}</h3>
          </div>
          <div className="col text-right">
            <button
              type="button"
              className="btn btn-primary"
              onClick={() => setShowAdditionModal(true)}
            >
              Pridėti
            </button>
          </div>
        </div>
        <div className="row">
          <div className="col">
            <AddTeamModal
              show={showAdditionModal}
              onHide={() => setShowAdditionModal(false)}
              addTeam={props.addTeam}
              companyId={props.companyId}
              additionState={props.additionState}
              setAdditionState={props.setAdditionState}
              teamData={props.teamData}
              setTeamData={props.setTeamData}
              fetchTeamData={props.fetchTeamData}
              selectedOfficeId={props.selectedOfficeId}
              selectedFloorId={props.selectedFloorId}
              selectedRoomId={props.selectedRoomId}
            />
            <UpdateTeamModal
              show={showUpdateModal}
              onHide={() => setShowUpdateModal(false)}
              selectedTeamData={props.selectedTeam}
              updateTeam={props.updateTeam}
              setInputs={setInputs}
              inputs={inputs}
              companyId={props.companyId}
              updateState={props.updateState}
              setUpdateState={props.setUpdateState}
              teamData={props.teamData}
              setTeamData={props.setTeamData}
              fetchTeamData={props.fetchTeamData}
              selectedOfficeId={props.selectedOfficeId}
              selectedFloorId={props.selectedFloorId}
              selectedRoomId={props.selectedRoomId}
            />
            <DeleteTeamModal
              show={showDeleteModal}
              onHide={() => setShowDeleteModal(false)}
              selectedTeamData={props.selectedTeam}
              deleteTeam={props.deleteTeam}
              companyId={props.companyId}
              deleteState={props.deleteState}
              setDeleteState={props.setDeleteState}
              teamData={props.teamData}
              setTeamData={props.setTeamData}
              fetchTeamData={props.fetchTeamData}
              selectedOfficeId={props.selectedOfficeId}
              selectedFloorId={props.selectedFloorId}
              selectedRoomId={props.selectedRoomId}
            />
            {props.selectedRoomId !== "" ? (
              props.itemList.length ? (
                <div className="scrollable-list">
                  <table className="table table-hover">
                    <thead>
                      <tr>
                        <th style={{ textAlign: "center" }}>Pavadinimas</th>
                        <th style={{ textAlign: "center" }}>Sritis</th>
                        <th style={{ textAlign: "center" }}>
                          Valdymo įrankiai
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {props.itemList
                        .filter((item) => item !== null && item !== undefined)
                        .map((item) => (
                          <tr key={item.name}>
                            <td>{item.name}</td>
                            <td>{item.stack}</td>
                            <td>
                              <div className="row justify-content-between">
                                <div className="col text-center">
                                  <button
                                    className="btn"
                                    onClick={() => {
                                      updateSelectedTeam(item);
                                    }}
                                  >
                                    Keisti
                                  </button>
                                </div>
                                <div className="col text-center">
                                  <button
                                    className="btn"
                                    onClick={() => {
                                      deleteSelectedTeam(item);
                                    }}
                                  >
                                    Ištrinti
                                  </button>
                                </div>
                              </div>
                            </td>
                          </tr>
                        ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <h4>Nepavyko gauti duomenų apie komandas</h4>
              )
            ) : (
              <h4>Pasirinkite kabinetą..</h4>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditableTeamListComponent;
