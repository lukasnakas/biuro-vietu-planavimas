import axios from "axios";
import { useState, useEffect } from "react";

const TeamService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [teamsData, setTeamsData] = useState([]);
  const [teamData, setTeamData] = useState([]);
  const [updateTeamState, setUpdateTeamState] = useState("");
  const [deleteTeamState, setDeleteTeamState] = useState("");
  const [additionTeamState, setAdditionTeamState] = useState("");

  useEffect(() => {
    fetchTeamsData();
  }, []);

  // useEffect(() => {
  //   fetchTeamData();
  // }, []);

  const fetchTeamData = (officeId, floorId, roomId) => {
    axios
      .get(baseUrl + "teams", {
        params: {
          companyId: props.companyId,
          officeId: officeId,
          floorId: floorId,
          roomId: roomId,
        },
      })
      .then((response) => {
        if (response.data !== null) {
          setTeamData(response.data);
        }
      })
      .catch((error) => {
        setTeamData([]);
      });
  };

  const updateTeam = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .put(baseUrl + "teams", requestBody)
      .then((response) => {
        console.log(response.data);
        if (
          response.data.id !== "" &&
          response.data.id !== null &&
          response.data.id !== undefined
        ) {
          setUpdateTeamState("success");
        } else {
          setUpdateTeamState("failed");
        }
      })
      .catch((error) => {
        setUpdateTeamState("failed");
      });
  };

  const deleteTeam = (teamId, companyId, officeId, floorId, roomId) => {
    console.log("teamId: " + teamId + "; companyId: " + companyId);
    axios
      .delete(baseUrl + "teams/" + teamId, {
        params: {
          companyId: companyId,
          officeId: officeId,
          floorId: floorId,
          roomId: roomId,
        },
      })
      .then((response) => {
        console.log(response.data);
        setDeleteTeamState("success");
      })
      .catch((error) => {
        setDeleteTeamState("failed");
      });
  };

  const addTeam = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .post(baseUrl + "teams", requestBody)
      .then((response) => {
        setAdditionTeamState("success");
      })
      .catch((error) => {
        setAdditionTeamState("failed");
      });
  };

  const fetchTeamsData = () => {
    axios
      .get(baseUrl + "teams/all", { params: { companyId: props.companyId } })
      .then((response) => {
        console.log(response.data);
        if (response.data !== null) {
          setTeamsData(response.data);
        }
      })
      .catch((error) => {
        setTeamsData([]);
      });
  };

  return {
    teamsData,
    setTeamsData,
    teamData,
    setTeamData,
    updateTeamState,
    setUpdateTeamState,
    deleteTeamState,
    setDeleteTeamState,
    additionTeamState,
    setAdditionTeamState,
    updateTeam,
    deleteTeam,
    addTeam,
    fetchTeamData,
  };
};

export default TeamService;
