package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
		
	//List<Vehicle> findAllVehicleByID(); --> findAll postoji automatski
}
