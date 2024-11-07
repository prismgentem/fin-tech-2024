package org.example.crudkudago.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.crudkudago.entity.User;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.model.ChangePasswordRequest;
import org.example.crudkudago.model.UserAuthRequest;
import org.example.crudkudago.model.UserRegistrationRequest;
import org.springframework.security.core.GrantedAuthority;
import org.example.crudkudago.repository.RoleRepository;
import org.example.crudkudago.repository.UserRepository;
import org.example.crudkudago.security.BlackList;
import org.example.crudkudago.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String VERIFICATION_CODE = "0000";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final BlackList blackList;

    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ServiceException(ErrorType.CONFLICT, "User with username %s already exists", request.getUsername());
        }

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roleUser = roleRepository.findByName("USER").get();
        user.getRoles().add(roleUser);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public String authenticate(UserAuthRequest request, Boolean rememberMe) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new ServiceException(ErrorType.UNAUTHORIZED, "Incorrect password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return jwtService.generateToken(userDetails.getUsername(), roles, rememberMe);
    }

    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        blackList.addToBlackList(jwt);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new ServiceException(ErrorType.NOT_FOUND, "User with username %s not found", request.getUsername())
        );

        if (!Objects.equals(VERIFICATION_CODE, request.getVerificationCode())) {
            throw new ServiceException(ErrorType.UNAUTHORIZED, "Wrong verification code");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ServiceException(ErrorType.UNAUTHORIZED, "Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
