import { useState } from "react";
import AddOfficeModal from "./office-modals/AddOfficeModal";
import UpdateOfficeModal from "./office-modals/UpdateOfficeModal";
import DeleteOfficeModal from "./office-modals/DeleteOfficeModal";

const EditableOfficeListComponent = (props) => {
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAdditionModal, setShowAdditionModal] = useState(false);
  const [inputs, setInputs] = useState({});

  const updateSelectedOffice = (office) => {
    props.setSelectedOffice(office);
    setInputs({
      companyId: props.companyId,
      id: office.id,
      name: office.name,
    });
    setShowUpdateModal(true);
  };

  const deleteSelectedOffice = (office) => {
    props.setSelectedOffice(office);
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
            <AddOfficeModal
              show={showAdditionModal}
              onHide={() => setShowAdditionModal(false)}
              addOffice={props.addOffice}
              companyId={props.companyId}
              additionState={props.additionState}
              setAdditionState={props.setAdditionState}
              officeData={props.officeData}
              setOfficeData={props.setOfficeData}
              fetchOfficeData={props.fetchOfficeData}
            />
            <UpdateOfficeModal
              show={showUpdateModal}
              onHide={() => setShowUpdateModal(false)}
              selectedOfficeData={props.selectedOffice}
              updateOffice={props.updateOffice}
              setInputs={setInputs}
              inputs={inputs}
              companyId={props.companyId}
              updateState={props.updateState}
              setUpdateState={props.setUpdateState}
              officeData={props.officeData}
              setOfficeData={props.setOfficeData}
              fetchOfficeData={props.fetchOfficeData}
            />
            <DeleteOfficeModal
              show={showDeleteModal}
              onHide={() => setShowDeleteModal(false)}
              selectedOfficeData={props.selectedOffice}
              deleteOffice={props.deleteOffice}
              companyId={props.companyId}
              deleteState={props.deleteState}
              setDeleteState={props.setDeleteState}
              officeData={props.officeData}
              setOfficeData={props.setOfficeData}
              fetchOfficeData={props.fetchOfficeData}
            />
            {props.itemList.length ? (
              <div className="scrollable-list">
                <table className="table table-hover">
                  <thead>
                    <tr>
                      <th style={{ textAlign: "center" }}>Pavadinimas</th>
                      <th style={{ textAlign: "center" }}>Valdymo įrankiai</th>
                    </tr>
                  </thead>
                  <tbody>
                    {props.itemList
                      .filter(
                        (item) =>
                          item !== null &&
                          item !== undefined &&
                          item.officeName !== "no_team"
                      )
                      .map((item) => (
                        <tr
                          key={item.officeName}
                          onClick={() => {
                            props.setTeamData([]);
                            props.setRoomData([]);
                            props.fetchFloorData(item.id);
                            props.setSelectedOfficeId(item.id);
                          }}
                        >
                          <td>{item.officeName}</td>
                          <td>
                            <div className="row justify-content-between">
                              <div className="col text-center">
                                <button
                                  className="btn"
                                  onClick={() => {
                                    updateSelectedOffice(item);
                                  }}
                                >
                                  Keisti
                                </button>
                              </div>
                              <div className="col text-center">
                                <button
                                  className="btn"
                                  onClick={() => {
                                    deleteSelectedOffice(item);
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
              <h4>Nepavyko gauti duomenų apie biurus</h4>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditableOfficeListComponent;
