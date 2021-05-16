package lt.lukasnakas.seatplanner.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lt.lukasnakas.seatplanner.model.Suggestion;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"completed", "status", "split", "suggestions"})
public class FindSeatResponse {

    private String id;
    private boolean completed;
    private List<Suggestion> suggestions;
    private boolean split;
    private String status = "0%";

    public FindSeatResponse(String id, boolean completed, List<Suggestion> suggestions, boolean split, String status) {
        this.id = id;
        this.completed = completed;
        this.suggestions = suggestions;
        this.split = split;
        this.status = status;
    }

    public FindSeatResponse() {
        suggestions = Collections.emptyList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FindSeatResponse that = (FindSeatResponse) o;
        return completed == that.completed && split == that.split && Objects
                .equals(id, that.id) && Objects.equals(suggestions, that.suggestions)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, completed, suggestions, split, status);
    }
}