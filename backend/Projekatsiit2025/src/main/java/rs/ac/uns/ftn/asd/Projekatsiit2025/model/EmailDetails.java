package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import jakarta.validation.constraints.NotNull;

public class EmailDetails {

	@NotNull(message = "Recipient is required")
    private String recipient;
	@NotNull(message = "Body is required")
    private String msgBody;
	@NotNull(message = "Subject is required")
    private String subject;
	
    private String attachment;

    public EmailDetails() {}

    public EmailDetails(String recipient, String msgBody, String subject, String attachment) {
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
        this.attachment = attachment;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
