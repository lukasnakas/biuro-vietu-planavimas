package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.mapper.ConstraintMapper;
import lt.lukasnakas.seatplanner.model.Constraint;
import lt.lukasnakas.seatplanner.model.Team;
import lt.lukasnakas.seatplanner.model.TeamRestriction;
import lt.lukasnakas.seatplanner.model.dto.ConstraintDto;
import lt.lukasnakas.seatplanner.model.exception.ConstraintNotFoundException;
import lt.lukasnakas.seatplanner.model.request.ConstraintAlterationRequest;
import lt.lukasnakas.seatplanner.repository.ConstraintRepository;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static lt.lukasnakas.seatplanner.model.enumerators.HardSoftScore.HARD;

@Service
public class ConstraintService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(ConstraintService.class);

    private final TeamRestrictionService teamRestrictionService;
    private final ConstraintRepository constraintRepository;
    private final ConstraintMapper constraintMapper;

    public ConstraintService(TeamRestrictionService teamRestrictionService, ConstraintRepository constraintRepository,
                             ConstraintMapper constraintMapper) {
        this.teamRestrictionService = teamRestrictionService;
        this.constraintRepository = constraintRepository;
        this.constraintMapper = constraintMapper;
    }

    public ConstraintDto updateConstraint(ConstraintAlterationRequest request) {
        CONSOLE_LOGGER.info("Processing request to change [{}] to 'isActive': {}",
                request.getConstraintId(), request.isActive());

        Constraint constraint = constraintMapper.buildConstraint(findById(request.getConstraintId()), request);
        return constraintMapper.mapToDto(constraintRepository.save(constraint));
    }

    public List<Constraint> findAll() {
        return constraintRepository.findAll();
    }

    public Optional<Constraint> findByName(String name) {
        return constraintRepository.findByName(name);
    }

    public List<ConstraintDto> getAllConstraintDtos() {
        CONSOLE_LOGGER.info("Collecting all constraints");
        return constraintMapper.mapToDtoList(findAll());
    }

    public Constraint findById(String id) {
        CONSOLE_LOGGER.info("Collecting constraint #{}", id);
        return constraintRepository.findById(id)
                .orElseThrow(() -> new ConstraintNotFoundException(String.format("Constraint %s not found!", id)));
    }

    public ConstraintDto getConstraintDtoById(String id) {
        return constraintMapper.mapToDto(findById(id));
    }

    public List<String> getAllActiveConstraintNames() {
        CONSOLE_LOGGER.info("Collecting active constraint names");
        return findAll().stream()
                .filter(Constraint::isActive)
                .map(Constraint::getName)
                .collect(Collectors.toList());
    }

    public Optional<TeamRestriction> fetchMoveRestrictionByTeam(Team team) {
        return teamRestrictionService.findMoveRestriction(team);
    }

    public HardSoftScore findHardSoftScore(String name) {
        Optional<Constraint> constraintOptional = findByName(name);
        if (constraintOptional.isEmpty()) {
            return HardSoftScore.ONE_HARD;
        }

        Constraint constraint = constraintOptional.get();
        return constraint.getScoreType() == HARD
                ? HardSoftScore.ofHard(constraint.getScore())
                : HardSoftScore.ofSoft(constraint.getScore());
    }
}