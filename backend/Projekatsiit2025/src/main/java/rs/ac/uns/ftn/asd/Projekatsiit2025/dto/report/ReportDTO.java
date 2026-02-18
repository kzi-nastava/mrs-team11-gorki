package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.report;

import java.util.List;

public class ReportDTO {
	private List<ReportPointDTO> points;
	private SummaryDTO summary;
	public ReportDTO() {
		super();
	}
	public ReportDTO(List<ReportPointDTO> points, SummaryDTO summary) {
		super();
		this.points = points;
		this.summary = summary;
	}
	public List<ReportPointDTO> getPoints() {
		return points;
	}
	public void setPoints(List<ReportPointDTO> points) {
		this.points = points;
	}
	public SummaryDTO getSummary() {
		return summary;
	}
	public void setSummary(SummaryDTO summary) {
		this.summary = summary;
	}
	
}
