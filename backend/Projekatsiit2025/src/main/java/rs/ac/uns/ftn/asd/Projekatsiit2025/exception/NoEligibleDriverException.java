package rs.ac.uns.ftn.asd.Projekatsiit2025.exception;

public class NoEligibleDriverException extends RuntimeException{
	private static final long serialVersionUID = -2907544197477567443L;

	public NoEligibleDriverException(String message) {
		super(message);
	}
}
