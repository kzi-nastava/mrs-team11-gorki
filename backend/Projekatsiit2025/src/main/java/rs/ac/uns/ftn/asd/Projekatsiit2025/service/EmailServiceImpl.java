package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.EmailDetails;
import java.io.File;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    public String sendSimpleMail(EmailDetails details)
    {

        try {

            SimpleMailMessage mailMessage
                = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    public String
    sendMailWithAttachment(EmailDetails details)
    {
        MimeMessage mimeMessage
            = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                details.getSubject());

            FileSystemResource file
                = new FileSystemResource(
                    new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(
                file.getFilename(), file);

            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        catch (MessagingException e) {
            e.printStackTrace();
            return "Error while Sending Mail: " + e.getMessage();
        }
    }
    
    public void sendActivationLinkToMail(String activationToken) {
        String fixedEmail = "mrs.team11.gorki@gmail.com";
        String link = "http://localhost:8080/api/auth/activate?token=" + activationToken;
        //Za mobilne: localhost se menja u IP adresu racunara npr.


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(fixedEmail);
        mailMessage.setSubject("Aktivacija naloga");
        mailMessage.setText("Klikni na link za aktivaciju (vazi 24h):\n" + link);

        javaMailSender.send(mailMessage);
        System.out.println("Sending activation link to: " + fixedEmail);
        System.out.println("LINK: " + link);
    }
    
    public void sendActivationLinkToDriverMail(String activationToken) {
        String fixedEmail = "mrs.team11.gorki@gmail.com";
        String link = "http://localhost:8080/api/auth/redirect?token=" + activationToken;
        //Za mobilne: localhost se menja u IP adresu racunara npr.
      
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(fixedEmail);
        mailMessage.setSubject("Account activation");
        mailMessage.setText("Click the activation link to set up your password and activate your account (lasts 24h):\n" + link);

        javaMailSender.send(mailMessage);
    }

    public void sendResetLinkToFixedEmail(String resetToken, String email) {
        String fixedEmail = "mrs.team11.gorki@gmail.com";
        String link = "http://localhost:4200/reset?token=" + resetToken;
        //Za mobilne: localhost se menja u IP adresu racunara npr.

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(fixedEmail);
        mailMessage.setSubject("Reset lozinke");
        mailMessage.setText("Klikni na link za reset lozinke za email " + email + ":\n" + link);

        javaMailSender.send(mailMessage);
    }
    
    public void sendRideAcceptedMail(String to, String link) {
        EmailDetails d = new EmailDetails();
        d.setRecipient(to);
        d.setSubject("Ride accepted");
        d.setMsgBody("You were added to a ride and a driver has been found.\nTrack: " + link);
        sendSimpleMail(d);
    }

    public void sendRideFinishedMail(String to, String link) {
        EmailDetails d = new EmailDetails();
        d.setRecipient(to);
        d.setSubject("Ride completed");
        d.setMsgBody("Ride finished successfully.\nDetails: " + link);
        sendSimpleMail(d);
    }
}
