package com.tms.finalproject_autoshop.security;

import com.tms.finalproject_autoshop.exception.UsernameUsedException;
import com.tms.finalproject_autoshop.exception.WrongPasswordException;
import com.tms.finalproject_autoshop.model.Role;
import com.tms.finalproject_autoshop.model.Security;
import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.model.VerificationToken;
import com.tms.finalproject_autoshop.model.dto.AuthRequest;
import com.tms.finalproject_autoshop.model.dto.UserRegistrationDto;
import com.tms.finalproject_autoshop.repository.SecurityRepository;
import com.tms.finalproject_autoshop.repository.TokenRepository;
import com.tms.finalproject_autoshop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SecurityService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityRepository securityRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public SecurityService(BCryptPasswordEncoder bCryptPasswordEncoder,
                           SecurityRepository securityRepository,
                           JwtUtils jwtUtils, UserRepository userRepository, TokenRepository tokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityRepository = securityRepository;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public boolean registration(UserRegistrationDto userRegistrationDto) throws UsernameUsedException {
        log.info("Registration User" + userRegistrationDto.getEmail());
        if (isUsernameUsed(userRegistrationDto.getUsername())) {
            throw new UsernameUsedException(userRegistrationDto.getUsername());
        }
        try {
            User user = new User();
            user.setFirstName(userRegistrationDto.getFirstName());
            user.setLastName(userRegistrationDto.getLastName());
            user.setEmail(userRegistrationDto.getEmail());
            user.setAge(userRegistrationDto.getAge());
            user.setCreated(LocalDateTime.now());
            user.setUpdated(LocalDateTime.now());
            userRepository.save(user);

            Security security = new Security();
            security.setUser(user);
            security.setUsername(userRegistrationDto.getUsername());
            security.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));
            security.setRole(Role.USER);

            securityRepository.save(security);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    public boolean isUsernameUsed(String username) {
        return securityRepository.existsByUsername(username);
    }

    public Optional<Security> getSecurityById(Long id) {
        return securityRepository.findById(id);
    }

    public Boolean setRoleToAdmin(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("username not found");
        }
        return securityRepository.setAdminRoleByUserId(id) > 0;

    }

    public List<Security> getAllSecurityByRole(String role) {
        return securityRepository.customFindByRole(role);
    }

    public Optional<String> generateJwt(AuthRequest authRequest) throws WrongPasswordException {
        Security security = securityRepository.getByUsername(authRequest.getUsername());
        if (!bCryptPasswordEncoder.matches(authRequest.getPassword(), security.getPassword())) {
            throw new WrongPasswordException(authRequest.getPassword());
        }

        return Optional.ofNullable(jwtUtils.getToken(security.getUsername()));
    }

    public Optional<VerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void createVerificationToken(String username) {
        Security security = securityRepository.getByUsername(username);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setSecurity(security);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);
    }

    public String getTokenByUsername(String username) {
        VerificationToken verificationToken = tokenRepository.findTokenBySecurityUsername(username)
                .orElseThrow(() -> new RuntimeException("username not found"));
        return verificationToken.getToken();
    }
}
