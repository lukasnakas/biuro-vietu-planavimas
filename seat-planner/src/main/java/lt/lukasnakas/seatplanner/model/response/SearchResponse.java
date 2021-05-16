package lt.lukasnakas.seatplanner.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResponse {

    private UUID searchId;
    private String comment;

    public SearchResponse(UUID searchId) {
        this.searchId = searchId;
    }

    public SearchResponse(String comment) {
        this.comment = comment;
    }

    public UUID getSearchId() {
        return searchId;
    }

    public void setSearchId(UUID searchId) {
        this.searchId = searchId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
