package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideStopRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideStopResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RideService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class RideControllerStopRideIT {


    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    @Mock
    private RideService rideService;



    @BeforeEach
    void setUp() {

        RideController controller =
                new RideController(
                        null,
                        rideService
                );


        LocalValidatorFactoryBean validator =
                new LocalValidatorFactoryBean();

        validator.afterPropertiesSet();


        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(controller)
                        .setValidator(validator)
                        .build();


        objectMapper = new ObjectMapper();
    }



    @Test
    void stopRide_validRequest_returnsStopResponse() throws Exception {


        Location location =
                new Location(
                        45.2671,
                        19.8335,
                        "Bulevar oslobodjenja 1"
                );


        RideStopResponseDTO response =
                new RideStopResponseDTO(
                        location,
                        350,
                        LocalDateTime.now()
                );


        when(
            rideService.stopRide(
                    eq(10L),
                    any(Location.class)
            )
        )
        .thenReturn(response);



        RideStopRequestDTO request =
                new RideStopRequestDTO();

        request.setLatitude(45.2671);
        request.setLongitude(19.8335);
        request.setAddress("Bulevar oslobodjenja 1");



        mockMvc.perform(
                post("/api/rides/{id}/stop",10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                            objectMapper.writeValueAsString(request)
                        )
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.price").value(350)
        )
        .andExpect(
                jsonPath("$.stopAddress.address")
                .value("Bulevar oslobodjenja 1")
        );


        verify(rideService)
                .stopRide(
                        eq(10L),
                        any(Location.class)
                );
    }



    @Test
    void stopRide_missingAddress_returnsBadRequest()
            throws Exception {


        mockMvc.perform(
                post("/api/rides/{id}/stop",10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "latitude":45.2671,
                          "longitude":19.8335
                        }
                        """)
        )
        .andExpect(status().isBadRequest());

    }



    @Test
    void stopRide_invalidLatitude_returnsBadRequest()
            throws Exception {


        mockMvc.perform(
                post("/api/rides/{id}/stop",10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "latitude":200,
                          "longitude":19.8335,
                          "address":"Test"
                        }
                        """)
        )
        .andExpect(status().isBadRequest());

    }



    @Test
    void stopRide_invalidLongitude_returnsBadRequest()
            throws Exception {


        mockMvc.perform(
                post("/api/rides/{id}/stop",10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "latitude":45.2671,
                          "longitude":300,
                          "address":"Test"
                        }
                        """)
        )
        .andExpect(status().isBadRequest());

    }

}