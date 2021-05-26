import axios from "axios";
import { useState, useEffect } from "react";

const FloorsService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [floorData, setFloorData] = useState([]);
  const [updateFloorState, setUpdateFloorState] = useState("");
  const [deleteFloorState, setDeleteFloorState] = useState("");
  const [additionFloorState, setAdditionFloorState] = useState("");
  const [floorOfficeId, setFloorOfficeId] = useState("");
  const [addedFloor, setAddedFloor] = useState({});

  // useEffect(() => {
  //   fetchFloorData(floorOfficeId);
  // }, [floorOfficeId]);

  const fetchFloorData = (officeId) => {
    axios
      .get(baseUrl + "floors", {
        params: { companyId: props.companyId, officeId: officeId },
      })
      .then((response) => {
        if (response.data !== null) {
          setFloorData(response.data);
        }
      })
      .catch((error) => {
        setFloorData([]);
      });
  };

  const updateFloor = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .put(baseUrl + "floors", requestBody)
      .then((response) => {
        console.log(response.data);
        if (
          response.data.id !== "" &&
          response.data.id !== null &&
          response.data.id !== undefined
        ) {
          setUpdateFloorState("success");
          setAddedFloor(response.data);
        } else {
          setUpdateFloorState("failed");
        }
      })
      .catch((error) => {
        setUpdateFloorState("failed");
      });
  };

  const deleteFloor = (floorId, companyId, officeId) => {
    console.log("floorId: " + floorId + "; companyId: " + companyId);
    axios
      .delete(baseUrl + "floors/" + floorId, {
        params: { companyId: companyId, officeId: officeId },
      })
      .then((response) => {
        console.log(response.data);
        setDeleteFloorState("success");
      })
      .catch((error) => {
        setDeleteFloorState("failed");
      });
  };

  const addFloor = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .post(baseUrl + "floors", requestBody)
      .then((response) => {
        setAdditionFloorState("success");
      })
      .catch((error) => {
        setAdditionFloorState("failed");
      });
  };

  return {
    floorData,
    setFloorData,
    updateFloorState,
    setUpdateFloorState,
    deleteFloorState,
    setDeleteFloorState,
    additionFloorState,
    setAdditionFloorState,
    updateFloor,
    deleteFloor,
    addFloor,
    floorOfficeId,
    setFloorOfficeId,
    fetchFloorData,
    addedFloor,
    setAddedFloor,
  };
};

export default FloorsService;
