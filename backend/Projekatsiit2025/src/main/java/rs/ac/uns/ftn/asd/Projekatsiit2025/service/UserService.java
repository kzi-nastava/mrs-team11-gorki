package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RegisterRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdateUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt.ActivationTokenUtil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired private final UserRepository userRepository;
	private static final String DEFAULT_IMAGE = "default-avatar.png";
	@Autowired private EmailService emailService;
	@Autowired private ActivationTokenUtil activationTokenUtil;
  
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

      // BITNO:
      // - password MORA biti vec enkodovan u bazi (BCrypt)
      // - role u Spring-u obično ide kao ROLE_...
      return org.springframework.security.core.userdetails.User
          .withUsername(user.getEmail())
          .password(user.getPassword())
          .roles(user.getRole().toString()) // npr ADMIN -> dobija se ROLE_ADMIN
          .build();
	}
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public GetUserDTO getById(Long id) {
		return userRepository.findById(id).map(this::mapToGetUserDTO).orElse(null);
	}
	
	@Transactional
	public UpdatedUserDTO updatePersonalInfo(Long id, UpdateUserDTO dto) {
		User user = userRepository.findById(id).get();
		user.setEmail(dto.getEmail());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setPhoneNumber(dto.getPhoneNumber());
		user.setAddress(dto.getAddress());
		user.setProfileImage(dto.getProfileImage());
		userRepository.save(user);
		return mapToUpdatedUserDTO(user);
	}
	
	@Transactional
	public UpdatedUserDTO updatePassword(Long id, UpdateUserDTO dto) {
		User user = userRepository.findById(id).get();
		user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
		userRepository.save(user);
		return mapToUpdatedUserDTO(user);
	}
	
	private GetUserDTO mapToGetUserDTO(User user) {
	    GetUserDTO dto = new GetUserDTO();
	    dto.setId(user.getId());
	    dto.setEmail(user.getEmail());
	    dto.setFirstName(user.getFirstName());
	    dto.setLastName(user.getLastName());
	    dto.setPhoneNumber(user.getPhoneNumber());
	    dto.setAddress(user.getAddress());
	    dto.setProfileImage(user.getProfileImage());
	    dto.setActive(user.getActive());
	    dto.setRole(user.getRole());
	    return dto;
	}
	
	private UpdatedUserDTO mapToUpdatedUserDTO(User user) {
		UpdatedUserDTO dto = new UpdatedUserDTO();
	    dto.setId(user.getId());
	    dto.setEmail(user.getEmail());
	    dto.setFirstName(user.getFirstName());
	    dto.setLastName(user.getLastName());
	    dto.setPhoneNumber(user.getPhoneNumber());
	    dto.setAddress(user.getAddress());
	    dto.setProfileImage(user.getProfileImage());
	    return dto;
	}

	public void register(RegisterRequestDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        if (userRepository.existsByEmail(dto.getEmail())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        if (dto.getPassword() == null || dto.getPassword().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        if (!dto.getPassword().equals(dto.getConfirmPassword())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
        if (dto.getLastName() == null || dto.getLastName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
        if (dto.getAddress() == null || dto.getAddress().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required");
        if (dto.getPhoneNumber() == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        String img = (dto.getProfileImage() == null || dto.getProfileImage().isBlank())
                ? DEFAULT_IMAGE
                : dto.getProfileImage();
        user.setProfileImage(img);
        user.setRole(UserRole.PASSENGER);
        user.setActive(false);
        user.setBlocked(false);
        userRepository.save(user);

    	String token = activationTokenUtil.generateActivationToken(user.getEmail());
    	emailService.sendActivationLinkToMail(token);
    }
	
	
}
