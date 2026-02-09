package rs.ac.uns.ftn.asd.Projekatsiit2025.exception;

public class EmailAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = -6568999028138133705L;

	public EmailAlreadyExistsException(String message) {
		super(message);
	}
	
}
