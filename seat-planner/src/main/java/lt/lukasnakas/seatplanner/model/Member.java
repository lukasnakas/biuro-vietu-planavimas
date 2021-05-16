package lt.lukasnakas.seatplanner.model;

import java.util.Objects;
import java.util.UUID;

public class Member {

    private String id;
    private String memberCode;
    private String firstName;
    private String lastName;
    private String email;
    private String stack;
    private int experience;

    public Member(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Member(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Member() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return experience == member.experience && Objects.equals(id, member.id)
                && Objects.equals(memberCode, member.memberCode) && Objects
                .equals(firstName, member.firstName) && Objects.equals(lastName, member.lastName)
                && Objects.equals(email, member.email) && Objects
                .equals(stack, member.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberCode, firstName, lastName, email, stack, experience);
    }
}
