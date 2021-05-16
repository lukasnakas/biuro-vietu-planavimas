import { useState } from "react";
import OverviewService from "../services/OverviewService";
import AddTeamMembersComponent from "../components/planner/AddTeamMembersComponent";
import SuggestionsComponent from "../components/planner/SuggestionsComponent";

const PlanningPage = (props) => {
  const { data } = OverviewService(props);
  const [showPlanTab, setShowPlanTab] = useState(true);

  const handleTabChange = (event) => {
    if (
      (event.target.id === "planButton" && showPlanTab === false) ||
      (event.target.id === "resultsButton" && showPlanTab === true)
    ) {
      setShowPlanTab(!showPlanTab);
    }
  };

  return (
    <div className="main-body">
      <div className="container h-100">
        <div className="row h-100">
          {props.isLoggedIn ? (
            <div className="col my-auto">
              <div className="row justify-content-center">
                <div className="col text-center">
                  <ul
                    className="nav nav-tabs nav-justified"
                    style={{ paddingLeft: "46px" }}
                  >
                    <li className="nav-item" role="presentation">
                      <a
                        id="planButton"
                        tabIndex="0"
                        role="tab"
                        data-toggle="tab"
                        aria-controls="normal"
                        onClick={handleTabChange}
                      >
                        Pridėti komandos narius
                      </a>
                    </li>
                    <li className="nav-item" role="presentation">
                      <a
                        id="resultsButton"
                        tabIndex="0"
                        role="tab"
                        data-toggle="tab"
                        aria-controls="normal"
                        onClick={handleTabChange}
                      >
                        Peržiūrėti patvirtintus persodinimus
                      </a>
                    </li>
                  </ul>
                </div>
              </div>
              <div className="row">
                <div className="col">
                  <div className="page-container rounded-20">
                    <div className="row">
                      <div className="col">
                        <div className="tab-content">
                          {showPlanTab ? (
                            <AddTeamMembersComponent
                              data={data}
                              companyId={props.companyId}
                            />
                          ) : (
                            <SuggestionsComponent companyId={props.companyId} />
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ) : (
            <div className="col my-auto">
              <div className="page-container rounded-20">
                <h1>Norėdami matyti šį puslapį, turite prisijungti</h1>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PlanningPage;
