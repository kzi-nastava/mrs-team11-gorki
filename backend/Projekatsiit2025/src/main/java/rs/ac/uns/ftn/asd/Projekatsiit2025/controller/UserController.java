package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdateUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetUserDTO> getUser(@PathVariable("id") Long id){
		GetUserDTO user = new GetUserDTO();
		
		user.setActive(false);
		user.setAddress("Mornarska 51");
		user.setEmail("marko.pavlovic2404004@gmail.com");
		user.setFirstName("Marko");
		user.setId(1L);
		user.setLastName("Pavlovic");
		user.setPhoneNumber(648816145);
		user.setProfileImage("Putanja do slike");
		user.setRole(UserRole.DRIVER);
		
		if (user == null) {
			return new ResponseEntity<GetUserDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<GetUserDTO>(user, HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedUserDTO> updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserDTO requestUser){
		UpdatedUserDTO responseUser = new UpdatedUserDTO();
		
		responseUser.setAddress("Mornarska 51");
		responseUser.setEmail("marko.pavlovic2404004@gmail.com");
		responseUser.setFirstName("Marko");
		responseUser.setId(1L);
		responseUser.setLastName("Pavlovic");
		responseUser.setPhoneNumber(648816145);
		responseUser.setProfileImage("Putanja do slike");
		
		return new ResponseEntity<UpdatedUserDTO>(responseUser, HttpStatus.OK); 
	}
	
	@PutMapping(value = "/{id}/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedUserDTO> updateUserPassword(@PathVariable("id") Long id, @RequestBody UpdateUserDTO requestUser){
		UpdatedUserDTO responseUser = new UpdatedUserDTO();
		
		responseUser.setAddress("Mornarska 51");
		responseUser.setEmail("marko.pavlovic2404004@gmail.com");
		responseUser.setFirstName("Marko");
		responseUser.setId(1L);
		responseUser.setLastName("Pavlovic");
		responseUser.setPhoneNumber(648816145);
		responseUser.setProfileImage("Putanja do slike");
		
		return new ResponseEntity<UpdatedUserDTO>(responseUser, HttpStatus.OK); 
	}
}
