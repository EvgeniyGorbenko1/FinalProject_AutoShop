package com.tms.finalproject_autoshop.security;

import com.tms.finalproject_autoshop.exception.UsernameUsedException;
import com.tms.finalproject_autoshop.exception.WrongPasswordException;
import com.tms.finalproject_autoshop.model.Role;
import com.tms.finalproject_autoshop.model.Security;
import com.tms.finalproject_autoshop.model.VerificationToken;
import com.tms.finalproject_autoshop.model.dto.AuthRequest;
import com.tms.finalproject_autoshop.model.dto.AuthResponse;
import com.tms.finalproject_autoshop.model.dto.UserRegistrationDto;
import com.tms.finalproject_autoshop.repository.SecurityRepository;
import com.tms.finalproject_autoshop.service.EmailService;
import com.tms.finalproject_autoshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/security")
@Tag(name = "Security", description = "Authentication, registration and role management")
public class SecurityController {

    private final SecurityRepository securityRepository;
    public SecurityService securityService;
    public UserService userService;
    public EmailService emailService;

    public SecurityController(SecurityService securityService, UserService userService, EmailService emailService, SecurityRepository securityRepository) {
        this.securityService = securityService;
        this.userService = userService;
        this.emailService = emailService;
        this.securityRepository = securityRepository;
    }

    @Operation(
            summary = "Generate JWT token",
            description = "Authenticates user and returns JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT generated",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PostMapping("/jwt")
    public ResponseEntity<AuthResponse> jwt(
            @Parameter(description = "Authentication request") @RequestBody AuthRequest authRequest)
            throws ValidationException, WrongPasswordException {

        log.info("AUTH REQUEST: username='{}', password='{}'", authRequest.getUsername(), authRequest.getPassword());

        if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
            throw new ValidationException("Invalid request");
        }

        Optional<String> jwt = securityService.generateJwt(authRequest);
        if (jwt.isPresent()) {
            return ResponseEntity.ok(new AuthResponse(jwt.get()));
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @Operation(
            summary = "Get security record by ID",
            description = "ADMIN only",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Record found"),
                    @ApiResponse(responseCode = "404", description = "Record not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Security> getSecurityById(
            @Parameter(description = "Security ID") @PathVariable("id") Long id) {

        Optional<Security> security = securityService.getSecurityById(id);
        return security.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get all security records by role",
            description = "Returns list of users with specified role",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List returned"),
                    @ApiResponse(responseCode = "400", description = "Invalid role"),
                    @ApiResponse(responseCode = "404", description = "No users found")
            }
    )
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Security>> getAllSecuritiesByRole(
            @Parameter(description = "Role name") @PathVariable("role") String role) {

        try {
            role = role.toUpperCase();
            Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Security> allSecuritiesByRole = securityService.getAllSecurityByRole(role);
        if (!allSecuritiesByRole.isEmpty()) {
            return new ResponseEntity<>(allSecuritiesByRole, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "User registration",
            description = "Registers a new user and sends confirmation email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registration successful, email sent"),
                    @ApiResponse(responseCode = "409", description = "Username already exists"),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            }
    )
    @PostMapping("/registration")
    public ResponseEntity<HttpStatusCode> registration(
            @Valid @RequestBody UserRegistrationDto userRegistrationDto,
            BindingResult bindingResult) throws UsernameUsedException, ValidationException {

        if (bindingResult.hasErrors()) {
            List<String> errMessages = new ArrayList<>();

            for (ObjectError objectError : bindingResult.getAllErrors()) {
                log.warn(objectError.toString());
                errMessages.add(objectError.getDefaultMessage());
            }
            throw new ValidationException(String.valueOf(errMessages));
        }

        if (securityService.registration(userRegistrationDto)) {
            securityService.createVerificationToken(userRegistrationDto.getUsername());
            return emailService.sendEmail(
                    userRegistrationDto.getEmail(),
                    "Добро Пожаловать!",
                    userRegistrationDto.getEmail() + "Подтверждение регистрации" +
                            "<h1>Привет, " + userRegistrationDto.getFirstName() + "!</h1>" +
                            "<p>Для завершения регистрации перейдите по ссылке:</p>" +
                            "<a href=http://localhost:8081/security/confirm?token=" +
                            securityService.getTokenByUsername(userRegistrationDto.getUsername()) +
                            ">Подтвердить email</a>"
            );
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Operation(
            summary = "User login",
            description = "Authenticates user and returns JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest requestDto) {
        log.info("Received login request for user: {}", requestDto.getUsername());
        AuthResponse responseDto = userService.loginUser(requestDto);
        log.info("Login successful for user: {}", requestDto.getUsername());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Set user role to ADMIN",
            description = "ADMIN only",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Role updated"),
                    @ApiResponse(responseCode = "409", description = "Failed to update role")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/admin")
    public ResponseEntity<HttpStatusCode> setRoleToAdmin(
            @Parameter(description = "User ID") @PathVariable Long id) {

        if (securityService.setRoleToAdmin(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Operation(
            summary = "Confirm email",
            description = "Validates email confirmation token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email confirmed"),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired token")
            }
    )
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(
            @Parameter(description = "Verification token") @RequestParam String token) {

        Optional<VerificationToken> optionalToken = securityService.findByToken(token);

        if (optionalToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
        }

        Security security = verificationToken.getSecurity();
        security.setIsEnabled(true);
        securityRepository.save(security);

        return ResponseEntity.ok("Email confirmed successfully");
    }
}
