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
import com.tms.finalproject_autoshop.repository.TokenRepository;
import com.tms.finalproject_autoshop.service.EmailService;
import com.tms.finalproject_autoshop.service.UserService;

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
@RequestMapping("/security")
@RestController
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

    @PostMapping("/jwt")
    public ResponseEntity<AuthResponse> jwt(@RequestBody AuthRequest authRequest) throws ValidationException, WrongPasswordException {

        log.info("AUTH REQUEST: username='{}', password='{}'", authRequest.getUsername(), authRequest.getPassword());
        if (authRequest == null || authRequest.getUsername() == null || authRequest.getPassword() == null) {
            throw new ValidationException("Invalid request");
        }
        Optional<String> jwt = securityService.generateJwt(authRequest);
        if (jwt.isPresent()) {
            return ResponseEntity.ok(new AuthResponse(jwt.get()));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Security> getSecurityById(@PathVariable("id") Long id) {
        Optional<Security> security = securityService.getSecurityById(id);
        if (security.isPresent()) {
            return new ResponseEntity<>(security.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<Security>> getAllSecuritiesByRole(@PathVariable("role") String role) {
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

    @PostMapping("/registration")
    public ResponseEntity<HttpStatusCode> registration(@Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                                       BindingResult bindingResult) throws UsernameUsedException {

        if (bindingResult.hasErrors()) {
            List<String> errMessages = new ArrayList<>();

            for (ObjectError objectError : bindingResult.getAllErrors()) {
                log.warn(objectError.toString());
                errMessages.add(objectError.getDefaultMessage());
            }
            throw new jakarta.validation.ValidationException(String.valueOf(errMessages));
        }
        if (securityService.registration(userRegistrationDto)) {
            securityService.createVerificationToken(userRegistrationDto.getUsername());
            return emailService.sendEmail(userRegistrationDto.getEmail(), "Добро Пожаловать!", userRegistrationDto.getEmail() + "Подтверждение регистрации" + "<h1>Привет, " + userRegistrationDto.getFirstName() + "!</h1>" + "<p>Для завершения регистрации перейдите по ссылке:</p>" + "<a href=http://localhost:8081/security/confirm?token=" + securityService.getTokenByUsername(userRegistrationDto.getUsername()) + ">Подтвердить email</a>");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/admin")
    public ResponseEntity<HttpStatusCode> setRoleToAdmin(@PathVariable Long id) {
        if (securityService.setRoleToAdmin(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        Optional<VerificationToken> optionalToken = securityService.findByToken(token);

        if (optionalToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
        }
        Security security = optionalToken.get().getSecurity();
        security.setIsEnabled(true);
        securityRepository.save(security);
        return ResponseEntity.ok("Email confirmed successfully");
    }



}
