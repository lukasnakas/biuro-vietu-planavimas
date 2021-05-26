import { useState } from "react";

const UpdateTeamModal = (props) => {
  const [updateHappened, setUpdateHappened] = useState(false);

  const updateSelectedTeam = (event) => {
    let request = props.inputs;
    props.updateTeam(request);
    setUpdateHappened(true);
  };

  const handleCancel = () => {
    props.setInputs({
      companyId: props.companyId,
      officeId: props.floorOfficeId,
      floorId: props.selectedFloorId,
      roomId: props.selectedRoomId,
      id: props.selectedTeamData.id,
      name: props.selectedTeamData.name,
      stack: props.selectedTeamData.stack,
    });
    props.onHide();
  };

  const handleInputChange = (event) => {
    event.persist();
    props.setInputs((inputs) => ({
      ...inputs,
      [event.target.name]: event.target.value,
    }));
  };

  const handleOkay = () => {
    if (props.updateState === "success") {
      setUpdateHappened(false);
      props.setUpdateState("");
      props.fetchTeamData(
        props.selectedOfficeId,
        props.selectedFloorId,
        props.selectedRoomId
      );
      handleCancel();
    } else {
      setUpdateHappened(false);
    }
  };

  return (
    <div
      id="modal-team-update"
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
                {updateHappened ? (
                  <div></div>
                ) : (
                  <h4 id="modal-title" className="modal-title">
                    Atnaujinti komandos duomenis
                  </h4>
                )}
              </div>
            </div>
          </div>
          <div className="modal-body">
            <div className="row w-100 no-gutters">
              <div className="col">
                {updateHappened ? (
                  props.updateState === "success" ? (
                    <div className="alert alert-success" role="alert">
                      <strong>Užklausa atlikta sėkmingai!</strong> Komandos
                      duomenys atnaujinti.
                    </div>
                  ) : props.updateState === "failed" ? (
                    <div className="alert alert-danger" role="alert">
                      <strong>Užklausos atlikti nepavyko!</strong> Pakeiskite
                      užklausą ir pamėginkite dar kartą.
                    </div>
                  ) : (
                    <div></div>
                  )
                ) : (
                  <form className="form-horizontal">
                    <div className="form-group">
                      <label
                        htmlFor="name"
                        className="col-sm-4 col-md-3 control-label"
                      >
                        Pavadinimas
                      </label>
                      <div className="col-sm-8 col-md-9">
                        <input
                          type="text"
                          className="form-control"
                          id="form-name"
                          required
                          value={props.inputs.name}
                          onChange={handleInputChange}
                          name="name"
                        />
                      </div>
                    </div>
                    <div className="form-group">
                      <label
                        htmlFor="stack"
                        className="col-sm-4 col-md-3 control-label"
                      >
                        Sritis
                      </label>
                      <div className="col-sm-8 col-md-9">
                        <input
                          type="text"
                          className="form-control"
                          id="form-stack"
                          value={props.inputs.stack}
                          onChange={handleInputChange}
                          name="stack"
                        />
                      </div>
                    </div>
                  </form>
                )}
              </div>
            </div>
          </div>
          <div
            className="modal-footer"
            style={{ border: "none", paddingBottom: "10px" }}
          >
            {updateHappened ? (
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
                    data-target="modal-team-update"
                    onClick={() => handleCancel()}
                  >
                    Atšaukti
                  </button>
                </div>
                <div className="col">
                  <button
                    type="button"
                    className="btn btn-primary"
                    data-toggle="modal"
                    data-target="modal-team-update"
                    onClick={updateSelectedTeam}
                  >
                    Atnaujinti
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

export default UpdateTeamModal;
