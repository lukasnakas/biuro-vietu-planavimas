package lt.lukasnakas.seatplanner.model.response;

import lt.lukasnakas.seatplanner.model.Member;

import java.util.List;

public class PaginationMemberListResponse {

    private double totalPaginationItems;
    private List<Member> memberList;

    public PaginationMemberListResponse(double totalPaginationItems, List<Member> memberList) {
        this.totalPaginationItems = totalPaginationItems;
        this.memberList = memberList;
    }

    public double getTotalPaginationItems() {
        return totalPaginationItems;
    }

    public void setTotalPaginationItems(double totalPaginationItems) {
        this.totalPaginationItems = totalPaginationItems;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
