package rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.UserService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter  {
	  @Autowired private JwtTokenUtil jwtTokenUtil;
	  @Autowired private UserService userService;

	  @Override
	  protected void doFilterInternal(HttpServletRequest request,
	                                  HttpServletResponse response,
	                                  FilterChain chain)
	      throws ServletException, IOException {

	    String authHeader = request.getHeader("Authorization");
	    String jwtToken = null;
	    String username = null;

	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	      jwtToken = authHeader.substring(7);
	      username = jwtTokenUtil.extractUsername(jwtToken);
	    }

	    // Ako nema autentikacije u kontekstu, a imamo username iz tokena:
	    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	      UserDetails userDetails = userService.loadUserByUsername(username);

	      if (jwtTokenUtil.validateToken(jwtToken, username)) {
	        UsernamePasswordAuthenticationToken authentication =
	            new UsernamePasswordAuthenticationToken(
	                userDetails, null, userDetails.getAuthorities());

	        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	      }
	    }

	    chain.doFilter(request, response);
	  }
}
