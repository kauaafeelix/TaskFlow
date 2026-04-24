package kaua.felix.taskflow.infra.web.controller;

import jakarta.validation.Valid;
import kaua.felix.taskflow.domain.ports.in.AuthUseCase;
import kaua.felix.taskflow.infra.web.dto.request.LoginRequestDto;
import kaua.felix.taskflow.infra.web.dto.request.RegisterRequestDto;
import kaua.felix.taskflow.infra.web.dto.response.AuthResponseDto;
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
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login (
            @Valid @RequestBody LoginRequestDto loginRequest
            ){

        String token = authUseCase.login(loginRequest.email(), loginRequest.password());

        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto registerRequest
            ){

        authUseCase.register(registerRequest.name(), registerRequest.email(), registerRequest.password());

        String token = authUseCase.login(registerRequest.email(), registerRequest.password());

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDto(token));
    }
}
