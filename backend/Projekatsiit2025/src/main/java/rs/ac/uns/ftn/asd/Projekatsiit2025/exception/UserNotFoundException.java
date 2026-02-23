package rs.ac.uns.ftn.asd.Projekatsiit2025.exception;

public class UserNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 755812720593004738L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
