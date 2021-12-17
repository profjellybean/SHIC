package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailConfirmationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityProperties securityProperties,
                                   JwtTokenizer jwtTokenizer, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        this.userService = userService;
        setFilterProcessesUrl(securityProperties.getLoginUri());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
        UserLoginDto user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);

            Authentication auth =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()));

            if(!userService.getConfirmationStatusByName(user.getUsername())){
                throw new EmailConfirmationException("Email confirmation needed");
            }

            return auth;

        } catch (IOException e) {
            throw new BadCredentialsException("Wrong API request or JSON schema", e);
        } catch (BadCredentialsException e) {
            if (user != null && user.getUsername() != null) {
                LOGGER.error("Unsuccessful authentication attempt for user {}", user.getUsername());
            }
            throw e;
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        LOGGER.debug("Invalid authentication attempt: {}", failed.getMessage());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        User user = ((User) authResult.getPrincipal());

        List<String> roles = user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        response.getWriter().write(jwtTokenizer.getAuthToken(user.getUsername(), roles));
        this.userService.setCurrUserGroup(user.getUsername());
        LOGGER.info("Successfully authenticated user {}", user.getUsername());
    }
}
