import { useState } from "react";

const DeleteTeamModal = (props) => {
  const [deleteHappened, setDeleteHappened] = useState(false);

  const deleteSelectedTeam = (event) => {
    props.deleteTeam(
      props.selectedTeamData.id,
      props.companyId,
      props.selectedOfficeId,
      props.selectedFloorId,
      props.selectedRoomId
    );
    setDeleteHappened(true);
  };

  const handleOkay = () => {
    if (props.deleteState === "success") {
      setDeleteHappened(false);
      props.setDeleteState("");
      props.fetchTeamData(
        props.selectedOfficeId,
        props.selectedFloorId,
        props.selectedRoomId
      );
      props.onHide();
    } else {
      setDeleteHappened(false);
    }
  };

  return (
    <div
      id="modal-team-delete"
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
                    Ištrinti komandos duomenis
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
                      <strong>Užklausa atlikta sėkmingai!</strong> Komandos
                      duomenys pašalinti.
                    </div>
                  ) : props.deleteState === "failed" ? (
                    <div className="alert alert-danger" role="alert">
                      <strong>Užklausos atlikti nepavyko!</strong> Komandos
                      duomenys nebuvo pašalinti.
                    </div>
                  ) : (
                    <div></div>
                  )
                ) : (
                  <h3>Ar tikrai norite ištrinti šios komandos duomenis?</h3>
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
                    data-target="modal-team-update"
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
                    data-target="modal-team-delete"
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
                    data-target="modal-team-delete"
                    onClick={deleteSelectedTeam}
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

export default DeleteTeamModal;
