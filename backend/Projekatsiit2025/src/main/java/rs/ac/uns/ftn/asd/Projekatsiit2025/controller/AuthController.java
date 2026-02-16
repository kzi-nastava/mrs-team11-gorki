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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.DriverStatusRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.LoginRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.LoginResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.RegisterRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.ResetPasswordDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt.ActivationTokenUtil;
import rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt.JwtTokenUtil;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.EmailService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository; 
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenUtil jwtTokenUtil;
    @Autowired private final UserService userService;
    @Autowired private ActivationTokenUtil activationTokenUtil;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailService emailService;
  
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

    @PostMapping(value="/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {

        userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String token = activationTokenUtil.generateActivationToken(email);

        emailService.sendResetLinkToFixedEmail(token, email);

        return ResponseEntity.ok().build();
    }
	
    @PostMapping(value="/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDTO dto) {

        if(dto.getNewPassword() == null || dto.getNewPassword().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password required");

        if(!dto.getNewPassword().equals(dto.getConfirmNewPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");

        String email = activationTokenUtil.validateAndGetEmail(dto.getToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO dto) {
        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
	@GetMapping(value = "/activate", produces = "text/html; charset=UTF-8")
public String activateAccount(@RequestParam String token) {

    try {
        String email = activationTokenUtil.validateAndGetEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        if (!user.getActive()) {
            user.setActive(true);
            userRepository.save(user);
        }

        return """
            <!doctype html>
            <html lang="en">
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>Activation successful</title>
              <style>
                body{font-family:system-ui,Segoe UI,Roboto,Arial;margin:0;background:#0b1220;color:#e6edf3;display:grid;place-items:center;min-height:100vh}
                .card{background:#121a2b;border:1px solid #22304f;border-radius:16px;padding:28px;max-width:520px;width:92%;box-shadow:0 10px 30px rgba(0,0,0,.35)}
                h1{margin:0 0 8px;font-size:22px}
                p{margin:0 0 18px;opacity:.9;line-height:1.4}
                .ok{display:inline-block;background:#16a34a;color:#052e12;padding:6px 10px;border-radius:999px;font-weight:700;font-size:12px}
                a.btn{display:inline-block;text-decoration:none;background:#60a5fa;color:#07101f;padding:10px 14px;border-radius:10px;font-weight:700}
              </style>
            </head>
            <body>
              <div class="card">
                <div class="ok">SUCCESS</div>
                <h1>Account activated</h1>
                <p>You can now log in to the application.</p>
              </div>
            </body>
            </html>
        """;

    } catch (Exception e) {
        return """
            <!doctype html>
            <html lang="en">
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>Activation failed</title>
              <style>
                body{font-family:system-ui,Segoe UI,Roboto,Arial;margin:0;background:#160b0b;color:#ffecec;display:grid;place-items:center;min-height:100vh}
                .card{background:#261010;border:1px solid #5a1f1f;border-radius:16px;padding:28px;max-width:520px;width:92%}
                h1{margin:0 0 8px;font-size:22px}
                p{margin:0 0 18px;opacity:.9;line-height:1.4}
                .bad{display:inline-block;background:#ef4444;color:#2b0b0b;padding:6px 10px;border-radius:999px;font-weight:800;font-size:12px}
                a.btn{display:inline-block;text-decoration:none;background:#fca5a5;color:#2b0b0b;padding:10px 14px;border-radius:10px;font-weight:700}
              </style>
            </head>
            <body>
              <div class="card">
                <div class="bad">ERROR</div>
                <h1>The link is invalid or has expired</h1>
                <p>Try registering again or request a new activation link.</p>
              </div>
            </body>
            </html>
        """;
    }
}
}
