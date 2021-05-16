import { useState } from "react";

const RoomDetailsComponent = (props) => {
  const [selectedEmployee, setSelectedEmployee] = useState({});
  const [selectedTeam, setSelectedTeam] = useState({});

  const decorateTeamName = (teamName) => {
    return teamName.split("_")[0];
  };

  return (
    <div className="row">
      <div className="col">
        <div className="row">
          <div className="col">
            <h1>
              <strong>{props.selectedLocationPath}</strong>
            </h1>
          </div>
        </div>
        <div className="row justify-content-start">
          <div className="col text-left">
            <h2 style={{ marginBottom: "0px" }}>Komandos</h2>
            <div
              className="panel panel-default"
              style={{
                paddingTop: "0px",
                paddingLeft: "10px",
                paddingBottom: "10px",
              }}
            >
              <div
                id="collapseOne"
                className="panel-collapse in"
                role="tabpanel"
                aria-expanded="true"
              >
                <div
                  className="panel-body"
                  style={{ padding: "0px 30px 5px 30px" }}
                >
                  {props.teams.length ? (
                    props.teams.map((team) => (
                      <div className="panel panel-default">
                        <div
                          className="panel-heading"
                          role="tab"
                          id={"headingOne" + team.id}
                        >
                          <h4 className="panel-title">
                            <a
                              data-toggle="collapse"
                              data-parent="#collapseOne"
                              href={"#collapseOne" + team.id}
                              aria-expanded="false"
                              aria-controls={"collapseOne" + team.id}
                              class="collapsed"
                              style={{ textDecoration: "none" }}
                            >
                              <span class="caret"></span>
                              {decorateTeamName(team.name)}
                            </a>
                          </h4>
                        </div>
                        <div
                          id={"collapseOne" + team.id}
                          class="panel-collapse collapse"
                          role="tabpanel"
                          aria-labelledby={"headingOne" + team.id}
                          aria-expanded="false"
                          aria-hidden="true"
                        >
                          <div class="panel-body">
                            <div
                              className="col"
                              style={{ padding: "5px 30px" }}
                            >
                              {team.members.length ? (
                                team.members.map((member) => (
                                  // add class to style hovering and change mouse pointer to hand
                                  <div
                                    style={{ cursor: "pointer" }}
                                    onClick={() => {
                                      setSelectedEmployee(member);
                                      setSelectedTeam(team);
                                    }}
                                  >
                                    <h4>
                                      {member.firstName + " " + member.lastName}
                                    </h4>
                                  </div>
                                ))
                              ) : (
                                <h4>Nerasta darbuotojų duomenų</h4>
                              )}
                            </div>
                          </div>
                        </div>
                      </div>
                    ))
                  ) : (
                    <h4>Nerasta komandų duomenų</h4>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="row">
          <div className="col text-left">
            <button
              type="button"
              className="btn btn-default"
              onClick={props.onBack}
            >
              Atgal
            </button>
          </div>
        </div>
      </div>
      <div className="col">
        <h1>
          <strong>Darbuotojo informacija</strong>
        </h1>
        {Object.keys(selectedEmployee).length > 0 ? (
          <div>
            <div className="row">
              <div className="col-3 text-left">
                <h2>Vardas: </h2>
              </div>
              <div className="col-9 text-left">
                <h2>{selectedEmployee.firstName}</h2>
              </div>
            </div>
            <div className="row">
              <div className="col-3 text-left">
                <h2>Pavardė: </h2>
              </div>
              <div className="col-9 text-left">
                <h2>{selectedEmployee.lastName}</h2>
              </div>
            </div>
            <div className="row">
              <div className="col-3 text-left">
                <h2>Projektas: </h2>
              </div>
              <div className="col-9 text-left">
                <h2>{decorateTeamName(selectedTeam.name)}</h2>
              </div>
            </div>
            <div className="row">
              <div className="col-3 text-left">
                <h2>Sritis: </h2>
              </div>
              <div className="col-9 text-left">
                <h2>
                  {selectedTeam.stack !== "" &&
                  selectedTeam.stack !== null &&
                  selectedTeam.stack !== undefined
                    ? selectedTeam.stack
                    : "-"}
                </h2>
              </div>
            </div>
            <div className="row">
              <div className="col-3 text-left">
                <h2>Patirtis: </h2>
              </div>
              <div className="col-9 text-left">
                <h2>0 m.</h2>
              </div>
            </div>
          </div>
        ) : (
          <div></div>
        )}
      </div>
    </div>
  );
};

export default RoomDetailsComponent;
