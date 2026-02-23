package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.inconsistencyReport.CreateInconsistencyReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.inconsistencyReport.CreatedInconsistencyReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.InconsistencyReport;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.InconsistencyReportRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;

@Service
public class InconsistencyReportService {
    private final InconsistencyReportRepository reportRepository;
    private final RideRepository rideRepository;
    
    public InconsistencyReportService(InconsistencyReportRepository reportRepository,
    		RideRepository rideRepository) {
        this.reportRepository = reportRepository;
        this.rideRepository=rideRepository;
    }
    
    @Transactional
    public CreatedInconsistencyReportDTO createReport(Long rideId, CreateInconsistencyReportDTO dto) {
        // 1️ Dobavi voznju iz repoa
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Voznja ne postoji"));

        // 2️ Kreiraj report
        InconsistencyReport report = new InconsistencyReport();
        report.setDescription(dto.getDescription());
        report.setTimeStamp(LocalDateTime.now());
        report.setRide(ride);

        // 3️ Sacuvaj u bazu
        InconsistencyReport saved = reportRepository.save(report);

        // 4️ Mapiranje na DTO
        return mapToDTO(saved);
    }

    private CreatedInconsistencyReportDTO mapToDTO(InconsistencyReport report) {
        CreatedInconsistencyReportDTO dto = new CreatedInconsistencyReportDTO();
        dto.setInconsistencyReportId(report.getId());
        dto.setDescription(report.getDescription());
        dto.setTimeStamp(report.getTimeStamp());
        return dto;
    }
}
