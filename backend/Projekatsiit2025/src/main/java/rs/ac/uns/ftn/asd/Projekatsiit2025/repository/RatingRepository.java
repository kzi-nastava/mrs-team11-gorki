package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
}
