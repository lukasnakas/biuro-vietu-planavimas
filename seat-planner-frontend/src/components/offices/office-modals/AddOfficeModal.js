import { useState } from "react";

const AddOfficeModal = (props) => {
  const [additionHappened, setAdditionHappened] = useState(false);
  const [inputs, setInputs] = useState({});

  const addOffice = () => {
    let request = { companyId: props.companyId, name: inputs.name };
    console.log(request);
    props.addOffice(request);
    setAdditionHappened(true);
  };

  const resetValues = () => {
    setInputs({
      name: "",
    });
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
      props.fetchOfficeData();
      resetValues();
      props.onHide();
    } else {
      setAdditionHappened(false);
    }
  };

  return (
    <div
      id="modal-office-add"
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
                    Pridėti naują biurą
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
                      <strong>Užklausa atlikta sėkmingai!</strong> Naujas biuras
                      pridėtas.
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
                  <form className="form-horizontal" onSubmit={addOffice}>
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
                          value={inputs.name}
                          onChange={handleInputChange}
                          name="name"
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
                          data-target="modal-office-add"
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
                          data-target="modal-office-add"
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
                    data-target="modal-office-add"
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

export default AddOfficeModal;
