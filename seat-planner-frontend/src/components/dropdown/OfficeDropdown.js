import React, { useState, useEffect } from "react";

const OfficeDropdown = (props) => {
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

  const handleOfficeSelect = (e) => {
    if (e.target.id !== "dropdown-basic-button-nodata") {
      props.dropdownSetter(e.target.innerText);
      const currentOffice = props.offices.find(
        (office) => office.officeName === e.target.innerText
      );
      if (props.screen === "overview") {
        props.setFloors(currentOffice.overviewFloorList);
        props.setSelectedOffice(currentOffice.officeName);
      }
      if (props.screen === "plan") {
        props.setTeams(getTeamsFromOffice(currentOffice));
        props.setSelectedOffice(currentOffice);
      }
      props.resetLowerDropdowns();
    }
    setDropdownMenuActive(false);
  };

  const getTeamsFromOffice = (office) => {
    let allTeams = [];
    office.overviewFloorList.forEach((floor) => {
      floor.roomList.forEach((room) => {
        allTeams = allTeams.concat(room.teams);
      });
    });
    allTeams.sort((obj1, obj2) =>
      obj1.name > obj2.name ? 1 : obj2.name > obj1.name ? -1 : 0
    );
    return allTeams;
  };

  return (
    <div
      className={`w-100 dropdown ${dropdownMenuActive ? "open" : ""}`}
      ref={dropdownRef}
    >
      <button
        type="button"
        id="dropdown-office-button"
        name="office"
        className="w-100 btn btn-default dropdown-toggle"
        data-toggle="dropdown"
        aria-haspopup="true"
        aria-expanded="false"
        onClick={dropdownMenuOpen}
      >
        {props.dropdownValue}
      </button>
      <ul
        id="dropdown-office-menu-list"
        className="dropdown-menu scrollable-list"
        role="menu"
        aria-expanded="false"
        aria-hidden="true"
        aria-labelledby="dropdown-office-button"
      >
        {props.offices.length ? (
          props.offices
            .filter((office) => office.officeName !== "no_team")
            .map((office) => (
              <li role="menuitem" key={office.officeName}>
                <a
                  href="#"
                  id={"dropdown-basic-button-" + office.officeName}
                  onClick={handleOfficeSelect}
                >
                  {office.officeName}
                </a>
              </li>
            ))
        ) : (
          <li role="menuitem" key="nodata">
            <a
              href="#"
              id={"dropdown-basic-button-nodata"}
              onClick={handleOfficeSelect}
            >
              Nerasta biurų duomenų
            </a>
          </li>
        )}
      </ul>
    </div>
  );
};

export default OfficeDropdown;
