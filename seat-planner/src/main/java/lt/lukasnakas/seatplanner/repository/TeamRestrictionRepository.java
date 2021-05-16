package lt.lukasnakas.seatplanner.repository;

import lt.lukasnakas.seatplanner.model.Team;
import lt.lukasnakas.seatplanner.model.TeamRestriction;
import lt.lukasnakas.seatplanner.model.enumerators.Restriction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRestrictionRepository extends MongoRepository<TeamRestriction, String> {

    Optional<TeamRestriction> findByTeam(Team team);

    List<TeamRestriction> findAllByRestrictionSet(Restriction restriction);
}
