package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.InconsistencyReport;

@Repository
public interface InconsistencyReportRepository extends JpaRepository<InconsistencyReport, Long> {

}
