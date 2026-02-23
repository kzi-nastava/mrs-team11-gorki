package rs.ac.uns.ftn.asd.Projekatsiit2025.exception;

public class RouteAlreadyAddedException extends RuntimeException {
	private static final long serialVersionUID = 4442977356077870833L;

	public RouteAlreadyAddedException(String message) {
		super(message);
	}

}
