import axios from "axios";
import { useState } from "react";

const AuthenticationService = () => {
  const baseUrl = "http://localhost:8080/v1/planner/";
  const [inputs, setInputs] = useState({});
  const [isRegistrationSuccessful, setRegistrationSuccessful] = useState(false);
  const [isLoginSuccessful, setLoginSuccessful] = useState(false);
  const [isAdmin, setAdmin] = useState(false);
  const [companyId, setCompanyId] = useState("");

  const loginUser = (event) => {
    if (event) {
      event.preventDefault();
      const user = {
        email: inputs.email,
        password: inputs.password,
      };
      console.log(user);
      axios.post(baseUrl + "login", user).then((response) => {
        if (response.data !== null) {
          setCompanyId(response.data.company);
          setAdmin(response.data.role === "ADMIN");
          setLoginSuccessful(response.data.successful);
        } else {
          setLoginSuccessful(false);
        }
        console.log(response.data);
      });
    }
  };

  const handleChange = (event) => {
    event.persist();
    setInputs((inputs) => ({
      ...inputs,
      [event.target.name]: event.target.value,
    }));
  };

  return {
    loginUser,
    handleChange,
    inputs,
    isRegistrationSuccessful,
    isLoginSuccessful,
    isAdmin,
    companyId,
  };
};

export default AuthenticationService;
