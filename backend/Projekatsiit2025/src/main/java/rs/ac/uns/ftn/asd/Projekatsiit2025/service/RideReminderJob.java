package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;

@Service
public class RideReminderJob {
	private final RideRepository rideRepository;
    private final NotificationService notificationService;

    public RideReminderJob(RideRepository rideRepository,
                           NotificationService notificationService) {
        this.rideRepository = rideRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    @Scheduled(fixedRate = 10_000)
    public void sendScheduledRideReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15 = now.plusMinutes(15);

        List<Ride> rides = rideRepository.findRidesForReminderWindow(now, nowPlus15);

        for (Ride r : rides) {
            // stop uslov: ako je scheduledTime proSao, gasi reminder
            if (!now.isBefore(r.getScheduledTime())) {
                r.setReminderActive(false);
                continue;
            }

            LocalDateTime last = r.getLastReminderSentAt();

            boolean shouldSend =
                    last == null || Duration.between(last, now).toMinutes() >= 1;

            if (!shouldSend) continue;

            long minutesLeft = Duration.between(now, r.getScheduledTime()).toMinutes();
            String content = "Reminder: your ride starts in ~" + minutesLeft + " minutes.";

            notificationService.createAndSend(
                    r.getCreator().getEmail(),
                    r.getId(),
                    "RIDE_REMINDER",
                    content
            );

            r.setLastReminderSentAt(now);

            // opciono: ako minutesLeft <= 0, ugasi
            if (minutesLeft <= 0) r.setReminderActive(false);
        }
    }

}
