package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.exception.MissingHttpRequestBodyParameters;
import lt.lukasnakas.seatplanner.model.request.RestrictionRequest;
import lt.lukasnakas.seatplanner.model.response.RestrictionResponse;
import lt.lukasnakas.seatplanner.repository.TeamRestrictionRepository;
import lt.lukasnakas.seatplanner.model.Team;
import lt.lukasnakas.seatplanner.model.TeamRestriction;
import lt.lukasnakas.seatplanner.model.enumerators.Restriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class TeamRestrictionService {

    private static final String RESTRICTION_MOVE_TOGETHER = "Neišskirti";
    private static final String RESTRICTION_DO_NOT_MERGE = "Nejungti su kitais team'ais";
    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(TeamRestrictionService.class);

    private final TeamService teamService;
    private final TeamRestrictionRepository teamRestrictionRepository;

    public TeamRestrictionService(TeamRestrictionRepository teamRestrictionRepository, TeamService teamService) {
        this.teamRestrictionRepository = teamRestrictionRepository;
        this.teamService = teamService;
    }

    public RestrictionResponse addRestriction(RestrictionRequest restrictionRequest) {
        if (restrictionRequest.getRestriction() == Restriction.MOVE_TOGETHER && restrictionRequest.getArgs() == null) {
            throw new MissingHttpRequestBodyParameters(String.format("Restriction [%s] must have field 'args' " +
                    "with opposite team's name", Restriction.MOVE_TOGETHER));
        }

        TeamRestriction existingTeamRestriction = getExistingTeamRestriction(restrictionRequest);
        existingTeamRestriction.addRestriction(restrictionRequest.getRestriction());

        if (isRestrictionNew(restrictionRequest, existingTeamRestriction)) {
            CONSOLE_LOGGER.info("Added restriction {} to team ID {}", restrictionRequest.getRestriction(),
                    restrictionRequest.getTeamId());
        }

        teamRestrictionRepository.save(existingTeamRestriction);
        return new RestrictionResponse(restrictionRequest.getTeamId(), existingTeamRestriction.getRestrictionSet());
    }

    public RestrictionResponse removeRestriction(RestrictionRequest restrictionRequest) {
        TeamRestriction existingTeamRestriction = getExistingTeamRestriction(restrictionRequest);
        existingTeamRestriction.removeRestriction(restrictionRequest.getRestriction());
        teamRestrictionRepository.save(existingTeamRestriction);

        CONSOLE_LOGGER.info("Removed restriction {} from team ID {}", restrictionRequest.getRestriction(),
                restrictionRequest.getTeamId());

        return new RestrictionResponse(restrictionRequest.getTeamId(), existingTeamRestriction.getRestrictionSet());
    }

    public List<TeamRestriction> findMoveRestrictions(Restriction restriction) {
        return teamRestrictionRepository.findAllByRestrictionSet(restriction);
    }

    public List<Restriction> getAllRestrictionsValues() {
        return List.of(Restriction.values());
    }

    public void createTeamRestrictionsByTeams(String companyId, String officeId, List<Team> teams) {
        teamRestrictionRepository.deleteAll();
        teamService.getTeamsGroupedByTeamLocation(companyId, officeId, teams)
                .forEach((location, teamList) -> teamService.getConditionsOfTeams(teamList)
                        .forEach(condition -> applyCondition(teamList, condition)));
    }

    public Optional<TeamRestriction> findMoveRestriction(Team team) {
        return teamRestrictionRepository.findByTeam(team);
    }

    public List<TeamRestriction> findAll() {
        return teamRestrictionRepository.findAll();
    }

    private TeamRestriction getExistingTeamRestriction(RestrictionRequest restrictionRequest) {
        Team team = teamService.findTeamById(restrictionRequest.getTeamId());
        Optional<TeamRestriction> teamRestriction = findMoveRestriction(team);
        return teamRestriction.isEmpty()
                ? createNewMoveRestriction(restrictionRequest, team)
                : teamRestriction.get();
    }

    private boolean isRestrictionNew(RestrictionRequest restrictionRequest, TeamRestriction teamRestriction) {
        return teamRestriction.getId() == null
                || (teamRestriction.getId() != null
                && !teamRestriction.getRestrictionSet().contains(restrictionRequest.getRestriction()));
    }

    private void applyCondition(List<Team> teamList, String condition) {
        switch (condition) {
            case RESTRICTION_MOVE_TOGETHER:
                createMoveTogetherRestrictions(teamList);
                break;
            case RESTRICTION_DO_NOT_MERGE:
                createDoNotMergeRestrictions(teamList);
                break;
            default:
                CONSOLE_LOGGER.error("Condition [{}] not supported", condition);
                break;
        }
    }

    private void createMoveTogetherRestrictions(List<Team> teamList) {
        List<Team> teamsByRestriction = teamService.getTeamsByRestriction(teamList, RESTRICTION_MOVE_TOGETHER);

        for (int index = 0; index < teamsByRestriction.size() - 1; index++) {
            Team currentTeam = teamsByRestriction.get(index);
            Team nextTeam = teamsByRestriction.get(index + 1);
            Restriction restriction = Restriction.MOVE_TOGETHER;
            RestrictionRequest restrictionRequest = new RestrictionRequest(currentTeam.getId(), restriction, nextTeam.getId());
            addRestriction(restrictionRequest);
        }
    }

    private void createDoNotMergeRestrictions(List<Team> teamList) {
        List<Team> teamsByRestriction = teamService.getTeamsByRestriction(teamList, RESTRICTION_DO_NOT_MERGE);

        for (Team currentTeam : teamsByRestriction) {
            Restriction restriction = Restriction.DO_NOT_MERGE;
            RestrictionRequest restrictionRequest = new RestrictionRequest(currentTeam.getId(), restriction);
            addRestriction(restrictionRequest);
        }
    }

    private TeamRestriction createNewMoveRestriction(RestrictionRequest restrictionRequest, Team team) {
        TeamRestriction teamRestriction = new TeamRestriction(team, new HashSet<>());
        String teamArgs = restrictionRequest.getArgs();

        if (teamArgs != null) {
            Team argsTeam = teamService.findTeamById(teamArgs);
            teamRestriction.setAdditionalTeam(argsTeam);
        }

        return teamRestriction;
    }
}
