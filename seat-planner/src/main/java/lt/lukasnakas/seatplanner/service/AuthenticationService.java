package lt.lukasnakas.seatplanner.service;

import lt.lukasnakas.seatplanner.model.User;
import lt.lukasnakas.seatplanner.model.request.LoginUserRequest;
import lt.lukasnakas.seatplanner.model.enumerators.Role;
import lt.lukasnakas.seatplanner.model.request.RegisterUserRequest;
import lt.lukasnakas.seatplanner.model.response.LoginUserResponse;
import lt.lukasnakas.seatplanner.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthenticationService {

    private static final Logger CONSOLE_LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public boolean registerUser(RegisterUserRequest registerUserRequest) {
        if (emailExists(registerUserRequest.getEmail())) {
            CONSOLE_LOGGER.info("User registration not successful");
            return false;
        }

        if (!registerUserRequest.getPassword().equals(registerUserRequest.getPasswordConfirm())) {
            CONSOLE_LOGGER.info("Password do not match");
            return false;
        }

        User user = new User();
        user.setCompany(registerUserRequest.getCompany());
        user.setEmail(registerUserRequest.getEmail());
        user.setRoles(Collections.singletonList(Role.USER));
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        userRepository.save(user);
        CONSOLE_LOGGER.info("User registration successful");
        return true;
    }

    public RegisterUserRequest buildRegisterUserRequest(String email, String password, String companyId) {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setCompany(companyId);
        registerUserRequest.setEmail(email);
        registerUserRequest.setPassword(password);
        registerUserRequest.setPasswordConfirm(password);
        return registerUserRequest;
    }

    public LoginUserResponse loginUser(LoginUserRequest loginUserRequest) {
        Optional<User> user = userRepository.findByEmail(loginUserRequest.getEmail());
        if (user.isPresent()) {
            boolean isAdmin = user.get().getRoles().contains(Role.ADMIN);
            String existingPassword = user.get().getPassword();
            String companyId = user.get().getCompany();
            if (passwordEncoder.matches(loginUserRequest.getPassword(), existingPassword)) {
                CONSOLE_LOGGER.info("User login successful");
                return new LoginUserResponse(true, companyId, isAdmin ? Role.ADMIN : Role.USER);
            }
        }
        CONSOLE_LOGGER.info("User login not successful");
        return new LoginUserResponse(false);
    }

    private boolean emailExists(String email) {
        return !userRepository.findByEmail(email).isEmpty();
    }

}
