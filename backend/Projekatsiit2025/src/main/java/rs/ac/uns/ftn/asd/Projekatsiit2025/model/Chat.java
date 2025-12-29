package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.util.List;

public class Chat {
	private Long id;
	private List<Message>messages;
	private Passenger passenger;
	private Admin admin;
	
	public Chat() {
	}
	public Chat(Long id, List<Message> messages, Passenger passenger, Admin admin) {
		this.id = id;
		this.messages = messages;
		this.passenger = passenger;
		this.admin = admin;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public Passenger getPassenger() {
		return passenger;
	}
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	
	
}
