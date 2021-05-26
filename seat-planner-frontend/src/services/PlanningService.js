import axios from "axios";
import { useState, useEffect } from "react";

const PlanningService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [teamlessMembers, setTeamlessMembers] = useState([]);
  const [confirmationState, setConfirmationState] = useState("");
  const [suggestionResult, setSuggestionResult] = useState({});
  const [suggestions, setSuggestions] = useState([]);
  const [status, setStatus] = useState("0%");
  const [correlationId, setCorrelationId] = useState("");

  useEffect(() => {
    getTeamlessEmployess();
  }, []);

  const submitToPlanner = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .post(baseUrl + "submit", requestBody)
      .then((response) => {
        if (response.data.searchId) {
          setCorrelationId(response.data.searchId);
        }
      })
      .catch((error) => {
        setConfirmationState("failed");
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

  const confirmTransfer = (suggestionId, companyId, correlationId) => {
    console.log("REQUEST -------");
    axios
      .post(baseUrl + "suggestions/" + suggestionId + "/confirm", null, {
        params: { companyId, correlationId },
      })
      .then((response) => {
        if (response.data.state === "CONFIRMED") {
          setConfirmationState("success");
        } else {
          setConfirmationState("failed");
        }
      })
      .catch((error) => {
        setConfirmationState("failed");
      });
  };

  const updateTransfers = (correlationId) => {
    axios.get(baseUrl + "results/" + correlationId).then((response) => {
      if (response.data !== null) {
        setSuggestions(response.data.suggestions);
        setStatus(response.data.status);
        console.log(response.data);
      }
    });
  };

  return {
    submitToPlanner,
    correlationId,
    setCorrelationId,
    teamlessMembers,
    confirmTransfer,
    confirmationState,
    setConfirmationState,
    updateTransfers,
    suggestions,
    status,
  };
};

export default PlanningService;
