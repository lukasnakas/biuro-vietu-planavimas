import SuggestionService from "services/SuggestionService";

const SuggestionsComponent = (props) => {
  const transfers = [
    { id: "transfer1", from: "6.01", to: "6.02", personAmount: 5 },
    { id: "transfer2", from: "6.03", to: "6.04", personAmount: 3 },
    { id: "transfer3", from: "6.08", to: "6.09", personAmount: 6 },
    { id: "transfer4", from: "6.10", to: "6.11", personAmount: 2 },
  ];
  const { data } = SuggestionService(props);

  return (
    <div className="row" style={{ padding: "10px 0" }}>
      {console.log(data)}
      <div className="col text-left">
        <h2>Patvirtinti persodinimo pasiūlymai</h2>
        <div className="scrollable-expanded-list">
          <div
            className="panel panel-default box-shadowless"
            style={{
              paddingTop: "0px",
              paddingLeft: "10px",
              paddingBottom: "10px",
            }}
          >
            <div
              id="collapseOne"
              className="panel-collapse in"
              role="tabpanel"
              aria-expanded="true"
            >
              <div
                className="panel-body"
                style={{ padding: "0px 30px 5px 30px" }}
              >
                {transfers.length ? (
                  transfers.map((transfer) => (
                    <div className="panel panel-default box-shadowless">
                      <div
                        className="panel-heading"
                        role="tab"
                        id={"headingOne" + transfer.id}
                      >
                        <h4 className="panel-title">
                          <a
                            data-toggle="collapse"
                            data-parent="#collapseOne"
                            href={"#collapseOne" + transfer.id}
                            aria-expanded="false"
                            aria-controls={"collapseOne" + transfer.id}
                            className="collapsed"
                            style={{ textDecoration: "none" }}
                          >
                            <span className="caret"></span>
                            {transfer.id}
                          </a>
                        </h4>
                      </div>
                      <div
                        id={"collapseOne" + transfer.id}
                        className="panel-collapse collapse"
                        role="tabpanel"
                        aria-labelledby={"headingOne" + transfer.id}
                        aria-expanded="false"
                        aria-hidden="true"
                      >
                        <div className="panel-body">
                          <div className="col" style={{ padding: "5px 30px" }}>
                            <h4>Iš: {transfer.from}</h4>
                            <h4>Į: {transfer.to}</h4>
                            <h4>
                              Perkeltų žmonių kiekis: {transfer.personAmount}
                            </h4>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))
                ) : (
                  <h4>Nerasta komandų duomenų</h4>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SuggestionsComponent;
