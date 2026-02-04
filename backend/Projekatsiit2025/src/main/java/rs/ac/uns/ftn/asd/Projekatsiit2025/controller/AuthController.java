package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverStatusRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LoginRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LoginResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RegisterRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {


        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(1L);
        response.setRole(UserRole.DRIVER);
        response.setActive(true);
        response.setBlocked(false);
        response.setMessage("Login successful");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DriverStatusRequestDTO> changeDriverStatus(@RequestBody DriverStatusRequestDTO request) {

    	
        DriverStatusRequestDTO response = new DriverStatusRequestDTO();
        response.setStatus(request.getStatus() != null ? request.getStatus() : DriverStatus.ACTIVE);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout() {

        return new ResponseEntity<>("User logged out successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> resetPassword(@RequestParam String email) {

        return new ResponseEntity<>("Password reset link sent to " + email, HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO dto) {
        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateAccount(@RequestParam String token) {

        return new ResponseEntity<>("Account activated with token: " + token, HttpStatus.OK);
    }
}
