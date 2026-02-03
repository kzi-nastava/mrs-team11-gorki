package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverStatusRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LoginRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LoginResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RegisterRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt.JwtTokenUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenUtil jwtTokenUtil;
    @Autowired private UserRepository userRepository; 
   
    public AuthController(UserRepository userRepository) {
	    this.userRepository=userRepository;
    }
	
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {

    	
    	// 1) autentikacija (uporedi sifru preko UserDetailsService + PasswordEncoder)
        UsernamePasswordAuthenticationToken authReq =
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(authReq);

        // 2) “prijavi” korisnika u SecurityContext 
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 3) JWT
        String token = jwtTokenUtil.generateToken(request.getEmail());

        // 4) popuni response iz baze (role, id, flags...)
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found after"));

        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(user.getId());
        response.setRole(user.getRole());
        response.setActive(user.getActive());
        response.setBlocked(user.getBlocked());
        response.setToken(token);
        response.setMessage("Login successful");


        if (user.getBlocked()) {
	    	return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
		}
		if (!user.getActive()) {
			return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
		}
    	
		 return new ResponseEntity<>(response, HttpStatus.OK);
    	
    	/*
        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(1L);
        response.setRole(UserRole.DRIVER);
        response.setActive(true);
        response.setBlocked(false);
        response.setMessage("Login successful");

        return new ResponseEntity<>(response, HttpStatus.OK);*/
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

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterRequestDTO> register(@RequestBody RegisterRequestDTO request) {

        RegisterRequestDTO response = new RegisterRequestDTO();
        response.setEmail(request.getEmail());
        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setAddress(request.getAddress());
        response.setPhoneNumber(request.getPhoneNumber());
        response.setProfileImage(request.getProfileImage());
        response.setPassword(null);
        response.setConfirmPassword(null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateAccount(@RequestParam String token) {

        return new ResponseEntity<>("Account activated with token: " + token, HttpStatus.OK);
    }
}
