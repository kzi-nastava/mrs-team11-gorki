package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @ElementCollection
    @CollectionTable(name="chat_message", joinColumns=@JoinColumn(name="chat_id"))
    @OrderBy("timeStamp ASC")
    private List<Message> messages=new ArrayList<>();;

    @OneToOne
    @JoinColumn(name="user_id", unique=true)
    private User user;

    @ManyToOne
    @JoinColumn(name="admin_id", nullable=true)
    private Admin admin;
	
	public Chat() {
	}
	public Chat(Long id, List<Message> messages, User user, Admin admin) {
		this.id = id;
		this.messages = messages;
		this.user = user;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	
	
}
