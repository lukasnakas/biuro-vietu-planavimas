import axios from "axios";
import { useState, useEffect } from "react";

const SuggestionService = (props) => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [data, setData] = useState([]);

  useEffect(() => {
    fetchSuggestionsData();
  }, []);

  const fetchSuggestionsData = () => {
    axios
      .get(baseUrl + "suggestions", { params: { companyId: props.companyId } })
      .then((response) => {
        if (response.data !== null) {
          setData(response.data);
        }
      });
  };

  return { data };
};

export default SuggestionService;
