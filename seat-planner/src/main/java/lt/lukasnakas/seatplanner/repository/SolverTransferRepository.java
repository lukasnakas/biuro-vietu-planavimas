package lt.lukasnakas.seatplanner.repository;

import lt.lukasnakas.seatplanner.model.SolverTransfer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolverTransferRepository extends MongoRepository<SolverTransfer, String> {
}