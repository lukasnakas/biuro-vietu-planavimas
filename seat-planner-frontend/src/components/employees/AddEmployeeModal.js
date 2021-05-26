import { useState } from "react";
import TeamDropdown from "../dropdown/TeamDropdown";

const AddEmployeeModal = (props) => {
  const [additionHappened, setAdditionHappened] = useState(false);
  const [inputs, setInputs] = useState({});
  const [selectedDropdownTeam, setSelectedDropdownTeam] = useState({});

  const addEmployee = () => {
    let request = {};
    if (props.teamless) {
      request = {
        companyId: props.companyId,
        firstName: inputs.firstName,
        lastName: inputs.lastName,
        email: inputs.email,
        experience: inputs.experience,
        teamless: true,
      };
    } else {
      request = {
        companyId: props.companyId,
        firstName: inputs.firstName,
        lastName: inputs.lastName,
        email: inputs.email,
        experience: inputs.experience,
        teamName: selectedDropdownTeam.name,
        teamless: false,
      };
    }

    props.addEmployee(request);
    setAdditionHappened(true);
  };

  const resetValues = () => {
    setInputs({
      firstName: "",
      lastName: "",
      email: "",
      experience: 0,
    });
    props.setTeamless(false);
  };

  const handleInputChange = (event) => {
    event.persist();
    setInputs((inputs) => ({
      ...inputs,
      [event.target.name]: event.target.value,
    }));
  };

  const handleOkay = () => {
    if (props.additionState === "success") {
      setAdditionHappened(false);
      props.setAdditionState("");
      props.fetchEmployeeData();
      resetValues();
      props.onHide();
    } else {
      setAdditionHappened(false);
    }
  };

  const handleCheckboxChange = (event) => {
    if (event.target.id === "form-no-team-flag") {
      if (event.target.checked) {
        props.setTeamless(true);
      } else {
        props.setTeamless(false);
      }
    }
  };

  return (
    <div
      id="modal-employee-add"
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
                {additionHappened ? (
                  <div></div>
                ) : (
                  <h4 id="modal-title" className="modal-title">
                    Pridėti naują darbuotoją
                  </h4>
                )}
              </div>
            </div>
          </div>
          <div className="modal-body" style={{ paddingBottom: "10px" }}>
            <div className="row w-100 no-gutters">
              <div className="col">
                {additionHappened ? (
                  props.additionState === "success" ? (
                    <div className="alert alert-success" role="alert">
                      <strong>Užklausa atlikta sėkmingai!</strong> Naujas
                      darbuotojas pridėtas.
                    </div>
                  ) : props.additionState === "failed" ? (
                    <div className="alert alert-danger" role="alert">
                      <strong>Užklausos atlikti nepavyko!</strong> Pakeiskite
                      užklausą ir pamėginkite dar kartą.
                    </div>
                  ) : (
                    <div></div>
                  )
                ) : (
                  <form className="form-horizontal" onSubmit={addEmployee}>
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
                          value={inputs.firstName}
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
                          value={inputs.lastName}
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
                          value={inputs.email}
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
                          value={inputs.experience}
                          onChange={handleInputChange}
                          name="experience"
                        />
                      </div>
                    </div>
                    <div className="row" style={{ marginBottom: "10px" }}>
                      <div className="col text-left offset-2">
                        <div className="checkbox">
                          <input
                            type="checkbox"
                            className="form-control"
                            name="no-team-flag"
                            onChange={handleCheckboxChange}
                            id="form-no-team-flag"
                            defaultChecked={false}
                          />
                          <label
                            htmlFor="form-no-team-flag"
                            className="control-label"
                          >
                            Nepriskirti į komandą
                          </label>
                        </div>
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
                          isDisabled={props.teamless}
                        />
                      </div>
                    </div>
                    <div
                      className="row w-100 no-gutters"
                      style={{ paddingTop: "20px" }}
                    >
                      <div className="col">
                        <button
                          type="button"
                          className="btn btn-default"
                          data-toggle="modal"
                          data-target="modal-employee-add"
                          onClick={() => {
                            props.onHide();
                            resetValues();
                          }}
                        >
                          Atšaukti
                        </button>
                      </div>
                      <div className="col">
                        <button
                          type="submit"
                          className="btn btn-primary"
                          data-toggle="modal"
                          data-target="modal-employee-add"
                        >
                          Pridėti
                        </button>
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
            {additionHappened ? (
              <div className="row w-100 no-gutters">
                <div className="col">
                  <button
                    type="button"
                    className="btn btn-default"
                    data-toggle="modal"
                    data-target="modal-employee-add"
                    onClick={() => handleOkay()}
                  >
                    Gerai
                  </button>
                </div>
              </div>
            ) : (
              <div></div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddEmployeeModal;
