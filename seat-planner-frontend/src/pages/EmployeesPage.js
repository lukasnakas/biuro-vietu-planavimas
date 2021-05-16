import EmployeeTableComponent from "../components/employees/EmployeeTableComponent";

const EmployeesPage = (props) => {
  return (
    <div className="main-body">
      <div className="container h-100">
        <div className="row h-100">
          <div className="col my-auto">
            <div className="page-container rounded-20">
              {props.isLoggedIn ? (
                <EmployeeTableComponent companyId={props.companyId} />
              ) : (
                <h1>Norėdami matyti šį puslapį, turite prisijungti</h1>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmployeesPage;
