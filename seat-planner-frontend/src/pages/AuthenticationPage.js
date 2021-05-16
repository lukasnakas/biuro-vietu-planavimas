import LoginComponent from "../components/login/LoginComponent";

const AuthenticationPage = (props) => {
  return (
    <div className="main-body">
      <div className="container h-100">
        <div className="row h-100">
          <div className="col my-auto">
            <div className="page-container rounded-20">
              <LoginComponent
                setAdmin={props.setAdmin}
                setLoggedIn={props.setLoggedIn}
                setCompanyId={props.setCompanyId}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AuthenticationPage;
