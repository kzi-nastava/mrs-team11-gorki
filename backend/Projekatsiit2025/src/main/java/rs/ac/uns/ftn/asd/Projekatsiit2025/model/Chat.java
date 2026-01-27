package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @ElementCollection
    private List<Message> messages;

    @ManyToOne
    private Passenger passenger;

    @ManyToOne
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
