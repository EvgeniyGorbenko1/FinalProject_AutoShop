package com.tms.finalproject_autoshop.security;

import com.tms.finalproject_autoshop.exception.UsernameUsedException;
import com.tms.finalproject_autoshop.exception.WrongPasswordException;
import com.tms.finalproject_autoshop.model.Role;
import com.tms.finalproject_autoshop.model.Security;
import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.model.dto.AuthRequest;
import com.tms.finalproject_autoshop.model.dto.UserRegistrationDto;
import com.tms.finalproject_autoshop.repository.SecurityRepository;
import com.tms.finalproject_autoshop.repository.UserRepository;
import com.tms.finalproject_autoshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SecurityService {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityRepository securityRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public SecurityService(UserService userService,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           SecurityRepository securityRepository,
                           JwtUtils jwtUtils, UserRepository userRepository) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityRepository = securityRepository;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    public boolean registration(UserRegistrationDto userRegistrationDto) throws UsernameUsedException {
        log.info("Registration User" + userRegistrationDto.getEmail());
        if (isUsernameUsed(userRegistrationDto.getUsername())) {
            throw new UsernameUsedException(userRegistrationDto.getUsername());
        }
        try{
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
        } catch (Exception e){
            log.error(e.getMessage());
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
        if (!userRepository.existsById(id)){
            throw new UsernameNotFoundException("username not found");
        }
           return securityRepository.setAdminRoleByUserId(id) > 0;

    }

    public List<Security> getAllSecurityByRole(String role) {
       return securityRepository.customFindByRole(role);
    }

    public Optional<String> generateJwt(AuthRequest authRequest) throws WrongPasswordException {
        Optional<Security> security = securityRepository.getByUsername(authRequest.getUsername());
        if(security.isEmpty()){
            throw new UsernameNotFoundException("username not found" + authRequest.getUsername());
        }
        if(!bCryptPasswordEncoder.matches(authRequest.getPassword(), security.get().getPassword())){
            throw new WrongPasswordException(authRequest.getPassword());
        }

        return Optional.ofNullable(jwtUtils.getToken(security.get().getUsername()));
    }
}
