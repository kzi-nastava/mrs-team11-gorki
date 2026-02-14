package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
	  Driver findByVehicle_Id(Long vehicleId);
	
	  @Query("""
			  select d from Driver d
			  where lower(d.firstName) like lower(concat('%', :q, '%'))
			     or lower(d.lastName)  like lower(concat('%', :q, '%'))
			     or lower(concat(d.firstName,' ',d.lastName)) like lower(concat('%', :q, '%'))
			""")
	  List<Driver> searchByName(@Param("q") String q, Pageable pageable);
}
