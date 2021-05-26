import { useState } from "react";
import { useHistory } from "react-router-dom";

const IncomingTransfersModal = (props) => {
  const [confirmationHappened, setConfirmationHappened] = useState(false);
  const [suggestionId, setSuggestionId] = useState("");
  const [suggestionSelected, setSuggestionSelected] = useState(false);
  const [selectedSuggestion, setSelectedSuggestion] = useState({});
  const history = useHistory();

  const confirmTransfer = () => {
    console.log("suggestionId: " + suggestionId);
    console.log("companyId: " + props.companyId);
    console.log("correlationId: " + props.correlationId);
    props.confirmTransfer(suggestionId, props.companyId, props.correlationId);
    setConfirmationHappened(true);
  };

  const handleOkay = () => {
    if (props.confirmationState === "success") {
      setConfirmationHappened(false);
      props.setConfirmationState("");
      props.onHide();
      history.push("/overview");
    } else {
      setConfirmationHappened(false);
    }
  };

  const updateTransfers = () => {
    props.updateTransfers(props.correlationId);
  };

  const getMoveFrom = (moveTo) => {
    return moveTo.split("-->")[0].split("_").join(" > ");
  };

  const getMoveTo = (moveTo) => {
    return moveTo.split("-->")[1].split("_").join(" > ");
  };

  const handleSuggestionSelect = (suggestion) => {
    setSuggestionId(suggestion.id);
    setSelectedSuggestion(suggestion);
    setSuggestionSelected(true);
    console.log(suggestionId);
  };

  return (
    <div
      id="modal-incoming-transfer"
      className={`modal ${props.show ? "in" : ""}`}
      tabIndex="-1"
      role="dialog"
      aria-labelledby="modal-title"
      aria-hidden="true"
    >
      <div className="modal-dialog" role="document" style={{ width: "800px" }}>
        <div className="modal-content" style={{ border: "none" }}>
          <div className="modal-header" style={{ border: "none" }}>
            <div className="row w-100 no-gutters">
              <div className="col text-center">
                {confirmationHappened ? (
                  <div></div>
                ) : (
                  <h4 id="modal-title" className="modal-title">
                    Patvirtinti persodinimo pasiūlymą
                  </h4>
                )}
              </div>
            </div>
          </div>
          <div className="modal-body">
            <div className="row w-100 no-gutters">
              <div className="col">
                {confirmationHappened ? (
                  props.confirmationState === "success" ? (
                    <div className="alert alert-success" role="alert">
                      <strong>Užklausa atlikta sėkmingai!</strong> Planavimas
                      baigtas ir komandos persodintos.
                    </div>
                  ) : props.confirmationState === "failed" ? (
                    <div className="alert alert-danger" role="alert">
                      <strong>Užklausos atlikti nepavyko!</strong> Pakeiskite
                      užklausą ir pamėginkite dar kartą.
                    </div>
                  ) : (
                    <div></div>
                  )
                ) : (
                  <div className="row">
                    <div className="col">
                      <div className="row w-100 no-gutters">
                        {suggestionSelected ? (
                          <div className="col">
                            <h3 style={{ marginBottom: "15px" }}>
                              Pasiūlymo ID: {selectedSuggestion.id}
                            </h3>
                            <div className="row">
                              {selectedSuggestion.transfers.map((transfer) => {
                                return (
                                  <div
                                    className="col text-left"
                                    key={transfer.teamName}
                                  >
                                    {console.log(transfer)}
                                    <h4>Komanda: {transfer.teamName}</h4>
                                    <h4>Iš: {getMoveFrom(transfer.moveTo)}</h4>
                                    <h4>Į: {getMoveTo(transfer.moveTo)}</h4>
                                    <h4>
                                      Perkeltų darbuotojų kiekis:{" "}
                                      {transfer.peopleTransferredAmount}
                                    </h4>
                                  </div>
                                );
                              })}
                            </div>
                          </div>
                        ) : (
                          <div className="col text-left scrollable-expanded-list">
                            {props.suggestions.length > 0 ? (
                              props.suggestions.map((suggestion) => (
                                <div
                                  className="suggestion-item"
                                  key={suggestion.id}
                                  style={{ margin: "20px 0px 20px 0px" }}
                                  onClick={() =>
                                    handleSuggestionSelect(suggestion)
                                  }
                                >
                                  <h3 style={{ marginBottom: "5px" }}>
                                    Pasiūlymo ID: {suggestion.id}
                                  </h3>
                                  <div className="row">
                                    {suggestion.transfers.map((transfer) => {
                                      return (
                                        <div
                                          className="col"
                                          key={transfer.teamName}
                                        >
                                          <h4>Komanda: {transfer.teamName}</h4>
                                          <h4>
                                            Iš: {getMoveFrom(transfer.moveTo)}
                                          </h4>
                                          <h4>
                                            Į: {getMoveTo(transfer.moveTo)}
                                          </h4>
                                          <h4>
                                            Perkeltų darbuotojų kiekis:{" "}
                                            {transfer.peopleTransferredAmount}
                                          </h4>
                                        </div>
                                      );
                                    })}
                                  </div>
                                </div>
                              ))
                            ) : (
                              <h4></h4>
                            )}
                          </div>
                        )}
                      </div>
                      <div className="row">
                        {suggestionSelected ? (
                          <div></div>
                        ) : (
                          <div
                            className="col text-left"
                            style={{ marginTop: "30px" }}
                          >
                            <button
                              type="button"
                              className="btn btn-default"
                              onClick={updateTransfers}
                            >
                              Atnaujinti
                            </button>
                            <h4>Baigta: {props.status}</h4>
                          </div>
                        )}
                      </div>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
          <div
            className="modal-footer"
            style={{ border: "none", paddingBottom: "10px" }}
          >
            {confirmationHappened ? (
              <div className="row w-100 no-gutters">
                <div className="col">
                  <button
                    type="button"
                    className="btn btn-default"
                    data-toggle="modal"
                    data-target="modal-incoming-transfer"
                    onClick={() => handleOkay()}
                  >
                    Gerai
                  </button>
                </div>
              </div>
            ) : (
              <div className="row w-100 no-gutters">
                {suggestionSelected ? (
                  <div className="row w-100">
                    <div className="col">
                      <button
                        type="button"
                        className="btn btn-default"
                        data-toggle="modal"
                        data-target="modal-incoming-transfer"
                        onClick={() => setSuggestionSelected(false)}
                      >
                        Atgal
                      </button>
                    </div>
                    <div className="col">
                      <button
                        type="button"
                        className="btn btn-primary"
                        data-toggle="modal"
                        data-target="modal-incoming-transfer"
                        onClick={confirmTransfer}
                      >
                        Patvirtinti
                      </button>
                    </div>
                  </div>
                ) : (
                  <div className="col">
                    <button
                      type="button"
                      className="btn btn-default"
                      data-toggle="modal"
                      data-target="modal-incoming-transfer"
                      onClick={props.onHide}
                    >
                      Atšaukti
                    </button>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default IncomingTransfersModal;
