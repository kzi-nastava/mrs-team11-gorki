package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.EmailDetails;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class EmailController {

    @Autowired private EmailService emailService;

    @PostMapping("/sendMail")
    public String
    sendMail(@Valid @RequestBody EmailDetails details)
    {
        String status
            = emailService.sendSimpleMail(details);

        return status;
    }

    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
        @Valid @RequestBody EmailDetails details)
    {
        String status
            = emailService.sendMailWithAttachment(details);

        return status;
    }
}
