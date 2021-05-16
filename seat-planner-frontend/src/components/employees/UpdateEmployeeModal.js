import { useEffect, useState } from "react";

const UpdateEmployeeModal = (props) => {
  const [inputs, setInputs] = useState({
    firstName: props.employeeData.firstName,
    lastName: props.employeeData.lastName,
    email: props.employeeData.email,
    stack: props.employeeData.stack,
    experience: props.employeeData.experience,
  });

  const updateSelectedEmployee = (event) => {
    console.log(inputs);
  };

  const handleCancel = () => {
    setInputs({
      firstName: props.employeeData.firstName,
      lastName: props.employeeData.lastName,
      email: props.employeeData.email,
      stack: props.employeeData.stack,
      experience: props.employeeData.experience,
    });
    console.log(inputs);
    console.log(props.employeeData.firstName);
    console.log(props.employeeData.lastName);
    console.log(props.employeeData.email);
    console.log(props.employeeData.stack);
    console.log(props.employeeData.experience);
    props.onHide();
  };

  const handleInputChange = (event) => {
    event.persist();
    setInputs((inputs) => ({
      ...inputs,
      [event.target.name]: event.target.value,
    }));
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
                <h4 id="modal-title" className="modal-title">
                  Atnaujinti darbuotojo duomenis
                </h4>
              </div>
            </div>
          </div>
          <div className="modal-body">
            <div className="row w-100 no-gutters">
              <div className="col">
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
                        value={props.employeeData.firstName}
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
                        defaultValue={props.employeeData.lastName}
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
                        defaultValue={props.employeeData.email}
                        onChange={handleInputChange}
                        name="email"
                      />
                    </div>
                  </div>
                  <div className="form-group">
                    <label
                      htmlFor="stack"
                      className="col-sm-3 col-md-2 control-label"
                    >
                      Sritis
                    </label>
                    <div className="col-sm-9 col-md-10">
                      <input
                        type="text"
                        className="form-control"
                        id="form-stack"
                        required
                        defaultValue={props.employeeData.stack}
                        onChange={handleInputChange}
                        name="stack"
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
                        type="text"
                        className="form-control"
                        id="form-experience"
                        required
                        defaultValue={props.employeeData.experience}
                        onChange={handleInputChange}
                        name="experience"
                      />
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
          <div
            className="modal-footer"
            style={{ border: "none", paddingBottom: "10px" }}
          >
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
          </div>
        </div>
      </div>
    </div>
  );
};

export default UpdateEmployeeModal;
