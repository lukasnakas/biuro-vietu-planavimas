package lt.lukasnakas.seatplanner.controller;

import lt.lukasnakas.seatplanner.model.request.LoginUserRequest;
import lt.lukasnakas.seatplanner.model.response.LoginUserResponse;
import lt.lukasnakas.seatplanner.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/planner/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserRequest loginUserRequest) {
        LoginUserResponse loginUserResponse = authenticationService.loginUser(loginUserRequest);
        return ResponseEntity.ok(loginUserResponse);
    }
}

