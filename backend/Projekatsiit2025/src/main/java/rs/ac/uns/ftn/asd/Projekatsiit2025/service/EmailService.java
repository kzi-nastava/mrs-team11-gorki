package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);

    void sendActivationLinkToMail(String activationToken);
}