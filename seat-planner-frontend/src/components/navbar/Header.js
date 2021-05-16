import { Link, NavLink, Route } from "react-router-dom";

const Header = (props) => {
  return (
    <header className="navbar navbar-default navbar-expand-md">
      <div className="navbar-header">
        <div className="navbar-brand">
          {props.isLoggedIn ? (
            <Link to="/overview" title="SeatPlanner">
              SeatPlanner
            </Link>
          ) : (
            <Link to="/" title="SeatPlanner">
              SeatPlanner
            </Link>
          )}
        </div>
      </div>
      <nav
        className="collapse navbar-collapse"
        id="nc4navCollapseMain"
        aria-label="Menu"
      >
        {props.isLoggedIn ? (
          <ul
            className="nav navbar-nav nav-tabs first-level"
            id="nc4navFlMenu"
            role="menubar"
            aria-label="Menu"
          >
            <Route path="/about">
              {({ match }) => (
                <li className={match ? "active" : undefined}>
                  <NavLink id="aboutPage" activeClassName="active" to="/about">
                    <span>{"Apie"}</span>
                  </NavLink>
                </li>
              )}
            </Route>
            <Route path="/overview">
              {({ match }) => (
                <li className={match ? "active" : undefined}>
                  <NavLink
                    id="overviewPage"
                    activeClassName="active"
                    to="/overview"
                  >
                    <span>{"Apžvalga"}</span>
                  </NavLink>
                </li>
              )}
            </Route>
            <Route path="/plan">
              {({ match }) => (
                <li className={match ? "active" : undefined}>
                  <NavLink id="planPage" activeClassName="active" to="/plan">
                    <span>{"Planavimas"}</span>
                  </NavLink>
                </li>
              )}
            </Route>
            <Route path="/employees">
              {({ match }) => (
                <li className={match ? "active" : undefined}>
                  <NavLink
                    id="empPage"
                    activeClassName="active"
                    to="/employees"
                  >
                    <span>{"Darbuotojai"}</span>
                  </NavLink>
                </li>
              )}
            </Route>
            {props.isAdmin ? (
              <Route path="/offices">
                {({ match }) => (
                  <li className={match ? "active" : undefined}>
                    <NavLink
                      id="officePage"
                      activeClassName="active"
                      to="/offices"
                    >
                      <span>{"Biurai"}</span>
                    </NavLink>
                  </li>
                )}
              </Route>
            ) : undefined}
            <Route path="/faq">
              {({ match }) => (
                <li className={match ? "active" : undefined}>
                  <NavLink id="faqPage" activeClassName="active" to="/faq">
                    <span>{"D. U. K."}</span>
                  </NavLink>
                </li>
              )}
            </Route>
          </ul>
        ) : (
          <ul
            className="nav navbar-nav nav-tabs first-level"
            id="nc4navFlMenu"
            role="menubar"
            aria-label="Menu"
          >
            <Route path="/about">
              {({ match }) => (
                <li className={match ? "active" : undefined}>
                  <NavLink id="aboutPage" activeClassName="active" to="/about">
                    <span>{"Apie"}</span>
                  </NavLink>
                </li>
              )}
            </Route>
            <Route path="/faq">
              {({ match }) => (
                <li className={match ? "active" : undefined}>
                  <NavLink id="faqPage" activeClassName="active" to="/faq">
                    <span>{"D. U. K."}</span>
                  </NavLink>
                </li>
              )}
            </Route>
          </ul>
        )}
      </nav>
    </header>
  );
};

export default Header;
