package lt.lukasnakas.seatplanner.mapper;

import lt.lukasnakas.seatplanner.model.dto.ConstraintDto;
import lt.lukasnakas.seatplanner.model.request.ConstraintAlterationRequest;
import lt.lukasnakas.seatplanner.model.Constraint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConstraintMapper {

    public List<ConstraintDto> mapToDtoList(List<Constraint> constraints) {
        return constraints.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ConstraintDto mapToDto(Constraint constraint) {
        ConstraintDto dto = new ConstraintDto();
        dto.setId(constraint.getId());
        dto.setName(constraint.getName());
        dto.setScoreType(constraint.getScoreType());
        dto.setActive(constraint.isActive());
        dto.setScore(constraint.getScore());
        return dto;
    }

    public Constraint buildConstraint(Constraint constraint, ConstraintAlterationRequest request) {
        constraint.setScoreType(request.getScoreType());
        constraint.setActive(request.isActive());
        constraint.setScore(request.getScore());
        return constraint;
    }
}
