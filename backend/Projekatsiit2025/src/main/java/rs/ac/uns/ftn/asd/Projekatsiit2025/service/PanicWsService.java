package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto.PanicEventDTO;

@Service
public class PanicWsService {

    private final SimpMessagingTemplate template;

    public PanicWsService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void broadcast(PanicEventDTO dto) {
        template.convertAndSend("/topic/panic", dto);
    }
}