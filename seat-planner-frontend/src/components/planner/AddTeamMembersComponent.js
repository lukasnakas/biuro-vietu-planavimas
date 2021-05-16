import { useState } from "react";
import OfficeDropdown from "../dropdown/OfficeDropdown";
import TeamDropdown from "../dropdown/TeamDropdown";
import { Multiselect } from "multiselect-react-dropdown";
import PlanningService from "../../services/PlanningService";

const AddTeamMembersComponent = (props) => {
  const { submitToPlanner, teamlessMembers } = PlanningService(props);
  const [teams, setTeams] = useState([]);
  const [selectedTeam, setSelectedTeam] = useState({});
  const [selectedOffice, setSelectedOffice] = useState({});
  const [selectedEmployees, setSelectedEmployees] = useState([]);
  const [teamDropdownValue, setTeamDropdownValue] = useState(
    "Pasirinkite komandą.."
  );
  const [officeDropdownValue, setOfficeDropdownValue] = useState(
    "Pasirinkite biurą.."
  );
  const [isDisabled, setDisabled] = useState(true);
  const [additionalSeats, setAdditionalSeats] = useState(0);
  const [teamsInvolved, setTeamsInvolved] = useState(3);
  const [isSplittable, setSplittable] = useState(false);
  const [requestBody, setRequestBody] = useState({});

  const resetTeamsDropdown = () => {
    setTeamDropdownValue("Pasirinkite komandą..");
    setSelectedTeam({});
  };

  const handleCheckboxChange = (event) => {
    if (event.target.id === "form-additionalSeatsRequired") {
      setDisabled(!isDisabled);
      setAdditionalSeats(0);
    }
    if (event.target.id === "form-splittable") {
      setSplittable(!isSplittable);
    }
  };

  const handleInputFieldChange = (event) => {
    if (event.target.id === "form-additionalSeats") {
      if (!isNaN(event.target.value) && event.target.value >= 0) {
        setAdditionalSeats(parseInt(event.target.value));
      } else {
        setAdditionalSeats(event.target.name, 0);
      }
    }
    if (event.target.id === "form-teamsInvolved") {
      if (!isNaN(event.target.value) && event.target.value >= 0) {
        setTeamsInvolved(parseInt(event.target.value));
      } else {
        setTeamsInvolved(event.target.name, 2);
      }
    }
  };

  const handlePlannerSubmit = () => {
    const peopleAmount = selectedEmployees.length + additionalSeats;
    const request = {
      teamName: selectedTeam.name,
      location: selectedOffice.officeName,
      peopleAmount: peopleAmount,
      members: selectedEmployees,
      maxTeamsInvolved: teamsInvolved,
      splittable: isSplittable,
      companyId: props.companyId,
      officeId: selectedOffice.id,
    };
    submitToPlanner(request);
  };

  const decorateTeamlessEmployees = () => {
    teamlessMembers.forEach(function (member) {
      member.fullName = member.firstName + " " + member.lastName;
    });
    return teamlessMembers;
  };

  return (
    <div className="row" style={{ padding: "10px 0" }}>
      <div className="col-4">
        <h2>Konfigūracija</h2>
        <div className="row">
          <div className="col">
            <div className="row">
              <div className="col text-left">
                <label htmlFor="officeDropdown" className="control-label">
                  Biuras
                </label>
              </div>
            </div>
            <div className="row">
              <div className="col">
                <OfficeDropdown
                  dropdownValue={officeDropdownValue}
                  dropdownSetter={setOfficeDropdownValue}
                  offices={props.data}
                  setTeams={setTeams}
                  resetLowerDropdowns={resetTeamsDropdown}
                  screen="plan"
                  setSelectedOffice={setSelectedOffice}
                />
              </div>
            </div>
          </div>
        </div>
        <div className="row">
          <div className="col">
            <div className="row">
              <div className="col text-left">
                <label htmlFor="floorDropdown" className="control-label">
                  Aukštas
                </label>
              </div>
            </div>
            <div className="row">
              <div className="col">
                <TeamDropdown
                  dropdownValue={teamDropdownValue}
                  dropdownSetter={setTeamDropdownValue}
                  teams={teams}
                  setSelectedTeam={setSelectedTeam}
                />
              </div>
            </div>
            <div className="row" style={{ marginTop: "10px" }}>
              <div className="col text-left" style={{ margin: "10px 0" }}>
                <h4 style={{ marginTop: "10px" }}>
                  Didžiausias persodinime dalyvaujančių komandų kiekis
                </h4>
                <input
                  type="number"
                  className="form-control"
                  name="teamsInvolved"
                  onChange={handleInputFieldChange}
                  value={teamsInvolved === undefined ? "" : teamsInvolved}
                  id="form-teamsInvolved"
                  size="9"
                  min="2"
                  max="4"
                />
                <div className="checkbox" style={{ marginTop: "20px" }}>
                  <input
                    type="checkbox"
                    className="form-control"
                    name="splittable"
                    onChange={handleCheckboxChange}
                    id="form-splittable"
                  />
                  <label htmlFor="form-splittable" className="control-label">
                    Skelti komandą per pusę
                  </label>
                </div>
                <div className="checkbox">
                  <input
                    type="checkbox"
                    className="form-control"
                    name="additionalSeatsRequired"
                    onChange={handleCheckboxChange}
                    id="form-additionalSeatsRequired"
                  />
                  <label
                    htmlFor="form-additionalSeatsRequired"
                    className="control-label"
                  >
                    Rezervuoti papildomas vietas
                  </label>
                </div>
                <h4 style={{ marginTop: "10px" }}>Kiekis</h4>
                <input
                  type="number"
                  className="form-control"
                  name="additionalSeats"
                  onChange={handleInputFieldChange}
                  value={additionalSeats === undefined ? "" : additionalSeats}
                  id="form-additionalSeats"
                  size="9"
                  disabled={isDisabled}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="col-8">
        <div className="row">
          <div className="col">
            <h2 style={{ marginBottom: "35px" }}>
              Darbuotojai, nepriskirti komandoms
            </h2>
          </div>
        </div>
        <div className="row" style={{ height: "74%" }}>
          <div className="col">
            <Multiselect
              style={{
                multiselectContainer: { height: "30px" },
                searchBox: { height: "30px", padding: "0 0 0 5px" },
                chips: { height: "20px" },
                inputField: { margin: "0" },
              }}
              options={decorateTeamlessEmployees()}
              displayValue="fullName"
              onSelect={(emps) => setSelectedEmployees(emps)}
              onRemove={(emps) => setSelectedEmployees(emps)}
              placeholder="Pasirinkite darbuotojus.."
              closeOnSelect={false}
              avoidHighlightFirstOption={true}
              hidePlaceholder={true}
              showArrow={true}
            />
          </div>
        </div>
        <div className="row">
          <div className="col text-right">
            <button
              type="button"
              className="btn btn-primary"
              onClick={handlePlannerSubmit}
            >
              Gauti pasiūlymus
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddTeamMembersComponent;
