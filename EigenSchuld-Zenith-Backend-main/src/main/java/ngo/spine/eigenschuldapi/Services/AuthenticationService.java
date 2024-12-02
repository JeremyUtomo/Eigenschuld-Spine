package ngo.spine.eigenschuldapi.Services;


import ngo.spine.eigenschuldapi.DAO.User.*;
import ngo.spine.eigenschuldapi.DTO.AuthenticationDTO;
import ngo.spine.eigenschuldapi.Exception.*;
import ngo.spine.eigenschuldapi.Interface.*;
import ngo.spine.eigenschuldapi.Model.User;
import ngo.spine.eigenschuldapi.Model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class AuthenticationService {

    private UserDAO userDAO;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private RegisterStrategy registerStrategy;

    @Autowired
    public AuthenticationService(
        UserDAO userDAO,
        JwtService jwtService,
        AuthenticationManager authenticationManager,
        RegisterStrategy registerStrategy
    ) {
        this.userDAO = userDAO;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.registerStrategy = registerStrategy;
    }

    public AuthenticationService(RegisterStrategy registerStrategy){
        this.registerStrategy = registerStrategy;
    }

    public AuthenticationDTO executeStrategy(User request, Role role, String token){
        return registerStrategy.register(request, role, token);
    }

    public AuthenticationDTO executeStrategy(User user, Role role) {
        return registerStrategy.register(user, role, null);
    }

    public AuthenticationDTO authenticate(User request) throws CustomAuthenticationException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new CustomAuthenticationException("Invalid credentials");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found", e);
        }

        User user = userDAO.getUserByEmail(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setLastLogin(new Date());
        userDAO.save(user);
        String token = jwtService.generateToken(user);

        return new AuthenticationDTO(token, user.getId());
    }
}
