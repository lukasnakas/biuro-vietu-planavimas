import EmployeeService from "services/EmployeeService";
import { useState } from "react";
import UpdateEmployeeModal from "./UpdateEmployeeModal";
import DeleteEmployeeModal from "./DeleteEmployeeModal";

const EmployeeTableComponent = (props) => {
  const { data, updateEmployee, deleteEmployee } = EmployeeService(props);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState({});

  const updateSelectedEmployee = (employee) => {
    setSelectedEmployee(employee);
    setShowUpdateModal(true);
  };

  const deleteSelectedEmployee = (employee) => {
    setSelectedEmployee(employee);
    setShowDeleteModal(true);
  };

  return (
    <div className="row">
      <div className="col">
        <h1>Darbuotojai</h1>
        <UpdateEmployeeModal
          show={showUpdateModal}
          onHide={() => setShowUpdateModal(false)}
          employeeData={selectedEmployee}
          updateEmployee={updateEmployee}
        />
        <DeleteEmployeeModal
          show={showDeleteModal}
          onHide={() => setShowDeleteModal(false)}
          employeeData={selectedEmployee}
          deleteEmployee={deleteEmployee}
        />
        {data.length ? (
          <div className=" scrollable-table">
            <table className="table table-hover">
              <thead>
                <tr>
                  <th style={{ textAlign: "center" }}>Vardas</th>
                  <th style={{ textAlign: "center" }}>Pavardė</th>
                  <th style={{ textAlign: "center" }}>El. paštas</th>
                  <th style={{ textAlign: "center" }}>Sritis</th>
                  <th style={{ textAlign: "center" }}>Patirtis</th>
                  <th style={{ textAlign: "center" }}>Valdymo įrankiai</th>
                </tr>
              </thead>
              <tbody>
                {data.map((employee) => (
                  <tr key={employee.id}>
                    <td>{employee.firstName}</td>
                    <td>{employee.lastName}</td>
                    <td>{employee.email}</td>
                    {employee.stack === "" ? (
                      <td>-</td>
                    ) : (
                      <td>{employee.stack}</td>
                    )}
                    <td>{employee.experience}</td>
                    <td>
                      <div className="row justify-content-between">
                        <div className="col text-center">
                          <button
                            className="btn"
                            onClick={() => {
                              updateSelectedEmployee(employee);
                            }}
                          >
                            Keisti
                          </button>
                        </div>
                        <div className="col text-center">
                          <button
                            className="btn"
                            onClick={() => {
                              deleteSelectedEmployee(employee);
                            }}
                          >
                            Ištrinti
                          </button>
                        </div>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <h4>Nepavyko gauti darbuotojų duomenų</h4>
        )}
      </div>
    </div>
  );
};

export default EmployeeTableComponent;
