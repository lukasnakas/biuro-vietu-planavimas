package lt.lukasnakas.seatplanner.repository;

import lt.lukasnakas.seatplanner.model.Constraint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConstraintRepository extends MongoRepository<Constraint, String> {

    Optional<Constraint> findByName(String name);
}
