const OfficesPage = (props) => {
  return (
    <div className="main-body">
      <div className="container h-100">
        <div className="row h-100">
          <div className="col my-auto">
            <div className="page-container rounded-20">
              {props.isLoggedIn ? (
                props.isAdmin ? (
                  <div>Biurai.</div>
                ) : (
                  <h1>Tik administratoriai turi teisę matyti šį puslapį</h1>
                )
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

export default OfficesPage;
