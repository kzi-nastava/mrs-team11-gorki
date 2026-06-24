package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.FinishRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.FinishedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.DriverService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RideService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.VehicleService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DriverControllerFinishRideIT {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private RideService rideService;

    @Mock
    private DriverService driverService;

    @Mock
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        DriverController driverController = new DriverController(
                rideService,
                driverService,
                vehicleService
        );

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(driverController)
                .setValidator(validator)
                .build();

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void finishRide_validRequest_returnsFinishedRideDto() throws Exception {
        FinishedRideDTO response = new FinishedRideDTO();
        response.setRideId(10L);
        response.setRideStatus(RideStatus.FINISHED);
        response.setDriverStatus(DriverStatus.ACTIVE);
        response.setHasNextScheduledRide(false);

        when(rideService.finishRide(eq(2L), any(FinishRideDTO.class)))
                .thenReturn(response);

        FinishRideDTO request = new FinishRideDTO();
        request.setRideId(10L);
        request.setPaid(true);

        mockMvc.perform(put("/api/drivers/{driverId}/rides/finish", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideId").value(10))
                .andExpect(jsonPath("$.rideStatus").value("FINISHED"))
                .andExpect(jsonPath("$.driverStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.hasNextScheduledRide").value(false));

        ArgumentCaptor<FinishRideDTO> captor = ArgumentCaptor.forClass(FinishRideDTO.class);
        verify(rideService).finishRide(eq(2L), captor.capture());

        assertEquals(10L, captor.getValue().getRideId());
        assertEquals(true, captor.getValue().getPaid());
    }

    @Test
    void finishRide_missingRideId_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/drivers/{driverId}/rides/finish", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "paid": true
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(rideService);
    }

    @Test
    void finishRide_negativeRideId_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/drivers/{driverId}/rides/finish", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "rideId": -1,
                                  "paid": true
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(rideService);
    }

    @Test
    void finishRide_missingPaidFlag_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/drivers/{driverId}/rides/finish", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "rideId": 10
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(rideService);
    }
}