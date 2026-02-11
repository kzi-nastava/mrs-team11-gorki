package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.GetUserDTO;

public class GetDriverDTO {
	private GetUserDTO user;
	private double activityLast24h;
	
	public GetDriverDTO() {
		super();
	}
	
	public GetDriverDTO(GetUserDTO user, double activityLast24h) {
		super();
		this.user = user;
		this.activityLast24h = activityLast24h;
	}
	
	public GetUserDTO getUser() {
		return user;
	}
	public void setUser(GetUserDTO user) {
		this.user = user;
	}
	public double getActivityLast24h() {
		return activityLast24h;
	}
	public void setActivityLast24h(double activityLast24h) {
		this.activityLast24h = activityLast24h;
	}
	
}
