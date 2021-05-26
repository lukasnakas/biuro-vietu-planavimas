import AddFloorModal from "./floor-modals/AddFloorModal";
import UpdateFloorModal from "./floor-modals/UpdateFloorModal";
import DeleteFloorModal from "./floor-modals/DeleteFloorModal";
import { useState } from "react";

const EditableFloorListComponent = (props) => {
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAdditionModal, setShowAdditionModal] = useState(false);
  const [inputs, setInputs] = useState({});

  const updateSelectedFloor = (floor) => {
    props.setSelectedFloor(floor);
    setInputs({
      companyId: props.companyId,
      officeId: props.selectedOfficeId,
      id: floor.id,
      name: floor.name,
    });
    setShowUpdateModal(true);
  };

  const deleteSelectedFloor = (floor) => {
    props.setSelectedFloor(floor);
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
            <AddFloorModal
              show={showAdditionModal}
              onHide={() => setShowAdditionModal(false)}
              addFloor={props.addFloor}
              companyId={props.companyId}
              additionState={props.additionState}
              setAdditionState={props.setAdditionState}
              floorData={props.floorData}
              setFloorData={props.setFloorData}
              fetchFloorData={props.fetchFloorData}
              selectedOfficeId={props.selectedOfficeId}
            />
            <UpdateFloorModal
              show={showUpdateModal}
              onHide={() => setShowUpdateModal(false)}
              selectedFloorData={props.selectedFloor}
              updateFloor={props.updateFloor}
              setInputs={setInputs}
              inputs={inputs}
              companyId={props.companyId}
              updateState={props.updateState}
              setUpdateState={props.setUpdateState}
              floorData={props.floorData}
              setFloorData={props.setFloorData}
              fetchFloorData={props.fetchFloorData}
              selectedOfficeId={props.selectedOfficeId}
            />
            <DeleteFloorModal
              show={showDeleteModal}
              onHide={() => setShowDeleteModal(false)}
              selectedFloorData={props.selectedFloor}
              deleteFloor={props.deleteFloor}
              companyId={props.companyId}
              deleteState={props.deleteState}
              setDeleteState={props.setDeleteState}
              floorData={props.floorData}
              setFloorData={props.setFloorData}
              fetchFloorData={props.fetchFloorData}
              selectedOfficeId={props.selectedOfficeId}
            />
            {props.selectedOfficeId !== "" ? (
              props.itemList.length ? (
                <div className=" scrollable-list">
                  <table className="table table-hover">
                    <thead>
                      <tr>
                        <th style={{ textAlign: "center" }}>Pavadinimas</th>
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
                            key={item.floorName}
                            onClick={() => {
                              props.setTeamData([]);
                              props.fetchRoomData(
                                props.selectedOfficeId,
                                item.id
                              );
                              props.setSelectedFloorId(item.id);
                            }}
                          >
                            {console.log(props.floorOfficeId)}
                            <td>{item.floorName}</td>
                            <td>
                              <div className="row justify-content-between">
                                <div className="col text-center">
                                  <button
                                    className="btn"
                                    onClick={() => {
                                      updateSelectedFloor(item);
                                    }}
                                  >
                                    Keisti
                                  </button>
                                </div>
                                <div className="col text-center">
                                  <button
                                    className="btn"
                                    onClick={() => {
                                      deleteSelectedFloor(item);
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
                <h4>Nepavyko gauti duomenų apie aukštus</h4>
              )
            ) : (
              <h4>Pasirinkite biurą..</h4>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditableFloorListComponent;
