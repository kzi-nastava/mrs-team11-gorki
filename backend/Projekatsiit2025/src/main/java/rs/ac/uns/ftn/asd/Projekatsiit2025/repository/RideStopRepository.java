package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import java.sql.Time;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;

public interface RideStopRepository extends JpaRepository<Ride, Time> {

}
