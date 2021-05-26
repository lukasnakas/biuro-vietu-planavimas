import { useState } from "react";
import TeamDropdown from "../dropdown/TeamDropdown";

const UpdateEmployeeModal = (props) => {
  const [updateHappened, setUpdateHappened] = useState(false);
  const [selectedDropdownTeam, setSelectedDropdownTeam] = useState({});

  const updateSelectedEmployee = (event) => {
    let request = props.inputs;
    if (
      selectedDropdownTeam.name === null ||
      selectedDropdownTeam.name === undefined
    ) {
      request.teamName = props.teamDropdownValue;
    } else {
      request.teamName = selectedDropdownTeam.name;
    }
    props.updateEmployee(request);
    setUpdateHappened(true);
  };

  const handleCancel = () => {
    props.setInputs({
      companyId: props.companyId,
      id: props.selectedEmployeeData.id,
      firstName: props.selectedEmployeeData.firstName,
      lastName: props.selectedEmployeeData.lastName,
      email: props.selectedEmployeeData.email,
      stack: props.selectedEmployeeData.stack,
      experience: props.selectedEmployeeData.experience,
      teamName: selectedDropdownTeam.name,
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
      props.fetchEmployeeData();
      handleCancel();
    } else {
      setUpdateHappened(false);
    }
  };

  return (
    <div
      id="modal-employee-update"
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
                    Atnaujinti darbuotojo duomenis
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
                      <strong>Užklausa atlikta sėkmingai!</strong> Darbuotojo
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
                        htmlFor="firstName"
                        className="col-sm-3 col-md-2 control-label"
                      >
                        Vardas
                      </label>
                      <div className="col-sm-9 col-md-10">
                        <input
                          type="text"
                          className="form-control"
                          id="form-firstName"
                          required
                          value={props.inputs.firstName}
                          onChange={handleInputChange}
                          name="firstName"
                        />
                      </div>
                    </div>
                    <div className="form-group">
                      <label
                        htmlFor="lastName"
                        className="col-sm-3 col-md-2 control-label"
                      >
                        Pavardė
                      </label>
                      <div className="col-sm-9 col-md-10">
                        <input
                          type="text"
                          className="form-control"
                          id="form-lastName"
                          required
                          value={props.inputs.lastName}
                          onChange={handleInputChange}
                          name="lastName"
                        />
                      </div>
                    </div>
                    <div className="form-group">
                      <label
                        htmlFor="email"
                        className="col-sm-3 col-md-2 control-label"
                      >
                        El. paštas
                      </label>
                      <div className="col-sm-9 col-md-10">
                        <input
                          type="email"
                          className="form-control"
                          id="form-email"
                          required
                          value={props.inputs.email}
                          onChange={handleInputChange}
                          name="email"
                        />
                      </div>
                    </div>
                    <div className="form-group">
                      <label
                        htmlFor="experience"
                        className="col-sm-3 col-md-2 control-label"
                      >
                        Patirtis
                      </label>
                      <div className="col-sm-9 col-md-10">
                        <input
                          type="number"
                          className="form-control"
                          id="form-experience"
                          required
                          value={props.inputs.experience}
                          onChange={handleInputChange}
                          name="experience"
                        />
                      </div>
                    </div>
                    <div className="form-group">
                      <label
                        htmlFor="teamName"
                        className="col-sm-3 col-md-2 control-label"
                      >
                        Komanda
                      </label>
                      <div className="col-sm-9 col-md-10">
                        <TeamDropdown
                          dropdownValue={props.teamDropdownValue}
                          dropdownSetter={props.setTeamDropdownValue}
                          teams={props.teamData}
                          setSelectedTeam={setSelectedDropdownTeam}
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
                    data-target="modal-employee-update"
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
                    data-target="modal-employee-update"
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
                    data-target="modal-employee-update"
                    onClick={updateSelectedEmployee}
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

export default UpdateEmployeeModal;
