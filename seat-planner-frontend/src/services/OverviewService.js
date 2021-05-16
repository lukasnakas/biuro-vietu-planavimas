import axios from "axios";
import { useState, useEffect } from "react";

const OverviewService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [data, setData] = useState([]);

  useEffect(() => {
    fetchOverviewData();
  }, []);

  const fetchOverviewData = () => {
    axios
      .get(baseUrl + "overview", { params: { companyId: props.companyId } })
      .then((response) => {
        if (response.data !== null) {
          setData(response.data.overviewOfficeList);
        }
      });
  };

  return { data };
};

export default OverviewService;
