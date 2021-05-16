import "./App.css";
import AuthenticationPage from "./pages/AuthenticationPage";
import OverviewPage from "./pages/OverviewPage";
import PlanningPage from "./pages/PlanningPage";
import AboutPage from "./pages/AboutPage";
import EmployeesPage from "./pages/EmployeesPage";
import OfficesPage from "./pages/OfficesPage";
import FaqPage from "./pages/FaqPage";
import { BrowserRouter as Router, Route } from "react-router-dom";
import Header from "./components/navbar/Header";
import Footer from "./components/navbar/Footer";
import { useState } from "react";

function App() {
  const [isAdmin, setAdmin] = useState(false);
  const [isLoggedIn, setLoggedIn] = useState(false);
  const [companyId, setCompanyId] = useState("");

  return (
    <div id="app" className="App">
      <Router>
        <Header isAdmin={isAdmin} isLoggedIn={isLoggedIn} />
        <Route
          exact
          path="/"
          render={(props) => (
            <AuthenticationPage
              {...props}
              setAdmin={setAdmin}
              setLoggedIn={setLoggedIn}
              setCompanyId={setCompanyId}
            />
          )}
        />
        <Route
          exact
          path="/login"
          render={(props) => (
            <AuthenticationPage
              {...props}
              setAdmin={setAdmin}
              setLoggedIn={setLoggedIn}
              setCompanyId={setCompanyId}
            />
          )}
        />
        <Route
          exact
          path="/about"
          render={(props) => <AboutPage {...props} />}
        />
        <Route
          exact
          path="/overview"
          render={(props) => (
            <OverviewPage
              {...props}
              isAdmin={isAdmin}
              isLoggedIn={isLoggedIn}
              companyId={companyId}
            />
          )}
        />
        <Route
          exact
          path="/plan"
          render={(props) => (
            <PlanningPage
              {...props}
              isAdmin={isAdmin}
              isLoggedIn={isLoggedIn}
              companyId={companyId}
            />
          )}
        />
        <Route
          exact
          path="/employees"
          render={(props) => (
            <EmployeesPage
              {...props}
              isAdmin={isAdmin}
              isLoggedIn={isLoggedIn}
              companyId={companyId}
            />
          )}
        />
        <Route
          exact
          path="/offices"
          render={(props) => (
            <OfficesPage
              {...props}
              isAdmin={isAdmin}
              isLoggedIn={isLoggedIn}
              companyId={companyId}
            />
          )}
        />
        <Route exact path="/faq" render={(props) => <FaqPage {...props} />} />
        <Footer />
      </Router>
    </div>
  );
}

export default App;
