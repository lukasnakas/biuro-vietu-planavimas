import { useState } from "react";

const DeleteFloorModal = (props) => {
  const [deleteHappened, setDeleteHappened] = useState(false);

  const deleteSelectedFloor = (event) => {
    props.deleteFloor(
      props.selectedFloorData.id,
      props.companyId,
      props.selectedOfficeId
    );
    setDeleteHappened(true);
  };

  const handleOkay = () => {
    if (props.deleteState === "success") {
      setDeleteHappened(false);
      props.setDeleteState("");
      props.fetchFloorData(props.selectedOfficeId);
      props.onHide();
    } else {
      setDeleteHappened(false);
    }
  };

  return (
    <div
      id="modal-floor-delete"
      className={`modal ${props.show ? "in" : ""}`}
      tabIndex="-1"
      role="dialog"
      aria-labelledby="modal-title"
      aria-hidden="true"
    >
      <div className="modal-dialog" role="document">
        <div className="modal-content" style={{ border: "none" }}>
          <div className="modal-header" style={{ border: "none" }}>
            <div className="row w-100 no-gutters">
              <div className="col text-center">
                {deleteHappened ? (
                  <div></div>
                ) : (
                  <h4 id="modal-title" className="modal-title">
                    Ištrinti aukšto duomenis
                  </h4>
                )}
              </div>
            </div>
          </div>
          <div className="modal-body">
            <div className="row w-100 no-gutters">
              <div className="col">
                {deleteHappened ? (
                  props.deleteState === "success" ? (
                    <div className="alert alert-success" role="alert">
                      <strong>Užklausa atlikta sėkmingai!</strong> Aukšto
                      duomenys pašalinti.
                    </div>
                  ) : props.deleteState === "failed" ? (
                    <div className="alert alert-danger" role="alert">
                      <strong>Užklausos atlikti nepavyko!</strong> Aukšto
                      duomenys nebuvo pašalinti.
                    </div>
                  ) : (
                    <div></div>
                  )
                ) : (
                  <h3>Ar tikrai norite ištrinti šio aukšto duomenis?</h3>
                )}
              </div>
            </div>
          </div>
          <div
            className="modal-footer"
            style={{ border: "none", paddingBottom: "10px" }}
          >
            {deleteHappened ? (
              <div className="row w-100 no-gutters">
                <div className="col">
                  <button
                    type="button"
                    className="btn btn-default"
                    data-toggle="modal"
                    data-target="modal-floor-update"
                    onClick={() => handleOkay()}
                  >
                    Gerai
                  </button>
                </div>
              </div>
            ) : (
              <div className="row w-100 no-gutters">
                <div className="col">
                  <button
                    type="button"
                    className="btn btn-default"
                    data-toggle="modal"
                    data-target="modal-floor-delete"
                    onClick={() => props.onHide()}
                  >
                    Atšaukti
                  </button>
                </div>
                <div className="col">
                  <button
                    type="button"
                    className="btn btn-primary"
                    data-toggle="modal"
                    data-target="modal-floor-delete"
                    onClick={deleteSelectedFloor}
                  >
                    Patvirtinti
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DeleteFloorModal;
