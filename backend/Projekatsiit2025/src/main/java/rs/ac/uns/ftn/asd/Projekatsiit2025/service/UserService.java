package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdateUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
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
		user.setPassword(dto.getPassword());
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
}
