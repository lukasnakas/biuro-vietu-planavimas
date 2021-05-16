import React, { useState, useEffect } from "react";

const RoomDropdown = (props) => {
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

  const handleRoomSelect = (e) => {
    if (e.target.id !== "dropdown-basic-button-nodata") {
      props.dropdownSetter(e.target.innerText);
      const currentRoom = props.rooms.find(
        (room) => room.location.roomNumb === e.target.innerText
      );
      props.setTeams(currentRoom.teams);
      props.setSelectedRoom(currentRoom.location.roomNumb);
      props.resetLowerDropdowns();
      props.setMoreDetailButtonDisabled(false);
    }
    setDropdownMenuActive(false);
  };

  return (
    <div
      className={`w-100 dropdown ${dropdownMenuActive ? "open" : ""}`}
      ref={dropdownRef}
    >
      <button
        type="button"
        id="dropdown-room-button"
        name="room"
        className="w-100 btn btn-default dropdown-toggle"
        data-toggle="dropdown"
        aria-haspopup="true"
        aria-expanded="false"
        onClick={dropdownMenuOpen}
      >
        {props.dropdownValue}
      </button>
      <ul
        id="dropdown-room-menu-list"
        className="dropdown-menu scrollable-list"
        role="menu"
        aria-expanded="false"
        aria-hidden="true"
        aria-labelledby="dropdown-room-button"
      >
        {props.rooms.length ? (
          props.rooms.map((room) => (
            <li role="menuitem" key={room.id}>
              <a
                href="#"
                id={"dropdown-basic-button-" + room.location.roomNumb}
                onClick={handleRoomSelect}
              >
                {room.location.roomNumb}
              </a>
            </li>
          ))
        ) : (
          <li role="menuitem" key="nodata">
            <a
              href="#"
              id={"dropdown-basic-button-nodata"}
              onClick={handleRoomSelect}
            >
              Nerasta kabinetų duomenų
            </a>
          </li>
        )}
      </ul>
    </div>
  );
};

export default RoomDropdown;
