package com.tms.finalproject_autoshop.security;

import com.tms.finalproject_autoshop.model.Security;
import com.tms.finalproject_autoshop.repository.SecurityRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final SecurityRepository securityRepository;

    public CustomUserDetailService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Security> securityOptional = securityRepository.getByUsername(username);
        if (securityOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username not found" + username);
        }
        Security security = securityOptional.get();
        return User
                .withUsername(security.getUsername())
                .password(security.getPassword())
                .roles(security.getRole().name())
                .build();

    }

    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        CustomUserDetailService customUserDetailService = (CustomUserDetailService) authentication.getPrincipal();
        assert customUserDetailService != null;
        return getUserId();
    }
}
