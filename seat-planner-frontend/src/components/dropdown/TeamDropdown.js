import React, { useState, useEffect } from "react";

const TeamDropdown = (props) => {
  const [dropdownMenuActive, setDropdownMenuActive] = useState(false);
  const dropdownRef = React.createRef();

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  });

  const handleClickOutside = (e) => {
    if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
      setDropdownMenuActive(false);
    }
  };

  const dropdownMenuOpen = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDropdownMenuActive(!dropdownMenuActive);
  };

  const handleTeamSelect = (e) => {
    if (e.target.id !== "dropdown-basic-button-nodata") {
      props.dropdownSetter(e.target.innerText);
      const currentTeam = props.teams.find(
        (team) => decorateTeamName(team.name) === e.target.innerText
      );
      props.setSelectedTeam(currentTeam);
    }
    setDropdownMenuActive(false);
  };

  const decorateTeamName = (teamName) => {
    return teamName.split("_")[0];
  };

  return (
    <div
      className={`w-100 dropdown ${dropdownMenuActive ? "open" : ""} ${
        props.isDisabled ? "disabled" : ""
      }`}
      ref={dropdownRef}
    >
      <button
        type="button"
        id="dropdown-team-button"
        name="team"
        className="w-100 btn btn-default dropdown-toggle"
        data-toggle="dropdown"
        aria-haspopup="true"
        aria-expanded="false"
        onClick={dropdownMenuOpen}
      >
        {props.dropdownValue === "no_team" ? "-" : props.dropdownValue}
      </button>
      <ul
        id="dropdown-team-menu-list"
        className="dropdown-menu scrollable-list"
        role="menu"
        aria-expanded="false"
        aria-hidden="true"
        aria-labelledby="dropdown-team-button"
      >
        {props.teams.length ? (
          props.teams
            .filter((team) => decorateTeamName(team.name) !== "no")
            .map((team) => (
              <li role="menuitem" key={team.id}>
                <a
                  href="#"
                  id={"dropdown-basic-button-" + decorateTeamName(team.name)}
                  onClick={handleTeamSelect}
                >
                  {decorateTeamName(team.name)}
                </a>
              </li>
            ))
        ) : (
          <li role="menuitem" key="nodata">
            <a
              href="#"
              id={"dropdown-basic-button-nodata"}
              onClick={handleTeamSelect}
            >
              Nerasta komandų duomenų
            </a>
          </li>
        )}
      </ul>
    </div>
  );
};

export default TeamDropdown;
