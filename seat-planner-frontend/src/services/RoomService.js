import axios from "axios";
import { useState, useEffect } from "react";

const RoomsService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [roomData, setRoomData] = useState([]);
  const [updateRoomState, setUpdateRoomState] = useState("");
  const [deleteRoomState, setDeleteRoomState] = useState("");
  const [additionRoomState, setAdditionRoomState] = useState("");
  const [roomFloorId, setRoomFloorId] = useState("");

  // useEffect(() => {
  //   fetchRoomData(props.officeFloorId, roomFloorId);
  // }, [roomFloorId]);

  const fetchRoomData = (officeId, floorId) => {
    axios
      .get(baseUrl + "rooms", {
        params: {
          companyId: props.companyId,
          officeId: officeId,
          floorId: floorId,
        },
      })
      .then((response) => {
        if (response.data !== null) {
          setRoomData(response.data);
        }
      })
      .catch((error) => {
        setRoomData([]);
      });
  };

  const updateRoom = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .put(baseUrl + "rooms", requestBody)
      .then((response) => {
        console.log(response.data);
        if (
          response.data.id !== "" &&
          response.data.id !== null &&
          response.data.id !== undefined
        ) {
          setUpdateRoomState("success");
        } else {
          setUpdateRoomState("failed");
        }
      })
      .catch((error) => {
        setUpdateRoomState("failed");
      });
  };

  const deleteRoom = (roomId, companyId, officeId, floorId) => {
    console.log("roomId: " + roomId + "; companyId: " + companyId);
    axios
      .delete(baseUrl + "rooms/" + roomId, {
        params: { companyId: companyId, officeId: officeId, floorId: floorId },
      })
      .then((response) => {
        console.log(response.data);
        setDeleteRoomState("success");
      })
      .catch((error) => {
        setDeleteRoomState("failed");
      });
  };

  const addRoom = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .post(baseUrl + "rooms", requestBody)
      .then((response) => {
        setAdditionRoomState("success");
      })
      .catch((error) => {
        setAdditionRoomState("failed");
      });
  };

  return {
    roomData,
    setRoomData,
    updateRoomState,
    setUpdateRoomState,
    deleteRoomState,
    setDeleteRoomState,
    additionRoomState,
    setAdditionRoomState,
    updateRoom,
    deleteRoom,
    addRoom,
    roomFloorId,
    setRoomFloorId,
    fetchRoomData,
  };
};

export default RoomsService;
