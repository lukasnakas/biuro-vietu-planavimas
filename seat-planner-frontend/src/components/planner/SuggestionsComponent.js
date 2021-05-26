import SuggestionService from "services/SuggestionService";

const SuggestionsComponent = (props) => {
  const { confirmedSuggestions } = SuggestionService(props);

  const getMoveFrom = (moveTo) => {
    return moveTo.split("-->")[0].split("_").join(" > ");
  };

  const getMoveTo = (moveTo) => {
    return moveTo.split("-->")[1].split("_").join(" > ");
  };

  return (
    <div className="row" style={{ padding: "10px 0" }}>
      <div className="col text-left">
        <h2>Patvirtinti persodinimo pasiūlymai</h2>
        <div className="scrollable-expanded-list">
          <div className="col text-left scrollable-expanded-list">
            {confirmedSuggestions.length > 0 ? (
              confirmedSuggestions.map((suggestion) => (
                <div
                  key={suggestion.id}
                  style={{ margin: "20px 0px 20px 0px" }}
                >
                  {console.log(suggestion)}
                  <h3 style={{ marginBottom: "5px" }}>
                    Pasiūlymo ID: {suggestion.id}
                  </h3>
                  <h4>Patvirtinimo data: {suggestion.date}</h4>
                  <div className="row">
                    {suggestion.transfers.map((transfer) => {
                      return (
                        <div className="col" key={transfer.teamName}>
                          <h4>Komanda: {transfer.teamName}</h4>
                          <h4>Iš: {getMoveFrom(transfer.moveTo)}</h4>
                          <h4>Į: {getMoveTo(transfer.moveTo)}</h4>
                          <h4>
                            Perkeltų darbuotojų kiekis:{" "}
                            {transfer.peopleTransferredAmount}
                          </h4>
                        </div>
                      );
                    })}
                  </div>
                </div>
              ))
            ) : (
              <h4></h4>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SuggestionsComponent;
