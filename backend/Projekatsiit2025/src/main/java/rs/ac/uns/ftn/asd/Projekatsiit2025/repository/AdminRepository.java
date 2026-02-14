package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
