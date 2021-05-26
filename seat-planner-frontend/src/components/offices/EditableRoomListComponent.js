import { useState } from "react";
import AddRoomModal from "./room-modals/AddRoomModal";
import UpdateRoomModal from "./room-modals/UpdateRoomModal";
import DeleteRoomModal from "./room-modals/DeleteRoomModal";

const EditableRoomListComponent = (props) => {
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAdditionModal, setShowAdditionModal] = useState(false);
  const [inputs, setInputs] = useState({});

  const updateSelectedRoom = (room) => {
    props.setSelectedRoom(room);
    setInputs({
      companyId: props.companyId,
      officeId: props.selectedOfficeId,
      floorId: props.selectedFloorId,
      id: room.id,
      name: room.name,
    });
    setShowUpdateModal(true);
  };

  const deleteSelectedRoom = (room) => {
    props.setSelectedRoom(room);
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
            <AddRoomModal
              show={showAdditionModal}
              onHide={() => setShowAdditionModal(false)}
              addRoom={props.addRoom}
              companyId={props.companyId}
              additionState={props.additionState}
              setAdditionState={props.setAdditionState}
              roomData={props.roomData}
              setRoomData={props.setRoomData}
              fetchRoomData={props.fetchRoomData}
              selectedOfficeId={props.selectedOfficeId}
              selectedFloorId={props.selectedFloorId}
            />
            <UpdateRoomModal
              show={showUpdateModal}
              onHide={() => setShowUpdateModal(false)}
              selectedRoomData={props.selectedRoom}
              updateRoom={props.updateRoom}
              setInputs={setInputs}
              inputs={inputs}
              companyId={props.companyId}
              updateState={props.updateState}
              setUpdateState={props.setUpdateState}
              roomData={props.roomData}
              setRoomData={props.setRoomData}
              fetchRoomData={props.fetchRoomData}
              selectedOfficeId={props.selectedOfficeId}
              selectedFloorId={props.selectedFloorId}
            />
            <DeleteRoomModal
              show={showDeleteModal}
              onHide={() => setShowDeleteModal(false)}
              selectedRoomData={props.selectedRoom}
              deleteRoom={props.deleteRoom}
              companyId={props.companyId}
              deleteState={props.deleteState}
              setDeleteState={props.setDeleteState}
              roomData={props.roomData}
              setRoomData={props.setRoomData}
              fetchRoomData={props.fetchRoomData}
              selectedOfficeId={props.selectedOfficeId}
              selectedFloorId={props.selectedFloorId}
            />
            {props.selectedFloorId !== "" ? (
              props.itemList.length ? (
                <div className="scrollable-list">
                  <table className="table table-hover">
                    <thead>
                      <tr>
                        <th style={{ textAlign: "center" }}>Pavadinimas</th>
                        <th style={{ textAlign: "center" }}>
                          Didžiausia talpa
                        </th>
                        <th style={{ textAlign: "center" }}>
                          Valdymo įrankiai
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {props.itemList
                        .filter((item) => item !== null && item !== undefined)
                        .map((item) => (
                          <tr
                            key={item.location.roomNumb}
                            onClick={() => {
                              props.fetchTeamData(
                                props.selectedOfficeId,
                                props.selectedFloorId,
                                item.id
                              );
                              props.setSelectedRoomId(item.id);
                            }}
                          >
                            <td>{item.location.roomNumb}</td>
                            <td>{item.maxCapacity}</td>
                            <td>
                              <div className="row justify-content-between">
                                <div className="col text-center">
                                  <button
                                    className="btn"
                                    onClick={() => {
                                      updateSelectedRoom(item);
                                    }}
                                  >
                                    Keisti
                                  </button>
                                </div>
                                <div className="col text-center">
                                  <button
                                    className="btn"
                                    onClick={() => {
                                      deleteSelectedRoom(item);
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
                <h4>Nepavyko gauti duomenų apie kabinetus</h4>
              )
            ) : (
              <h4>Pasirinkite aukštą..</h4>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditableRoomListComponent;
