import React, { useState, useEffect } from "react";

const FloorDropdown = (props) => {
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

  const handleFloorSelect = (e) => {
    if (e.target.id !== "dropdown-basic-button-nodata") {
      props.dropdownSetter(e.target.innerText);
      const currentFloor = props.floors.find(
        (floor) => decorateFloorName(floor.floorName) === e.target.innerText
      );
      props.setRooms(currentFloor.roomList);
      props.setSelectedFloor(decorateFloorName(currentFloor.floorName));
      props.resetLowerDropdowns();
    }
    setDropdownMenuActive(false);
  };

  const decorateFloorName = (floorName) => {
    return floorName.split("_")[0];
  };

  return (
    <div
      className={`w-100 dropdown ${dropdownMenuActive ? "open" : ""}`}
      ref={dropdownRef}
    >
      <button
        type="button"
        id="dropdown-floor-button"
        name="floor"
        className="w-100 btn btn-default dropdown-toggle"
        data-toggle="dropdown"
        aria-haspopup="true"
        aria-expanded="false"
        onClick={dropdownMenuOpen}
      >
        {props.dropdownValue}
      </button>
      <ul
        id="dropdown-floor-menu-list"
        className="dropdown-menu scrollable-list"
        role="menu"
        aria-expanded="false"
        aria-hidden="true"
        aria-labelledby="dropdown-floor-button"
      >
        {props.floors.length ? (
          props.floors.map((floor) => (
            <li role="menuitem" key={decorateFloorName(floor.floorName)}>
              <a
                href="#"
                id={
                  "dropdown-basic-button-" + decorateFloorName(floor.floorName)
                }
                onClick={handleFloorSelect}
              >
                {decorateFloorName(floor.floorName)}
              </a>
            </li>
          ))
        ) : (
          <li role="menuitem" key="nodata">
            <a
              href="#"
              id={"dropdown-basic-button-nodata"}
              onClick={handleFloorSelect}
            >
              Nerasta aukštų duomenų
            </a>
          </li>
        )}
      </ul>
    </div>
  );
};

export default FloorDropdown;
