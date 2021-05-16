package lt.lukasnakas.seatplanner.mapper;

import lt.lukasnakas.seatplanner.helper.TeamDataHelper;
import lt.lukasnakas.seatplanner.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamMapperTest {

    private TeamMapper teamMapper;

    @BeforeEach
    void setUp() {
        teamMapper = new TeamMapper();
    }

    @Test
    void shouldBuildCurrentTeamCopy() {
        // given
        Team team = TeamDataHelper.getTeam();

        // when
        Team teamCopy = teamMapper.buildCurrentTeamCopy(team);

        // then
        assertEquals(team, teamCopy);
    }

}
