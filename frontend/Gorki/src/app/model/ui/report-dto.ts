import { ReportPointDTO } from "./report-point-dto";
import { SummaryDTO } from "./summary-dto";

export interface ReportDTO{
    points:ReportPointDTO[],
    summary:SummaryDTO
}