package lt.lukasnakas.seatplanner.repository;

import lt.lukasnakas.seatplanner.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
}