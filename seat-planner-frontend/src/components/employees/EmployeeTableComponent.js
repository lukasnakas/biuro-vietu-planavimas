import EmployeeService from "services/EmployeeService";
import { useState } from "react";
import UpdateEmployeeModal from "./UpdateEmployeeModal";
import DeleteEmployeeModal from "./DeleteEmployeeModal";
import TeamService from "services/TeamService";
import AddEmployeeModal from "./AddEmployeeModal";
import Pagination from "react-js-pagination";

const EmployeeTableComponent = (props) => {
  const { teamsData } = TeamService(props);
  const {
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
  } = EmployeeService(props);

  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAdditionModal, setShowAdditionModal] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState({});
  const [inputs, setInputs] = useState({});
  const [teamDropdownValue, setTeamDropdownValue] = useState("");
  const [teamless, setTeamless] = useState(false);
  const [pageNumber, setPageNumber] = useState(1);
  const [sortingOptions, setSortingOptions] = useState({
    sortAscending: true,
    sortField: "firstName",
  });

  const updateSelectedEmployee = (employee) => {
    setSelectedEmployee(employee);
    setTeamDropdownValue(employee.teamName);
    setInputs({
      companyId: props.companyId,
      id: employee.id,
      firstName: employee.firstName,
      lastName: employee.lastName,
      email: employee.email,
      stack: employee.stack,
      experience: employee.experience,
      teamName: employee.teamName,
    });
    setShowUpdateModal(true);
  };

  const deleteSelectedEmployee = (employee) => {
    setSelectedEmployee(employee);
    setShowDeleteModal(true);
  };

  const handlePageChange = (pageNumber) => {
    console.log(`active page is ${pageNumber}`);
    setPageNumber(pageNumber);
    fetchEmployeeDataPagingSorting(
      pageNumber,
      sortingOptions.sortField,
      sortingOptions.sortAscending
    );
  };

  const handleSortingOptions = (fieldName) => {
    if (sortingOptions.sortField === fieldName) {
      setSortingOptions((options) => ({
        ...options,
        sortAscending: !sortingOptions.sortAscending,
      }));
      fetchEmployeeDataPagingSorting(
        pageNumber,
        fieldName,
        !sortingOptions.sortAscending
      );
    } else {
      setSortingOptions((options) => ({
        ...options,
        sortField: fieldName,
        sortAscending: true,
      }));
      fetchEmployeeDataPagingSorting(pageNumber, fieldName, true);
    }
  };

  return (
    <div className="row">
      <div className="col">
        <div className="row">
          <div className="col text-left">
            <h1>Darbuotojai</h1>
          </div>
          <div className="col text-right">
            <button
              className="btn btn-primary"
              onClick={() => setShowAdditionModal(true)}
            >
              Pridėti darbuotoją
            </button>
          </div>
        </div>
        <div className="row">
          <div className="col">
            <AddEmployeeModal
              show={showAdditionModal}
              onHide={() => setShowAdditionModal(false)}
              addEmployee={addEmployee}
              companyId={props.companyId}
              additionState={additionState}
              setAdditionState={setAdditionState}
              teamData={teamsData}
              teamDropdownValue={teamDropdownValue}
              setTeamDropdownValue={setTeamDropdownValue}
              employeeData={employeeData}
              setEmployeeData={setEmployeeData}
              teamless={teamless}
              setTeamless={setTeamless}
              fetchEmployeeData={fetchEmployeeData}
            />
            <UpdateEmployeeModal
              show={showUpdateModal}
              onHide={() => setShowUpdateModal(false)}
              selectedEmployeeData={selectedEmployee}
              updateEmployee={updateEmployee}
              setInputs={setInputs}
              inputs={inputs}
              companyId={props.companyId}
              updateState={updateState}
              setUpdateState={setUpdateState}
              employeeData={employeeData}
              setEmployeeData={setEmployeeData}
              teamData={teamsData}
              teamDropdownValue={teamDropdownValue}
              setTeamDropdownValue={setTeamDropdownValue}
              fetchEmployeeData={fetchEmployeeData}
            />
            <DeleteEmployeeModal
              show={showDeleteModal}
              onHide={() => setShowDeleteModal(false)}
              selectedEmployeeData={selectedEmployee}
              deleteEmployee={deleteEmployee}
              companyId={props.companyId}
              deleteState={deleteState}
              setDeleteState={setDeleteState}
              employeeData={employeeData}
              setEmployeeData={setEmployeeData}
              fetchEmployeeData={fetchEmployeeData}
            />
            {employeeData.length ? (
              <div>
                <table className="table table-hover">
                  <thead>
                    <tr>
                      <th
                        style={{ textAlign: "center" }}
                        onClick={() => handleSortingOptions("firstName")}
                      >
                        Vardas{" "}
                        {sortingOptions.sortField === "firstName" ? (
                          sortingOptions.sortAscending ? (
                            <span>▲</span>
                          ) : (
                            <span>▼</span>
                          )
                        ) : (
                          <span></span>
                        )}
                      </th>
                      <th
                        style={{ textAlign: "center" }}
                        onClick={() => handleSortingOptions("lastName")}
                      >
                        Pavardė{" "}
                        {sortingOptions.sortField === "lastName" ? (
                          sortingOptions.sortAscending ? (
                            <span>▲</span>
                          ) : (
                            <span>▼</span>
                          )
                        ) : (
                          <span></span>
                        )}
                      </th>
                      <th
                        style={{ textAlign: "center" }}
                        onClick={() => handleSortingOptions("email")}
                      >
                        El. paštas{" "}
                        {sortingOptions.sortField === "email" ? (
                          sortingOptions.sortAscending ? (
                            <span>▲</span>
                          ) : (
                            <span>▼</span>
                          )
                        ) : (
                          <span></span>
                        )}
                      </th>
                      <th
                        style={{ textAlign: "center" }}
                        onClick={() => handleSortingOptions("teamName")}
                      >
                        Komanda{" "}
                        {sortingOptions.sortField === "teamName" ? (
                          sortingOptions.sortAscending ? (
                            <span>▲</span>
                          ) : (
                            <span>▼</span>
                          )
                        ) : (
                          <span></span>
                        )}
                      </th>
                      <th
                        style={{ textAlign: "center" }}
                        onClick={() => handleSortingOptions("stack")}
                      >
                        Sritis{" "}
                        {sortingOptions.sortField === "stack" ? (
                          sortingOptions.sortAscending ? (
                            <span>▲</span>
                          ) : (
                            <span>▼</span>
                          )
                        ) : (
                          <span></span>
                        )}
                      </th>
                      <th
                        style={{ textAlign: "center" }}
                        onClick={() => handleSortingOptions("experience")}
                      >
                        Patirtis{" "}
                        {sortingOptions.sortField === "experience" ? (
                          sortingOptions.sortAscending ? (
                            <span>▲</span>
                          ) : (
                            <span>▼</span>
                          )
                        ) : (
                          <span></span>
                        )}
                      </th>
                      <th style={{ textAlign: "center" }}>Valdymo įrankiai</th>
                    </tr>
                  </thead>
                  <tbody>
                    {employeeData
                      .filter(
                        (employee) =>
                          employee !== null && employee !== undefined
                      )
                      .map((employee) => (
                        <tr key={employee.id}>
                          <td>{employee.firstName}</td>
                          <td>{employee.lastName}</td>
                          <td>{employee.email}</td>
                          {employee.teamName !== "no_team" ? (
                            <td>{employee.teamName}</td>
                          ) : (
                            <td className="text-center">-</td>
                          )}
                          {employee.stack === "" ? (
                            <td className="text-center">-</td>
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
                <div className="row justify-content-center">
                  <div className="col-3">
                    <Pagination
                      activePage={pageNumber}
                      onChange={handlePageChange}
                      totalItemsCount={totalItems}
                      itemsCountPerPage={10}
                      pageRangeDisplayed={5}
                      itemClass="page-item"
                      linkClass="page-link"
                    />
                  </div>
                </div>
              </div>
            ) : (
              <h4>Nepavyko gauti darbuotojų duomenų</h4>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmployeeTableComponent;
