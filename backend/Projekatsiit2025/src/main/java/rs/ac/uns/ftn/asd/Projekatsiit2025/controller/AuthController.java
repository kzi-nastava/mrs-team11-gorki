package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverStatusRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LoginRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LoginResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RegisterRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt.JwtTokenUtil;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	  @Autowired private UserRepository userRepository; 
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenUtil jwtTokenUtil;
    @Autowired private final UserService userService;
  
    public AuthController(UserRepository userRepository, UserService userService) {
    	this.userRepository=userRepository;
      this.userService=userService;
    }
	
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {

    	// 1) autentikacija (uporedi sifru preko UserDetailsService + PasswordEncoder)
        UsernamePasswordAuthenticationToken authReq =
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(authReq);
        
        User user = userRepository.findByEmail(request.getEmail())
        	    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        
        if(!user.getActive() || user.getBlocked()) {
        	LoginResponseDTO response = new LoginResponseDTO();
        	response.setId(user.getId());
            response.setRole(user.getRole());
            response.setActive(user.getActive());
            response.setBlocked(user.getBlocked());
            response.setMessage("Account is blocked or inactive.");
        	return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        // 2) “prijavi” korisnika u SecurityContext 
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 3) JWT
        String token = jwtTokenUtil.generateToken(user);

        // 4) popuni response iz baze (role, id, flags...)
        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(user.getId());
        response.setRole(user.getRole());
        response.setActive(user.getActive());
        response.setBlocked(user.getBlocked());
        response.setToken(token);
        response.setMessage("Login successful");
    	
		return new ResponseEntity<>(response, HttpStatus.OK);
    }

	@PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PatchMapping(value = "/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DriverStatusRequestDTO> changeDriverStatus(@RequestBody DriverStatusRequestDTO request) {

    	
        DriverStatusRequestDTO response = new DriverStatusRequestDTO();
        response.setStatus(request.getStatus() != null ? request.getStatus() : DriverStatus.ACTIVE);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@PreAuthorize("hasAnyAuthority('ROLE_DRIVER', 'ROLE_ADMIN', 'ROLE_PASSENGER')")
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout() {

        return new ResponseEntity<>("User logged out successfully", HttpStatus.OK);
    }
	
	@PreAuthorize("hasAnyAuthority('ROLE_DRIVER', 'ROLE_ADMIN', 'ROLE_PASSENGER')")
    @PostMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> resetPassword(@RequestParam String email) {

        return new ResponseEntity<>("Password reset link sent to " + email, HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO dto) {
        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
    @GetMapping(value = "/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateAccount(@RequestParam String token) {

        return new ResponseEntity<>("Account activated with token: " + token, HttpStatus.OK);
    }
}
