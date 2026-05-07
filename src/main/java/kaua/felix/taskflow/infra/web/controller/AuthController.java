package kaua.felix.taskflow.infra.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kaua.felix.taskflow.domain.ports.in.AuthUseCase;
import kaua.felix.taskflow.infra.web.dto.auth.request.LoginRequestDto;
import kaua.felix.taskflow.infra.web.dto.auth.request.RegisterRequestDto;
import kaua.felix.taskflow.infra.web.dto.auth.response.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name="Authentication", description = "Endpoint for user authentication")
public class AuthController {

    private final AuthUseCase authUseCase;

    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login (
            @Valid @RequestBody LoginRequestDto loginRequest
            ){

        String token = authUseCase.login(loginRequest.email(), loginRequest.password());

        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    @Operation(summary = "Register", description = "Registers a new user and returns a JWT token")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "User registered successfuly"),
            @ApiResponse(responseCode = "401", description = "Email already in use"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto registerRequest
    ) {
        String token = authUseCase.register(registerRequest.name(), registerRequest.email(), registerRequest.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDto(token));
    }
}
