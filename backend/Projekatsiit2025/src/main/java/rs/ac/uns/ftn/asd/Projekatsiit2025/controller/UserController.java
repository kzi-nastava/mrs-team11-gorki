package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdateUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_PASSENGER', 'ROLE_ADMIN', 'ROLE_DRIVER')")
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetUserDTO> getUser(@PathVariable("id") Long id){
		GetUserDTO user = userService.getById(id);
		if (user == null) {
			return new ResponseEntity<GetUserDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<GetUserDTO>(user, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_PASSENGER', 'ROLE_ADMIN', 'ROLE_DRIVER')")
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedUserDTO> updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserDTO requestUser){
		UpdatedUserDTO responseUser = userService.updatePersonalInfo(id, requestUser);
		return new ResponseEntity<UpdatedUserDTO>(responseUser, HttpStatus.OK); 
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_PASSENGER', 'ROLE_ADMIN', 'ROLE_DRIVER')")
	@PutMapping(value = "/{id}/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedUserDTO> updateUserPassword(@PathVariable("id") Long id, @RequestBody UpdateUserDTO requestUser){
		UpdatedUserDTO responseUser = userService.updatePassword(id, requestUser);
		return new ResponseEntity<UpdatedUserDTO>(responseUser, HttpStatus.OK); 
	}
}
