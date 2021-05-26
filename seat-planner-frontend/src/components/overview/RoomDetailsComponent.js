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
        <div className="row justify-content-start">
          <div className="col text-left">
            <h2 style={{ marginBottom: "0px" }}>Komandos</h2>
            <div
              className="panel panel-default box-shadowless"
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
                  className="panel-body box-shadowless"
                  style={{ padding: "0px 30px 5px 30px" }}
                >
                  {props.teams.length ? (
                    props.teams.map((team) => (
                      <div className="panel panel-default box-shadowless">
                        <div
                          className="panel-heading box-shadowless"
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
                                      props.setShowMemberDetails(true);
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
      </div>
      <div className="col">
        {props.showMemberDetails ? (
          <div>
            <div>
              <h2>
                <strong>Darbuotojo informacija</strong>
              </h2>
            </div>
            <div style={{ marginBottom: "40px" }}>
              <div className="row">
                <div className="col-3 text-left">
                  <h3>Vardas: </h3>
                </div>
                <div className="col-9 text-left">
                  <h3>{selectedEmployee.firstName}</h3>
                </div>
              </div>
              <div className="row">
                <div className="col-3 text-left">
                  <h3>Pavardė: </h3>
                </div>
                <div className="col-9 text-left">
                  <h3>{selectedEmployee.lastName}</h3>
                </div>
              </div>
              <div className="row">
                <div className="col-3 text-left">
                  <h3>Projektas: </h3>
                </div>
                <div className="col-9 text-left">
                  <h3>{decorateTeamName(selectedTeam.name)}</h3>
                </div>
              </div>
              <div className="row">
                <div className="col-3 text-left">
                  <h3>Sritis: </h3>
                </div>
                <div className="col-9 text-left">
                  <h3>
                    {selectedTeam.stack !== "" &&
                    selectedTeam.stack !== null &&
                    selectedTeam.stack !== undefined
                      ? selectedTeam.stack
                      : "-"}
                  </h3>
                </div>
              </div>
              <div className="row">
                <div className="col-3 text-left">
                  <h3>Patirtis: </h3>
                </div>
                <div className="col-9 text-left">
                  <h3>0 m.</h3>
                </div>
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
