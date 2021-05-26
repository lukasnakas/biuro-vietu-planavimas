import axios from "axios";
import { useState, useEffect } from "react";

const OfficesService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [officeData, setOfficeData] = useState([]);
  const [updateOfficeState, setUpdateOfficeState] = useState("");
  const [deleteOfficeState, setDeleteOfficeState] = useState("");
  const [additionOfficeState, setAdditionOfficeState] = useState("");
  const [addedOffice, setAddedOffice] = useState({});

  useEffect(() => {
    console.log("FETCHING OFFICE DATA");
    fetchOfficeData();
  }, []);

  const fetchOfficeData = () => {
    axios
      .get(baseUrl + "offices", { params: { companyId: props.companyId } })
      .then((response) => {
        if (response.data !== null) {
          setOfficeData(response.data);
        }
      })
      .catch((error) => {
        setOfficeData([]);
      });
  };

  const updateOffice = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .put(baseUrl + "offices", requestBody)
      .then((response) => {
        console.log(response.data);
        if (
          response.data.id !== "" &&
          response.data.id !== null &&
          response.data.id !== undefined
        ) {
          setUpdateOfficeState("success");
        } else {
          setUpdateOfficeState("failed");
        }
      })
      .catch((error) => {
        setUpdateOfficeState("failed");
      });
  };

  const deleteOffice = (officeId, companyId) => {
    console.log("officeId: " + officeId + "; companyId: " + companyId);
    axios
      .delete(baseUrl + "offices/" + officeId, {
        params: { companyId: companyId },
      })
      .then((response) => {
        console.log(response.data);
        setDeleteOfficeState("success");
      })
      .catch((error) => {
        setDeleteOfficeState("failed");
      });
  };

  const addOffice = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .post(baseUrl + "offices", requestBody)
      .then((response) => {
        setAddedOffice(response.data);
        setAdditionOfficeState("success");
      })
      .catch((error) => {
        setAdditionOfficeState("failed");
      });
  };

  return {
    fetchOfficeData,
    officeData,
    setOfficeData,
    updateOfficeState,
    setUpdateOfficeState,
    deleteOfficeState,
    setDeleteOfficeState,
    additionOfficeState,
    setAdditionOfficeState,
    updateOffice,
    deleteOffice,
    addOffice,
    addedOffice,
    setAddedOffice,
  };
};

export default OfficesService;
