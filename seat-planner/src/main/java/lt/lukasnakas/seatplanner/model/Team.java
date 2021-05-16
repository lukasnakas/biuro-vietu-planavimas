package lt.lukasnakas.seatplanner.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@PlanningEntity
public class Team {

    @PlanningId
    private String id;
    private String name;
    private int size;
    private List<Member> members;
    private Room room;
    private boolean isSplit;
    private String stack;
    private String conditions;

    public Team(int size, Room room, List<Member> members, String name, String stack, boolean isSplit, String conditions) {
        this();
        this.size = size;
        this.members = members;
        this.room = room;
        this.isSplit = isSplit;
        this.name = name;
        this.stack = stack;
        this.conditions = conditions;
    }

    public Team() {
        this.id = UUID.randomUUID().toString();
        this.members = new ArrayList<>();
        this.size = 0;
        this.isSplit = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    @PlanningVariable(valueRangeProviderRefs = {"roomRange"})
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isSplit() {
        return isSplit;
    }

    public void setSplit(boolean split) {
        isSplit = split;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Team)) {
            return false;
        }
        Team team = (Team) o;

        return Objects.equals(this.getSize(), team.getSize()) &&
                Objects.equals(this.getName(), team.getName()) &&
                Objects.equals(this.getMembers(), team.getMembers()) &&
                Objects.equals(this.getId(), team.getId()) &&
                Objects.equals(this.getStack(), team.getStack()) &&
                Objects.equals(this.isSplit(), team.isSplit()) &&
                Objects.equals(this.getConditions(), team.getConditions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size, members, name, stack, isSplit, conditions);
    }

}
