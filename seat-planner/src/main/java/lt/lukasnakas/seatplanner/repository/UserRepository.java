package lt.lukasnakas.seatplanner.repository;

import lt.lukasnakas.seatplanner.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
}
