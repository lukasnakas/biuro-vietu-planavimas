import axios from "axios";
import { useState, useEffect } from "react";

const EmployeeService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [employeeData, setEmployeeData] = useState([]);
  const [totalItems, setTotalItems] = useState(1);
  const [updateState, setUpdateState] = useState("");
  const [deleteState, setDeleteState] = useState("");
  const [additionState, setAdditionState] = useState("");

  useEffect(() => {
    fetchEmployeeData();
  }, []);

  const fetchEmployeeData = () => {
    axios
      .get(baseUrl + "members/all", { params: { companyId: props.companyId } })
      .then((response) => {
        if (response.data !== null) {
          setEmployeeData(response.data.memberList);
          setTotalItems(response.data.totalPaginationItems);
        }
      })
      .catch((error) => {
        setEmployeeData([]);
      });
  };

  const fetchEmployeeDataPaging = (pageNo) => {
    axios
      .get(baseUrl + "members/all", {
        params: { companyId: props.companyId, page: pageNo },
      })
      .then((response) => {
        if (response.data !== null) {
          setEmployeeData(response.data.memberList);
          setTotalItems(response.data.totalPaginationItems);
        }
      })
      .catch((error) => {
        setEmployeeData([]);
      });
  };

  const fetchEmployeeDataPagingSorting = (pageNo, sortField, sortAscending) => {
    const sortOrder = sortAscending ? "ASC" : "DESC";

    axios
      .get(baseUrl + "members/all", {
        params: {
          companyId: props.companyId,
          page: pageNo,
          sortBy: sortField,
          sortOrder: sortOrder,
        },
      })
      .then((response) => {
        if (response.data !== null) {
          setEmployeeData(response.data.memberList);
          setTotalItems(response.data.totalPaginationItems);
        }
      })
      .catch((error) => {
        setEmployeeData([]);
      });
  };

  const updateEmployee = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .put(baseUrl + "members", requestBody)
      .then((response) => {
        console.log(response.data);
        if (
          response.data.id !== "" &&
          response.data.id !== null &&
          response.data.id !== undefined
        ) {
          setUpdateState("success");
        } else {
          setUpdateState("failed");
        }
      })
      .catch((error) => {
        setUpdateState("failed");
      });
  };

  const deleteEmployee = (memberId, companyId) => {
    console.log("memberId: " + memberId + "; companyId: " + companyId);
    axios
      .delete(baseUrl + "members/" + memberId, {
        params: { companyId: companyId },
      })
      .then((response) => {
        console.log(response.data);
        setDeleteState("success");
      })
      .catch((error) => {
        setDeleteState("failed");
      });
  };

  const addEmployee = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios
      .post(baseUrl + "members", requestBody)
      .then((response) => {
        setAdditionState("success");
      })
      .catch((error) => {
        setAdditionState("failed");
      });
  };

  return {
    employeeData,
    totalItems,
    setEmployeeData,
    updateState,
    setUpdateState,
    deleteState,
    setDeleteState,
    additionState,
    setAdditionState,
    updateEmployee,
    deleteEmployee,
    addEmployee,
    fetchEmployeeData,
    fetchEmployeeDataPaging,
    fetchEmployeeDataPagingSorting,
  };
};

export default EmployeeService;
