import axios from "axios";
import { useState, useEffect } from "react";

const EmployeeService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [data, setData] = useState([]);

  useEffect(() => {
    fetchEmployeeData();
  }, []);

  const fetchEmployeeData = () => {
    axios
      .get(baseUrl + "members/all", { params: { companyId: props.companyId } })
      .then((response) => {
        if (response.data !== null) {
          setData(response.data);
        }
      });
  };

  const updateEmployee = (requestBody) => {
    console.log("REQUEST -------");
    console.log(requestBody);
    axios.post(baseUrl + "members", requestBody).then((response) => {
      console.log(response.data);
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
      });
  };

  return { data, updateEmployee, deleteEmployee };
};

export default EmployeeService;
