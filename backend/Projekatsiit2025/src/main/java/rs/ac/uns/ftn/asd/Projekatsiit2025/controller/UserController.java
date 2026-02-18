package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.report.ReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.BlockUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.UpdatePasswordDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.UpdateUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.UpdatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.ReportService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	private final ReportService reportService;
	
	public UserController(UserService userService, ReportService reportService) {
		this.userService = userService;
		this.reportService = reportService;
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
	public ResponseEntity<UpdatedUserDTO> updateUserPassword(@PathVariable("id") Long id, @RequestBody UpdatePasswordDTO dto){
		UpdatedUserDTO responseUser = userService.updatePassword(id, dto);
		if(responseUser == null) {
			return new ResponseEntity<UpdatedUserDTO>(HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<UpdatedUserDTO>(responseUser, HttpStatus.OK); 
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetUserDTO>> getAllUsers(){
		Collection<GetUserDTO> usersExceptAdmin = userService.getAllUsers();
		return new ResponseEntity<Collection<GetUserDTO>>(usersExceptAdmin, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping(value = "/block", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetUserDTO> blockUser(@RequestBody BlockUserDTO dto){
		GetUserDTO user = userService.blockUser(dto);
		return new ResponseEntity<GetUserDTO>(user, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
	@GetMapping(value = "/passenger/{id}/report", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReportDTO> generatePassengerReport(@PathVariable("id") Long id,
			@RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		    LocalDate from,
		    @RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		    LocalDate to){
		ReportDTO dto = reportService.getPassengerReport(id, from, to);
		return new ResponseEntity<ReportDTO>(dto, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_DRIVER')")
	@GetMapping(value = "/driver/{id}/report", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReportDTO> generateDriverReport(@PathVariable("id") Long id,
			@RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		    LocalDate from,
		    @RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		    LocalDate to){
		ReportDTO dto = reportService.getDriverReport(id, from, to);
		return new ResponseEntity<ReportDTO>(dto, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(value = "/all/report", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReportDTO> generateAdminAggregateReport(@RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		    LocalDate from,
		    @RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		    LocalDate to){
		ReportDTO dto = reportService.getAdminAggregateReport(from, to);
		return new ResponseEntity<ReportDTO>(dto, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(value = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReportDTO> generateAdminUserReport(@RequestParam String email,
			@RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		    LocalDate from,
		    @RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		    LocalDate to){
		ReportDTO dto = reportService.getAdminUserReport(email, from, to);
		return new ResponseEntity<ReportDTO>(dto, HttpStatus.OK);
	}
	
}
