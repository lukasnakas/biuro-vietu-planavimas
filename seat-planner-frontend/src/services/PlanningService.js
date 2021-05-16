import axios from "axios";
import { useState, useEffect } from "react";

const PlanningService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [teamlessMembers, setTeamlessMembers] = useState([]);

  useEffect(() => {
    getTeamlessEmployess();
  }, []);

  const submitToPlanner = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios.post(baseUrl + "submit", requestBody).then((response) => {
      console.log(response.data);
    });
  };

  const getTeamlessEmployess = () => {
    axios
      .get(baseUrl + "members/teamless", {
        params: { companyId: props.companyId },
      })
      .then((response) => {
        if (response.data !== null) {
          setTeamlessMembers(response.data);
        }
      });
  };

  return {
    submitToPlanner,
    teamlessMembers,
  };
};

export default PlanningService;
