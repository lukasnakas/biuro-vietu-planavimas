import AuthenticationService from "../../services/AuthenticationService";
import { useEffect } from "react";
import { useHistory } from "react-router-dom";

const LoginComponent = (props) => {
  const history = useHistory();
  const {
    loginUser,
    handleChange,
    inputs,
    isLoginSuccessful,
    isAdmin,
    companyId,
  } = AuthenticationService();

  useEffect(() => {
    console.log("companyId: " + companyId);
    console.log("is loggin in successful: " + isLoginSuccessful);
    console.log("is admin logged in: " + isAdmin);
    if (isLoginSuccessful === true) {
      props.setCompanyId(companyId);
      props.setLoggedIn(true);
      props.setAdmin(isAdmin);
      history.push("/overview");
    }
  }, [isLoginSuccessful]);

  return (
    <form className="form-horizontal" onSubmit={loginUser}>
      <h1>Prisijungimas</h1>
      <form className="form-horizontal">
        <div className="form-group">
          <label
            htmlFor="emailInputLogin"
            className="col-sm-3 col-md-2 control-label"
          >
            El. paštas
          </label>
          <div className="col-sm-9 col-md-10">
            <input
              type="email"
              className="form-control"
              id="emailInputLogin"
              required
              value={inputs.email}
              onChange={handleChange}
              name="email"
            />
          </div>
        </div>
        <div className="form-group">
          <label
            htmlFor="passwordInputLogin"
            className="col-sm-3 col-md-2 control-label"
          >
            Slaptažodis
          </label>
          <div className="col-sm-9 col-md-10">
            <input
              type="password"
              className="form-control"
              id="passwordInputLogin"
              required
              value={inputs.password}
              onChange={handleChange}
              name="password"
            />
          </div>
        </div>
      </form>
      <div className="col">
        <div className="row justify-content-center">
          <button type="submit" className="btn btn-primary" id="button-login">
            Prisijungti
          </button>
        </div>
      </div>
    </form>
  );
};

export default LoginComponent;
