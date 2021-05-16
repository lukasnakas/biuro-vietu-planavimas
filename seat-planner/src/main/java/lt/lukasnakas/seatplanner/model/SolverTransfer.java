package lt.lukasnakas.seatplanner.model;

import org.bson.codecs.pojo.annotations.BsonId;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SolverTransfer {

    @BsonId
    private String id;
    private Map<String, List<Suggestion>> solverSuggestionsMap;

    public SolverTransfer(String id,
                          Map<String, List<Suggestion>> solverSuggestionsMap) {
        this.id = id;
        this.solverSuggestionsMap = solverSuggestionsMap;
    }

    public SolverTransfer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, List<Suggestion>> getSolverSuggestionsMap() {
        return solverSuggestionsMap;
    }

    public void setSolverSuggestionsMap(
            Map<String, List<Suggestion>> solverSuggestionsMap) {
        this.solverSuggestionsMap = solverSuggestionsMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SolverTransfer transfer = (SolverTransfer) o;
        return Objects.equals(id, transfer.id) && Objects
                .equals(solverSuggestionsMap, transfer.solverSuggestionsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, solverSuggestionsMap);
    }
}
