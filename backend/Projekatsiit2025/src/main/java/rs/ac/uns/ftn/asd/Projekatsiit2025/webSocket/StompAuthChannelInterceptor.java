package rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;    
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt.JwtTokenUtil;

@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    public StompAuthChannelInterceptor(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) { 
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> auth = accessor.getNativeHeader("Authorization");
            if (auth != null && !auth.isEmpty()) {
                String header = auth.get(0);
                if (header != null && header.startsWith("Bearer ")) {
                    String token = header.substring(7);

                    String email = jwtTokenUtil.extractUsername(token); // prilagodi naziv
                    if (email != null && jwtTokenUtil.validateToken(token,email)) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                        accessor.setUser(authentication);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }

        return message;
    }
}
